package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.mes;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SeenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            mes.msg(sender, "&cИспользование: /seen <игрок>");
            return true;
        }

        mes.msg(sender, "Игрок " + args[0] + " " + (FlexingNetwork.isPlayerOnline(args[0]) ? "&aонлайн" : "&cне в сети"));

        return true;
    }
}
