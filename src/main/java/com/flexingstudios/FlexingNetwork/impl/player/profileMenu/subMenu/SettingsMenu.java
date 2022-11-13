package com.flexingstudios.FlexingNetwork.impl.player.profileMenu.subMenu;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.profileMenu.FPlayerMenu;
import net.minecraft.server.v1_12_R1.EntityEnderPearl;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsMenu implements InvMenu {
    private final Inventory inv;
    private final FPlayerMenu parent;
    private final FLPlayer player;
    private static final List<Togglable> TOGGLABLES = Arrays.asList(
            new FlagTogglable(9, Items.name(Material.SULPHUR, "ignore all"), 1));
    private static final ItemStack ENABLED_ITEM = Items.name(new ItemStack(Material.INK_SACK, 1, (short) 10), "enabled");
    private static final ItemStack DISABLED_ITEM = Items.name(new ItemStack(Material.INK_SACK, 1, (short) 8), "disabled");

    public SettingsMenu(FLPlayer player, FPlayerMenu parent) {
        inv = Bukkit.createInventory(this, 27, "Настройки");
        this.parent = parent;
        this.player = player;
        updateTogglables();
    }

    public void updateTogglables() {
        for (Togglable togglable : TOGGLABLES) {
            inv.setItem(togglable.slot, togglable.is);
            inv.setItem(togglable.slot + 9, togglable.isEnabled(player) ? ENABLED_ITEM : DISABLED_ITEM);
        }
    }

    @Override
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
        EntityPlayer entityPlayer = MinecraftServer.getServer().getPlayerList().getPlayer(player.getName());
        Togglable togglable = null;

        for (Togglable t : TOGGLABLES) {
            if (t.slot == slot || t.slot == slot - 9) {
                togglable = t;
                break;
            }
        }

        if (togglable != null) {
            for (int i = 0; i < 150L; i++) {
                if (i == 150L) {
                    entityPlayer.getCooldownTracker().a(Item.getById(ENABLED_ITEM.getTypeId()), 150);
                    togglable.toggle(this.player);
                    updateTogglables();
                }
            }
        }
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    static abstract class Togglable {
        int slot;
        ItemStack is;

        Togglable(int slot, ItemStack is) {
            this.slot = slot;
            this.is = is;
        }

        abstract boolean toggle(FLPlayer player);
        abstract boolean isEnabled(FLPlayer player);
    }

    static class FlagTogglable extends Togglable {
        protected int flag;

        FlagTogglable(int slot, ItemStack is, int flag) {
            super(slot, is);
            this.flag = flag;
        }

        @Override
        boolean toggle(FLPlayer player) {
            return player.settings.get(flag);
        }

        @Override
        boolean isEnabled(FLPlayer player) {
            boolean f = !player.settings.get(flag);
            player.settings.set(flag, f);
            return f;
        }
    }
}
