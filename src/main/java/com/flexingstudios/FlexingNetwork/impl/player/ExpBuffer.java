package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Bukkit;

public class ExpBuffer {
    private final FlexingNetworkPlugin plugin;

    public ExpBuffer(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::flush, 200L, 200L);
    }

    private void flush() {
        int total = 0;
        TIntObjectHashMap tIntObjectHashMap = new TIntObjectHashMap();
        for (FLPlayer player : FLPlayer.PLAYERS.values()) {
            if (player.expBuffer > 0) {
                total += player.expBuffer;
                TIntLinkedList list = (TIntLinkedList) tIntObjectHashMap.get(player.expBuffer);
                if (list == null)
                    tIntObjectHashMap.put(player.expBuffer, list = new TIntLinkedList());
                list.add(player.id);
                player.expBuffer = 0;
            }
        }
        FlexingNetwork.metrics().add("exp.added", total);
        TIntObjectIterator<TIntLinkedList> it = tIntObjectHashMap.iterator();
        while (it.hasNext()) {
            it.advance();
            this.plugin.mysql.query("UPDATE authme SET exp=exp+" + it.key() + " WHERE id IN (" + it.value().toString().substring(1, it.value().toString().length() - 1) + ")");
        }
        tIntObjectHashMap.clear();
    }

    public void saveNow(FLPlayer player) {
        if (player.expBuffer != 0) {
            this.plugin.mysql.query("UPDATE authme SET exp=exp+" + player.expBuffer + " WHERE id = " + player.id);
            player.expBuffer = 0;
        }
    }

    public void finish() {
        flush();
    }
}
