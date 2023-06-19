package com.flexingstudios.flexingnetwork.api.mysql;

import com.flexingstudios.flexingnetwork.api.util.Reflect;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
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
    protected final Logger LOGGER;

    public MysqlThread(Plugin plugin, @NotNull String url, @NotNull String user, @NotNull String pass) {
        this(plugin, new MysqlConfigString(url, user, pass));
    }

    public MysqlThread(Plugin plugin, @NotNull Supplier<String> url, @NotNull Supplier<String> user, @NotNull Supplier<String> pass) {
        this(plugin, new MysqlConfigSupplier(url, user, pass));
    }

    public MysqlThread(Plugin plugin, @NotNull MysqlConfig config) {
        setName(plugin.getName() + " - MySQL");
        setDaemon(true);
        this.config = config;
        queries = new ConcurrentLinkedDeque<>();
        LOGGER = plugin.getLogger();
    }

    public void query(@NotNull String query) {
        update(query, null);
    }

    public void select(@NotNull String query, SelectCallback callback) {
        queries.add(new Query(query, callback));
        synchronized (lock) {
            lock.notify();
        }
    }

    public void update(@NotNull String query, UpdateCallback callback) {
        queries.add(new Query(query, callback));
        synchronized (lock) {
            lock.notify();
        }
    }

    public void execute(@NotNull File file) {
        safe(() -> execute(Files.newInputStream(file.toPath())));
    }

    public void execute(@NotNull InputStream is) {
        try (Scanner s = new Scanner(is).useDelimiter(";")){
            while (s.hasNext()) {
                String query = s.next().trim();
                if (!query.isEmpty())
                    query(query);
            }
        }
    }

    public void start() {
        if (running)
            return;
        running = true;
        super.start();
    }

    public void finish() {
        if (!running)
            return;
        running = false;
        safe(this::join);
        if (db != null) {
            safe(this::checkConnection);
            safe(this::executeQueries);
            safe(db::close);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isRunning() {
        return running;
    }

    public void useUnicode() {
        useUnicode = true;
    }

    protected void onConnect() {
    }

    protected void onDisconnect() {
    }

    protected String onPreQuery(String query) {
        return query;
    }

    protected void onPostQuery(String query, boolean success) {
    }

    protected void safe(SafeRunnable safeRunnable) {
        try {
            safeRunnable.run();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void run() {
        checkConnection();
        while (running) {
            if (!queries.isEmpty())
                if (checkConnection()) {
                    executeQueries();
                } else {
                    queries.clear();
                }
            try {
                synchronized (lock) {
                    lock.wait(1000L);
                }
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    private void executeQueries() {
        while (!queries.isEmpty()) {
            Query query = queries.poll();
            String q = onPreQuery(query.query);
            if (q == null)
                continue;
            try (Statement statement = db.createStatement()) {
                boolean isSelect = statement.execute(q);
                try {
                    if (isSelect) {
                        if (query.callback != null) {
                            ResultSet rs = statement.getResultSet();
                            ((SelectCallback) query.callback).output(rs);
                            rs.close();
                        }
                    } else if (query.callback != null) {
                        ((UpdateCallback) query.callback).output(statement.getUpdateCount());
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Query " + q + " is failed!", e);
                }
                onPostQuery(q, true);
            } catch (Exception e) {
                onPostQuery(q, false);
                if (e.getMessage() != null && e.getMessage().contains("try restarting transaction")) {
                    queries.add(query);
                    LOGGER.warning(" Query " + q + " is failed! Restarting: " + e.getMessage());
                    continue;
                }
                LOGGER.severe("Query " + q + " is failed! Message: " + e.getMessage());
            }
        }
    }

    private boolean checkConnection() {
        boolean state = false;

        try {
            if (db != null && !isValid()) {
                safe(db::close);
                db = null;
            }
            if (db == null)
                connect();
            state = (db != null && isValid());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error while connecting to database: {0}", e.getMessage());
        }
        if (connected != state) {
            connected = state;
            if (!connected)
                onDisconnect();
        }

        return state;
    }

    private void connect() {
        try {
            String url = config.getUrl();
            if (useUnicode)
                url = addUnicodeParams(config.getUrl());
            db = DriverManager.getConnection(url, config.getUser(), config.getPass());
            if (isValid()) {
                LOGGER.info("MySQL connected.");
                onConnect();
            }
        } catch (SQLException ex) {
            LOGGER.warning(ex.getMessage());
        }
    }

    private String addUnicodeParams(String url) {
        if (url.contains("?")) {
            if (url.contains(UNICODE_PARAMS))
                return url;
            url = url + "&";
        } else {
            url = url + "?";
        }
        return url + UNICODE_PARAMS;
    }

    private boolean isValid() throws SQLException {
        return db.isValid(40);
    }

    public interface MysqlConfig {
        @NotNull
        String getUrl();

        @NotNull
        String getUser();

        @NotNull
        String getPass();
    }

    protected interface SafeRunnable {
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
        public @NotNull String getUrl() {
            return url;
        }

        public @NotNull String getUser() {
            return user;
        }

        public @NotNull String getPass() {
            return pass;
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

        public @NotNull String getUrl() {
            return url.get();
        }

        public @NotNull String getUser() {
            return user.get();
        }

        public @NotNull String getPass() {
            return pass.get();
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
