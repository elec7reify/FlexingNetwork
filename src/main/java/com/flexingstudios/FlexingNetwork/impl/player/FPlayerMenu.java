package com.flexingstudios.FlexingNetwork.impl.player;

import com.flexingstudios.Commons.player.Leveling;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.mes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FPlayerMenu implements InvMenu {
    public final FLPlayer player;
    public final Inventory inv;

    public FPlayerMenu(Player player) {
        this(FlexingNetwork.getPlayer(player));
    }

    public FPlayerMenu(NetworkPlayer player) {
        this.player = (FLPlayer) player;
        int expToNextLevel = Leveling.getExpToNextLevel(player.getLevel());
        float progress = player.getPartialExp() / expToNextLevel;
        this.inv = Bukkit.createInventory(this, 45, player.getName());
        this.inv.setItem(22, Items.appendLore(Items.head(((FLPlayer) player).username),
                "&fВаш ник &r" + player.getName(),
                "&fВаша привилегия &r" + player.getRank().getDisplayName(),
                "&fФлекс-коинов &r" + mes.pluralsCoins(player.getCoins()),
                "&b&lУровень " + player.getLevel() + " (" + (int)(progress * 100.0F) + "%) " + mes.genBar(48, progress, '|', "&7", "&a")));
    }

    @Override
    public void onClick(ItemStack is, Player player, int slot, ClickType clickType) {

    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
