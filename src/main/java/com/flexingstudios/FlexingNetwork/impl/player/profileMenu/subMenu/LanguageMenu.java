package com.flexingstudios.FlexingNetwork.impl.player.profileMenu.subMenu;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.profileMenu.FlexPlayerMenu;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LanguageMenu implements InvMenu {
    private final Inventory inv;
    private final FlexPlayerMenu parent;

    public LanguageMenu(Player player, FlexPlayerMenu parent) {
        inv = Bukkit.createInventory(this, 27, Language.getMsg(player, Messages.MENU_LANGUAGE_TITLE));
        this.parent = parent;
        int index = 0;
        String title;

        inv.setItem(22, Items.name(Material.FEATHER, "&aCome back &7(My Profile)"));

        for (Languages lang : Languages.values()) {
            ItemStack is = lang.getItem();

            if (lang.isNew()) {
                if (lang.getItem().getType().equals(Material.BOOK_AND_QUILL)) {
                    title = "&a" + Language.getLang(lang.getId()).getString(Messages.LANGUAGE_NAME) + // (English)
                            " &a&l" + Language.getLang(lang.getId()).getString(Messages.NEW) + // (NEW)
                            " &c" + Language.getLang(lang.getId()).getString(Messages.COMMING_SOON); // (COMMING SOON)
                } else {
                    title = "&a" + Language.getLang(lang.getId()).getString(Messages.LANGUAGE_NAME) + // (English)
                            " &a&l" + Language.getLang(lang.getId()).getString(Messages.NEW); // (NEW)
                }
            } else {
                if (lang.getItem().getType().equals(Material.BOOK_AND_QUILL)) {
                    title = "&a" + Language.getLang(lang.getId()).getString(Messages.LANGUAGE_NAME) + // (English)
                            " &c" + Language.getLang(lang.getId()).getString(Messages.COMMING_SOON); // (COMMING SOON)
                } else {
                    title = "&a" + Language.getLang(lang.getId()).getString(Messages.LANGUAGE_NAME); // (English)
                }
            }

            Items.name(is, title,
                    // Lore
                    lang.getItem().getType().equals(Material.BOOK_AND_QUILL) ?
                            Language.getLang(lang.getId()).getStringList(Messages.MENU_LANGUAGE_LORE_COMMING_SOON) :
                            Language.getLang(lang.getId()).getStringList(Messages.MENU_LANGUAGE_LORE));
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
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
        if (slot == 22) player.openInventory(parent.getInventory());

        int index = getIndex(slot);
        if (index < 0 || index >= Languages.values().length) return;

        Languages selected = Languages.values()[index];
        TextComponent url = new TextComponent(new ComponentBuilder(Utilities.colored("&3&nhttps://crowdin.com/project/flexingworld"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("&7Нажмите, чтобы открыть ссылку")))
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://crowdin.com/project/flexingworld"))
                .create());
        if (selected.getItem().getType() == Material.BOOK_AND_QUILL) {
            player.closeInventory();
            Utilities.msg(player, "&fДанный язык пока невозможно выбрать!",
                    "&fЧтобы помочь перевести FlexingWorld",
                    "&fПерейдите по этой ссылке:");
            player.spigot().sendMessage(url);
            return;
        }
        Language.setPlayerLanguage(player.getUniqueId(), selected.getId());
        Bukkit.getScheduler().runTaskLaterAsynchronously(FlexingNetworkPlugin.getInstance(), player::closeInventory, 1L);
        Bukkit.getScheduler().runTaskLaterAsynchronously(FlexingNetworkPlugin.getInstance(), () -> player.openInventory(getInventory()), 10L);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}

