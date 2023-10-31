package com.flexingstudios.flexingnetwork.api.entity;

import com.flexingstudios.flexingnetwork.api.util.Utils;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NPC extends NMSEntityUtils {
    private final String name;
    private Location location;

    public NPC(String name, World world, double x, double y, double z, float yaw, float pitch) {
        this.name = name;
        this.location = new Location(world, x, y, z, yaw, pitch);

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
        WorldServer nmsWorld = NMSEntityUtils.getNMSWorld(world);

        entity = new EntityPlayer(server, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
        Player player = ((EntityPlayer) entity).getBukkitEntity().getPlayer();
        player.setPlayerListName("");

        entity.setLocation(x, y, z, yaw, pitch);
        for (Player user : Bukkit.getOnlinePlayers()) {
            Utils.sendPacket(user, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((EntityPlayer) entity)));
            Utils.sendPacket(user, new PacketPlayOutNamedEntitySpawn((EntityPlayer) entity));
            Utils.sendPacket(user, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((EntityPlayer) entity)));
        }
    }

    public NPC(String name, Location location) {
        this(name, location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }
}
