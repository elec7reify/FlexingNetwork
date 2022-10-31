package com.flexingstudios.FlexingNetwork.impl.player.actionsMenu.subMenu;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.menu.ConfirmMenu;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.actionsMenu.ActionsMenu;
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

public class KickMenu implements InvMenu {
    private final String target;
    private final Inventory inv;
    public static final List<String> REASON = Arrays.asList(
            "Использование читов",
            "Помеха игровому процессу",
            "Багоюз",
            "Игрок невыносим в чате",
            "Аморальное поведение",
            "Анти-AFK машина",
            "");

    public KickMenu(String target) {
        this.target = target;
        inv = Bukkit.createInventory(this, 27, "Выберите причину...");
        int index = 0;

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);

        Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 17,
                18, 19, 20, 21, 23, 24, 25, 26);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        inv.setItem(22, Items.name(new ItemStack(Material.STAINED_GLASS, 1, (short) 14), "&c&lОтменить действие", "&e► &7Вернуться в меню быстрых действий"));

        for (String reasons : REASON) {
            inv.setItem(getSlot(index++), Items.name(Material.EMPTY_MAP, "&a" + reasons));
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

        FLPlayer flPlayer = FLPlayer.get(player);
        int index = getIndex(slot);
        if (index >= 0 && index < KickMenu.REASON.size()) {
            String objects = KickMenu.REASON.get(index);
            player.closeInventory();
            ConfirmMenu menu = new ConfirmMenu(() -> {
                for (Player players : Bukkit.getOnlinePlayers())
                    Utilities.msg(players, Language.getMsg(players, Messages.KICKED_BY_ADMIN)
                            .replace("{kicked}", player.getName())
                            .replace("{targetName}", target)
                            .replace("{reason}", objects));

                // Immunity to kick
                if (flPlayer.has(Rank.ADMIN)) {
                    FlexingNetwork.kick(target, objects, player.getName(), false);
                } else if (flPlayer.has(Rank.SADMIN)) {
                    if (!FlexingNetwork.getPlayer(target).has(Rank.ADMIN)) {
                        FlexingNetwork.kick(target, objects, player.getName(), false);
                    }
                } else if (flPlayer.has(Rank.VADMIN)) {
                    if (!FlexingNetwork.getPlayer(target).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(target).has(Rank.SADMIN)) {
                        FlexingNetwork.kick(target, objects, player.getName(), false);
                    }
                } else if (flPlayer.has(Rank.TEAM)) {
                    if (!FlexingNetwork.getPlayer(target).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(target).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(target).has(Rank.VADMIN)) {
                        FlexingNetwork.kick(target, objects, player.getName(), false);
                    }
                } else if (flPlayer.has(Rank.GOD) && !FlexingNetwork.getPlayer(target).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(target).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(target).has(Rank.VADMIN) && !FlexingNetwork.getPlayer(target).has(Rank.TEAM)) {
                    FlexingNetwork.kick(target, objects, player.getName(), false);
                }
            }, "Подтвердите действия");
            menu.setBackOnConfirm(false);
            menu.setConfirmText("OK");
            menu.setCancelText("NO");
            player.openInventory(menu.getInventory());
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
