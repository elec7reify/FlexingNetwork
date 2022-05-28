package com.flexingstudios.FlexingNetwork.friends.commands;

import com.flexingstudios.FlexingNetwork.friends.utils.GUI;
import com.flexingstudios.FlexingNetwork.friends.utils.enums.Error;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendGUI implements CommandExecutor {

    private boolean hasperms = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            hasperms = true;
        } else {
            if(sender.hasPermission("friends.friendgui")) {
                hasperms = true;
            } else {
                hasperms = false;
            }
        }
        if(hasperms) {
            Player player = (Player) sender;
            player.openInventory(GUI.firstGUIInventory(player));
            return true;
        } else {
            sender.sendMessage(Error.NO_PERMISSION.getError());
            return true;
        }
    }
}
