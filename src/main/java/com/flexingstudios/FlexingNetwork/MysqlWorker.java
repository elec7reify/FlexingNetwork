package com.flexingstudios.FlexingNetwork;

import com.flexingstudios.Commons.player.Leveling;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.event.PlayerLoadedEvent;
import com.flexingstudios.FlexingNetwork.api.mysql.MysqlThread;
import com.flexingstudios.FlexingNetwork.api.mysql.SelectCallback;
import com.flexingstudios.FlexingNetwork.api.mysql.UpdateCallback;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import com.flexingstudios.Commons.F;
import com.mysql.jdbc.log.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Logger;

public class MysqlWorker extends MysqlThread {
    private final FlexingNetworkPlugin plugin;
    private int queryCounter = 0;
    private long queryStartTime = 0L;
    private Map<String, MysqlPlayer.MetaValue> metaMap = new ConcurrentHashMap<>();

    MysqlWorker(FlexingNetworkPlugin plugin) {
        super(plugin, new MysqlThread.MysqlConfigSupplier(() -> plugin.config.mysqlUrl, () -> plugin.config.mysqlUsername, () -> plugin.config.mysqlPassword));
        useUnicode();
        this.plugin = plugin;
        SelectCallback.class.getName();
        UpdateCallback.class.getName();
    }

    @Override
    protected void onConnect() {
        for (Player player : Bukkit.getOnlinePlayers())
            addLoadPlayer(FLPlayer.get(player));
    }

    @Override
    protected void onDisconnect() {
        for (FLPlayer player : FLPlayer.PLAYERS.values()) {
            player.coins = 0;
        }
    }

    @Override
    protected String onPreQuery(String query) {
        if (Debug.MYSQL.isEnabled())
            queryStartTime = System.nanoTime();
        return query;
    }

    @Override
    protected void onPostQuery(String query, boolean success) {
        if (success)
            queryCounter++;
        if (Debug.MYSQL.isEnabled())
            logger.info("- [" + F.formatFloat((float)(System.nanoTime() - queryStartTime) / 1000000.0F, 1) + " ms.] " + query);
    }

    public int getExecutedQueries() {
        return queryCounter;
    }

    void addLoadPlayer(FLPlayer player) {
        loadPlayer(player);
    }

    private void loadPlayer(FLPlayer player) {
        if (!player.isOnline())
            return;
        select("SELECT id, coins, exp, status FROM authme WHERE username = '" + player.getName() + "'", rs -> {
            if (rs.next()) {
                player.id = rs.getInt("id");
                player.rank = Rank.getRank(rs.getString("status"));
                player.coins += rs.getInt("coins");
                player.exp = rs.getInt("exp");
                player.level = Leveling.getLevel(player.exp);
                select("SELECT `key`, `value` FROM `users_meta` WHERE `userid` = " + player.id, rs1 -> {
                    String key;
                    MysqlPlayer.MetaValue value;
                    while (rs1.next()) {
                        key = rs1.getString("key");
                        value = new MysqlPlayer.MetaValue(rs1.getString("value"));
                        value.saved = true;
                        value.prev = value.value;
                        player.meta.put(key, value);
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new LoadFinishRunnable(player));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (!player.isOnline()) {
                            Bukkit.getPluginManager().callEvent(new PlayerLoadedEvent(player));
                        }
                    }, 1L);
                });
            } else {
                query("INSERT INTO authme (username, coins) VALUES ('" + player.getName() + "', 0)");
                select("SELECT `id` FROM authme WHERE username = '" + player.getName() + "'", rs1 -> {
                    rs1.next();
                    player.id = rs1.getInt("id");
                    return;
                });
                player.coins = 0;
            }
        });
    }

    public class LoadFinishRunnable implements Runnable {
        private MysqlPlayer player;

        public LoadFinishRunnable(FLPlayer player) {
            this.player = (MysqlPlayer) player;
        }

        @Override
        public void run() {
            if (!player.isOnline())
                return;
            player.onMetaLoaded();
            if (player.rank == Rank.PLAYER)
                return;
            if ((FlexingNetwork.features()).CHANGE_PLAYER_LIST_NAMES.isEnabled()) {
                String name = player.rank.getColor() + player.rank.getName() + " " + player.username;
                if (name.length() > 16)
                    name = name.substring(0, 15);
                player.player.setPlayerListName(name);
            }
//            player.player.setDisplayName(player.getPrefixedName());
            player.player.addAttachment(MysqlWorker.this.plugin).setPermission("flexingworld." + player.rank.name().toLowerCase(), true);
            if (player.getMeta("pm-ignore") != null)
                player.ignoreAll = true;
        }
    }
}
