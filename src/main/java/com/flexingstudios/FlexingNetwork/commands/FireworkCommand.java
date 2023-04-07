package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.api.util.Fireworks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.Buffer;

public class FireworkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;

        command.setUsage("Usage: /firework [player name]");
        if (args.length != 1) {
            player.sendMessage(command.getUsage());
            return true;
        }

        Player player1 = Bukkit.getPlayer(args[0]);
        if (player1 != null) {
            Fireworks.launchRandom(player1.getLocation());
        } else {
            player.sendMessage("nothing");
        }

        return false;
    }
}
