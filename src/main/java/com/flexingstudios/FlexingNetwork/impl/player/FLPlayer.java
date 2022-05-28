package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.Commons.player.Leveling;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.player.ArrowTrail;
import com.flexingstudios.FlexingNetwork.api.player.Collectable;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Fireworks;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.mes;
import gnu.trove.set.hash.TIntHashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class FLPlayer implements NetworkPlayer {
    public static final ConcurrentHashMap<String, FLPlayer> PLAYERS = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, FLPlayer> IDS = new ConcurrentHashMap<>();
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
    public TIntHashSet availableArrowTrails;
    public Collectable settings;
    public Player lastDamager = null;
    public Entity lastDamagerEntity = null;
    public int lastDamagerPurgeTask = -1;
    public long lastDamageFromPlayer = 0L;
    public long lastDeath = 0L;
    public int coins = 0;
    public volatile int coinsAddBuffer = 0;
    public int level = 0;
    public int exp = 0;
    public int expBuffer = 0;

    FLPlayer(Player player) {
        this.player = player;
        this.username = player.getName();
        this.availableArrowTrails = new TIntHashSet();
        this.settings = new Collectable(this, "settings", new boolean[] {true, true, true, true, true, true, false, true});
    }

    public void onMetaLoaded() {
        this.settings.load();
        loadArrows();
    }

    public void onMetaUpdate(String key, String value) {
        if (key.equals("settings")) {
            this.settings.load();
        } else if (key.equals("arr.sel") || key.equals("arr.open")) {
            loadArrows();
        }
    }

    public int addCoins(int amount) {
        addCoinsExact(amount);
        return amount;
    }

    public void addCoinsExact(int amount) {
        this.plugin.coins.addCoins(this, amount, false);
    }

    public int getCoins() {
        return this.coins;
    }

    public void takeCoins(int amount) {
        this.plugin.coins.takeCoins(this, amount, false);
    }

    public Rank getRank() {
        return this.rank;
    }

    public Player getBukkitPlayer() {
        return this.player;
    }

    public void toLobby() {
        FlexingNetwork.toLobby(this.player);
    }

    public void toServer(String id) {
        FlexingNetwork.toServer(id, this.player);
    }

    public String getName() {
        return this.username;
    }

    public int getId() {
        return this.id;
    }

    public String getPrefixedName() {
        String prefix = getPrefix();
        if (!prefix.isEmpty())
            return this.rank.getColor() + "[" + prefix + ChatColor.RESET + this.rank.getColor() + "] " + this.username + ChatColor.RESET;
        return this.rank.getColor() + this.username + ChatColor.RESET;
    }

    public String getPrefix() {
        String prefix = getMeta("prefix");
        return (prefix == null) ? this.rank.getPrefix() : prefix;
    }

    public String getColoredName() {
        return this.rank.getColor() + this.username + ChatColor.RESET;
    }

    public boolean isOnline() {
        return PLAYERS.containsKey(this.username);
    }

    public ArrowTrail getArrowTrail() {
        return this.arrowTrail;
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

    public void unlockArrowTrail(ArrowTrail trail) {
        if (this.availableArrowTrails.add(trail.getId()))
            saveAvailableArrowTrails();
    }

    private void loadArrows() {
        String val = getMeta("arr.sel");
        if (val != null)
            try {
                this.arrowTrail = ArrowTrail.byId(Integer.parseInt(val));
            } catch (Exception ex) {
                this.plugin.getLogger().warning("[" + this.username + "] ArrowTrail " + val + " not exists [1]");
                removeMeta("arr.sel");
            }
        val = getMeta("arr.open");
        if (val != null) {
            boolean changed = false;
            for (String str : val.split(",")) {
                try {
                    this.availableArrowTrails.add(Integer.parseInt(str));
                } catch (Exception ex) {
                    this.plugin.getLogger().warning("[" + this.username + "] ArrowTrail " + val + " not exists [2]");
                    changed = true;
                }
            }
            if (changed)
                saveAvailableArrowTrails();
        }
    }

    private void saveAvailableArrowTrails() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int id : this.availableArrowTrails.toArray()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(id);
        }
        setMeta("arr.open", sb.toString());
    }

    public long getLoginTime() {
        return this.loginTime;
    }

    public int getLevel() {
        return this.level;
    }

    public int getTotalExp() {
        return this.exp;
    }

    public int getPartialExp() {
        return this.exp - Leveling.getTotalExp(this.level);
    }

    public void giveExp(int exp) {
        if (exp <= 0)
            return;
        this.exp += exp;
        this.expBuffer += exp;
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
            mes.msg(this.player, T.success("Flexing&f&lWorld", "Вы получили &3" + this.level + " &fуровень!"));
            boolean launchFirework = true;

            if (launchFirework)
                Fireworks.playRandom(this.player.getLocation());
        }
    }

    public boolean equals(Object obj) {
        return (obj == this);
    }

    public int hashCode() {
        return this.username.hashCode();
    }

    public String toString() {
        return "FLPlayer{name='" + this.username + "'}";
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
