package com.flexingstudios.FlexingNetwork.tasks;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class PlayerMetaSaver implements Runnable {
    private final FlexingNetworkPlugin plugin;

    public PlayerMetaSaver(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        for (FLPlayer player : getAllPlayers()) {
            plugin.getLogger().info("Type: getAllPlayers");
            for (Map.Entry<String, MysqlPlayer.MetaValue> entry : new MysqlPlayer(player.getBukkitPlayer()).meta.entrySet()) {
                plugin.getLogger().info("Type: new MysqlPlayer");
                if (!entry.getValue().saved && time - entry.getValue().changed > 5000L)
                    plugin.getLogger().info("Type: save");
                    save((MysqlPlayer) player, entry);
            }
        }
    }

    private Collection<FLPlayer> getAllPlayers() {
        return FLPlayer.PLAYERS.values();
    }

    public void saveNow(MysqlPlayer player) {
        for (Map.Entry<String, MysqlPlayer.MetaValue> entry : player.meta.entrySet()) {
            if (!(entry.getValue()).saved)
                save(player, entry);
        }
    }

    public void finish() {
        for (FLPlayer flPlayer : getAllPlayers()) {
            saveNow((MysqlPlayer) flPlayer);
        }
    }

    private void save(MysqlPlayer player, Map.Entry<String, MysqlPlayer.MetaValue> entry) {
        MysqlPlayer.MetaValue value = entry.getValue();

        if (value.value == null) {
            plugin.getLogger().info("Type: value == null");
            if (value.prev != null)
                plugin.mysql.query("DELETE FROM `users_meta` WHERE `userid` = " + player.id + " AND `key` = '" + entry.getKey() + "'");
            player.meta.remove(entry.getKey());
        } else {
            plugin.getLogger().info("Type: value == ???");
            String escaped = StringEscapeUtils.escapeSql(value.value);
            if (value.prev == null) {
                plugin.mysql.query("INSERT INTO `users_meta` (`userid`, `key`, `value`) VALUES (" + player.id + ", '" + entry.getKey() + "', '" + escaped + "')");
            } else if (!value.value.equals(value.prev)) {
                plugin.mysql.query("UPDATE `users_meta` SET `value` = '" + escaped + "' WHERE userid = " + player.id + " AND `key` = '" + entry.getKey() + "'");
            }
            value.prev = value.value;
            value.saved = true;
        }
    }
}
