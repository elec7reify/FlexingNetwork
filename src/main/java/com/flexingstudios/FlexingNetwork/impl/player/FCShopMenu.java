package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.ItemBuilder;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.SkullBuilder;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class FCShopMenu implements InvMenu {
    private static final Set<Integer> GLASS_PANE_BLACK_SLOTS = ImmutableSet.of(0, 7, 9, 16, 17, 18, 25, 26, 27, 28, 35, 36, 37, 44, 45, 46, 53);
    private static final Set<Integer> GLASS_PANE_BLUE_SLOTS = ImmutableSet.of(1, 2, 3, 10, 12, 19, 20, 21, 29, 30, 31, 38, 40, 47, 48, 49);
    private static final Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(4, 5, 6, 13, 15, 22, 23, 24, 32, 33, 34, 41, 43, 50, 51, 52);
    private final Inventory inv;

    public FCShopMenu(Player player) {
        inv = Bukkit.createInventory(this, 54, "Магазин FlexCoin");

        NetworkPlayer nplayer = FlexingNetwork.getPlayer(player);

        ItemStack GLASS_PANE_BLACK = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta GLASS_PANE_BLACK_META = GLASS_PANE_BLACK.getItemMeta();
        GLASS_PANE_BLACK_META.setDisplayName(Utilities.colored("&6&k|&a&k|&e&k|&c&k|"));
        GLASS_PANE_BLACK.setItemMeta(GLASS_PANE_BLACK_META);
        GLASS_PANE_BLACK_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_BLACK));

        ItemStack GLASS_PANE_BLUE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta GLASS_PANE_BLUE_META = GLASS_PANE_BLUE.getItemMeta();
        GLASS_PANE_BLUE_META.setDisplayName(Utilities.colored("&6&k|&a&k|&e&k|&c&k|"));
        GLASS_PANE_BLUE.setItemMeta(GLASS_PANE_BLUE_META);
        GLASS_PANE_BLUE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_BLUE));

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName(Utilities.colored("&6&k|&a&k|&e&k|&c&k|"));
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        inv.setItem(8, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/647cf0f3b9ec9df2485a9cd4795b60a391c8e6ebac96354de06e3357a9a88607", 1)).build(), "&3&lНа главную"));

        inv.setItem(11, Items.name(Material.CHEST, "&e&lТитулы", "&fТитулы &f- интересные приписки", "&fКоторые отображаются в чате и табе сразу после ника", "  &fНапример:",
                nplayer.getRank().getDisplayName() + " &7" + player.getDisplayName() + " &5&lГачист", "", "&fВ магазине вы найдёте много разных титулов", "&fКоторые обязательно вам понравятся!", "", "&aНажмите, чтобы перейти."));

        inv.setItem(14, Items.name(Material.CHEST, "&b&lСообщения при входе", "&fСпециальные сообщения, которые приходят всем", "&fКогда вы зайдёте на сервер", "  &fНапример:",
                "&8&l[&c&l+&8&l] " + nplayer.getRank().getDisplayName() + " &7" + player.getDisplayName() + " &aзалетел на тусу", "", "&aНажмите, чтобы перейти."));

        inv.setItem(39, Items.name(Material.CHEST, "&d&lГаджеты и плюшки &c&l(Survival only)", "&fМножество разнообразных плюшек", "&fОт крутых шапок до красивых баннеров",
                "&fИдеально подойдут, чтобы повеселиться с друзьями", "", "&aНажмите, чтобы перейти."));

        inv.setItem(42, Items.name(Material.CHEST, "&6&lСледы от стрел &c&l(Minigames only)", "&fРазличные следы при выстрелах из лука", "&fКрасиво, просто, и со вкусом", "", "&aНажмите, чтобы перейти."));
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
                player.openInventory(new ArrowTrailMenu(flplayer, this).getInventory());
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
