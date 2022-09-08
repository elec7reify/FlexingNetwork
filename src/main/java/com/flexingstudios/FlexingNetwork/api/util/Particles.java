package com.flexingstudios.FlexingNetwork.api.util;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.List;

public enum Particles {
    HEART("HEART"),
    DRIP_WATER("DRIP_WATER"),
    DRIP_LAVA("DRIP_LAVA"),
    FIREWORKS_SPARK("FIREWORKS_SPARK"),
    NOTE("NOTE"),
    SLIME("SLIME"),
    VILLAGER_HAPPY("VILLAGER_HAPPY"),
    VILLAGER_ANGRY("VILLAGER_ANGRY"),
    ENCHANTMENT_TABLE("ENCHANTMENT_TABLE"),
    ;

    private final String id;

    Particles(String id) {
        this.id = id;
    }

    public void play(World world, float x, float y, float z, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        play(world, this.id, x, y, z, xOffset, yOffset, zOffset, effectSpeed, amountOfParticles, players);
    }

    public void play(Location loc, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        play(loc.getWorld(), this.id, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), xOffset, yOffset, zOffset, effectSpeed, amountOfParticles, players);
    }

    public static void playIconCrack(World world, int id, float x, float y, float z, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        play(world, "iconcrack_" + id, x, y, z, xOffset, yOffset, zOffset, effectSpeed, amountOfParticles, players);
    }

    public static void playTileCrack(World world, int id, int meta, float x, float y, float z, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        play(world, "tilecrack_" + id + "_" + meta, x, y, z, xOffset, yOffset, zOffset, effectSpeed, amountOfParticles, players);
    }

    public static void play(World world, String particle, float x, float y, float z, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particle), true, x, y, z, xOffset, yOffset, zOffset, effectSpeed, amountOfParticles);
        if (players.length == 0) {
            int radius = 400;
            List<EntityPlayer> list = MinecraftServer.getServer().getPlayerList().players;
            for (EntityPlayer player : list) {
                double distanceSquared = NumberConversions.square(player.locX - x) + NumberConversions.square(player.locY - y) + NumberConversions.square(player.locZ - z);
                if (distanceSquared < radius)
                    player.playerConnection.sendPacket(packet);
            }
        } else {
            for (Player player : players)
                Utilities.sendPacket(player, packet);
        }
    }
}
