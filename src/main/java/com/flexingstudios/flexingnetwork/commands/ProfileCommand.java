package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.impl.player.profileMenu.ProfileMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ((Player) sender).openInventory(new ProfileMenu(FlexingNetwork.INSTANCE.getPlayer(sender.getName())).getInventory());
        return true;
    }
}