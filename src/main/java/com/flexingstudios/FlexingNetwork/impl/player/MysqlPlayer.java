package com.flexingstudios.FlexingNetwork.impl.player;

import org.bukkit.entity.Player;

import java.util.*;

public class MysqlPlayer extends FlexPlayer {
    public Set<String> ignored = new HashSet<>();
    public boolean ignoreAll = false;
    public String lastWriter = null;

    public MysqlPlayer(Player player) {
        super(player);
    }

    @Override
    public String getMeta(String key) {
        MetaValue value = meta.get(key);
        return value == null ? null : value.value;
    }

    @Override
    public void setMeta(String key, String value) {
        if (value == null) {
            removeMeta(key);
            return;
        }

        MetaValue metaValue = meta.computeIfAbsent(key, k -> new MetaValue(value));
        metaValue.changed = System.currentTimeMillis();
        metaValue.value = value;
        metaValue.saved = false;
    }

    @Override
    public boolean hasMeta(String key) {
        return meta.containsKey(key);
    }

    @Override
    public String removeMeta(String key) {
        MetaValue prev = meta.get(key);
        if (prev != null) {
            prev.value = null;
            prev.saved = false;
            prev.changed = System.currentTimeMillis();
        }

        return (prev == null) ? null : prev.value;
    }

    @Override
    public Map<String, String> getMetaMap() {
        HashMap<String, String> newMap = new HashMap<>(meta.size());
        for (Map.Entry<String, MetaValue> entry : meta.entrySet())
            newMap.put(entry.getKey(), (entry.getValue()).value);

        return newMap;
    }

    public void dispose() {
        meta.clear();
        lastWriter = null;
        ignored.clear();
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
