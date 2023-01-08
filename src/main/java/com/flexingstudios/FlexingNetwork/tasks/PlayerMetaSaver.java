package com.flexingstudios.FlexingNetwork.tasks;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.impl.player.FlexPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Collection;
import java.util.Map;

public class PlayerMetaSaver implements Runnable {
    private final FlexingNetworkPlugin plugin;

    public PlayerMetaSaver(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        for (FlexPlayer player : getAllPlayers()) {
            for (Map.Entry<String, MysqlPlayer.MetaValue> entry : player.meta.entrySet()) {
                if (!entry.getValue().saved && time - entry.getValue().changed > 5000L) {
                    save(player, entry);
                }
            }
        }
    }

    private Collection<FlexPlayer> getAllPlayers() {
        return FlexPlayer.PLAYERS.values();
    }

    public void saveNow(FlexPlayer player) {
        for (Map.Entry<String, MysqlPlayer.MetaValue> entry : player.meta.entrySet()) {
            if (!entry.getValue().saved)
                save(player, entry);
        }
    }

    public void finish() {
        getAllPlayers().forEach(this::saveNow);
    }

    private void save(FlexPlayer player, Map.Entry<String, MysqlPlayer.MetaValue> entry) {
        MysqlPlayer.MetaValue value = entry.getValue();

        if (value.value == null) {
            if (value.prev != null)
                plugin.mysql.query("DELETE FROM `users_meta` WHERE `userid` = " + player.getId() + " AND `key` = '" + entry.getKey() + "'");
            player.meta.remove(entry.getKey());
        } else {
            String escaped = StringEscapeUtils.escapeSql(value.value);
            if (value.prev == null) {
                plugin.mysql.query("INSERT INTO `users_meta` (`userid`, `key`, `value`) VALUES (" + player.getId() + ", '" + entry.getKey() + "', '" + escaped + "')");
            } else if (!value.value.equals(value.prev)) {
                plugin.mysql.query("UPDATE `users_meta` SET `value` = '" + escaped + "' WHERE userid = " + player.getId() + " AND `key` = '" + entry.getKey() + "'");
            }
            value.prev = value.value;
            value.saved = true;
        }
    }
}
