package com.flexingstudios.flexingnetwork.impl.player.actionsMenu.subMenu;

import com.flexingstudios.flexingnetwork.impl.player.actionsMenu.ActionsMenu;
import com.flexingstudios.flexingnetwork.api.menu.ConfirmMenu;
import com.flexingstudios.flexingnetwork.api.menu.InvMenu;
import com.flexingstudios.flexingnetwork.api.util.Items;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.Pair;
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

public class MuteMenu implements InvMenu {
    private final Inventory inv;
    private final String target;
    private final ActionsMenu parent;
    public static final List<Pair<String, String>> REASON = Arrays.asList(
            Pair.of("1h","Токсичное поведение"),
            Pair.of("20m","Спам/Флуд"),
            Pair.of("20m","Попрошайничество"),
            Pair.of("6h","Политическая агитация"),
            Pair.of("6h","Религиозаная агитация"),
            Pair.of("6h","Розжиг межнациональной розни"));


    public MuteMenu(String target, ActionsMenu parent) {
        inv = Bukkit.createInventory(this, 54, "Выберите причину...");
        this.target = target;
        this.parent = parent;

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);

        Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17,
                18, 26,
                27, 35,
                36, 44,
                45, 46, 47, 48, 50, 51, 52, 53);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        inv.setItem(49, Items.name(new ItemStack(Material.STAINED_GLASS, 1, (short) 14), "&3&lОтменить действие", "&3► &7Вернуться в меню быстрых действий"));
        int index = 0;
        for (Pair<String , String> objects : REASON) {
            inv.setItem(getSlot(index++), Items.name(Material.EMPTY_MAP, "&a" + objects.getRight()));
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
        if (slot == 49) player.openInventory(parent.getInventory());

        int index = getIndex(slot);
        if (index >= 0 && index < REASON.size()) {
            for (Pair<String, String> object : REASON) {
                ConfirmMenu menu = new ConfirmMenu(getInventory(), () -> {
                    Bukkit.dispatchCommand(player, "mute " + target + " " + object.getLeft() + " " + object.getRight());
                }, "Подтвердите действия");
                menu.setBackOnConfirm(false);
                menu.setConfirmText("&aВыдать мут игроку", "&fВремя мута: &6" + object.getLeft());
                menu.setCancelText("&cОтменить действие");
                player.openInventory(menu.getInventory());
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}