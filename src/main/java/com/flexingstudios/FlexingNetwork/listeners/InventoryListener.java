package com.flexingstudios.flexingnetwork.listeners;

import com.flexingstudios.flexingnetwork.api.menu.InvMenu;
import com.flexingstudios.flexingnetwork.api.menu.MenuClosable;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.logging.Level;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getType() != EntityType.PLAYER)
            return;

        InventoryHolder topHolder = event.getView().getTopInventory().getHolder();

        if (topHolder instanceof InvMenu) {
            if (event.getCurrentItem() != null && event.getClickedInventory().getHolder() instanceof InvMenu)
                try {
                    ((InvMenu) topHolder).onClick(event.getCurrentItem(), (Player) event.getWhoClicked(), event.getSlot(), event.getClick());
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.WARNING, topHolder.getClass().getName(), e);
                }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer().getType() == EntityType.PLAYER && event.getInventory().getHolder() instanceof MenuClosable)
            ((MenuClosable) event.getInventory().getHolder()).onClose();
    }
}
