package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.Invs;
import com.flexingstudios.FlexingNetwork.api.util.Spectators;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class VanishCommand implements CommandExecutor {
    public Map<String, VanishData> data = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!FlexingNetwork.hasPermission(sender, Permission.VANISH, true))
            return true;

        Player player = (Player) sender;
        if (Spectators.instance().contains(player)) {
            disableVanish(player);
        } else {
            enableVanish(player);
        }

        return true;
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
            Utilities.msg(player, T.success("GOT IT!", "&fРежим наблюдателя деактивирован"));
        }
    }

    public void enableVanish(Player player) {
        if (!Spectators.instance().contains(player)) {
            data.put(player.getName(), new VanishData(player));
            Spectators.instance().add(player);
            Invs.clear(player);
            player.setAllowFlight(true);
            player.setFlying(true);
            Utilities.msg(player, T.success("GOT IT!", "&fРежим наблюдателя активирован"));
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