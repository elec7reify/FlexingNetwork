package com.flexingstudios.FlexingNetwork.api.menu;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.Particles;
import net.minecraft.server.v1_12_R1.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TrailMenu implements InvMenu {
    private static boolean ignoreInvisibility = false;
    private static boolean enabled = false;
    private static String PREFIX = null;
    private static String TABLE = null;
    private static Map<String, TrailPlayer> PLAYERS;
    private final Inventory inv = Bukkit.getServer().createInventory(this, 18, "Выбор следа");

    public TrailMenu(Player player) {
        NetworkPlayer networkPlayer = FlexingNetwork.getPlayer(player.getName());
        TrailPlayer trailPlayer = PLAYERS.get(player.getName());
        for (Trail trail : Trail.values())
            inv.setItem(trail.slot, trail.withPrice(trailPlayer, networkPlayer));
    }

    @Override
    public void onClick(ItemStack is, Player bukkitPlayer, int slot, ClickType clickType) {
        Trail finded = null;
        for (Trail t : Trail.values()) {
            if (t.slot == slot) {
                finded = t;
                break;
            }
        }

        if (finded == null)
            return;
        Trail trail = finded;
        TrailPlayer player = PLAYERS.get(bukkitPlayer.getName());
        NetworkPlayer networkPlayer = FlexingNetwork.getPlayer(bukkitPlayer.getName());

        if (player.active != trail) {
            if (networkPlayer.getRank().has(trail.rank))
                if (trail.price == -1 || networkPlayer.getRank() == Rank.ADMIN || player.owned.contains(trail)) {
                    player.active = trail;
                    bukkitPlayer.closeInventory();
                } else if (networkPlayer.getCoins() >= trail.price) {
                    networkPlayer.takeCoins(trail.price);
                    FlexingNetwork.mysql().query("INSERT INTO `" + TABLE + "` (`userid`, `trail`) VALUES (" + networkPlayer.getId() + ", '" + trail.name() + "')");
                    player.owned.add(trail);

                    for (Trail t : Trail.values()) {
                        if (t == player.active) {
                            player.active = trail;
                            inv.setItem(t.slot, t.withPrice(player, networkPlayer));
                            break;
                        }
                    }

                    player.active = trail;
                    this.inv.setItem(trail.slot, trail.withPrice(player, networkPlayer));
                    FlexingNetwork.metrics().add(PREFIX + ".buy.trail", trail.price);
                }
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public static void init(JavaPlugin plugin, String prefix) {
        PLAYERS = new ConcurrentHashMap<>();
        PREFIX = prefix;
        TABLE = prefix + "_trails";
        enabled = true;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new TrailTask(), 20L, 1L);
        Bukkit.getPluginManager().registerEvents(new TrailListener(plugin), plugin);
        //FlexingNetwork.mysql().query("CREATE TABLE IF NOT EXISTS `" + TABLE + "` (`userid` int(11) NOT NULL, `trail` varchar(20) NOT NULL,  PRIMARY KEY (`userid`,`trail`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;");
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static TrailPlayer getPlayer(String player) {
        return PLAYERS.get(player);
    }

    public static void setIgnoreInvisibility(boolean flag) {
        ignoreInvisibility = flag;
    }

    public enum Trail {
        HEARTS("Разбитые сердца", 0, new ItemStack(Material.INK_SACK, 1, (short) 1), 50, Rank.ADMIN);

        private ItemStack is;
        final Rank rank;
        final String displayName;
        final int slot;
        final int price;

        Trail(String displayName, int slot, ItemStack is, int price, Rank rank) {
            this.displayName = displayName;
            this.slot = slot;
            this.is = Items.name(is, "След: " + displayName, new String[0]);
            this.price = price;
            this.rank = rank;
        }

        public ItemStack withPrice(TrailMenu.TrailPlayer player, NetworkPlayer networkPlayer) {
            String status = null;
            if (player.active == this) {
                status = "&aАктивно";
            } else if (networkPlayer.getRank() == Rank.ADMIN || (networkPlayer.getRank().has(rank) && (price == -1 || player.owned.contains(this)))) {
                status = "&bДоступно";
            } else if (this.price != -1 && networkPlayer.getCoins() >= this.price) {
                status = "Купить: " + this.price;
            } else {
                status = "Цена: " + this.price;
            }
            return Items.appendLore(is.clone(), "&7---------------", status);
        }
    }

    public static class TrailPlayer {
        public final Player bukkit;
        public final Set<Trail> owned;
        public TrailMenu.Trail active = null;
        public boolean visible = true;
        int ticks = 0;
        boolean walking = false;
        int standing = 0;
        Location lastLoc;

        private TrailPlayer(Player bukkit) {
            this.bukkit = bukkit;
            this.owned = EnumSet.noneOf(TrailMenu.Trail.class);
            this.ticks = 0;
        }

        public void setActive(String name) {
            if (name == null)
                return;
            try {
                TrailMenu.Trail type = TrailMenu.Trail.valueOf(name.toUpperCase());
                Rank rank = FlexingNetwork.getPlayer(this.bukkit).getRank();
                if (this.owned.contains(type) || rank == Rank.ADMIN || rank.has(type.rank))
                    this.active = type;
            } catch (Exception exception) {}
        }

        public String getActiveMysqlString() {
            return (this.active == null) ? "NULL" : ("'" + this.active.name() + "'");
        }

        public String getActive() {
            return (this.active == null) ? null : this.active.name();
        }
    }

    private static class TrailListener implements Listener {
        private final JavaPlugin plugin;

        public TrailListener(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerLoaded(PlayerJoinEvent event) {
            NetworkPlayer flplayer = FlexingNetwork.getPlayer(event.getPlayer());
            TrailMenu.TrailPlayer player = new TrailMenu.TrailPlayer(event.getPlayer());
            TrailMenu.PLAYERS.put(event.getPlayer().getName(), player);
            FlexingNetwork.mysql().select("SELECT trail FROM `" + TrailMenu.TABLE + "` WHERE `userid` = " + flplayer.getId(), rs -> {
                while (rs.next()) {
                    try {
                        player.owned.add(TrailMenu.Trail.valueOf(rs.getString("trail").toUpperCase()));
                    } catch (Exception exception) {}
                }
            });
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onPlayerLeave(PlayerBedLeaveEvent event) {
            TrailMenu.PLAYERS.remove(event.getPlayer().getName());
        }

        @EventHandler
        public void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin().equals(this.plugin))
                TrailMenu.enabled = false;
        }
    }

    private static class TrailTask implements Runnable {
        private TrailTask() {}

        public void run() {
            boolean hideInvis = !TrailMenu.ignoreInvisibility;
            for (TrailMenu.TrailPlayer player : TrailMenu.PLAYERS.values()) {
                float a, x, f1, y, z, f2;
                if (player.active == null || !player.visible || player.bukkit.isDead())
                    continue;
                if (hideInvis && player.bukkit.hasPotionEffect(PotionEffectType.INVISIBILITY))
                    continue;
                player.ticks++;
                Location loc = player.bukkit.getLocation();
                if (equalsLoc(loc, player.lastLoc)) {
                    player.standing++;
                    if (player.standing >= 2)
                        player.walking = false;
                } else {
                    player.lastLoc = loc;
                    player.standing = 0;
                    player.walking = true;
                }
                World w = loc.getWorld();
                switch (player.active) {
                    case HEARTS:
                        if (player.ticks % 3 != 0)
                            continue;
                        a = -player.ticks * 0.2F;
                        x = MathHelper.sin(a) * 0.5F;
                        f1 = MathHelper.cos(a) * 0.5F;
                        Particles.HEART.play(w, (float)loc.getX() + x, (float)loc.getY() + 2.4F, (float)loc.getZ() + f1, 0.0F, 0.0F, 0.0F, 0.0F, 1);
                }
            }
        }

        private static boolean equalsLoc(Location a, Location b) {
            if (a == null || b == null)
                return false;
            return (a.getX() == b.getX() && a.getY() == b.getY() && a.getZ() == b.getZ());
        }
    }
}
