package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.Commons.player.Leveling;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.player.*;
import com.flexingstudios.FlexingNetwork.api.util.Fireworks;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import gnu.trove.set.hash.TIntHashSet;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class FLPlayer implements NetworkPlayer {
    public static final ConcurrentHashMap<String, FLPlayer> PLAYERS = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, FLPlayer> IDS = new ConcurrentHashMap<>();
    public Map<String, MysqlPlayer.MetaValue> meta = new ConcurrentHashMap<>();
    private static final String ARROW_SELECTED = "arr.sel";
    private static final String ARROW_AVAILABLE = "arr.open";
    protected final FlexingNetworkPlugin plugin = FlexingNetworkPlugin.getInstance();
    public int id = -1;
    public final String username;
    public final Player player;
    public boolean loaded = false;
    public long loginTime = System.currentTimeMillis();
    public Rank rank = Rank.PLAYER;
    private ArrowTrail arrowTrail = null;
    private MessageOnJoin messageOnJoin = null;
    public TIntHashSet availableArrowTrails;
    public TIntHashSet availableJoinMessages;
    public Collectable settings;
    public Player lastDamager = null;
    public Entity lastDamagerEntity = null;
    public int lastDamagerPurgeTask = -1;
    public long lastDamageFromPlayer = 0L;
    public long lastDeath = 0L;
    public Language playerLanguage;
    public int coins = 0;
    public volatile int coinsAddBuffer = 0;
    public int level = 0;
    public int exp = 0;
    public int expBuffer = 0;
    public @Setter boolean restrict = false;

    FLPlayer(Player player) {
        this.player = player;
        id = -1;
        username = player.getName();
        availableArrowTrails = new TIntHashSet();
        availableJoinMessages = new TIntHashSet();
        settings = new Collectable(this, "settings", new boolean[] {true, true, true, true, true, true, false, true});
    }

    public void onMetaLoaded() {
        settings.load();
        loadArrows();
        loadMessages();
    }

    public void onMetaUpdate(String key, String value) {
        if (key.equals("settings")) {
            settings.load();
        } else if (key.equals("arr.sel") || key.equals("arr.open")) {
            loadArrows();
        } else if (key.equals("msg.sel") || key.equals("msg.open")) {
            loadMessages();
        }
    }

    public int addCoins(int amount) {
        addCoinsExact(amount);
        return amount;
    }

    public void addCoinsExact(int amount) {
        plugin.coins.addCoins(this, amount, false);
    }

    public int getCoins() {
        return coins;
    }

    public void takeCoins(int amount) {
        plugin.coins.takeCoins(this, amount, false);
    }

    public Rank getRank() {
        return rank;
    }

    public Player getBukkitPlayer() {
        return player;
    }

    public void toLobby() {
        FlexingNetwork.toLobby(player);
    }

    public void toServer(String id) {
        FlexingNetwork.toServer(id, player);
    }

    public String getName() {
        return username;
    }

    /**
     * @return player id
     */
    public int getId() {
        return id;
    }

    public String getPrefixedName() {
        String prefix = getPrefix();
        if (!prefix.isEmpty())
            return rank.getColor() + "[" + prefix + ChatColor.RESET + rank.getColor() + "] " + username + ChatColor.RESET;
        return rank.getColor() + username + ChatColor.RESET;
    }

    public String getPrefix() {
        String prefix = getMeta("prefix");
        return (prefix == null) ? rank.getPrefix() : prefix;
    }

    public String getColoredName() {
        return rank.getColor() + username + ChatColor.RESET;
    }

    public boolean isOnline() {
        return PLAYERS.containsKey(username);
    }

    public ArrowTrail getArrowTrail() {
        return arrowTrail;
    }

    public MessageOnJoin getMessageOnJoin() {
        return messageOnJoin;
    }

    public void setArrowTrail(ArrowTrail arrowTrail) {
        if (arrowTrail != this.arrowTrail) {
            this.arrowTrail = arrowTrail;
            if (arrowTrail == null) {
                removeMeta("arr.sel");
            } else {
                setMeta("arr.sel", arrowTrail.getId() + "");
            }
        }
    }

    public void setMessageOnJoin(MessageOnJoin msg) {
        if (msg != messageOnJoin) {
            messageOnJoin = msg;
            if (messageOnJoin == null) {
                removeMeta("msg.sel");
            } else {
                setMeta("msg.sel", messageOnJoin.getId() + "");
            }
        }
    }

    public void unlockArrowTrail(ArrowTrail trail) {
        if (availableArrowTrails.add(trail.getId()))
            saveAvailableArrowTrails();
    }

    public void unlockJoinMessage(MessageOnJoin msg) {
        if (availableJoinMessages.add(msg.getId()))
            saveAvailableMessagesOnJoin();
    }

    private void loadArrows() {
        String val = getMeta("arr.sel");
        if (val != null)
            try {
                arrowTrail = ArrowTrail.byId(Integer.parseInt(val));
            } catch (Exception ex) {
                plugin.getLogger().warning("[" + username + "] ArrowTrail " + val + " not exists [1]");
                removeMeta("arr.sel");
            }

        val = getMeta("arr.open");
        if (val != null) {
            boolean changed = false;
            for (String str : val.split(",")) {
                try {
                    availableArrowTrails.add(Integer.parseInt(str));
                } catch (Exception ex) {
                    plugin.getLogger().warning("[" + username + "] ArrowTrail " + val + " not exists [2]");
                    changed = true;
                }
            }
            if (changed)
                saveAvailableArrowTrails();
        }
    }

    private void loadMessages() {
        String val = getMeta("msg.sel");
        if (val != null)
            try {
                messageOnJoin = MessageOnJoin.byId(Integer.parseInt(val));
            } catch (Exception ex) {
                plugin.getLogger().warning("[" + username + "] MessageOnJoin " + val + " not exists [1]");
                removeMeta("msg.sel");
            }

        val = getMeta("msg.open");
        if (val != null) {
            boolean changed = false;
            for (String str : val.split(",")) {
                try {
                    availableJoinMessages.add(Integer.parseInt(str));
                } catch (Exception ex) {
                    plugin.getLogger().warning("[" + username + "] MessageOnJoin " + val + " not exists [2]");
                    changed = true;
                }
            }
            if (changed)
                saveAvailableMessagesOnJoin();
        }
    }

    private void saveAvailableArrowTrails() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int id : availableArrowTrails.toArray()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(id);
        }
        setMeta("arr.open", sb.toString());
    }

    private void saveAvailableMessagesOnJoin() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int id : availableJoinMessages.toArray()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(id);
        }
        setMeta("msg.open", sb.toString());
    }

    /**
     * @return player login time
     */
    public long getLoginTime() {
        return loginTime;
    }

    public int getLevel() {
        return level;
    }

    public int getTotalExp() {
        return exp;
    }

    public int getPartialExp() {
        return exp - Leveling.getTotalExp(level);
    }

    public void giveExp(int exp) {
        if (exp <= 0)
            return;

        this.exp += exp;
        expBuffer += exp;
        updateExp(exp);
    }

    public void giveExpExact(int exp) {
        if (exp <= 0)
            return;
        this.exp += exp;
        updateExp(exp);
    }

    public void updateExp(int given) {
        if (this.exp >= Leveling.getTotalExp(this.level + 1)) {
            this.level = Leveling.getLevel(this.exp);
            Utilities.msg(this.player, T.success("Flexing&f&lWorld", "Вы получили &3" + this.level + " &fуровень!"));
            boolean launchFirework = true;

            if (launchFirework)
                Fireworks.playRandom(player.getLocation());
        }
    }

    @Override
    public boolean getRestrict() {
        return restrict;
    }

    public boolean equals(Object obj) {
        return obj == this;
    }

    public int hashCode() {
        return username.hashCode();
    }

    public String toString() {
        return "FLPlayer{" +
                "name='" + username +
                "'}";
    }

    public static Function<Player, FLPlayer> CONSTRUCTOR = null;

    public static FLPlayer get(String player) {
        FLPlayer val = PLAYERS.get(player);

        if (val == null) {
            Player bukkitPlayer = Bukkit.getPlayerExact(player);
            if (bukkitPlayer == null)
                throw new IllegalArgumentException("Player with name '" + player + "' not exists on server");

            val = PLAYERS.computeIfAbsent(player, name -> CONSTRUCTOR.apply(bukkitPlayer));
        }

        return val;
    }

    public static FLPlayer get(Player player) {
        FLPlayer val = PLAYERS.get(player.getName());

        if (val == null)
            val = PLAYERS.computeIfAbsent(player.getName(), name -> CONSTRUCTOR.apply(player));

        return val;
    }
}
