package com.flexingstudios.FlexingNetwork.api.entity;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class NMSEntityUtils {
    private static Field PathfinderGoalSelector_list1;
    private static Field PathfinderGoalSelector_list2;
    private static Field EntityInsentient_goalSelector;
    private static Field EntityInsentient_targetSelector;
    private static Field EntityTypes_d;
    private static Field EntityTypes_e;
    private static Method EntityTypes_registerEntity;

    static {
        try {
            PathfinderGoalSelector_list1 = PathfinderGoalSelector.class.getDeclaredField("a");
            PathfinderGoalSelector_list1.setAccessible(true);
            PathfinderGoalSelector_list2 = PathfinderGoalSelector.class.getDeclaredField("b");
            PathfinderGoalSelector_list2.setAccessible(true);
            EntityInsentient_goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
            EntityInsentient_goalSelector.setAccessible(true);
            EntityInsentient_targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
            EntityInsentient_targetSelector.setAccessible(true);
            EntityTypes_registerEntity = EntityTypes.class.getDeclaredMethod("a", new Class[] { Class.class, String.class, int.class });
            EntityTypes_registerEntity.setAccessible(true);
            EntityTypes_d = EntityTypes.class.getDeclaredField("d");
            EntityTypes_d.setAccessible(true);
            EntityTypes_e = EntityTypes.class.getDeclaredField("e");
            EntityTypes_e.setAccessible(true);
        } catch (Exception e) {
            FlexingNetworkPlugin.getInstance().getLogger().log(Level.SEVERE, "NMSEntityUtils initialization failed", e);
            throw new RuntimeException(e);
        }
    }

    public static void safeRegisterCustomEntity(Class<? extends Entity> clazz, String name) {
        safeRegisterCustomEntity(clazz, clazz.getSuperclass(), name);
    }

    public static void safeRegisterCustomEntity(Class<? extends Entity> clazz, Class<?> nmsClazz, String name) {
        try {
            int id = ((Integer)((Map)EntityTypes_e.get(null)).get(nmsClazz)).intValue();
            Map<Integer, Class<?>> d = (Map<Integer, Class<?>>)EntityTypes_d.get(null);
            EntityTypes_registerEntity.invoke(null, new Object[] { clazz, name, Integer.valueOf(id) });
            d.put(Integer.valueOf(id), nmsClazz);
        } catch (Exception e) {
            FlexingNetworkPlugin.getInstance().getLogger().log(Level.SEVERE, "NMSEntityUtils failed to register custom entity", e);
        }
    }

    public static void clearPathfinding(EntityInsentient entity) {
        try {
            Object goalSelector = EntityInsentient_goalSelector.get(entity);
            ((List)PathfinderGoalSelector_list1.get(goalSelector)).clear();
            ((List)PathfinderGoalSelector_list2.get(goalSelector)).clear();
            Object targetSelector = EntityInsentient_targetSelector.get(entity);
            ((List)PathfinderGoalSelector_list1.get(targetSelector)).clear();
            ((List)PathfinderGoalSelector_list2.get(targetSelector)).clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static double getPathfindingRange(EntityLiving entity) {
        return entity.getAttributeInstance(GenericAttributes.g).getValue();
    }

    public static void setMovementSpeed(EntityLiving entity, double speed) {
        entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
    }

    public static double getMovementSpeed(EntityLiving entity) {
        return entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
    }

    public static WorldServer getNMSWorld(World bukkitWorld) {
        return ((CraftWorld) bukkitWorld).getHandle();
    }
}
