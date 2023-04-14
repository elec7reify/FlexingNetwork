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

import java.util.concurrent.TimeUnit;

public class BanCommand implements CommandExecutor {
    public static long maxBanTime;

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
                Utilities.msg(sender, Messages.COMMAND_BAN_USAGE.replace("{command}", "/" + command.getName()));
            } else {
                long time;
                String reason = "";

                if (args.length > 1) {
                    try {
                        time = F.toMilliSec(args[1]);
                        if (FlexingNetwork.hasRank(sender, Rank.TEAM, false)) {
                            for (int i = 2; i < args.length; i++)
                                reason = reason + args[i] + " ";
                        } else {
                            if (maxBanTime <= time) {
                                for (int i = 2; i < args.length; i++)
                                    reason = reason + args[i] + " ";
                            } else {
                                Utilities.msg(sender, "&cВремя бана превышает максимальное допустимое время для Вашего ранга!");
                                return false;
                            }
                        }
                    } catch (NumberFormatException e) {
                        if (!FlexingNetwork.hasRank(sender, Rank.TEAM, false)) {
                            Utilities.msg(sender, "&cВремя бана указано некорректно!");
                            return false;
                        }
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
                        if (time == 0) {
                            Utilities.bcast("&3{admin} &fзабанил игрока &3{player} &6навсегда &fпо причине: &6{reason}"
                                    .replace("{admin}", sender.getName())
                                    .replace("{player}", banned.getName())
                                    .replace("{reason}", reason));
                        } else {
                            Utilities.bcast("&3{admin} &fзабанил игрока &3{player} &3на {time} &fпо причине: &6{reason}"
                                    .replace("{admin}", sender.getName())
                                    .replace("{player}", banned.getName())
                                    .replace("{time}", F.formatSecondsShort((int) TimeUnit.MILLISECONDS.toSeconds(time)))
                                    .replace("{reason}", reason));
                        }
                    } else {
                        if (time == 0) {
                            Utilities.bcast("&3{admin} &fзабанил игрока &3{player} &6навсегда &fпо причине: &6{reason}"
                                    .replace("{admin}", sender.getName())
                                    .replace("{player}", args[0])
                                    .replace("{reason}", reason));
                        } else {
                            Utilities.bcast("&3{admin} &fзабанил игрока &3{player} &3на {time} &fпо причине: &6{reason}"
                                    .replace("{admin}", sender.getName())
                                    .replace("{player}", args[0])
                                    .replace("{time}", F.formatSecondsShort((int) TimeUnit.MILLISECONDS.toSeconds(time)))
                                    .replace("{reason}", reason));
                        }
                    }
                }

                return true;
            }
        }

        if (command.getName().equals("unban")) {
            if (args.length == 0) {
                Utilities.msg(sender, "&cИспользование: /unban <игрок>");
            } else {
                FlexingNetwork.unban(args[0], sender.getName(), false);
            }
        }

        return false;
    }
}
