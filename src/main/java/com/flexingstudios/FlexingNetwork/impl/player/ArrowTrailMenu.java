package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.ArrowTrail;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ArrowTrailMenu implements InvMenu {
    private final Inventory inv;
    private final FLPlayer player;

    public ArrowTrailMenu(NetworkPlayer nplayer) {
        this.inv = Bukkit.createInventory(this, 36, "След за стрелой");
        this.player = (FLPlayer) nplayer;
        int index = 0;
        for (ArrowTrail trail : ArrowTrail.values()) {
            String color, lore;
            ItemStack is = trail.getItem();
            if (this.player.availableArrowTrails.contains(trail.getId())) {
                if (player.getArrowTrail() == trail) {
                    color = "&a";
                    lore = "&aВыбрано";
                } else {
                    color = "&b";
                    lore = "&2Нажмите для выбора";
                }
            } else {
                color = "&c";
                lore = "&cНужно купить";
            }
            Items.name(is, color + trail.getName(), lore);
            this.inv.setItem(getSlot(index++), is);
        }
    }

    private int getSlot(int index) {
        return 10 + 9 * index / 7 + index % 7;
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
        int index = getIndex(slot);
        if (index < 0 || index >= (ArrowTrail.values()).length)
            return;
        ArrowTrail selected = ArrowTrail.values()[index];

        player.availableArrowTrails.add(selected.getId());
        player.unlockArrowTrail(selected);

        /*if (player.availableArrowTrails.contains(selected.getId()) &&
                player.getArrowTrail() != selected) {*/
            player.setArrowTrail(selected);
            bukkitPlayer.closeInventory();
        /*}*/
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
