package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.menu.ConfirmMenu;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenuImpl;
import com.flexingstudios.FlexingNetwork.api.player.ArrowTrail;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Invs;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ArrowTrailMenu extends InvMenuImpl {
    private final FCShopMenu parent;
    private int page = 0;
    private boolean hasNextPage;

    public ArrowTrailMenu(NetworkPlayer player, FCShopMenu parent) {
        super(Bukkit.createInventory(null, 54, "Следы за стрелой"));
        this.parent = parent;

        ItemStack GLASS_PANE_BLUE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        fill(0, 53, GLASS_PANE_BLUE);

        ItemStack GLASS_PANE_WHITE = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        fill(1, 52, GLASS_PANE_WHITE);

        getInventory().setItem(40, Items.name(Material.FEATHER, "&3Вернуться назад", "&3► &7Вернуться в Магазин FlexCoin"));

        int index = 0;
        for (ArrowTrail trail : ArrowTrail.values()) {
            String color, title;
            List<String> lore = new ArrayList<>();
            ItemStack is = trail.getItem();

            if (trail.getAttributes() != null) {
                String tag = Arrays.stream(trail.getAttributes()).map(attribute -> attribute.getColor() + attribute.getTag()).collect(Collectors.joining(" &7&&r "));
                title = trail.getName() + " " + tag;
            } else  {
                title = trail.getName();
            }

            if (player.getAvailableArrowTrails().contains(trail.getId())) {
                if (player.getArrowTrail() == trail) {
                    color = "&a";
                    lore.add("&fРедкость: &3" + trail.getRarity().getTag());
                    lore.add("");
                    lore.add("&aВыбрано!");
                } else {
                    color = "&6";
                    lore.add("&fРедкость: &3" + trail.getRarity().getTag());
                    lore.add("");
                    lore.add("&2Нажмите для выбора.");
                }
            } else if (player.getCoins() <= trail.getPrice()) {
                color = "&7";
                lore.add("&fЦена: &3" + trail.getPrice());
                lore.add("");
                lore.add("&fРедкость: &3" + trail.getRarity().getTag());
                lore.add("");
                lore.add("&cУ Вас недостаточно средств");
                lore.add("&cЧтобы приобрести &6след от стрелы&c:&r " + trail.getName());
            } else {
                color = "&6";
                lore.add("&fЦена: &3" + trail.getPrice());
                lore.add("");
                lore.add("&fРедкость: &3" + trail.getRarity().getTag());
                lore.add("");
                lore.add("&6Нажмите, чтобы купить.");
            }

            Items.name(is, color + title, lore);
            Arrays.sort(ArrowTrail.values(), Comparator.comparingInt(ArrowTrail::getPrice));
            getInventory().setItem(getSlot(index++), is);
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
    public void onClick(ItemStack item, NetworkPlayer player, int slot, ClickType clickType) {
        Player bukkitPlayer = player.getBukkitPlayer();
        if (slot == 40) Invs.forceOpen(player.getBukkitPlayer(), parent.getInventory());

        int index = getIndex(slot);
        if (index < 0 || index >= ArrowTrail.values().length) return;

        ArrowTrail selected = ArrowTrail.values()[index];
        if (!player.getAvailableArrowTrails().contains(selected.getId())) {
            if (player.getCoins() >= selected.getPrice()) {
                ConfirmMenu menu = new ConfirmMenu(getInventory(), () -> {
                    player.takeCoins(selected.getPrice());
                    player.unlockArrowTrail(selected);
                }, selected.getName());
                menu.setConfirmText("&a&lПОДТВЕРДИТЬ", "&7С Вас будет списано " + selected.getPrice() + " FlexCoin");
                menu.setCancelText("&c&lОТМЕНИТЬ ДЕЙСТВИЕ");
                bukkitPlayer.openInventory(menu.getInventory());
            } else {
                bukkitPlayer.closeInventory();
                Utilities.msg(bukkitPlayer, "&cУ Вас недостаточно коинов для покупки этого следа от стрелы.");
            }
        } else if (player.getAvailableArrowTrails().contains(selected.getId()) && player.getArrowTrail() != selected) {
            Utilities.msg(bukkitPlayer, Messages.ARROWTRAIL_SELECTED.replace("{trail_name}", selected.getName()));
            player.setArrowTrail(selected);
            bukkitPlayer.closeInventory();
        }
    }
}
