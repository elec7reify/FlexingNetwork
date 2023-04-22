package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.*;
import com.flexingstudios.FlexingNetwork.impl.GroupsMenu;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class FlexCoinsMenu implements InvMenu {
    private final Inventory inv;

    public FlexCoinsMenu(NetworkPlayer player) {
        this.inv = Bukkit.createInventory(this, 54, "FlexCoin");
        ItemStack INFO_BOOK = Items.name(Items.glow(Material.BOOK), "&a&lИнформация", "&9&lFlex&f&lCoin &f- особая игровая валюта, которую",
                "&fМожно получить только за &a&lреальные деньги",
                "&fДанная валюта используется для персонализации профиля:",
                "&a◆ &fПокупки &e&lТитулов", "&a◆ &fПокупки &b&lПерсонализированных сообщений", "&a◆ &fПокупки &d&lГаджетов и плюшек на выживании",
                "&a◆ &fПокупки &6&lСледов от стрел", "", "&f&lДанная валюта никак не влияет на экономику сервера",
                "&f&lЕё нельзя обменять на внутриигровую валюту и наоборот");

        Set<Integer> INFO_BOOK_SLOTS = ImmutableSet.of(12, 13, 14);
        INFO_BOOK_SLOTS.forEach(slot -> inv.setItem(slot, INFO_BOOK));

        this.inv.setItem(8, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/647cf0f3b9ec9df2485a9cd4795b60a391c8e6ebac96354de06e3357a9a88607", 1)).build(), "&3Вернуться назад"));
        this.inv.setItem(28, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/4cb3acdc11ca747bf710e59f4c8e9b3d949fdd364c6869831ca878f0763d1787", 1)).build(), "&fКупить &f&lМешочек &9&lFlex&f&lCoin &f- &a&l19 руб", "", "&7&lДля аскетычей, которым много не надо", "", "&fСодержит &b&l&n60&9&l Flex&f&lCoin"));
        this.inv.setItem(30, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/198df42f477f213ff5e9d7fa5a4cc4a69f20d9cef2b90c4ae4f29bd17287b5", 1)).build(), "&fКупить &f&lКопилку &9&lFlex&f&lCoin &f- &a&l39 руб", "", "&7&lДля желающих купить что-то, что сильно запало в душу", "", "&fСодержит &b&l&n120&9&l Flex&f&lCoin"));
        this.inv.setItem(32, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/b0b068709790d41b8927b8422d21bb52404b55b4ca352cdb7c68e4b36592721", 1)).build(), "&fКупить &f&lКоробочку &9&lFlex&f&lCoin &f- &a&l79 руб", "", "&7&lДля работяг, которым хочется больше", "", "&fСодержит &b&l&n240&9&l Flex&f&lCoin"));
        this.inv.setItem(34, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/ddcc189633c789cb6d5e78d13a5043b26e7b40cdb7cfc4e23aa2279574967b4", 1)).build(), "&fКупить &f&lЭндер-сундук &9&lFlex&f&lCoin &f- &a&l199 руб", "", "&7&lДля энтузиастов, которые любят повышать ставки", "", "&fСодержит &b&l&n600&9&l Flex&f&lCoin"));
        this.inv.setItem(37, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/d5c6dc2bbf51c36cfc7714585a6a5683ef2b14d47d8ff714654a893f5da622", 1)).build(), "&fКупить &f&lбольшой сундук &9&lFlex&f&lCoin &f- &a&l359 руб", "", "&7&lдля ребят, которые ставят персонализацию выше остального", "", "&fСодержит &b&l&n1080&9&l Flex&f&lCoin"));
        this.inv.setItem(39, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/776ad9ff7d606f31adb624b1496f67eb6d269944e147052e57e48741b1482a4", 1)).build(), "&fКупить &f&lПозолоченный сундук &9&lFlex&f&lCoin &f- &a&l799 руб", "", "&7&lДля обеспеченных богачей, которые хотят как можно больше", "", "&fСодержит &b&l&n1800&9&l Flex&f&lCoin"));
        this.inv.setItem(41, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/8358fba2f1bdc519ba85e5366bab8def9bbe404ef639c2e3c654d10a1d8d3", 1)).build(), "&fКупить &f&lЯщик &9&lFlex&f&lCoin &f- &a&l1599 руб", "", "&7&lДля олигархов, которым многое кажется малым", "", "&fСодержит &b&l&n4500&9&l Flex&f&lCoin"));
        this.inv.setItem(43, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/4ced34211fed4010a8c85724a27fa5fb205d67684b3da517b6821279c6b65d3f", 1)).build(), "&fКупить &f&lТелегу &9&lFlex&f&lCoin &f- &a&l2399 руб", "", "&7&lДля элиты, желающей выжать максимум", "", "&fСодержит &b&l&n7500&9&l Flex&f&lCoin"));
        this.inv.setItem(49, Items.name(Items.glow(Material.DIAMOND), "&b&lОткрыть магазин &9&lFlex&f&lCoin"));

        ItemStack GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta GLASS_PANE_META = GLASS_PANE.getItemMeta();
        GLASS_PANE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE.setItemMeta(GLASS_PANE_META);

        Set<Integer> GLASS_PANE_SLOTS = ImmutableSet.of(
                0, 1, 2, 3, 4, 5, 6, 7,
                9, 10, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 29, 31, 33, 35, 36, 38, 40, 42,44, 45, 46, 47, 48, 50, 51, 52, 53);
        GLASS_PANE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE));
    }

    @Override
    public void onClick(ItemStack item, NetworkPlayer player, int slot, ClickType clickType) {
        switch (slot) {
            case 8:
                player.getBukkitPlayer().openInventory(new GroupsMenu(player).getInventory());
                break;
            case 49:
                player.getBukkitPlayer().openInventory(new FCShopMenu(player).getInventory());
                break;
            case 28:
            case 30:
            case 32:
            case 34:
            case 37:
            case 39:
            case 41:
            case 43:
                Utilities.msg(player.getBukkitPlayer(), "&fДля покупки &9&lFlex&f&lCoin", "&fВы должны перейти на сайт &7↴", "&ehttps://vk.com/zzzrazum", "&fи написать администратору.");
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }
}
