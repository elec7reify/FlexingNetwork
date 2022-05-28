package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FlyCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("fly")) {

            if (!FlexingNetwork.hasRank(sender, Rank.TEAM, true))
                return true;

            if (!player.getAllowFlight()) {
              player.setAllowFlight(true);
              player.setFlying(true);
              player.sendMessage("§a§lGot it! §e> §fРежим полёта включен");
        } else {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.sendMessage("§a§lGot it! §e> §fРежим полёта выключен");
            }
        }

        return true;
    }

    @EventHandler
    public void onjoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(player.hasPermission("fly")) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

    }

}
