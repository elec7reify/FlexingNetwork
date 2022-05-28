package com.flexingstudios.FlexingNetwork.friends.commands;

import com.flexingstudios.FlexingNetwork.friends.utils.Colour;
import com.flexingstudios.FlexingNetwork.friends.utils.FriendsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class FriendAdd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Player only command");
            return false;
        }
        Player senderPlayer = (Player) sender;

        if (args.length < 1) {
            sendUsage(senderPlayer);
        }

            if (args.length > 2) {
                senderPlayer.sendMessage("usage: f add");
            } if (command.getName().equalsIgnoreCase("add")) {
                 Player targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer.hasPlayedBefore()) {
                    try {
                        if (FriendsManager.incomingRequests(targetPlayer.getUniqueId().toString()).size() > 20) {
                            sender.sendMessage(Colour.prefix(ChatColor.RED) + Colour.translate("&cThat player already has the maximum number of incoming friend requests (20). Please try again later or ask them to either accept or deny some requests."));
                            return true;
                        } else if (FriendsManager.getPlayerFriends(targetPlayer.getUniqueId().toString()).size() > 20) {
                            sender.sendMessage(Colour.prefix(ChatColor.RED) + Colour.translate("&cThat player already has the maximum number of friends (20). Please try again later or ask them to remove some friends."));
                            return true;
                        } else if (FriendsManager.outgoingRequests(senderPlayer.getUniqueId().toString()).size() > 20) {
                            sender.sendMessage(Colour.prefix(ChatColor.RED) + Colour.translate("&cYou already have the maximum number of friend requests (20). Please either accept or deny some requests."));
                            return true;
                        } else if (FriendsManager.getPlayerFriends(senderPlayer.getUniqueId().toString()).size() > 20) {
                            sender.sendMessage(Colour.prefix(ChatColor.RED) + Colour.translate("&cYou already have the maximum number of friends (20). Please remove some friends."));
                            return true;
                        } else {
                            FriendsManager.addFriendRequest(senderPlayer.getUniqueId().toString(), targetPlayer.getUniqueId().toString());
                            sender.sendMessage(Colour.prefix(ChatColor.DARK_GREEN) + "§aИгроку " + ChatColor.WHITE + targetPlayer.getName() + " §aотправлена заявка в друзья.");
                            return true;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage(Colour.prefix(ChatColor.RED) + "According to the world files that player has not played before. If they are online please ask them to relog.");
                    return true;
                }
        }
            return true;
    }

    private void sendUsage(Player player) {
        player.sendMessage("");
        player.sendMessage("§2/friends add §8<ник> §7- Отправить запрос в друзья игроку");
        player.sendMessage("");
    }
}