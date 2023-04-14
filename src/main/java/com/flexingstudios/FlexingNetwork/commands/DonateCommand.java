package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.impl.GroupsMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DonateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new GroupsMenu(FlexingNetwork.getPlayer((Player) sender)).show((((Player) sender)));
        return true;
    }
}
