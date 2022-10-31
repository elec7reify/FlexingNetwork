package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
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

public class FCoinsMenu implements InvMenu {
    private static final Set<Integer> GLASS_PANE_SLOTS = ImmutableSet.of(0, 1, 2, 3, 4, 5, Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11), Integer.valueOf(15), Integer.valueOf(16), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(29), Integer.valueOf(31), Integer.valueOf(33), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(38), Integer.valueOf(40), Integer.valueOf(42), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53));
    private static final Set<Integer> INFO_BOOK_SLOTS = ImmutableSet.of(12, 13, 14);
    private final Inventory inv;

    public FCoinsMenu(Player player) {
        this.inv = Bukkit.createInventory(this, 54, "FlexCoin");
        ItemStack INFO_BOOK = Items.name(Items.glow(Material.BOOK), "&a&lИнформация", "&9&lFlex&f&lCoin &f- особая игровая валюта, которую",
                "&fМожно получить только за &a&lреальные деньги",
                "&fДанная валюта используется для персонализации профиля:",
                "&a◆ &fПокупки &e&lТитулов", "&a◆ &fПокупки &b&lПерсонализированных сообщений", "&a◆ &fПокупки &d&lГаджетов и плюшек на выживании",
                "&a◆ &fПокупки &6&lСледов от стрел", "", "&f&lДанная валюта никак не влияет на экономику сервера",
                "&f&lЕё нельзя обменять на внутриигровую валюту и наоборот");
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
        GLASS_PANE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE));
    }

    @Override
    public void onClick(ItemStack is, Player player, int slot, ClickType clickType) {
        switch (slot) {
            case 8:
                player.openInventory(new GroupsMenu(player).getInventory());
                break;
            case 49:
                player.openInventory(new FlexCoinShop(player).getInventory());
                break;
            case 28:
            case 30:
            case 32:
            case 34:
            case 37:
            case 39:
            case 41:
            case 43:
                Utilities.msg(player, "&fДля покупки &9&lFlex&f&lCoin", "&fВы должны перейти на сайт &7↴", "&ehttps://vk.com/zzzrazum", "&fи написать администратору.");
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }

    private static class FlexCoinShop implements InvMenu {
        private static final Set<Integer> GLASS_PANE_BLACK_SLOTS = ImmutableSet.of(0, 7, 9, 16, 17, 18, 25, 26, 27, 28, 35, 36, 37, 44, 45, 46, 53);
        private static final Set<Integer> GLASS_PANE_BLUE_SLOTS = ImmutableSet.of(1, 2, 3, 10, 12, 19, 20, 21, 29, 30, 31, 38, 40, 47, 48, 49);
        private static final Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(4, 5, 6, 13, 15, 22, 23, 24, 32, 33, 34, 41, 43, 50, 51, 52);
        private final Inventory inv;

        public FlexCoinShop(Player player) {
            this.inv = Bukkit.createInventory(this, 54, "Магазин FlexCoin");

            NetworkPlayer nplayer = FlexingNetwork.getPlayer(player);

            ItemStack GLASS_PANE_BLACK = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            ItemMeta GLASS_PANE_BLACK_META = GLASS_PANE_BLACK.getItemMeta();
            GLASS_PANE_BLACK_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
            GLASS_PANE_BLACK.setItemMeta(GLASS_PANE_BLACK_META);
            GLASS_PANE_BLACK_SLOTS.forEach(slot -> this.inv.setItem(slot, GLASS_PANE_BLACK));

            ItemStack GLASS_PANE_BLUE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
            ItemMeta GLASS_PANE_BLUE_META = GLASS_PANE_BLUE.getItemMeta();
            GLASS_PANE_BLUE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
            GLASS_PANE_BLUE.setItemMeta(GLASS_PANE_BLUE_META);
            GLASS_PANE_BLUE_SLOTS.forEach(slot -> this.inv.setItem(slot, GLASS_PANE_BLUE));

            ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
            ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
            GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
            GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);
            GLASS_PANE_WHITE_SLOTS.forEach(slot -> this.inv.setItem(slot, GLASS_PANE_WHITE));

            this.inv.setItem(8, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/647cf0f3b9ec9df2485a9cd4795b60a391c8e6ebac96354de06e3357a9a88607", 1)).build(), "&3&lНа главную"));

            this.inv.setItem(11, Items.name(Material.CHEST, "&e&lТитулы", "&fТитулы &f- интересные приписки", "&fКоторые отображаются в чате и табе сразу после ника", "  &fНапример:",
                     nplayer.getRank().getDisplayName() + " &7" + player.getDisplayName() + " &5&lГачист", "", "&fВ магазине вы найдёте много разных титулов", "&fКоторые обязательно вам понравятся!", "", "&a&lКликните, чтобы перейти!"));

            this.inv.setItem(14, Items.name(Material.CHEST, "&b&lСообщения при входе", "&fСпециальные сообщения, которые приходят всем", "&fКогда вы зайдёте на сервер", "  &fНапример:",
                    "&8&l[&c&l+&8&l] " + nplayer.getRank().getDisplayName() + " &7" + player.getDisplayName() + " &aзалетел на тусу", "", "&a&lКликните, чтобы перейти!"));

            this.inv.setItem(39, Items.name(Material.CHEST, "&d&lГаджеты и плюшки &c&l(Survival only)", "&fМножество разнообразных плюшек", "&fОт крутых шапок до красивых баннеров",
                    "&fИдеально подойдут, чтобы повеселиться с друзьями", "", "&a&lКликните, чтобы перейти!"));

            this.inv.setItem(42, Items.name(Material.CHEST, "&6&lСледы от стрел и меча &c&l(Minigames only)", "&fРазличные следы при выстрелах из лука", "&fИли ударов мечом", "&fКрасиво, просто и со вкусом", "", "&a&lКликните, чтобы перейти!"));
        }

        @Override
        public void onClick(ItemStack is, Player player, int slot, ClickType clicktype) {
            FLPlayer flplayer = (FLPlayer) FlexingNetwork.getPlayer(player);
            switch (slot) {
                case 8:
                    player.openInventory(new FCoinsMenu(player).getInventory());
                    break;
                case 11:
                    break;
                case 14:
                    player.openInventory(new MsgJoinMenu(flplayer).getInventory());
                    break;
                case 39:
                    break;
                case 42:
                    player.openInventory(new ArrowTrailMenu(flplayer).getInventory());
                    break;
            }
        }

        @Override
        public Inventory getInventory() {
            return this.inv;
        }
    }
}
