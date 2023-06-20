package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.common.player.Permission;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.command.UpCommand;
import com.flexingstudios.flexingnetwork.api.util.Invs;
import com.flexingstudios.flexingnetwork.api.util.Spectators;
import com.flexingstudios.flexingnetwork.api.util.Notifications;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class VanishCommand extends UpCommand {
    public Map<String, VanishData> data = new HashMap<>();

    @Override
    protected boolean main(CommandSender sender, Command command, String label, String[] args) {
        if (!FlexingNetwork.INSTANCE.hasPermission(sender, Permission.VANISH, true))
            return true;

        Player player = (Player) sender;
        if (Spectators.instance().contains(player)) {
            disableVanish(player);
        } else {
            enableVanish(player);
        }

        return false;
    }

    public void purge(Player player) {
        data.remove(player.getName());
    }

    public void disableVanish(Player player) {
        if (Spectators.instance().contains(player)) {
            VanishData vanishData = data.remove(player.getName());

            Spectators.instance().remove(player);
            player.getInventory().setContents(vanishData.inventory);
            player.getInventory().setArmorContents(vanishData.armor);
            player.teleport(vanishData.lastLoc);
            player.setAllowFlight(vanishData.allowFlight);
            player.setFlying(vanishData.flying);
            player.setWalkSpeed(vanishData.walkspeed);
            player.setFlySpeed(vanishData.flyspeed);
            player.setHealth(vanishData.health);
            player.setFallDistance(0.0F);
            Notifications.success(player, "GOT IT!", "&fРежим наблюдателя деактивирован");
        }
    }

    public void enableVanish(Player player) {
        if (!Spectators.instance().contains(player)) {
            data.put(player.getName(), new VanishData(player));
            Spectators.instance().add(player);
            Invs.clear(player);
            player.setAllowFlight(true);
            player.setFlying(true);
            Notifications.success(player, "GOT IT!", "&fРежим наблюдателя активирован");
        }
    }

    public static class VanishData {
        public ItemStack[] inventory;
        public ItemStack[] armor;
        public Location lastLoc;
        public boolean allowFlight;
        public boolean flying;
        public float flyspeed;
        public float walkspeed;
        public double health;

        public VanishData(Player player) {
            inventory = player.getInventory().getContents();
            armor = player.getInventory().getArmorContents();
            lastLoc = player.getLocation();
            allowFlight = player.getAllowFlight();
            flying = player.isFlying();
            flyspeed = player.getFlySpeed();
            walkspeed = player.getWalkSpeed();
            health = player.getHealth();
        }
    }
}