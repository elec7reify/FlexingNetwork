package com.flexingstudios.FlexingNetwork.commands;
;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.impl.player.profileMenu.ProfileMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProfileCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ((Player) sender).openInventory(new ProfileMenu(FlexingNetwork.getPlayer(sender.getName())).getInventory());
        return true;
    }
}