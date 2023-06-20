package com.flexingstudios.flexingnetwork.impl.player.profileMenu;

import com.flexingstudios.flexingnetwork.api.Language.Messages;
import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer;
import com.flexingstudios.flexingnetwork.api.util.ItemBuilder;
import com.flexingstudios.flexingnetwork.api.util.Items;
import com.flexingstudios.flexingnetwork.api.util.SkullBuilder;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
import com.flexingstudios.flexingnetwork.impl.player.profileMenu.subMenu.PunishmentsMenu;
import com.flexingstudios.common.player.Leveling;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.menu.InvMenu;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ProfileMenu implements InvMenu {
    public final FlexPlayer player;
    public final Inventory inv;

    public ProfileMenu(Player player) {
        this(FlexingNetwork.INSTANCE.getPlayer(player));
    }

    public ProfileMenu(NetworkPlayer player) {
        this.player = (FlexPlayer) player;
        inv = Bukkit.createInventory(this, 45, Messages.PROFILE_MENU_TITLE.replace("{name}", player.getName()));

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
                "&fFlexCoin: &e" + Utils.pluralsCoins(player.getCoins()))); // "&b&lУровень " + player.getLevel() + " (" + (int)(progress * 100.0F) + "%) " + Utilities.genBar(48, progress, '|', "&7", "&a"))
//        inv.setItem(14, Items.name(new ItemBuilder(SkullBuilder.getSkull(Languages.byId(Language.getPlayerLanguage(bukkitPlayer).getIso()).getSkinURL())).build(), "&aИзменить язык &cNEW", "", "&2Нажмите, чтобы открыть меню смены языка."));
        inv.setItem(29, Items.name(new ItemStack(Material.INK_SACK, 1, (short) 12), "FlexCoin", "&aНажмите, чтобы открыть меню магазин FlexCoin."));
        inv.setItem(31, Items.name(Material.ENDER_PEARL, "История наказаний", "&aНажмите, чтобы открыть меню история наказаний."));
        //inv.setItem(33, Items.name(Material.FIREWORK_CHARGE, "&7Настройки", "&aНажмите, чтобы открыть меню настроек."));
    }

    @Override
    public void onClick(ItemStack is, Player player, int slot, ClickType clickType) {
        player.setCooldown(is.getType(), 60);
        if (player.hasCooldown(is.getType())) return;
        switch (slot) {
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
