package com.flexingstudios.FlexingNetwork.impl;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Metrics;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class FMetrics implements Metrics {
    private static final int FLUSH_INTERVAL_TICKS = 24000;
    private final HashMap<String, Value> map;

    public FMetrics(FlexingNetworkPlugin plugin) {
        map = new HashMap<>();
        Value.class.getName();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::flush, 24000L, 24000L);
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
                FlexingNetwork.mysql().query("INSERT INTO `metrics` (`id`, `value`) VALUES ('" + entry.getKey() + "', " + entry.getValue().value + ") ON DUPLICATE KEY UPDATE `value` = `value` + " + entry.getValue().value);
            } else {
                FlexingNetwork.mysql().query("UPDATE `metrics` SET `value` = `value` + " + entry.getValue().value + " WHERE id = '" + entry.getKey() + "'");
            }
            entry.getValue().value = 0;
        }
    }

    private static class Value {

        private Value() {
            // no instances
        }

        boolean inserted = false;
        int value = 0;
    }
}
