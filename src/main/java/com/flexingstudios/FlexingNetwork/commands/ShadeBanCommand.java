package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Common.F;
import com.flexingstudios.Common.player.Permission;
import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FlexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShadeBanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!FlexingNetwork.hasPermission(commandSender, Permission.BAN, true))
            return true;

        Player sender = (Player) commandSender;

        if (command.getName().equals("shadeban")) {
            if (args.length == 0) {
                Utilities.msg(sender, Messages.COMMAND_BAN_USAGE.replace("{command}", command.getName()));
            } else {
                long time;
                String reason = "";

                if (args.length > 1) {
                    try {
                        time = F.toMilliSec(args[1]);
                        for (int i = 2; i < args.length; i++)
                            reason = reason + args[i] + " ";
                    } catch (NumberFormatException e) {
                        time = 0;
                        for (int i = 1; i < args.length; i++)
                            reason = reason + args[i] + " ";
                    }

                    if (reason.isEmpty()) {
                        reason = "Не указана";
                    } else {
                        reason = reason.substring(0, reason.length() - 1);
                    }

                    Player banned = Bukkit.getPlayerExact(args[0]);
                    FlexPlayer flPlayer = FlexPlayer.get(sender);

                    if (banned == sender) {
                        Utilities.msg(sender, Messages.BAN_ME);
                        return true;
                    }

                    // Immunity to kick
                    if (flPlayer.has(Rank.ADMIN)) {
                        FlexingNetwork.ban(args[0], time, reason, sender.getName(), true);
                    } else if (flPlayer.has(Rank.SADMIN)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN)) {
                            FlexingNetwork.ban(args[0], time, reason, sender.getName(), true);
                        }
                    } else if (flPlayer.has(Rank.VADMIN)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN)) {
                            FlexingNetwork.ban(args[0], time, reason, sender.getName(), true);
                        }
                    } else if (flPlayer.has(Rank.TEAM)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN)) {
                            FlexingNetwork.ban(args[0], time, reason, sender.getName(), true);
                        }
                    } else if (flPlayer.has(Rank.GOD) && !FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.TEAM)) {
                        FlexingNetwork.ban(args[0], time, reason, sender.getName(), true);
                    }

                    if (banned != null) {
                        for (Player players : Bukkit.getOnlinePlayers())
                            Utilities.msg(players, Messages.BANNED_BY_ADMIN
                                    .replace("{admin}", "&cТеневой админ")
                                    .replace("{player}", banned.getName())
                                    .replace("{reason}", reason));
                    } else {
                        for (Player players : Bukkit.getOnlinePlayers())
                            Utilities.msg(players, Messages.BANNED_BY_ADMIN
                                    .replace("{admin}", "&cТеневой админ")
                                    .replace("{player}", args[0])
                                    .replace("{reason}", reason));
                    }
                }

                return true;
            }
        }

        if (command.getName().equals("shadeunban")) {
            if (args.length == 0) {
                Utilities.msg(sender, "&cИспользование: /shadeunban <username>");
            } else {
                FlexingNetwork.unban(args[0], sender.getName(), true);
            }
        }

        return true;
    }
}
