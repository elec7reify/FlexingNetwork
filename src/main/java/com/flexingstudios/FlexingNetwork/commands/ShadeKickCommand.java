package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.BungeeListeners.BungeeBridge;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShadeKickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (!commandSender.hasPermission("flexingnetwork.shade.*")) {
            return true;
        }

        Player sender = (Player) commandSender;

        if (cmd.getName().equals("shadekick")) {
            if (args.length == 0) {
                Utilities.msg(commandSender, Language.getMsg(sender, Messages.COMMAND_SHADEKICK_USAGE));
            } else {
                String reason = "";
                if (args.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++)
                        sb.append(args[i]).append(' ');
                    reason = sb.substring(0, sb.length() - 1);
                } else {
                    for (Player players : Bukkit.getOnlinePlayers())
                        reason = Language.getMsg(players, Messages.REASON_NOT_SPECIFIED);
                }

                Player targetPlayer = Bukkit.getPlayerExact(args[0]);
                FLPlayer flPlayer = FLPlayer.get(sender);

                if (targetPlayer != null) {
                    for (Player players : Bukkit.getOnlinePlayers())
                        Utilities.msg(players, Language.getMsg(players, Messages.KICKED_BY_ADMIN)
                                .replace("{kicked}", sender.getName())
                                .replace("{targetName}", targetPlayer.getName())
                                .replace("{reason}", reason));

                    if (flPlayer.has(Rank.ADMIN)) {
                        FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                    } else if (flPlayer.has(Rank.SADMIN)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN)) {
                            FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                        }
                    } else if (flPlayer.has(Rank.VADMIN)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN)) {
                            FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                        }
                    } else if (flPlayer.has(Rank.TEAM)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN)) {
                            FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                        }
                    } else if (flPlayer.has(Rank.GOD) && !FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.TEAM)) {
                        FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                    }
                } else {
                    for (Player players : Bukkit.getOnlinePlayers())
                        Utilities.msg(players, Language.getMsg(players, Messages.KICKED_BY_ADMIN)
                                .replace("{kicked}", sender.getName())
                                .replace("{targetName}", targetPlayer.getName())
                                .replace("{reason}", reason));

                    if (flPlayer.has(Rank.ADMIN)) {
                        FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                    } else if (flPlayer.has(Rank.SADMIN)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN)) {
                            FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                        }
                    } else if (flPlayer.has(Rank.VADMIN)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN)) {
                            FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                        }
                    } else if (flPlayer.has(Rank.TEAM)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN)) {
                            FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                        }
                    } else if (flPlayer.has(Rank.GOD) && !FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.TEAM)) {
                        FlexingNetwork.kick(args[0], reason, sender.getName(), true);
                    }
                }
            }
        }

        return true;
    }
}

