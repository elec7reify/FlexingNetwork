package com.flexingstudios.flexingnetwork.impl;

import com.flexingstudios.flexingnetwork.FlexingNetworkPlugin;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.Metrics;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class FlexMetric implements Metrics {
    private static final int FLUSH_INTERVAL_TICKS = 24000;
    private final HashMap<String, Value> map;

    public FlexMetric(FlexingNetworkPlugin plugin) {
        map = new HashMap<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::flush, FLUSH_INTERVAL_TICKS, FLUSH_INTERVAL_TICKS);
    }

    @Override
    public void add(String key, int amount) {
        map.computeIfAbsent(key, k -> new Value()).value += amount;
    }

    public void flush() {
        for (Map.Entry<String, Value> entry : map.entrySet()) {
            if (entry.getValue().value == 0)
                continue;
            if (!entry.getValue().inserted) {
                entry.getValue().inserted = true;
                FlexingNetwork.INSTANCE.mysql().query("INSERT INTO `metrics` (`id`, `value`) VALUES ('" + entry.getKey() + "', " + entry.getValue().value + ") ON DUPLICATE KEY UPDATE `value` = `value` + " + entry.getValue().value);
            } else {
                FlexingNetwork.INSTANCE.mysql().query("UPDATE `metrics` SET `value` = `value` + " + entry.getValue().value + " WHERE id = '" + entry.getKey() + "'");
            }
            entry.getValue().value = 0;
        }
    }

    private static class Value {
        public Value() {
        }

        boolean inserted = false;
        int value = 0;
    }
}
