package com.flexingstudios.FlexingNetwork.impl.player.profileMenu;

import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenuImpl;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.*;
import com.flexingstudios.FlexingNetwork.impl.player.profileMenu.subMenu.PunishmentsMenu;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ProfileMenu implements InvMenu {
    private final Inventory inv;

    public ProfileMenu(NetworkPlayer player) {
        inv = Bukkit.createInventory(player.getBukkitPlayer(), 45, Messages.PROFILE_MENU_TITLE.replace("{name}", player.getName()));
        Player bukkitPlayer = player.getBukkitPlayer();

        ItemStack GLASS_PANE_BLUE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        fill(0, 44, GLASS_PANE_BLUE);

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);

//        Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(
//                1, 2, 3, 4, 5, 6, 7, 10, 16, 19, 25, 28, 34, 37, 38, 39, 40, 41, 42, 43);
//        GLASS_PANE_WHITE_SLOTS.forEach(slot -> getInventory().setItem(slot, GLASS_PANE_WHITE));
        fill(1, 43, GLASS_PANE_WHITE);

        GameProfile gameProfile = new GameProfile(player.getBukkitPlayer().getUniqueId(), player.getName());
        getInventory().setItem(13, Items.name(new ItemBuilder(SkullBuilder.getSkull(gameProfile, 1)).build(), "&3Информация об аккаунте",
                "&fВаш ранг: &r" + player.getRank().getDisplayName(),
                "&fБаланс FlexCoin: &b" + Utilities.pluralsCoins(player.getCoins())));
        getInventory().setItem(29, Items.name(new ItemStack(Material.INK_SACK, 1, (short) 12), "&3FlexCoin", "&bНажмите, чтобы открыть меню магазин FlexCoin."));
        getInventory().setItem(31, Items.name(Material.ENDER_PEARL, "&3История наказаний", "&7Информация о наказаниях", "", "&bНажмите, чтобы увидеть больше!"));
        //inv.setItem(33, Items.name(Material.FIREWORK_CHARGE, "&7Настройки", "&aНажмите, чтобы открыть меню настроек."));
    }

    @Override
    public void onClick(ItemStack item, NetworkPlayer player, int slot, ClickType clickType) {
        switch (slot) {
            case 31:
                Invs.forceOpen(player.getBukkitPlayer(), new PunishmentsMenu(player, this).getInventory());
                break;
            case 33:
                //player.openInventory(new SettingsMenu(this.player, this).getInventory());
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
