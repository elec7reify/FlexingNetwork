package com.flexingstudios.FlexingNetwork.api.menu;

import com.flexingstudios.FlexingNetwork.api.util.Invs;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface InvMenu extends InventoryHolder {
    void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType);

//    default void show(Player player) {
//        Invs.forceOpen(player, getInventory());
//    }
}
