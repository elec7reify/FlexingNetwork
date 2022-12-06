package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.menu.ConfirmMenu;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.ArrowTrail;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ArrowTrailMenu implements InvMenu {
    private final Inventory inv;
    private final FLPlayer player;
    private final FCShopMenu parent;
    private int page = 0;
    private boolean hasNextPage;

    public ArrowTrailMenu(NetworkPlayer nplayer, FCShopMenu parent) {
        inv = Bukkit.createInventory(this, 54, "Следы за стрелой");
        player = (FLPlayer) nplayer;
        this.parent = parent;

        ItemStack GLASS_PANE_BLUE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta GLASS_PANE_BLUE_META = GLASS_PANE_BLUE.getItemMeta();
        GLASS_PANE_BLUE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_BLUE.setItemMeta(GLASS_PANE_BLUE_META);

        Set<Integer> GLASS_PANE_BLUE_SLOTS = ImmutableSet.of(0, 8, 9, 17, 18, 26, 27, 35, 36, 38, 39, 41, 42, 44, 45, 53);
        GLASS_PANE_BLUE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_BLUE));

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta GLASS_PANE_WHITE_META = GLASS_PANE_WHITE.getItemMeta();
        GLASS_PANE_WHITE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE_WHITE.setItemMeta(GLASS_PANE_WHITE_META);

        Set<Integer> GLASS_PANE_WHITE_SLOTS = ImmutableSet.of(1, 2, 3, 4, 5, 6, 7, 37, 43, 46, 47, 48, 49, 50, 51, 52);
        GLASS_PANE_WHITE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE_WHITE));

        inv.setItem(40, Items.name(Material.FEATHER, "&3Вернуться назад", "&3► &7Вернуться в Магазин FlexCoin"));

        int index = 0;
        for (ArrowTrail trail : ArrowTrail.values()) {
            String color, title;
            List<String> lore = new ArrayList<>();
            ItemStack is = trail.getItem();

            if (trail.isNew()) {
                title = replaceToAnyLang(trail) + " &c" + Language.getMsg(player, Messages.NEW);
            } else  {
                title = replaceToAnyLang(trail);
            }

            if (player.availableArrowTrails.contains(trail.getId())) {
                if (player.getArrowTrail() == trail) {
                    color = "&a";
                    lore.add("&fРедкость: &3" + trail.getRarity());
                    lore.add("");
                    lore.add("&aВыбрано!");
                } else {
                    color = "&b";
                    lore.add("&fРедкость: &3" + trail.getRarity());
                    lore.add("");
                    lore.add("&2Нажмите для выбора.");
                }
            } else if (player.getCoins() <= trail.getPrice()) {
                color = "&c";
                lore.add("&fЦена: &3" + trail.getPrice());
                lore.add("");
                lore.add("&fРедкость: &3" + trail.getRarity());
                lore.add("");
                lore.add("&cУ Вас недостаточно средств");
                lore.add("&cЧтобы приобрести: " + replaceToAnyLang(trail));
            } else {
                color = "&6";
                lore.add("&fЦена: &3" + trail.getPrice());
                lore.add("");
                lore.add("&fРедкость: &3" + trail.getRarity());
                lore.add("");
                //for (String str : Language.getList(player.getBukkitPlayer(), Messages.CLICK_TO_BUY))
                lore.add(Language.getMsg(player.getBukkitPlayer(), Messages.CLICK_TO_BUY));
            }

            Items.name(is, color + title, lore);
            inv.setItem(getSlot(index++), is);
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
    public void onClick(ItemStack is, Player bukkitPlayer, int slot, ClickType clickType) {
        if (slot == 40) bukkitPlayer.openInventory(parent.getInventory());

        int index = getIndex(slot);
        if (index < 0 || index >= ArrowTrail.values().length) return;

        ArrowTrail selected = ArrowTrail.values()[index];
        if (!player.availableArrowTrails.contains(selected.getId())) {
            if (player.getCoins() >= selected.getPrice()) {
                ConfirmMenu menu = new ConfirmMenu(getInventory(), () -> {
                    player.takeCoins(selected.getPrice());
                    player.unlockArrowTrail(selected);
                }, Language.getMsg(bukkitPlayer, Messages.ARROWTRAIL_NAME + ArrowTrail.byId(selected.getId()).name().toLowerCase()));
                menu.setConfirmText("&a&lПОДТВЕРДИТЬ", "&7С Вас будет списано 70 FlexCoin");
                menu.setCancelText("&c&lОТМЕНИТЬ ДЕЙСТВИЕ");
                bukkitPlayer.openInventory(menu.getInventory());
            } else {
                bukkitPlayer.closeInventory();
                Utilities.msg(bukkitPlayer, "&cУ Вас недостаточно коинов для покупки этого следа от стрелы.");
            }
        } else if (player.availableArrowTrails.contains(selected.getId()) && player.getArrowTrail() != selected) {
            Utilities.msg(bukkitPlayer, Language.getMsg(player, Messages.ARROWTRAIL_SELECTED).replace("{trail_name}", replaceToAnyLang(selected)));
            player.setArrowTrail(selected);
            bukkitPlayer.closeInventory();
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    private String replaceToAnyLang(ArrowTrail trail) {
        return !Language.getPlayerLanguage(player.getBukkitPlayer()).exists(Messages.ARROWTRAIL_NAME + ArrowTrail.byId(trail.getId()).name().toLowerCase()) ?
                trail.getName() :
                Language.getMsg(player, Messages.ARROWTRAIL_NAME + ArrowTrail.byId(trail.getId()).name().toLowerCase());
    }
}
