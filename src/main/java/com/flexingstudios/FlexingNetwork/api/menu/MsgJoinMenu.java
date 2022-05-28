package com.flexingstudios.FlexingNetwork.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MsgJoinMenu implements InvMenu {
    private final Inventory inv = Bukkit.getServer().createInventory(this, 18, "Сообщение при входе");

    public MsgJoinMenu(Player player) {

    }

    @Override
    public void onClick(ItemStack paramItemStack, Player paramPlayer, int paramInt, ClickType paramClickType) {

    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
