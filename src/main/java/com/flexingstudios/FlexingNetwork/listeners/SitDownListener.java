package com.flexingstudios.flexingnetwork.listeners;

import com.flexingstudios.flexingnetwork.api.util.ClickType;
import com.flexingstudios.flexingnetwork.api.util.TitleManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class SitDownListener implements Listener {
    private final List<Material> BLOCKS = Arrays.asList(Material.STEP, Material.WOOD_STEP, Material.COBBLESTONE_STAIRS, Material.BRICK_STAIRS);

    public SitDownListener() {}

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (ClickType.isRightClick(event) && BLOCKS.contains(event.getClickedBlock().getType())) {
            if (!player.isSneaking()) {
                if (player.getPassengers().isEmpty()) {
                    event.setCancelled(true);
                    player.addPassenger(player);
                    TitleManager.sendActionBar(player, "&fВы сидите.");
                }
            } else if (player.getPassengers().contains(player)) {
                player.removePassenger(player);
            }
        }
    }
}
