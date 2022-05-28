package com.flexingstudios.FlexingNetwork.api.util;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCrafting;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Invs {
    public static void forceOpen(HumanEntity bukkitPlayer, Inventory inv) {
        IInventory iinventory = ((CraftInventory) inv).getInventory();
        EntityPlayer player = ((CraftPlayer) bukkitPlayer).getHandle();
        Container container = CraftEventFactory.callInventoryOpenEvent(player, new ContainerChest(player.inventory, iinventory, null));
        if (container != null) {
            if (player.activeContainer != null) {
                CraftEventFactory.handleInventoryCloseEvent(player);
                player.activeContainer.b(player);
            }
            int containerCounter = player.nextContainerCounter();
            //player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerCounter, 0, iinventory.getName(), iinventory.getSize(), (null)));
            player.activeContainer = container;
            player.activeContainer.windowId = containerCounter;
            player.activeContainer.addSlotListener(player);
        }
    }

    public static void forceOpen(HumanEntity bukkitPlayer, InventoryHolder menu) {
        forceOpen(bukkitPlayer, menu.getInventory());
    }

    public static void clear(HumanEntity entity) {
        entity.getInventory().clear();
        entity.getInventory().setArmorContents(new ItemStack[4]);
        Inventory top = entity.getOpenInventory().getTopInventory();
        if (top instanceof CraftInventoryCrafting) {
            CraftInventoryCrafting inv = (CraftInventoryCrafting)top;
            inv.setMatrix(new ItemStack[inv.getMatrixInventory().getSize()]);
            inv.setResult(null);
        }
    }

    public static int take(Inventory inv, Material type, int amount) {
        for (Map.Entry<Integer, ? extends ItemStack> entry : inv.all(type).entrySet()) {
            ItemStack is = entry.getValue();
            if (is.getAmount() <= amount) {
                inv.setItem(entry.getKey().intValue(), null);
                amount -= is.getAmount();
            } else {
                is.setAmount(is.getAmount() - amount);
                inv.setItem(entry.getKey().intValue(), is);
                amount = 0;
            }
            if (amount == 0)
                break;
        }
        return amount;
    }
}
