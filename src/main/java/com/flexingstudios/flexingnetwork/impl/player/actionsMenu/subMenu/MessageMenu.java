package com.flexingstudios.flexingnetwork.impl.player.actionsMenu.subMenu;

import com.flexingstudios.flexingnetwork.impl.player.actionsMenu.ActionsMenu;
import com.flexingstudios.flexingnetwork.api.menu.InvMenu;
import com.flexingstudios.flexingnetwork.api.util.Items;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MessageMenu implements InvMenu {
    private final String target;
    private final Inventory inv;
    public static final List<String> MESSAGES = Arrays.asList(
            "Привет",
            "Где играешь?",
            ".",
            "Давай дружить?",
            "Пойдёшь вместе играть?",
            "Отличная игра",
            "",
            "",
            "");

    public MessageMenu(String target) {
        this.target = target;
        inv = Bukkit.createInventory(this, 9 * 4, "Выберите сообщение...");
        int index = 0;

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);

        Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17,
                18, 26,
                27, 28, 29, 30, 32, 33, 34, 35);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        inv.setItem(31, Items.name(new ItemStack(Material.STAINED_GLASS, 1, (short) 14), "&3&lОтменить действие", "&3► &7Вернуться в меню быстрых действий"));

        for (String msg : MESSAGES) {
            inv.setItem(getSlot(index++), Items.name(Material.EMPTY_MAP, "&a" + msg));
        }
    }

    private int getSlot(int index) {
        return 10 + 9 * (index / 7) + index % 7;
    }

    private int getIndex(int slot) {
        if (slot % 9 == 0 || (slot + 1) % 9 == 0)
            return -1;

        slot -= 10;
        if (slot < 0)
            return -1;

        int row = slot / 9;

        return row * 7 + (slot - row * 9) % 7;
    }

    @Override
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
        if (slot == 22) player.openInventory(new ActionsMenu(target).getInventory());

        int index = getIndex(slot);
        if (index >= 0 && index < MESSAGES.size()) {
            String object = MESSAGES.get(index);
            Bukkit.dispatchCommand(player, "m " + target + " " + object);
            player.closeInventory();
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}