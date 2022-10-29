package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.Commons.F;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.menu.ConfirmMenu;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.friends.utils.FriendsManager;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ActionsMenu implements InvMenu {
    private static Inventory inv;
    private final Set<Integer> GLASS_PANE_BLUE_SLOTS= ImmutableSet.of(0, 1, 2, 6, 7, 8);
    private final Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(
            3, 5, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 21,
            23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
            38, 39, 41, 42, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
    private final String target;

    public ActionsMenu(String target) {
        this.target = target;
        inv = Bukkit.createInventory(this, 54, "Быстрые действия — " + target);

        ItemStack GLASS_PANE_BLUE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta GLASS_PANE_BLUE_META = GLASS_PANE_BLUE.getItemMeta();
        GLASS_PANE_BLUE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_BLUE.setItemMeta(GLASS_PANE_BLUE_META);
        GLASS_PANE_BLUE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_BLUE));

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        inv.setItem(22, Items.name(new ItemStack(new Dye(DyeColor.LIME).toItemStack(1)), "Добавить в друзья", ""));
        inv.setItem(37, Items.name(Material.REDSTONE, "кикнуть игрока — " + target, ""));
        inv.setItem(40, Items.name(Material.REDSTONE_BLOCK, "Забанить игрока — " + target, ""));
    }

    @Override
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
        switch (slot) {
            case 22:
                FriendsManager.addFriendRequest(player.getName(), target);
                Utilities.msg(player, "&2Друзья » §aИгроку " + target + " &aотправлена заявка в друзья.");
                break;
            case 37:
                break;
            case 40:
                player.openInventory(new BanMenu(target).getInventory());
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    private static class KickMenu implements InvMenu {
        private String target;

        public KickMenu(String target) {
            this.target = target;
            inv = Bukkit.createInventory(this, 54, "d");
            int index = 0;

            inv.setItem(getSlot(index++), Items.name(Material.BIRCH_FENCE, ""));
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

        }

        @Override
        public Inventory getInventory() {
            return inv;
        }
    }

    private static class BanMenu implements InvMenu {
        private final Inventory inv;
        private String target;
        private static final List<Pair<Integer, String>> REASON = Arrays.asList(
                Pair.of(F.toMilliSec("999y"),"Использование читов"),
                Pair.of(F.toMilliSec("2h"), "Тим на мини-играх (одиночная игра)"),
                Pair.of(F.toMilliSec("999y"), "Некорректный никнейм"),
                Pair.of(F.toMilliSec("1h"), "Помеха игровому процессу"),
                Pair.of(F.toMilliSec("1d"), "Попытка нарушения стабильности сервера"),
                Pair.of(F.toMilliSec("999y"), "Продажа/передача аккаунта третьим лицам"),
                Pair.of(F.toMilliSec("999y"), "Продажа за реальные деньги"),
                Pair.of(F.toMilliSec("999y"), "Пиар посторонних ресурсов не принадлежащих команде FlexingWorld"));

        public BanMenu(String target) {
            this.target = target;
            this.inv = Bukkit.createInventory(this, 54, "Выберите причину наказания");
            int index = 0;
            for (Pair<Integer, String> objects : REASON) {
                String time = F.formatSecondsShort(objects.getLeft());
                this.inv.setItem(getSlot(index++), Items.name(Material.PAPER, objects.getRight(), "" + time));
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
            int index = getSlot(slot);
            if (index >= 0 && index < BanMenu.REASON.size()) {
                Pair<Integer, String> objects = BanMenu.REASON.get(index);
                player.openInventory(new TimeValueMenu(target, objects.getLeft(), objects).getInventory());
            }
        }

        @Override
        public Inventory getInventory() {
            return this.inv;
        }
    }

    private static class TimeValueMenu implements InvMenu {
        private final Inventory inv;
        private final String target;

        public TimeValueMenu(String target, int time, Pair<Integer, String> REASON) {
            this.target = target;
            this.inv = Bukkit.createInventory(this, 9, "Time");
            this.inv.setItem(0, Items.name(Material.MAGMA, "Забанить навсегда", ""));
            this.inv.setItem(1, Items.name(Material.BIRCH_FENCE, "Забанить на 30 минут", ""));
        }

        @Override
        public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
            switch (slot) {
                case 0:
                    if (FlexingNetwork.hasRank(player, Rank.TEAM, true)) {
                        return;
                    } else {
                        Pair<Integer, String> objects = (Pair<Integer, String>) BanMenu.REASON;
                        ConfirmMenu cfm_ban = new ConfirmMenu(this.inv, () -> {
                            FlexingNetwork.ban(target, objects.getLeft(), objects.getRight(), player.getName(), false);
                        }, "Подтверждение бана");

                        cfm_ban.setBackOnConfirm(false);
                        cfm_ban.setConfirmText("", "");
                        cfm_ban.setCancelText("");
                    }
                    break;
                case 1:
                    if (FlexingNetwork.hasRank(player, Rank.SPONSOR, true)) {
                        return;
                    } else {
                        Pair<Integer, String> objects = (Pair<Integer, String>) BanMenu.REASON;
                        ConfirmMenu cfm_ban = new ConfirmMenu(this.inv, () -> {
                            FlexingNetwork.ban(target, objects.getLeft(), objects.getRight(), player.getName(), false);
                        }, "Подтверждение бана");

                        cfm_ban.setBackOnConfirm(false);
                        cfm_ban.setConfirmText("", "");
                        cfm_ban.setCancelText("");
                    }
                    break;
            }
        }

        @Override
        public Inventory getInventory() {
            return this.inv;
        }
    }
}
