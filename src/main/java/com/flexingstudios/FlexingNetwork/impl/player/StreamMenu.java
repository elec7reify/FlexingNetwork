package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StreamMenu implements InvMenu {
    private Inventory inv;

    @Override
    public void onClick(ItemStack paramItemStack, Player paramPlayer, int paramInt, ClickType paramClickType) {

    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
