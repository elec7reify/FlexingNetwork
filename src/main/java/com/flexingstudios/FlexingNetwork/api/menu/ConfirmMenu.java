package com.flexingstudios.FlexingNetwork.api.menu;

import com.flexingstudios.FlexingNetwork.api.util.Invs;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ConfirmMenu implements InvMenu {
    private static final Set<Integer> CONFIRM_SLOTS = ImmutableSet.of(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21);
    private static final Set<Integer> CANCEL_SLOTS = ImmutableSet.of(5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26);
    private static final Set<Integer> VOID_SLOTS = ImmutableSet.of(4, 22);
    private Runnable callback;
    private Runnable cancelledCallback;
    private Inventory prev;
    private Inventory inv;
    private boolean confirmInited = false;
    private boolean cancelInited = false;
    private boolean backOnConfirm = true;

    public ConfirmMenu(Inventory prev, Runnable callback, String title) {
        this.callback = callback;
        this.prev = prev;
        inv = Bukkit.createInventory(this, 27, title);
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
                    Invs.forceOpen(player, prev);
                } else {
                    player.closeInventory();
                }
            }
        } else if (ConfirmMenu.CANCEL_SLOTS.contains(slot)) {
            if (cancelledCallback != null) {
                cancelledCallback.run();
            } else if (prev != null) {
                Invs.forceOpen(player, prev);
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

        ItemStack void_item = Items.name(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0), "&fПУСТОЕ МЕСТО");
        ConfirmMenu.VOID_SLOTS.forEach(slot -> inv.setItem(slot, void_item));

        return inv;
    }
}
