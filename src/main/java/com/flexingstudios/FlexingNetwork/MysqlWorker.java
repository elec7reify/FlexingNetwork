package com.flexingstudios.FlexingNetwork;

import com.flexingstudios.Common.player.Leveling;
import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.event.PlayerLoadedEvent;
import com.flexingstudios.FlexingNetwork.api.mysql.MysqlThread;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FlexPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import com.flexingstudios.Common.F;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MysqlWorker extends MysqlThread {
    private final FlexingNetworkPlugin plugin;
    private int queryCounter = 0;
    private long queryStartTime = 0L;

    MysqlWorker(FlexingNetworkPlugin plugin) {
        super(plugin, new MysqlThread.MysqlConfigSupplier(() -> plugin.config.mysqlUrl, () -> plugin.config.mysqlUsername, () -> plugin.config.mysqlPassword));
        useUnicode();
        this.plugin = plugin;
    }

    @Override
    protected void onConnect() {
        for (Player player : Bukkit.getOnlinePlayers())
            addLoadPlayer(FlexPlayer.get(player));
    }

    @Override
    protected void onDisconnect() {
        for (FlexPlayer player : FlexPlayer.PLAYERS.values()) {
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

    void addLoadPlayer(FlexPlayer player) {
        select("SELECT `username`, banto, banfrom, reason, `admin` FROM bans WHERE status = 1 AND username = '" + player.getName() + "'", rs -> {
            if (rs.next()) {
                long currtime = System.currentTimeMillis();
                long banto = rs.getLong("banto");
                long banfrom = rs.getLong("banfrom");
                String username = rs.getString("username");
                String reason = rs.getString("reason");
                String admin = rs.getString("admin");

                if (banto > 0L && banto < currtime) {
                    query("UPDATE bans SET status = 0 WHERE username = '" + player.getName() + "'");
                } else {
                    String bantime = banto == 0L ? "навсегда" : F.formatSecondsShort((int) TimeUnit.MILLISECONDS.toSeconds(banto - banfrom + 1)) + "";
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            player.player.kickPlayer(Utilities.colored(T.BanMessage(player.player)
                            .replace("{player}", username)
                            .replace("{reason}", reason)
                            .replace("{admin}", admin)
                            .replace("{time}", bantime)
                            .replace("{date}", new SimpleDateFormat(Messages.DATE_FORMAT)
                                    .format(new Date(System.currentTimeMillis()))))));
                    return;
                }
            }

            loadPlayer(player);
        });
    }

    private void loadPlayer(FlexPlayer player) {
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
                    while (rs1.next()) {
                        String key = rs1.getString("key");
                        MysqlPlayer.MetaValue value = new MysqlPlayer.MetaValue(rs1.getString("value"));
                        value.saved = true;
                        value.prev = value.value;
                        player.meta.put(key, value);
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new LoadFinishRunnable(player));
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (player.isOnline()) {
                            Bukkit.getPluginManager().callEvent(new PlayerLoadedEvent(player));
                        }
                    }, 1L);
                });
            } else {
                query("INSERT INTO authme (username, coins) VALUES ('" + player.getName() + "', 0)");
                select("SELECT `id` FROM authme WHERE username = '" + player.getName() + "'", rs1 -> {
                    rs1.next();
                    player.id = rs1.getInt("id");
                });
                player.coins = 0;
            }
        });
    }

    public class LoadFinishRunnable implements Runnable {
        private final MysqlPlayer player;

        public LoadFinishRunnable(FlexPlayer player) {
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
