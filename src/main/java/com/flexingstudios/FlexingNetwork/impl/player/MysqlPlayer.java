package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MysqlPlayer extends FLPlayer {
    private FlexingNetworkPlugin plugin;
    public Set<String> ignored = new HashSet<>();
    public boolean ignoreAll = false;
    public String lastWriter = null;
    public Map<String, MetaValue> meta = new ConcurrentHashMap<>();

    public MysqlPlayer(Player player) {
        super(player);
    }

    public MysqlPlayer(FlexingNetworkPlugin plugin) {
        super(null);
        this.plugin = plugin;
    }

    @Override
    public String getMeta(String key) {
        MetaValue value = this.meta.get(key);
        return (value == null) ? null : value.value;
    }

    @Override
    public void setMeta(String key, String value) {
        if (value == null) {
            removeMeta(key);
            return;
        }
        MetaValue metaValue = this.meta.computeIfAbsent(key, k -> new MetaValue(value));
        metaValue.changed = System.currentTimeMillis();
        metaValue.value = value;
        metaValue.saved = false;
    }

    @Override
    public boolean hasMeta(String key) {
        return this.meta.containsKey(key);
    }

    @Override
    public String removeMeta(String key) {
        MetaValue prev = this.meta.get(key);
        if (prev != null) {
            prev.value = null;
            prev.saved = false;
            prev.changed = System.currentTimeMillis();
        }
        return (prev == null) ? null : prev.value;
    }

    @Override
    public Map<String, String> getMetaMap() {
        HashMap<String, String> newMap = new HashMap<>(this.meta.size());
        for (Map.Entry<String, MetaValue> entry : this.meta.entrySet())
            newMap.put(entry.getKey(), (entry.getValue()).value);
        return newMap;
    }

    public void dispose() {
        this.meta.clear();
        this.lastWriter = null;
        this.ignored.clear();
    }

    public static class MetaValue {
        public String value;
        public String prev;
        public long changed;
        public boolean saved = false;

        public MetaValue(String value) {
            this.value = value;
        }
    }
}
