package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.Language.Messages;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShadeKickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("flexingnetwork.shade.*")) {
            return true;
        }

        Player sender = (Player) commandSender;

        if (command.getName().equals("shadekick")) {
            if (args.length == 0) {
                Utils.msg(commandSender, Messages.COMMAND_KICK_USAGE.replace("{command}", command.getName()));
            } else {
                String reason = "";
                if (args.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++)
                        sb.append(args[i]).append(' ');
                    reason = sb.substring(0, sb.length() - 1);
                } else {
                    for (Player players : Bukkit.getOnlinePlayers())
                        reason = Messages.REASON_NOT_SPECIFIED;
                }

                Player targetPlayer = Bukkit.getPlayerExact(args[0]);
                FlexPlayer flPlayer = FlexPlayer.get(sender);

                if (targetPlayer == sender) {
                    Utils.msg(sender, Messages.KICK_ME);
                    return true;
                }

                // Immunity to kick
                if (flPlayer.has(Rank.ADMIN)) {
                    FlexingNetwork.INSTANCE.kick(args[0], reason, sender.getName(), true);
                } else if (flPlayer.has(Rank.SADMIN)) {
                    if (!FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN)) {
                        FlexingNetwork.INSTANCE.kick(args[0], reason, sender.getName(), true);
                    }
                } else if (flPlayer.has(Rank.VADMIN)) {
                    if (!FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.SADMIN)) {
                        FlexingNetwork.INSTANCE.kick(args[0], reason, sender.getName(), true);
                    }
                } else if (flPlayer.has(Rank.TEAM)) {
                    if (!FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.VADMIN)) {
                        FlexingNetwork.INSTANCE.kick(args[0], reason, sender.getName(), true);
                    }
                } else if (flPlayer.has(Rank.GOD) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.VADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.TEAM)) {
                    FlexingNetwork.INSTANCE.kick(args[0], reason, sender.getName(), true);
                }

                if (targetPlayer != null) {
                    for (Player players : Bukkit.getOnlinePlayers())
                        Utils.msg(players, Messages.KICKED_BY_ADMIN
                                .replace("{kicked}", "&cТеневой админ")
                                .replace("{targetName}", targetPlayer.getName())
                                .replace("{reason}", reason));
                } else {
                    for (Player players : Bukkit.getOnlinePlayers())
                        Utils.msg(players, Messages.KICKED_BY_ADMIN
                                .replace("{kicked}", "&cТеневой админ")
                                .replace("{targetName}", args[0])
                                .replace("{reason}", reason));
                }
            }
        }

        return false;
    }
}

