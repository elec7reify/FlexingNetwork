package com.flexingstudios.FlexingNetwork.api.util;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.event.PlayerUnloadEvent;
import com.flexingstudios.FlexingNetwork.api.menu.TrailMenu;
import io.netty.util.internal.ConcurrentSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Spectators implements Listener {
    private static Spectators inst = null;
    private final Set<Player> set;
    private final List<ListenerInfo> listeners;

    private Spectators(Plugin plugin) {
        set = new HashSet<>();
        listeners = new LinkedList<>();
        addListener(plugin, (player, spectator) -> {
            /*if (spectator) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (null), 10L);
            } else {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, (null), 10L);
            }*/
            player.setAllowFlight(spectator);
            player.setFlying(spectator);
            if (TrailMenu.isEnabled()) {
                TrailMenu.TrailPlayer trailPlayer = TrailMenu.getPlayer(player.getName());
                if (trailPlayer != null)
                    trailPlayer.visible = !spectator;
            }
        });
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public List<SpecListener> getListeners() {
        return listeners.stream().map(l -> l.listener).collect(Collectors.toList());
    }

    public void removeListener(SpecListener listener) {
        listeners.removeIf(info -> (info.listener == listener));
    }

    public void addListener(Plugin plugin, SpecListener listener) {
        removeListener(listener);
        listeners.add(new ListenerInfo(plugin, listener));
    }

    public boolean contains(Player player) {
        return set.contains(player);
    }

    /**
     * Add player to spectator mode
     * @param player player entity
     */
    public void add(Player player) {
        if (contains(player))
            return;
        set.add(player);
        for (Player otherPlayer : Bukkit.getOnlinePlayers())
            otherPlayer.hidePlayer(player);
        setStats(player, true);
    }

    public void remove(Player player) {
        if (!contains(player))
            return;
        set.remove(player);
        setStats(player, false);
        for (Player otherPlayer : Bukkit.getOnlinePlayers())
            otherPlayer.showPlayer(player);
    }

    private void setStats(Player player, boolean spectator) {
        if (spectator) {
            disableCollision(player);
        } else {
            enableCollision(player);
        }
        for (ListenerInfo listener : listeners) {
            try {
                listener.listener.equip(player, spectator);
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.WARNING, null, e);
            }
        }
    }

    private void disableCollision(Player player) {
        player.setCollidable(false);
    }

    private void enableCollision(Player player) {
        player.setCollidable(true);
    }

    public Set<Player> getSpectators() {
        return Collections.unmodifiableSet(set);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        if (contains(event.getPlayer()))
            setStats(event.getPlayer(), true);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        set.forEach(player::hidePlayer);
    }

    @EventHandler
    private void onPlayerUnload(PlayerUnloadEvent event) {
        enableCollision(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onPickup(PlayerPickupItemEvent event) {
        if (contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onPlace(BlockPlaceEvent event) {
        if (contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onDrop(PlayerDropItemEvent event) {
        if (contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onBreak(BlockBreakEvent event) {
        if (contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onInteract(PlayerInteractEvent event) {
        if (contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onInteractEntity(PlayerInteractEntityEvent event) {
        if (contains(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onDamageBySpectator(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER && contains((Player)event.getDamager()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && contains((Player)event.getEntity())) {
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID)
                event.getEntity().teleport(event.getEntity().getLocation().add(0.0D, 60.0D, 0.0D));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && contains((Player)event.getEntity())) {
            ((Player)event.getEntity()).setFoodLevel(20);
            ((Player)event.getEntity()).setSaturation(20.0F);
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget().getType() == EntityType.PLAYER && contains((Player) event.getTarget()))
            event.setCancelled(true);
    }

    @EventHandler
    private void onCombust(EntityCombustEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && contains((Player)event.getEntity())) {
            event.setCancelled(true);
            event.setDuration(1);
        }
    }

    @EventHandler
    private void onPotionSplash(PotionSplashEvent event) {
        for (LivingEntity entity : event.getAffectedEntities()) {
            if (entity.getType() == EntityType.PLAYER && contains((Player) entity))
                event.setIntensity(entity, 0.0D);
        }
    }

    @EventHandler
    private void onPluginDisable(PluginDisableEvent event) {
        listeners.removeIf(info -> info.plugin.equals(event.getPlugin()));
    }

    public static Spectators instance() {
        if (inst == null)
            inst = new Spectators(FlexingNetworkPlugin.getInstance());
        return inst;
    }

    public static boolean isEnabled() {
        return (inst != null);
    }

    private static class ListenerInfo {
        public final Plugin plugin;
        public final Spectators.SpecListener listener;

        public ListenerInfo(Plugin plugin, Spectators.SpecListener listener) {
            this.plugin = plugin;
            this.listener = listener;
        }
    }

    public static interface SpecListener {
        void equip(Player player, boolean param1Boolean);
    }
}
