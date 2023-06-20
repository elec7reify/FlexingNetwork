package com.flexingstudios.flexingnetwork.api.util;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
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
            player.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerCounter, iinventory.getName(), iinventory.getScoreboardDisplayName(), iinventory.getSize()));
            player.activeContainer = container;
            player.activeContainer.windowId = containerCounter;
            player.activeContainer.addSlotListener(player);
        }
    }

    public static void forceOpen(HumanEntity bukkitPlayer, InventoryHolder menu) {
        forceOpen(bukkitPlayer, menu.getInventory());
    }

    public static void sendItem(HumanEntity bukkitPlayer, Inventory inv, int slot, ItemStack item) {
        if (isInMenu(bukkitPlayer, inv)) {
            EntityPlayer player = ((CraftPlayer)bukkitPlayer).getHandle();
            player.playerConnection.sendPacket(new PacketPlayOutSetSlot(player.activeContainer.windowId, slot, CraftItemStack.asNMSCopy(item)));
        }
    }

    public static boolean isInMenu(HumanEntity bukkitPlayer, Inventory inv) {
        Container c = ((CraftPlayer) bukkitPlayer).getHandle().activeContainer;
        return c instanceof ContainerChest && ((ContainerChest) c).e() == ((CraftInventory) inv).getInventory();
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

    public static int count(Inventory inv, Material type) {
        return inv.all(type).values().stream().mapToInt(ItemStack::getAmount).sum();
    }

    public static int count(Inventory inv, ItemStack is) {
        return inv.all(is).values().stream().mapToInt(ItemStack::getAmount).sum();
    }

    public static int take(Inventory inv, Material type, int amount) {
        for (Map.Entry<Integer, ? extends ItemStack> entry : inv.all(type).entrySet()) {
            ItemStack is = entry.getValue();

            if (is.getAmount() <= amount) {
                inv.setItem(entry.getKey(), null);
                amount -= is.getAmount();
            } else {
                is.setAmount(is.getAmount() - amount);
                inv.setItem(entry.getKey(), is);
                amount = 0;
            }

            if (amount == 0)
                break;
        }

        return amount;
    }
}
