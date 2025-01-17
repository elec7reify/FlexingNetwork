package com.flexingstudios.flexingnetwork.impl.player;

import com.flexingstudios.flexingnetwork.api.player.MessageOnJoin;
import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer;
import com.flexingstudios.common.player.Leveling;
import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.FlexingNetworkPlugin;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.ServerType;
import com.flexingstudios.flexingnetwork.api.player.ArrowTrail;
import com.flexingstudios.flexingnetwork.api.player.Collectable;
import com.flexingstudios.flexingnetwork.api.util.Fireworks;
import com.flexingstudios.flexingnetwork.api.util.Notifications;
import gnu.trove.set.hash.TIntHashSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class FlexPlayer implements NetworkPlayer {
    public static final ConcurrentHashMap<String, FlexPlayer> PLAYERS = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, FlexPlayer> IDS = new ConcurrentHashMap<>();
    public Map<String, MysqlPlayer.MetaValue> meta = new ConcurrentHashMap<>();
    private static final String ARROW_SELECTED = "arr.sel";
    private static final String ARROW_AVAILABLE = "arr.open";
    protected final FlexingNetworkPlugin plugin = FlexingNetworkPlugin.getInstance();
    public int id;
    public final @NotNull String username;
    public final @NotNull Player player;
    public boolean loaded = false;
    public long loginTime = System.currentTimeMillis();
    public Rank rank = Rank.PLAYER;
    private ArrowTrail arrowTrail = null;
    private MessageOnJoin messageOnJoin = null;
    public TIntHashSet availableArrowTrails;
    private final @NotNull TIntHashSet availableJoinMessages;
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
    public boolean restrict = false;

    FlexPlayer(Player player) {
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

    @Override
    public int addCoins(int amount) {
        addCoinsExact(amount);
        return amount;
    }

    @Override
    public void addCoinsExact(int amount) {
        plugin.coins.addCoins(this, amount, false);
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override
    public void takeCoins(int amount) {
        plugin.coins.takeCoins(this, amount, false);
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public Player getBukkitPlayer() {
        return player;
    }

    @Override
    public void toLobby() {
        FlexingNetwork.INSTANCE.toLobby(player);
    }

    @Override
    public void toServer(ServerType id) {
        FlexingNetwork.INSTANCE.toServer(id, player);
    }

    @Nullable
    @Override
    public String getName() {
        return username;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getPrefixedName() {
        String prefix = getPrefix();
        if (!prefix.isEmpty())
            return rank.getColor() + "[" + prefix + ChatColor.RESET + rank.getColor() + "] " + username + ChatColor.RESET;
        return rank.getColor() + username + ChatColor.RESET;
    }

    @Override
    public String getPrefix() {
        String prefix = getMeta("prefix");
        return (prefix == null) ? rank.getPrefix() : prefix;
    }

    @Override
    public String getColoredName() {
        return rank.getColor() + username + ChatColor.RESET;
    }

    @Override
    public boolean isOnline() {
        return PLAYERS.containsKey(username);
    }

    @Override
    public ArrowTrail getArrowTrail() {
        return arrowTrail;
    }

    @Override
    public MessageOnJoin getMessageOnJoin() {
        return messageOnJoin;
    }

    @Override
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

    @Override
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

    @Override
    public void unlockArrowTrail(ArrowTrail trail) {
        if (getAvailableArrowTrails().add(trail.getId()))
            saveAvailableArrowTrails();
    }

    @Override
    public void unlockJoinMessage(MessageOnJoin msg) {
        if (getAvailableJoinMessages().add(msg.getId()))
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
                    getAvailableArrowTrails().add(Integer.parseInt(str));
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
                    getAvailableJoinMessages().add(Integer.parseInt(str));
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
        for (int id : getAvailableArrowTrails().toArray()) {
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
        for (int id : getAvailableJoinMessages().toArray()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(id);
        }
        setMeta("msg.open", sb.toString());
    }

    @Override
    public void setRestrict(boolean flag) {
        restrict = flag;
    }

    @Override
    public TIntHashSet getAvailableArrowTrails() {
        return availableArrowTrails;
    }

    @Override
    public TIntHashSet getAvailableJoinMessages() {
        return availableJoinMessages;
    }

    @Override
    public long getLoginTime() {
        return loginTime;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getTotalExp() {
        return exp;
    }

    @Override
    public int getPartialExp() {
        return exp - Leveling.getTotalExp(level);
    }

    @Override
    public void giveExp(int exp) {
        if (exp <= 0) return;
        this.exp += exp;
        expBuffer += exp;
        updateExp(exp);
    }

    @Override
    public void giveExpExact(int exp) {
        if (exp <= 0) return;
        this.exp += exp;
        updateExp(exp);
    }

    public void updateExp(int given) {
        if (this.exp >= Leveling.getTotalExp(this.level + 1)) {
            this.level = Leveling.getLevel(this.exp);
            Notifications.success(player, "Flexing&f&lWorld", "Вы получили &3" + this.level + " &fуровень!");
            boolean launchFirework = true;

            if (launchFirework)
                Fireworks.playRandom(player.getLocation());
        }
    }

    @Override
    public boolean getRestrict() {
        return restrict;
    }

    public int hashCode() {
        return username.hashCode();
    }

    public String toString() {
        return "FLPlayer{" +
                "name='" + username +
                "'}";
    }

    public static Function<Player, FlexPlayer> CONSTRUCTOR = null;

    public static FlexPlayer get(String player) {
        FlexPlayer val = PLAYERS.get(player);

        if (val == null) {
            Player bukkitPlayer = Bukkit.getPlayerExact(player);
            if (bukkitPlayer == null)
                throw new IllegalArgumentException("Player with name '" + player + "' not exists on server");

            val = PLAYERS.computeIfAbsent(player, name -> CONSTRUCTOR.apply(bukkitPlayer));
        }

        return val;
    }

    public static FlexPlayer get(Player player) {
        FlexPlayer val = PLAYERS.get(player.getName());

        if (val == null)
            val = PLAYERS.computeIfAbsent(player.getName(), name -> CONSTRUCTOR.apply(player));

        return val;
    }
}
