package com.flexingstudios.FlexingNetwork.listeners;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.ItemsDef;
import com.flexingstudios.FlexingNetwork.api.event.ItemsClickEvent;
import com.flexingstudios.FlexingNetwork.api.util.IC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ServiceItems implements Listener {
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (ItemsDef.isThisItem(event.getItemDrop().getItemStack()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.hasItem() && ItemsDef.isThisItem(event.getItem()))
            Bukkit.getPluginManager().callEvent(new ItemsClickEvent(event));
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemClicked(ItemsClickEvent event) {
        if (IC.isRightClick(event)) {
            Material id = event.getItem().getType();
            int meta = event.getItem().getDurability();

            if (id == ItemsDef.ITEM_TO_LOBBY.getType() && meta == 0) {
                FlexingNetwork.toLobby(event.getPlayer());
            }
        }
    }
}
