package com.flexingstudios.flexingnetwork.impl.player;

import com.flexingstudios.flexingnetwork.api.menu.ConfirmMenu;
import com.flexingstudios.flexingnetwork.api.menu.InvMenu;
import com.flexingstudios.flexingnetwork.api.player.MessageOnJoin;
import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer;
import com.flexingstudios.flexingnetwork.api.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MsgJoinMenu implements InvMenu {
    private final Inventory inv;
    private final FlexPlayer player;

    public MsgJoinMenu(NetworkPlayer nplayer) {
        inv = Bukkit.createInventory(this, 36, "Сообщение при входе");
        player = (FlexPlayer) nplayer;
        int index = 0;

        for (MessageOnJoin message : MessageOnJoin.values()) {
            String color;
            List<String> lore = new ArrayList<>();
            ItemStack is = message.getItem();

            if (player.getAvailableJoinMessages().contains(message.getId())) {
                if (player.getMessageOnJoin() == message) {
                    color = "&a";
                    lore.add("&fСообщение содержит текст:");
                    lore.add("&7\"" + message.getMessage()
                            .replace("{rank}", player.getRank().getDisplayName())
                            .replace("{player}", player.getName()) + "&7\"");
                    lore.add("");
                    lore.add("&fРедкость: &3" + message.getRarity().getTag());
                    lore.add("");
                    lore.add("&aВыбрано!");
                } else {
                    color = "&6";
                    lore.add("&fСообщение содержит текст:");
                    lore.add("&7\"" + message.getMessage()
                            .replace("{rank}", player.getRank().getDisplayName())
                            .replace("{player}", player.getName()) + "&7\"");
                    lore.add("");
                    lore.add("&fРедкость: &3" + message.getRarity().getTag());
                    lore.add("");
                    lore.add("&2Нажмите для выбора.");
                }
            } else if (player.getCoins() <= message.getPrice()) {
                color = "&7";
                lore.add("&fЦена: &3" + message.getPrice());
                lore.add("");
                lore.add("&fСообщение содержит текст:");
                lore.add("&7\"" + message.getMessage()
                        .replace("{rank}", player.getRank().getDisplayName())
                        .replace("{player}", player.getName()) + "&7\"");
                lore.add("");
                lore.add("&fРедкость: &3" + message.getRarity().getTag());
                lore.add("");
                lore.add("&cУ Вас недостаточно средств");
                lore.add("&cЧтобы приобрести &6уведомление о входе&c:&r &c#" + message.getId());
            } else {
                color = "&6";
                lore.add("&fЦена: &3" + message.getPrice());
                lore.add("");
                lore.add("&fСообщение содержит текст:");
                lore.add("&7\"" + message.getMessage()
                        .replace("{rank}", player.getRank().getDisplayName())
                        .replace("{player}", player.getName()) + "&7\"");
                lore.add("");
                lore.add("&fРедкость: &3" + message.getRarity().getTag());
                lore.add("");
                lore.add("&6Нажмите, чтобы купить.");
            }
            Items.name(is, color + "Сообщение #" + message.getId(), lore);
            inv.setItem(getSlot(index++), is);
        }
    }

    private int getSlot(int index) {
        return 9 + 9 / 7 + index % 7;
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
    public void onClick(ItemStack itemStack, Player bukkitPlayer, int slot, ClickType ClickType) {
        int index = getIndex(slot);
        if (index < 0 || index >= MessageOnJoin.values().length)
            return;

        MessageOnJoin selected = MessageOnJoin.values()[index];

        if (!player.getAvailableJoinMessages().contains(selected.getId())) {
            if (player.getCoins() >= selected.getPrice()) {
                ConfirmMenu menu = new ConfirmMenu(getInventory(), () -> {
                    player.takeCoins(selected.getPrice());
                    player.unlockJoinMessage(selected);
                }, "Сообщение о входе #" + selected.getId());
                menu.setConfirmText("&a&lПОДТВЕРДИТЬ", "&7С Вас будет списано " + selected.getPrice() + " FlexCoin");
                menu.setCancelText("&c&lОТМЕНИТЬ ДЕЙСТВИЕ");
                bukkitPlayer.openInventory(menu.getInventory());
            } else {
                bukkitPlayer.closeInventory();
                bukkitPlayer.sendMessage("&cУ Вас недостаточно Флекс-Коинов для покупки этого сообщения при входе");
            }
        } else if (player.getAvailableJoinMessages().contains(selected.getId()) && player.getMessageOnJoin() != selected) {
            player.setMessageOnJoin(selected);
            bukkitPlayer.closeInventory();
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}