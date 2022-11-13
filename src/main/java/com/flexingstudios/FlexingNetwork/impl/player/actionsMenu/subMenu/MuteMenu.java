package com.flexingstudios.FlexingNetwork.impl.player.actionsMenu.subMenu;

import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.impl.player.actionsMenu.ActionsMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MuteMenu implements InvMenu {
    private final Inventory inv;
    private final String target;
    private final ActionsMenu parent;

    public MuteMenu(String target, ActionsMenu parent) {
        inv = Bukkit.createInventory(this, 54, "Выберите причину...");
        this.target = target;
        this.parent = parent;

        inv.setItem(49, Items.name(new ItemStack(Material.STAINED_GLASS, 1, (short) 14), "&c&lОтменить действие", "&e► &7Вернуться в меню быстрых действий"));
    }

    @Override
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
        if (slot == 49) player.openInventory(parent.getInventory());
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
