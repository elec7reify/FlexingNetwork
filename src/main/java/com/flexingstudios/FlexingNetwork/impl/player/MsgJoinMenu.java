package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.ArrowTrail;
import com.flexingstudios.FlexingNetwork.api.player.MessageOnJoin;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MsgJoinMenu implements InvMenu {
    private final Inventory inv;
    private final FLPlayer player;

    public MsgJoinMenu(NetworkPlayer nplayer) {
        inv = Bukkit.createInventory(this, 36, "Сообщение при входе");
        player = (FLPlayer) nplayer;
        int index = 0;

        for (MessageOnJoin msg : MessageOnJoin.values()) {
            String color, lore, withPrice;
            ItemStack is = msg.getItem();
            if (player.availableJoinMessages.contains(msg.getId())) {
                if (player.getMessageOnJoin() == msg) {
                    color = "&a";
                    lore = "&aВыбрано";
                    withPrice = null;
                } else {
                    color = "&b";
                    lore = "&2Нажмите для выбора";
                    withPrice = null;
                }
            } else {
                color = "&c";
                lore = "&cНужно купить";
                withPrice = "Стоимость: " + msg.getPrice();
            }
            Items.name(is, color + msg.getItem().getItemMeta().getDisplayName(), msg.getItem().getItemMeta().getLore());
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

        if (!player.availableJoinMessages.contains(selected.getId())) {
            if (player.getCoins() >= selected.getPrice()) {
                player.takeCoins(selected.getPrice());
                player.unlockJoinMessage(selected);
                bukkitPlayer.closeInventory();
                bukkitPlayer.openInventory(inv);
            } else {
                bukkitPlayer.closeInventory();
                bukkitPlayer.sendMessage("&cУ Вас недостаточно Флекс-Коинов для покупки этого сообщения при входе");
            }
        } else if (player.availableJoinMessages.contains(selected.getId()) && player.getMessageOnJoin() != selected) {
            player.setMessageOnJoin(selected);
            bukkitPlayer.closeInventory();
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
