package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.F;
import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!FlexingNetwork.hasPermission(commandSender, Permission.BAN, true))
            return true;

        Player sender = (Player) commandSender;

        if (FlexingNetwork.getPlayer(sender).getRestrict()) {
            Utilities.msg(sender, "&cНа вас наложено ограничение на выдачу наказаний");
            return true;
        }

        if (command.getName().equals("ban")) {
            if (args.length == 0) {
                Utilities.msg(sender, Language.getMsg(sender, Messages.COMMAND_BAN_USAGE).replace("{command}", "/" + command.getName()));
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
                    FLPlayer flPlayer = FLPlayer.get(sender);

                    // Immunity to ban
                    if (flPlayer.has(Rank.ADMIN)) {
                        FlexingNetwork.ban(args[0], time, reason, sender.getName(), false);
                    } else if (flPlayer.has(Rank.SADMIN)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN)) {
                            FlexingNetwork.ban(args[0], time, reason, sender.getName(), false);
                        }
                    } else if (flPlayer.has(Rank.VADMIN)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN)) {
                            FlexingNetwork.ban(args[0], time, reason, sender.getName(), false);
                        }
                    } else if (flPlayer.has(Rank.TEAM)) {
                        if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN)) {
                            FlexingNetwork.ban(args[0], time, reason, sender.getName(), false);
                        }
                    } else if (flPlayer.has(Rank.GOD) && !FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.TEAM)) {
                        FlexingNetwork.ban(args[0], time, reason, sender.getName(), false);
                    }

                    if (banned != null) {
                        for (Player players : Bukkit.getOnlinePlayers())
                            Utilities.msg(players, Language.getMsg(players, Messages.KICKED_BY_ADMIN)
                                    .replace("{kicked}", sender.getName())
                                    .replace("{targetName}", banned.getName())
                                    .replace("{reason}", reason));
                    } else {
                        for (Player players : Bukkit.getOnlinePlayers())
                            Utilities.msg(players, Language.getMsg(players, Messages.KICKED_BY_ADMIN)
                                    .replace("{kicked}", sender.getName())
                                    .replace("{targetName}", args[0])
                                    .replace("{reason}", reason));
                    }
                }

                return true;
            }
        }

        if (command.getName().equals("unban")) {
            if (args.length == 0) {
                Utilities.msg(sender, "&cИспользование: /unban <username>");
            } else {
                FlexingNetwork.unban(args[0], sender.getName(), false);
            }
        }

        return true;
    }
}
