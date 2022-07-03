package com.flexingstudios.FlexingNetwork.friends.commands;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.command.CmdSub;
import com.flexingstudios.FlexingNetwork.api.command.UpCommand;
import com.flexingstudios.FlexingNetwork.api.command.dataCommand;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.friends.utils.Colour;
import com.flexingstudios.FlexingNetwork.friends.utils.FriendsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class FriendCommand extends UpCommand {

    @Override
    protected void runCommand(Runnable action, CommandSender sender, Command cmd, String label, String[] args) {
        if (FlexingNetwork.getPlayer(sender.getName()).getId() == -1) {
            Utilities.msg(sender, "&cподождите немного...");
            return;
        }

        super.runCommand(action, sender, cmd, label, args);
    }

    @CmdSub(value = {"add"}, aliases = "a")
    protected boolean add(dataCommand data) {
        if ((data.getArgs()).length != 1) {
            Utilities.msg(data.getSender(), "&cИспользование: /" + data.getLabel() + " add <ник игрока>");
            return false;
        }

        if (data.getArgs()[0].equalsIgnoreCase(data.getSender().getName())) {
            Utilities.msg(data.getSender(), "&cВы не можете добавить себя в друзья");
            return false;
        }
        Player senderPlayer = (Player) data.getSender();
        Player targetPlayer = Bukkit.getPlayer(data.getArgs()[0]);
        if (targetPlayer.hasPlayedBefore()) {
            try {
                if (FriendsManager.incomingRequests(targetPlayer.getUniqueId().toString()).size() > 20) {
                    data.getSender().sendMessage(Colour.prefix(ChatColor.RED) + Colour.translate("&cThat player already has the maximum number of incoming friend requests (20). Please try again later or ask them to either accept or deny some requests."));
                    return true;
                } else if (FriendsManager.getPlayerFriends(targetPlayer.getUniqueId().toString()).size() > 20) {
                    data.getSender().sendMessage(Colour.prefix(ChatColor.RED) + Colour.translate("&cThat player already has the maximum number of friends (20). Please try again later or ask them to remove some friends."));
                    return true;
                } else if (FriendsManager.outgoingRequests(senderPlayer.getUniqueId().toString()).size() > 20) {
                    data.getSender().sendMessage(Colour.prefix(ChatColor.RED) + Colour.translate("&cYou already have the maximum number of friend requests (20). Please either accept or deny some requests."));
                    return true;
                } else if (FriendsManager.getPlayerFriends(senderPlayer.getUniqueId().toString()).size() > 20) {
                    data.getSender().sendMessage(Colour.prefix(ChatColor.RED) + Colour.translate("&cYou already have the maximum number of friends (20). Please remove some friends."));
                    return true;
                } else {
                    FriendsManager.addFriendRequest(senderPlayer.getUniqueId().toString(), targetPlayer.getUniqueId().toString());
                    data.getSender().sendMessage(Colour.prefix(ChatColor.DARK_GREEN) + "§aИгроку " + ChatColor.WHITE + targetPlayer.getName() + " §aотправлена заявка в друзья.");
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            data.getSender().sendMessage(Colour.prefix(ChatColor.RED) + "According to the world files that player has not played before. If they are online please ask them to relog.");
            return true;
        }

        return true;
    }

    @Override
    protected boolean main(CommandSender sender, Command command, String label, String[] args) {
        help(new dataCommand(sender, label, "help", new String[0]));
        return false;
    }

    @CmdSub(value = {"help"}, hidden = true)
    protected void help(dataCommand data) {
        Utilities.msg(data.getSender(), "Друзья",
                "/" + data.getLabel() + " add - Добавить в друзья");
    }
}