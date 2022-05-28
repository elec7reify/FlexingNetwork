package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.impl.GroupsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DonateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ((Player) sender).openInventory(new GroupsMenu((Player) sender).getInventory());
        return true;
    }
}
