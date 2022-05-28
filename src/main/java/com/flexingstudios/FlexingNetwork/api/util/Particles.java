package com.flexingstudios.FlexingNetwork.api.util;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.List;

public enum Particles {
    HEART("HEART");

    private final String id;

    Particles(String id) {
        this.id = id;
    }

    public void play(World w, float x, float y, float z, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        play(this.id, x, y, z, xOffset, yOffset, zOffset, effectSpeed, amountOfParticles, players);
    }

    public void play(Location loc, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        play(this.id, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), xOffset, yOffset, zOffset, effectSpeed, amountOfParticles, players);
    }

    /*public static void playIconCrack(int id, float x, float y, float z, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        play("iconcrack_" + id, x, y, z, xOffset, yOffset, zOffset, effectSpeed, amountOfParticles, players);
    }

    public static void playTileCrack(int id, int meta, float x, float y, float z, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        play("tilecrack_" + id + "_" + meta, x, y, z, xOffset, yOffset, zOffset, effectSpeed, amountOfParticles, players);
    }*/

    public static void play(String particle, float x, float y, float z, float xOffset, float yOffset, float zOffset, float effectSpeed, int amountOfParticles, Player... players) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.HEART, false, x, y, z, xOffset, yOffset, zOffset, effectSpeed, amountOfParticles);
        if (players.length == 0) {
            int radius = 400;
            List<EntityPlayer> list = (MinecraftServer.getServer().getPlayerList()).players;
            for (EntityPlayer player : list) {
                double distanceSquared = NumberConversions.square(player.locX - x) + NumberConversions.square(player.locY - y) + NumberConversions.square(player.locZ - z);
                if (distanceSquared < radius)
                    player.playerConnection.sendPacket(packet);
            }
        } else {
            for (Player player : players)
                mes.sendPacket(player, packet);
        }
    }
}
