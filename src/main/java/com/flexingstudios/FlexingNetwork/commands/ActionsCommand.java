package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.impl.player.actionsMenu.ActionsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;

//        if (player.getName().equalsIgnoreCase(args[0])) {
//            Utilities.msg(commandSender, "вы лох");
//            return true;
//        }

        if (args.length == 1) {
            player.openInventory(new ActionsMenu(args[0]).getInventory());
        }

        return true;
    }
}
