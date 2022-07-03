package com.flexingstudios.FlexingNetwork.listeners;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.player.ArrowTrail;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Particles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ArrowTrailListener implements Listener {
    @EventHandler
    public void onPlayerShootArrow(ProjectileLaunchEvent event) {
        if (event.getEntityType() == EntityType.ARROW && event.getEntity().getType() == EntityType.PLAYER) {
            NetworkPlayer player = FlexingNetwork.getPlayer((Player) event.getEntity().getShooter());
            if (player.getArrowTrail() != null) {
                Trailer trailer = new Trailer((Arrow) event.getEntity(), player.getArrowTrail());
                trailer.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(FlexingNetworkPlugin.getInstance(), trailer, 3L, 1L);
            }
        }
    }

    private static class Trailer implements Runnable {
        Arrow arrow;
        ArrowTrail trail;
        int task = -1;
        int tick = 0;

        public Trailer(Arrow arrow, ArrowTrail trail) {
            this.arrow = arrow;
            this.trail = trail;
        }

        @Override
        public void run() {
            switch (trail) {
                case HEARTS:
                    Particles.HEART.play(this.arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1, new Player[0]);
                    break;
            }
            if (arrow.isDead() || arrow.isOnGround() || tick++ > 600) {
                Bukkit.getScheduler().cancelTask(task);
                task = -1;
            }
        }
    }
}
