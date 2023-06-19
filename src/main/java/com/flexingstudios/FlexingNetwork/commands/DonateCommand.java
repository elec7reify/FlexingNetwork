package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.flexingnetwork.impl.GroupsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DonateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new GroupsMenu((Player) sender).show((((Player) sender)));
        return true;
    }
}
