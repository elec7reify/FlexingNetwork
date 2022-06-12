package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SeenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0) {
            Utilities.msg(sender, "&cИспользование: /seen <игрок>");
            return true;
        }

        Utilities.msg(sender, "Игрок " + args[0] + " " + (FlexingNetwork.isPlayerOnline(args[0]) ? "&aонлайн" : "&cне в сети"));

        return true;
    }
}
