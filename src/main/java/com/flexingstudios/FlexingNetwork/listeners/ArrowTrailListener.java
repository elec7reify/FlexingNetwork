package com.flexingstudios.flexingnetwork.listeners;

import com.flexingstudios.flexingnetwork.FlexingNetworkPlugin;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.player.ArrowTrail;
import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer;
import com.flexingstudios.flexingnetwork.api.util.Particles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class ArrowTrailListener implements Listener {
    @EventHandler
    public void onPlayerShootArrow(EntityShootBowEvent event) {
        if (event.getProjectile().getType() == EntityType.ARROW && event.getEntity().getType() == EntityType.PLAYER) {
            NetworkPlayer player = FlexingNetwork.INSTANCE.getPlayer(event.getEntity().getName());

            if (player.getArrowTrail() != null) {
                Trailer trailer = new Trailer((Arrow) event.getProjectile(), player.getArrowTrail());
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
                    Particles.HEART.play(arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
                    break;
                case DRIP_WATER:
                    Particles.DRIP_WATER.play(arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
                    break;
                case DRIP_LAVA:
                    Particles.DRIP_LAVA.play(arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
                    break;
                case FIREWORK:
                    Particles.FIREWORKS_SPARK.play(arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
                    break;
                case NOTE:
                    Particles.NOTE.play(arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 3);
                    break;
                case SLIME:
                    Particles.SLIME.play(arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 2);
                    break;
                case VILLAGER_HAPPY:
                    Particles.VILLAGER_HAPPY.play(arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
                    break;
                case ANGRY_VILLAGER:
                    Particles.VILLAGER_ANGRY.play(arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 1);
                    break;
                case ENCHANTMENT_TABLE:
                    Particles.ENCHANTMENT_TABLE.play(arrow.getLocation(), 0.0F, 0.0F, 0.0F, 0.0F, 4);
                    break;
            }

            if (arrow.isDead() || arrow.isOnGround() || tick++ > 600L) {
                Bukkit.getScheduler().cancelTask(task);
                task = -1;
            }
        }
    }
}
