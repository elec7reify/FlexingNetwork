package com.flexingstudios.FlexingNetwork.api.util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public class LobbyProtector implements Listener {
    public static final int LOBBY_SIZE = 150;
    private static LobbyProtector instance;
    private Plugin plugin;
    private Location lobby;
    private int size;

    private LobbyProtector(Plugin plugin, Location lobby, int radius) {
        this.plugin = plugin;
        this.lobby = lobby;
        this.size = radius * radius;
    }

    @EventHandler
    private void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().equals(this.plugin))
            instance = null;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    private void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE && isNearLobby0(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    private void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE && isNearLobby0(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Player && isNearLobby0((Player) event.getEntity()))
            event.setCancelled(true);

        switch (event.getEntity().getType()) {
            case ITEM_FRAME:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onFoodChance(FoodLevelChangeEvent event) {
        if (event.getEntityType() == EntityType.PLAYER && isNearLobby0((Player) event.getEntity()))
            event.setFoodLevel(20);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockFromTo(BlockFromToEvent event) {
        if (isLobby0(event.getBlock().getLocation()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onPickup(PlayerPickupItemEvent event) {
        if (isNearLobby0(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockGrow(BlockGrowEvent event) {
        if (isLobby0(event.getBlock().getLocation()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockFade(BlockFadeEvent event) {
        if (isLobby0(event.getBlock().getLocation()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (isLobby0(event.getBlock().getLocation()))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockForm(BlockFormEvent event) {
        if (isLobby0(event.getBlock().getLocation()))
            event.setCancelled(true);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.hasBlock() && event.getPlayer().getGameMode() != GameMode.CREATIVE && isNearLobby0(event.getPlayer())) {
            if (event.getAction() == Action.PHYSICAL) {
                event.setCancelled(true);
                return;
            }

            switch (event.getClickedBlock().getType()) {
                case BED:
                case BED_BLOCK:
                case CHEST:
                case ENDER_CHEST:
                case TRAPPED_CHEST:
                case WORKBENCH:
                case DISPENSER:
                case DROPPER:
                case FURNACE:
                case BURNING_FURNACE:
                case BREWING_STAND:
                case HOPPER:
                case BEACON:
                    event.setCancelled(true);
                    break;
            }
        }
    }

    @EventHandler
    private void atEntityInteract(PlayerInteractAtEntityEvent event) {
        switch (event.getRightClicked().getType()) {
            case ARMOR_STAND:
            case PAINTING:
            case ITEM_FRAME:
            case MINECART:
            case MINECART_HOPPER:
            case MINECART_CHEST:
            case MINECART_FURNACE:
                event.setCancelled(true);
                break;
        }
    }

    private boolean isNearLobby0(Player player) {
        if (this.lobby.getWorld() != player.getWorld())
            return false;
        return (player.getLocation().distanceSquared(this.lobby) < this.size);
    }

    private boolean isLobby0(Location loc) {
        if (this.lobby.getWorld() != loc.getWorld())
            return false;
        return (loc.distanceSquared(this.lobby) < this.size);
    }

    public static void init(Plugin plugin, Location lobby) {
        init(plugin, lobby, 150);
    }

    public static void init(Plugin plugin, Location lobby, int radius) {
        if (instance != null)
            throw new IllegalStateException("LobbyProtector already inited");
        instance = new LobbyProtector(plugin, lobby, radius);
        Bukkit.getPluginManager().registerEvents(instance, plugin);
    }

    public static void dispose() {
        instance = null;
    }

    public static boolean isNearLobby(Player entity) {
        return instance.isNearLobby0(entity);
    }

    public static boolean isLobby(Location loc) {
        return instance.isLobby0(loc);
    }
}
