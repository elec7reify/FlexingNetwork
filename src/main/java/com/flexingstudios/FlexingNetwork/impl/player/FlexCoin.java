package com.flexingstudios.flexingnetwork.impl.player;

import com.flexingstudios.flexingnetwork.FlexingNetworkPlugin;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class FlexCoin {
    private final FlexingNetworkPlugin plugin;
    private volatile boolean waiting;

    public FlexCoin(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
        waiting = true;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::flush, 200L, 200L);
    }

    private void flush() {
        waiting = false;
        int total = 0;
        TIntObjectMap<TIntLinkedList> map = new TIntObjectHashMap<>();
        for (FlexPlayer player : FlexPlayer.PLAYERS.values()) {
            if (player.coinsAddBuffer != 0) {
                total += player.coinsAddBuffer;
                TIntLinkedList list = map.get(player.coinsAddBuffer);
                if (list == null)
                    map.put(player.coinsAddBuffer, list = new TIntLinkedList());
                list.add(player.id);
                player.coinsAddBuffer = 0;
            }
        }
        FlexingNetwork.INSTANCE.metrics().add("coins.added", total);
        waiting = true;
        TIntObjectIterator<TIntLinkedList> it = map.iterator();
        while (it.hasNext()) {
            it.advance();
            String ids = it.value().toString();
            plugin.mysql.query("UPDATE authme SET coins=coins+" + it.key() + " WHERE id IN (" + ids.substring(1, ids.length() - 1) + ")");
        }
        map.clear();
    }

    public void saveNow(FlexPlayer player) {
        if (!waiting)
            return;
        if (player.coinsAddBuffer > 0) {
            int amount = player.coinsAddBuffer;
            player.coinsAddBuffer = 0;
            plugin.mysql.query("UPDATE authme SET coins=coins+" + amount + " WHERE id = " + player.id);
        }
    }

    public int addCoins(FlexPlayer player, int amount, boolean simulate) {
        if (player == null || amount < 1)
            return -1;

        try {
            if (!simulate) {
                if (waiting) {
                    player.coinsAddBuffer += amount;
                    plugin.mysql.query("UPDATE authme SET coins=coins+" + amount + " WHERE id = " + player.id);
                }
                player.coins += amount;
            }
            player.player.sendMessage(ChatColor.GREEN + "Вы получили " + amount + " Флекс-коинов");
            return amount;
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, null, e);
            return -1;
        }
    }

    public void takeCoins(FlexPlayer player, int amount, boolean simulate) {
        if (amount < 1)
            return;
        if (!simulate) {
            player.coins -= amount;
            plugin.mysql.query("UPDATE authme SET coins=coins-" + amount + " WHERE id = " + player.id);
        }
    }

    public void finish() {
        flush();
        waiting = false;
    }
}
