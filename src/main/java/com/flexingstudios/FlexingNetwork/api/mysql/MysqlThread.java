package com.flexingstudios.FlexingNetwork.api.mysql;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MysqlThread extends Thread {
    private MysqlConfig config;
    private static final String UNICODE_PARAMS = "useUnicode=true&characterEncoding=utf-8";
    private boolean useUnicode = false;
    private final Object lock = new Object();
    private final Queue<Query> queries;
    private volatile boolean connected = false;
    private volatile boolean running = false;
    protected Connection db;
    protected final Logger logger;

    public MysqlThread(Plugin plugin, String url, String user, String pass) {
        this(plugin, new MysqlConfigString(url, user, pass));
    }

    public MysqlThread(Plugin plugin, Supplier<String> url, Supplier<String> user, Supplier<String> pass) {
        this(plugin, new MysqlConfigSupplier(url, user, pass));
    }

    public MysqlThread(Plugin plugin, MysqlConfig config) {
        setName(plugin.getName() + " - Mysql");
        setDaemon(true);
        this.config = config;
        this.queries = new ConcurrentLinkedDeque<>();
        this.logger = plugin.getLogger();
        SafeRunnable.class.getName();
    }

    public void query(String query) {
        update(query, null);
    }

    public void select(String query, SelectCallback callback) {
        this.queries.add(new Query(query, callback));
        synchronized (this.lock) {
            this.lock.notify();
        }
    }

    public void update(String query, UpdateCallback callback) {
        this.queries.add(new Query(query, callback));
        synchronized (this.lock) {
            this.lock.notify();
        }
    }

    public void execute(File file) {
        safe(() -> execute(new FileInputStream(file)));
    }

    public void execute(InputStream is) {
        try (Scanner s = new Scanner(is).useDelimiter(";")){
            while (s.hasNext()) {
                String query = s.next().trim();
                if (!query.isEmpty())
                    query(query);
            }
        }
    }

    public void start() {
        if (this.running)
            return;
        this.running = true;
        super.start();
    }

    public void finish() {
        if (!this.running)
            return;
        this.running = false;
        safe(this::join);
        if (this.db != null) {
            safe(this::checkConnection);
            safe(this::executeQueries);
            safe(this.db::close);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isRunning() {
        return running;
    }

    public void useUnicode() {
        this.useUnicode = true;
    }

    protected void onConnect() {}

    protected void onDisconnect() {}

    protected String onPreQuery(String query) {
        return query;
    }

    protected void onPostQuery(String query, boolean success) {}


    protected void safe(SafeRunnable safeRunnable) {
        try {
            safeRunnable.run();
        } catch (Exception exception) {}
    }

    @Override
    public void run() {
        checkConnection();
        while (this.running) {
            if (!this.queries.isEmpty())
                if (checkConnection()) {
                    executeQueries();
                } else {
                    this.queries.clear();
                }
            try {
                synchronized (lock) {
                    this.lock.wait(1000L);
                }
            } catch (InterruptedException e) {
                this.running = false;
            }
        }
    }

    private void executeQueries() {
        while (!this.queries.isEmpty()) {
            Query query = this.queries.poll();
            String q = onPreQuery(query.query);
            if (q == null)
                continue;
            try (Statement statement = this.db.createStatement()) {
                boolean isSelect = statement.execute(q);
                try {
                    if (isSelect) {
                        if (query.callback != null) {
                            ResultSet rs = statement.getResultSet();
                            ((SelectCallback)query.callback).done(rs);
                            rs.close();
                        }
                    } else if (query.callback != null) {
                        ((UpdateCallback)query.callback).done(statement.getUpdateCount());
                    }
                } catch (Exception e) {
                    this.logger.log(Level.SEVERE, "Query " + q + " is failed!", e);
                }
                onPostQuery(q, true);
            } catch (Exception e) {
                onPostQuery(q, false);
                if (e.getMessage() != null && e.getMessage().contains("try restarting transaction")) {
                    this.queries.add(query);
                    this.logger.warning(" Query " + q + " is failed! Restarting: " + e.getMessage());
                    continue;
                }
                this.logger.severe("Query " + q + " is failed! Message: " + e.getMessage());
            }
        }
    }

    private boolean checkConnection() {
        boolean state = false;
        try {
            if (this.db != null && !isValid()) {
                safe(this.db::close);
                this.db = null;
            }
            if (this.db == null)
                connect();
            state = (this.db != null && isValid());
        } catch (Exception e) {
            this.logger.log(Level.WARNING, "Error while connecting to database: {0}", e.getMessage());
        }
        if (this.connected != state) {
            this.connected = state;
            if (!this.connected)
                onDisconnect();
        }
        return state;
    }

    private void connect() {
        try {
            String url = this.config.getUrl();
            if (this.useUnicode)
                url = addUnicodeParams(this.config.getUrl());
            this.db = DriverManager.getConnection(url, this.config.getUser(), this.config.getPass());
            if (isValid()) {
                this.logger.info("Mysql connected.");
                onConnect();
            }
        } catch (SQLException ex) {
            this.logger.warning(ex.getMessage());
        }
    }

    private String addUnicodeParams(String url) {
        if (url.contains("?")) {
            if (url.contains("useUnicode=true&characterEncoding=utf-8"))
                return url;
            url = url + "&";
        } else {
            url = url + "?";
        }
        return url + "useUnicode=true&characterEncoding=utf-8";
    }

    private boolean isValid() throws SQLException {
        return this.db.isValid(40);
    }

    private static long limit(long min, long value, long max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    public static interface MysqlConfig {
        String getUrl();
        String getUser();
        String getPass();
    }

    protected static interface SafeRunnable {
        void run() throws Exception;
    }

    public static class MysqlConfigString implements MysqlConfig {
        private final String url;
        private final String user;
        private final String pass;

        public MysqlConfigString(String url, String user, String pass) {
            this.url = url;
            this.user = user;
            this.pass = pass;
        }
        public String getUrl() {
            return this.url;
        }

        public String getUser() {
            return this.user;
        }

        public String getPass() {
            return this.pass;
        }
    }

    public static class MysqlConfigSupplier implements MysqlConfig {
        private final Supplier<String> url;
        private final Supplier<String> user;
        private final Supplier<String> pass;

        public MysqlConfigSupplier(Supplier<String> url, Supplier<String> user, Supplier<String> pass) {
            this.url = url;
            this.user = user;
            this.pass = pass;
        }

        public String getUrl() {
            return this.url.get();
        }

        public String getUser() {
            return this.user.get();
        }

        public String getPass() {
            return this.pass.get();
        }
    }


    public static class Query {
        String query;
        Callback callback;

        public Query(String query, Callback callback) {
            this.query = query;
            this.callback = callback;
        }
    }

}
