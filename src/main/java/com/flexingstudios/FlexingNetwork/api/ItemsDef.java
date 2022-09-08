package com.flexingstudios.FlexingNetwork.api;

import com.flexingstudios.FlexingNetwork.api.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemsDef {
    public ItemStack ITEM_GAME_SELECT;
    public ItemStack ITEM_TEAM_SELECT;
    public ItemStack ITEM_TO_LOBBY;
    public ItemStack ITEM_DONATE;

    public ItemsDef(Player player) {
        ITEM_GAME_SELECT = Items.menuTitle(Material.BOOK, "Выбор арены", "§7Нажмите ПКМ");
        ITEM_TEAM_SELECT = Items.menuTitle(Material.BED, "Выбор команды", "§7Нажмите ПКМ");
        ITEM_TO_LOBBY = Items.menuTitle(Material.COMPASS, "Вернуться в лобби &7(ПКМ)", "§7Нажмите ПКМ");
        ITEM_DONATE = Items.menuTitle(Material.DOUBLE_PLANT, "&bДонат услуги", "§7Нажмите ПКМ", "&a&lОписание всех донат-привилегий", "&a&lи прочее", "", "&7FlexingWorld");
    }

    public static boolean isServiceItem(ItemStack is) {
        return "Нажмите ПКМ".equals(Items.getLore(is, -1));
    }
}
