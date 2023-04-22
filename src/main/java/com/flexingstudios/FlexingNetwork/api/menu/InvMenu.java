package com.flexingstudios.FlexingNetwork.api.menu;

import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Invs;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface InvMenu extends InventoryHolder {
    void onClick(ItemStack item, NetworkPlayer player, int slot, ClickType clickType);

    default void show(Player player) {
        Invs.forceOpen(player, getInventory());
    }

    /**
     * Fill a section of the inventory with the given item
     * @param start The starting index to fill from, inclusive
     * @param end   The ending index to fill to, exclusive
     * @param item  The item to set in these slots
     */
    default void fill(int start, int end, ItemStack item) {
        for (int i = start; i < end; i++) {
            getInventory().setItem(i, item == null ? null : item.clone());
        }
    }
}
