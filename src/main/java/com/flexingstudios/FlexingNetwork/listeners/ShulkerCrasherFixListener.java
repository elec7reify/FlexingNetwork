package com.flexingstudios.FlexingNetwork.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class ShulkerCrasherFixListener implements Listener {
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        if (event.getBlock().getLocation().getBlockY() >= 253 && event.getItem().getType().name().endsWith("SHULKER_BOX")) {
            event.setCancelled(true);
        } else if (event.getBlock().getLocation().getBlockY() <= 4 && event.getItem().getType().name().endsWith("SHULKER_BOX")) {
            event.setCancelled(true);
        }
    }
}
