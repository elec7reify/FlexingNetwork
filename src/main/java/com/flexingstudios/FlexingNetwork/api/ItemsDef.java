package com.flexingstudios.FlexingNetwork.api;

import com.flexingstudios.FlexingNetwork.api.util.Items;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemsDef {
    public static final ItemStack ITEM_GAME_SELECT = Items.menuTitle(Material.BOOK, "Выбор арены", "§7Нажмите ПКМ");
    public static final ItemStack ITEM_TEAM_SELECT = Items.menuTitle(Material.BED, "Выбор команды", "§7Нажмите ПКМ");
    public static final ItemStack ITEM_TO_LOBBY = Items.menuTitle(Material.COMPASS, "Вернуться в лобби &7(ПКМ)", "§7Нажмите ПКМ");
    public static final ItemStack ITEM_DONATE = Items.menuTitle(Material.DOUBLE_PLANT, "&bДонат услуги", "§7Нажмите ПКМ", "&a&lОписание всех донат-привилегий", "&a&lи прочее", "", "&7FlexingWorld");

    public static boolean isServiceItem(ItemStack is) {
        return "§7Нажмите ПКМ".equals(Items.getLore(is, -1));
    }

    public static boolean is(ItemStack is) {
        return "".equals(Items.getLore(is, -1));
    }
}
