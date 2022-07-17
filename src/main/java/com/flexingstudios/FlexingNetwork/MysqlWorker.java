package com.flexingstudios.FlexingNetwork;

import com.flexingstudios.Commons.player.Leveling;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.mysql.MysqlThread;
import com.flexingstudios.FlexingNetwork.api.mysql.SelectCallback;
import com.flexingstudios.FlexingNetwork.api.mysql.UpdateCallback;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import com.flexingstudios.Commons.F;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MysqlWorker extends MysqlThread {
    private final FlexingNetworkPlugin plugin;
    private int queryCounter = 0;
    private long queryStartTime = 0L;

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
        loadPlayer((MysqlPlayer) player);
        player.onMetaLoaded();
    }

    private void loadPlayer(MysqlPlayer player) {
        if (!player.isOnline())
            return;
        select("SELECT id, coins, exp, status FROM authme WHERE username = '" + player.getName() + "'", rs -> {
            if (rs.next()) {
                player.id = rs.getInt("id");
                player.rank = Rank.getRank(rs.getString("status"));
                player.coins += rs.getInt("coins");
                player.exp = rs.getInt("exp");
                player.level = Leveling.getLevel(player.exp);
                //player.playerLanguage = Language.getPlayerLanguage(player.player.getUniqueId());
                select("SELECT `key`, `value` FROM `users_meta` WHERE `userid` = " + player.getId(), null);
            } else {
                query("INSERT INTO authme (username, coins) VALUES ('" + player.getName() + "', 0)");
                select("SELECT `id` FROM authme WHERE username = '" + player.getName() + "'", null);
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
            player.player.setDisplayName(this.player.getPrefixedName());
            player.player.addAttachment(MysqlWorker.this.plugin).setPermission("flexingworld." + player.rank.name().toLowerCase(), true);
            if (player.getMeta("pm-ignore") != null)
                player.ignoreAll = true;
        }
    }
}
