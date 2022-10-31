package com.flexingstudios.FlexingNetwork.api.menu;

import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ConfirmMenu implements InvMenu {
    private static final Set<Integer> CONFIRM_SLOTS = ImmutableSet.of(11, 12, 20, 21);
    private static final Set<Integer> CANCEL_SLOTS = ImmutableSet.of(14, 15, 23, 24);
    private static final Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(1, 2, 3, 4, 5, 6, 7,
            10, 13, 16,
            19, 22, 25,
            28, 29, 30, 31, 32, 33, 34);
    private static final Set<Integer> GLASS_PANE_BLUE_SLOTS = ImmutableSet.of(0, 8, 9, 17, 18, 26, 27, 35);
    private final Runnable callback;
    private Runnable cancelledCallback;
    private Inventory prev;
    private final Inventory inv;
    private boolean confirmInited = false;
    private boolean cancelInited = false;
    private boolean backOnConfirm = true;

    public ConfirmMenu(Inventory prev, Runnable callback, String title) {
        this.callback = callback;
        this.prev = prev;
        inv = Bukkit.createInventory(this, 36, title);
    }

    public ConfirmMenu(Runnable callback, String title) {
        this.callback = callback;
        inv = Bukkit.createInventory(this, 36, title);
    }

    public void setConfirmText(String name, String... lore) {
        confirmInited = true;
        ItemStack item = Items.name(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), name, lore);
        ConfirmMenu.CONFIRM_SLOTS.forEach(slot -> inv.setItem(slot, item));
    }

    public void setCancelText(String name, String... lore) {
        cancelInited = true;
        ItemStack item = Items.name(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), name, lore);
        ConfirmMenu.CANCEL_SLOTS.forEach(slot -> inv.setItem(slot, item));
    }

    public void setBackOnConfirm(boolean flag) {
        backOnConfirm = flag;
    }

    public void setCancelledCallback(Runnable callback) {
        cancelledCallback = callback;
    }

    @Override
    public void onClick(ItemStack is, Player player, int slot, ClickType clickType) {
        if (ConfirmMenu.CONFIRM_SLOTS.contains(slot)) {
            callback.run();
            if (backOnConfirm) {
                if (prev != null) {
                    player.openInventory(prev);
                } else {
                    player.closeInventory();
                }
            }
        } else if (ConfirmMenu.CANCEL_SLOTS.contains(slot)) {
            if (cancelledCallback != null) {
                cancelledCallback.run();
            } else if (prev != null) {
                player.openInventory(prev);
            } else {
                player.closeInventory();
            }
        }
    }

    @Override
    public Inventory getInventory() {
        if (!confirmInited) {
            ItemStack confirm = Items.name(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), "&aOK");
            ConfirmMenu.CONFIRM_SLOTS.forEach(slot -> inv.setItem(slot, confirm));
        }
        if (!cancelInited) {
            ItemStack cancel = Items.name(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), "&cОТМЕНИТЬ");
            ConfirmMenu.CANCEL_SLOTS.forEach(slot -> inv.setItem(slot, cancel));
        }

        ItemStack GLASS_PANE_WHITE = Items.name(new ItemStack(Material.STAINED_GLASS_PANE, 1), "§6§k|§a§k|§e§k|§c§k|");
        ConfirmMenu.GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        ItemStack GLASS_PANE_BLUE = Items.name(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11), "§6§k|§a§k|§e§k|§c§k|");
        ConfirmMenu.GLASS_PANE_BLUE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_BLUE));

        return inv;
    }
}
