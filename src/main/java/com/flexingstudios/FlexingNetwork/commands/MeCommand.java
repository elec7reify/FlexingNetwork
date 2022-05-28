package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.impl.player.FPlayerMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ((Player) sender).openInventory(new FPlayerMenu((Player) sender).getInventory());
        return true;
    }
}