package com.flexingstudios.flexingnetwork.impl.player.actionsMenu;

import com.flexingstudios.flexingnetwork.api.util.ItemBuilder;
import com.flexingstudios.flexingnetwork.api.util.Items;
import com.flexingstudios.flexingnetwork.api.util.Notifications;
import com.flexingstudios.flexingnetwork.api.util.SkullBuilder;
import com.flexingstudios.flexingnetwork.impl.player.actionsMenu.subMenu.BanMenu;
import com.flexingstudios.flexingnetwork.impl.player.actionsMenu.subMenu.KickMenu;
import com.flexingstudios.flexingnetwork.impl.player.actionsMenu.subMenu.MessageMenu;
import com.flexingstudios.flexingnetwork.impl.player.actionsMenu.subMenu.MuteMenu;
import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.menu.InvMenu;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
import com.flexingstudios.flexingnetwork.impl.player.MysqlPlayer;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import java.util.Set;

public class ActionsMenu implements InvMenu {
    private static Inventory inv;
    private final String target;

    public ActionsMenu(String target) {
        this.target = target;
        inv = Bukkit.createInventory(this, 54, "Быстрые действия — " + target);

        ItemStack GLASS_PANE_BLUE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta GLASS_PANE_BLUE_META = GLASS_PANE_BLUE.getItemMeta();
        GLASS_PANE_BLUE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_BLUE.setItemMeta(GLASS_PANE_BLUE_META);

        Set<Integer> GLASS_PANE_BLUE_SLOTS = ImmutableSet.of(0, 1, 2, 6, 7, 8);
        GLASS_PANE_BLUE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_BLUE));

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);

        Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(
                3, 5, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 20, 21,
                23, 24, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
                38, 39, 41, 42, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        GameProfile profile = new GameProfile(Bukkit.getPlayer(target).getUniqueId(), target);
        inv.setItem(4, Items.name(new ItemBuilder(SkullBuilder.getSkull(profile, 1)).build(), "", ""));
        inv.setItem(19, Items.name(Material.EMERALD, "Быстрые сообщения", ""));
        inv.setItem(22, Items.name(new ItemStack(new Dye(DyeColor.LIME).toItemStack(1)), "Добавить в друзья", ""));
        inv.setItem(25, Items.name(Material.SULPHUR, "ignore", ""));
        inv.setItem(37, Items.name(Material.REDSTONE, "кикнуть игрока — " + target, ""));
        inv.setItem(40, Items.name(Material.REDSTONE_BLOCK, "Забанить игрока — " + target, ""));
        inv.setItem(43, Items.name(new ItemStack(Material.INK_SACK, 1, (short) 1), "Замутить игрока — " + target, ""));
    }

    @Override
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
        MysqlPlayer flPlayer = (MysqlPlayer) FlexPlayer.get(player);

        switch (slot) {
            case 19:
                player.openInventory(new MessageMenu(target).getInventory());
                break;
            case 22:
                break;
            case 25:
                if (!flPlayer.ignored.contains(target)) {
                    flPlayer.ignored.add(target);
                    player.closeInventory();
                    Notifications.success(player, "GOT IT!", "Теперь вы игнорируете игрока " + target);
                } else {
                    flPlayer.ignored.remove(target);
                    player.closeInventory();
                    Notifications.success(player, "GOT IT!", "Вы больше не игнорируете игрока " + target);
                }
                break;
            case 37:
                if (FlexingNetwork.INSTANCE.hasRank(flPlayer.getBukkitPlayer(), Rank.CHIKIBAMBONYLA, true))
                    player.openInventory(new KickMenu(target).getInventory());
                break;
            case 40:
                if (FlexingNetwork.INSTANCE.hasRank(flPlayer.getBukkitPlayer(), Rank.SPONSOR, true))
                    player.openInventory(new BanMenu(target).getInventory());
                break;
            case 43:
                if (FlexingNetwork.INSTANCE.hasRank(flPlayer.getBukkitPlayer(), Rank.GOD, true))
                    player.openInventory(new MuteMenu(target, this).getInventory());
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}