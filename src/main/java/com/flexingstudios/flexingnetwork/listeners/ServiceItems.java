package com.flexingstudios.flexingnetwork.listeners;

import com.flexingstudios.flexingnetwork.api.ItemsDef;
import com.flexingstudios.flexingnetwork.api.util.ClickType;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.event.ItemClickedEvent;
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
        if (ClickType.isRightClick(event)) {
            Material id = event.getItem().getType();
            int meta = event.getItem().getDurability();
            if (id == new ItemsDef(event.getPlayer()).ITEM_TO_LOBBY.getType() && meta == 0) {
                FlexingNetwork.INSTANCE.toLobby(event.getPlayer());
            } else if (id == new ItemsDef(event.getPlayer()).ITEM_GAME_SELECT.getType() && meta == 0) {
                event.getPlayer().chat("/bw gui");
            }
        }
    }
}
