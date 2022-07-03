package com.flexingstudios.FlexingNetwork.listeners;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.ItemsDef;
import com.flexingstudios.FlexingNetwork.api.event.ItemClickedEvent;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.IC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ServiceItems implements Listener {
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (ItemsDef.isServiceItem(event.getItemDrop().getItemStack()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (ItemsDef.isServiceItem(event.getEntity().getItemStack()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.hasItem() && ItemsDef.isServiceItem(event.getItem()))
            Bukkit.getPluginManager().callEvent(new ItemClickedEvent(event));
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemClicked(ItemClickedEvent event) {
        if (IC.isRightClick(event)) {
            Material id = event.getItem().getType();
            int meta = event.getItem().getDurability();

            if (id == ItemsDef.ITEM_TO_LOBBY.getType()) {
                FlexingNetwork.toLobby(event.getPlayer());
            } else if (id == ItemsDef.ITEM_GAME_SELECT.getType()) {
                event.getPlayer().chat("/bw gui");
            }
        }
    }
}
