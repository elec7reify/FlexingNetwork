package com.flexingstudios.FlexingNetwork.api.menu;

import com.flexingstudios.FlexingNetwork.api.util.Invs;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class InvMenuImpl extends Invs implements InvMenu {
    private final Inventory inventory;

    public InvMenuImpl(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void fill(int start, int end, ItemStack item) {
        for (int i = start; i < end; i++) {
            inventory.setItem(i, item == null ? null : item.clone());
        }
    }
}
