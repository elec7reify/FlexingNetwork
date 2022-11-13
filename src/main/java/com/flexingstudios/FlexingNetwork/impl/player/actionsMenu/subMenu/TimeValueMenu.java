package com.flexingstudios.FlexingNetwork.impl.player.actionsMenu.subMenu;

import com.flexingstudios.Commons.F;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.menu.ConfirmMenu;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;

public class TimeValueMenu implements InvMenu {
    private final Inventory inv;
    private final Inventory prev;
    private final String target;

    public TimeValueMenu(String target, Inventory prev) {
        this.target = target;
        this.prev = prev;
        inv = Bukkit.createInventory(this, 27, "Выберите время наказания...");

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);

        Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17,
                18, 19, 20, 21, 23, 24, 25, 26);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));
        inv.setItem(10, Items.name(Material.WATCH, "&aЗабанить на неделю", ""));
        inv.setItem(11, Items.name(Material.WATCH, "&aЗабанить на 1 день", ""));
        inv.setItem(12, Items.name(Material.WATCH, "&aЗабанить на 6 часов", ""));
        inv.setItem(13, Items.name(Material.WATCH, "&aЗабанить на 2 часа", ""));
        inv.setItem(14, Items.name(Material.WATCH, "&aЗабанить на 1 час", ""));
        inv.setItem(15, Items.name(Material.WATCH, "&aЗабанить на 30 минут", ""));
        inv.setItem(22, Items.name(new ItemStack(Material.STAINED_GLASS, 1, (short) 14), "&c&lОтменить действие", "&e► &7Вернуться в меню выбора наказания"));
    }

    @Override
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
        switch (slot) {
            case 10:
                if (!FlexingNetwork.hasRank(player, Rank.CHIKIBOMBASTER, true)) {
                    return;
                } else {
                    List<Pair<Integer, String>> objects = BanMenu.REASON;
                    for (Pair<Integer, String> object : objects) {
                        ConfirmMenu menu = new ConfirmMenu(() -> {
                            FlexingNetwork.ban(target, F.toMilliSec("1w"), object.getRight(), player.getName(), false);
                        }, "Подтвердите действия");
                        menu.setBackOnConfirm(false);
                        menu.setConfirmText("", "");
                        menu.setCancelText("");
                        player.openInventory(menu.getInventory());
                    }
                }
                break;
            case 11:
                if (!FlexingNetwork.hasRank(player, Rank.CHIKIBOMBASTER, true)) {
                    return;
                } else {
                    List<Pair<Integer, String>> objects = BanMenu.REASON;
                    for (Pair<Integer, String> object : objects) {
                        ConfirmMenu menu = new ConfirmMenu(() -> {
                            FlexingNetwork.ban(target, F.toMilliSec("1d"), object.getRight(), player.getName(), false);
                        }, "Подтвердите действия");
                        menu.setBackOnConfirm(false);
                        menu.setConfirmText("", "");
                        menu.setCancelText("");
                        player.openInventory(menu.getInventory());
                    }
                }
                break;
            case 12:
                if (!FlexingNetwork.hasRank(player, Rank.OWNER, true)) {
                    return;
                } else {
                    List<Pair<Integer, String>> objects = BanMenu.REASON;
                    for (Pair<Integer, String> object : objects) {
                        ConfirmMenu menu = new ConfirmMenu(() -> {
                            FlexingNetwork.ban(target, F.toMilliSec("6h"), object.getRight(), player.getName(), false);
                        }, "Подтвердите действия");
                        menu.setBackOnConfirm(false);
                        menu.setConfirmText("", "");
                        menu.setCancelText("");
                        player.openInventory(menu.getInventory());
                    }
                }
                break;
            case 13:
                if (!FlexingNetwork.hasRank(player, Rank.OWNER, true)) {
                    return;
                } else {
                    List<Pair<Integer, String>> objects = BanMenu.REASON;
                    for (Pair<Integer, String> object : objects) {
                        ConfirmMenu menu = new ConfirmMenu(() -> {
                            FlexingNetwork.ban(target, F.toMilliSec("2h"), object.getRight(), player.getName(), false);
                        }, "Подтвердите действия");
                        menu.setBackOnConfirm(false);
                        menu.setConfirmText("", "");
                        menu.setCancelText("");
                        player.openInventory(menu.getInventory());
                    }
                }
                break;
            case 14:
                if (!FlexingNetwork.hasRank(player, Rank.OWNER, true)) {
                    return;
                } else {
                    List<Pair<Integer, String>> objects = BanMenu.REASON;
                    for (Pair<Integer, String> object : objects) {
                        ConfirmMenu menu = new ConfirmMenu(() -> {
                            FlexingNetwork.ban(target, F.toMilliSec("1h"), object.getRight(), player.getName(), false);
                        }, "Подтвердите действия");
                        menu.setBackOnConfirm(false);
                        menu.setConfirmText("", "");
                        menu.setCancelText("");
                        player.openInventory(menu.getInventory());
                    }
                }
                break;
            case 15:
                if (!FlexingNetwork.hasRank(player, Rank.SPONSOR, true)) {
                    return;
                } else {
                    List<Pair<Integer, String>> objects = BanMenu.REASON;
                    for (Pair<Integer, String> object : objects) {
                        ConfirmMenu menu = new ConfirmMenu(() -> {
                            FlexingNetwork.ban(target, F.toMilliSec("30min"), object.getRight(), player.getName(), false);
                        }, "Подтвердите действия");
                        menu.setBackOnConfirm(false);
                        menu.setConfirmText("", "");
                        menu.setCancelText("");
                        player.openInventory(menu.getInventory());
                    }
                }
                break;
            case 22:
                player.openInventory(prev);
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
