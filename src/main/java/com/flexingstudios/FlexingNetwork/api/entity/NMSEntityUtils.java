package com.flexingstudios.FlexingNetwork.api.entity;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class NMSEntityUtils {

    public static double getMovementSpeed(EntityLiving entity) {
        return entity.getAttributeInstance(GenericAttributes.g).getValue();
    }

    public static WorldServer getNMSWorld(World bukkitWorld) {
        return ((CraftWorld) bukkitWorld).getHandle();
    }
}
