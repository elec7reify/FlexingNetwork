package com.flexingstudios.FlexingNetwork.impl.player.profileMenu;

import com.flexingstudios.Commons.player.Leveling;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.ItemBuilder;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.SkullBuilder;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.profileMenu.subMenu.LanguageMenu;
import com.flexingstudios.FlexingNetwork.impl.player.profileMenu.subMenu.Languages;
import com.flexingstudios.FlexingNetwork.impl.player.profileMenu.subMenu.PunishmentsMenu;
import com.flexingstudios.FlexingNetwork.impl.player.profileMenu.subMenu.SettingsMenu;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class FPlayerMenu implements InvMenu {
    public final FLPlayer player;
    public final Inventory inv;

    public FPlayerMenu(Player player) {
        this(FlexingNetwork.getPlayer(player));
    }

    public FPlayerMenu(NetworkPlayer player) {
        this.player = (FLPlayer) player;
        inv = Bukkit.createInventory(this, 45, Language.getMsg(player, Messages.PROFILE_MENU_TITLE).replace("{name}", player.getName()));

        Player bukkitPlayer = player.getBukkitPlayer();
        int expToNextLevel = Leveling.getExpToNextLevel(player.getLevel());
        float progress = player.getPartialExp() / expToNextLevel;

        ItemStack GLASS_PANE_BLUE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta GLASS_PANE_BLUE_META = GLASS_PANE_BLUE.getItemMeta();
        GLASS_PANE_BLUE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_BLUE.setItemMeta(GLASS_PANE_BLUE_META);

        Set<Integer> GLASS_PANE_BLUE_SLOTS = ImmutableSet.of(0, 8, 9, 17, 18, 26, 27, 35, 36, 44);
        GLASS_PANE_BLUE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_BLUE));

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);

        Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(
                1, 2, 3, 4, 5, 6, 7, 10, 16, 19, 25, 28, 34, 37, 38, 39, 40, 41, 42, 43);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        GameProfile gameProfile = new GameProfile(player.getBukkitPlayer().getUniqueId(), player.getName());
        inv.setItem(13, Items.name(new ItemBuilder(SkullBuilder.getSkull(gameProfile, 1)).build(), "Ваш профиль",
                "&fВаш ник: &e" + player.getName(),
                "&fВаш ранг: &r" + player.getRank().getDisplayName(),
                "&fFlexCoin: &e" + Utilities.pluralsCoins(player.getCoins()))); // "&b&lУровень " + player.getLevel() + " (" + (int)(progress * 100.0F) + "%) " + Utilities.genBar(48, progress, '|', "&7", "&a"))
        inv.setItem(14, Items.name(new ItemBuilder(SkullBuilder.getSkull(Languages.byId(Language.getPlayerLanguage(bukkitPlayer).getIso()).getSkinURL())).build(), "&aИзменить язык &cNEW", "", "&2Нажмите, чтобы открыть меню смены языка."));
        inv.setItem(29, Items.name(new ItemStack(Material.INK_SACK, 1, (short) 12), "FlexCoin", "&aНажмите, чтобы открыть меню магазин FlexCoin."));
        inv.setItem(31, Items.name(Material.ENDER_PEARL, "История наказаний", "&aНажмите, чтобы открыть меню история наказаний."));
        //inv.setItem(33, Items.name(Material.FIREWORK_CHARGE, "&7Настройки", "&aНажмите, чтобы открыть меню настроек."));
    }

    @Override
    public void onClick(ItemStack is, Player player, int slot, ClickType clickType) {
        switch (slot) {
            case 14:
                player.openInventory(new LanguageMenu(player, this).getInventory());
                break;
            case 31:
                player.openInventory(new PunishmentsMenu(player, this).getInventory());
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
