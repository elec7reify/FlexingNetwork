package com.flexingstudios.FlexingNetwork.tasks;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import net.minecraft.server.v1_12_R1.IInventory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;

import java.util.Iterator;

public class MemoryFix implements Runnable {
    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (Object tile : (((CraftWorld) world).getHandle()).tileEntityList) {
                if (tile instanceof IInventory) {
                    Iterator<HumanEntity> iter = ((IInventory) tile).getViewers().iterator();
                    while (iter.hasNext()) {
                        HumanEntity humanEntity = iter.next();
                        if (humanEntity instanceof CraftPlayer && !FlexingNetwork.isPlayerOnline(humanEntity.getName()))
                            iter.remove();
                    }
                }
            }
        }
    }
}
