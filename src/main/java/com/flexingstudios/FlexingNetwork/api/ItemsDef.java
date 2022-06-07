package com.flexingstudios.FlexingNetwork.api;

import com.flexingstudios.FlexingNetwork.api.util.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemsDef {

    public static final ItemStack ITEM_GAME_SELECT = Items.menuTitle(Material.BOOK, "Выбор игры", "§7ПКМ");
    public static final ItemStack ITEM_TEAM_SELECT = Items.menuTitle(Material.BED, "Выбор команды", "§7ПКМ");
    public static final ItemStack ITEM_TO_LOBBY = Items.menuTitle(Material.COMPASS, "Выйти в лобби", "§7ПКМ");
    public static final ItemStack ITEM_DONATE = Items.menuTitle(Material.DOUBLE_PLANT, "&bДонат услуги", "&a&lОписание всех донат-привилегий", "&a&lи прочее", "", "&7FlexingWorld");

    public static boolean isThisItem(ItemStack is) {
        return "§7ПКМ".equals(Items.getLore(is, -1));
    }
}
