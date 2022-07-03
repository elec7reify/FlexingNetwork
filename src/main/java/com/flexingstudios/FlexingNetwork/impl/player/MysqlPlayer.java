package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
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

    @Override
    public void setLanguage(UUID uuid, String iso) {
        //Player p = Bukkit.getPlayer(uuid);
        Player player = Bukkit.getPlayer(uuid);
        FLPlayer flPlayer = FLPlayer.get(player);
        FlexingNetwork.mysql().query("UPDATE authme SET language = '" + iso + "' WHERE username = '" + player.getName() + "'");
    }

    @Override
    public String getLanguage(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        FLPlayer flPlayer = FLPlayer.get(p.getPlayer());
        FlexingNetwork.mysql().query("SELECT language FROM authme WHERE id = " + flPlayer.getId());

        return Language.getDefaultLanguage().getIso();
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
