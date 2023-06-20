package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.flexingnetwork.api.Language.Messages;
import com.flexingstudios.common.F;
import com.flexingstudios.common.player.Permission;
import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
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
        if (!FlexingNetwork.INSTANCE.hasPermission(commandSender, Permission.BAN, true))
            return true;

        Player sender = (Player) commandSender;

        if (FlexingNetwork.INSTANCE.getPlayer(sender).getRestrict()) {
            Utils.msg(sender, "&cНа вас наложено ограничение на выдачу наказаний");
            return true;
        }

        if (command.getName().equals("ban")) {
            if (args.length == 0) {
                Utils.msg(sender, Messages.COMMAND_BAN_USAGE.replace("{command}", command.getName()));
            } else {
                long time;
                String reason = "";
                if (args.length > 1) {
                    try {
                        time = F.toMilliSec(args[1]);
                        if (FlexingNetwork.INSTANCE.hasRank(sender, Rank.TEAM, false)) {

                            for (int i = 2; i < args.length; i++)
                                reason = reason + args[i] + " ";
                        } else {
                            if (maxBanTime <= time) {
                                for (int i = 2; i < args.length; i++)
                                    reason = reason + args[i] + " ";
                            } else {
                                Utils.msg(sender, "&cВремя бана превышает максимальное допустимое время для Вашего ранга!");
                                return false;
                            }
                        }
                    } catch (NumberFormatException e) {
                        if (!FlexingNetwork.INSTANCE.hasRank(sender, Rank.TEAM, false)) {
                            Utils.msg(sender, "&cВремя бана указано некорректно!");
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
                        Utils.msg(sender, Messages.BAN_ME);
                        return true;
                    }

                    // Immunity to ban
                    if (flPlayer.has(Rank.ADMIN)) {
                        FlexingNetwork.INSTANCE.ban(args[0], time, reason, sender.getName(), false);
                    } else if (flPlayer.has(Rank.SADMIN)) {
                        if (!FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN)) {
                            FlexingNetwork.INSTANCE.ban(args[0], time, reason, sender.getName(), false);
                        }
                    } else if (flPlayer.has(Rank.VADMIN)) {
                        if (!FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.SADMIN)) {
                            FlexingNetwork.INSTANCE.ban(args[0], time, reason, sender.getName(), false);
                        }
                    } else if (flPlayer.has(Rank.TEAM)) {
                        if (!FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.VADMIN)) {
                            FlexingNetwork.INSTANCE.ban(args[0], time, reason, sender.getName(), false);
                        }
                    } else if (flPlayer.has(Rank.GOD) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.VADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.TEAM)) {
                        FlexingNetwork.INSTANCE.ban(args[0], time, reason, sender.getName(), false);
                    }

                    if (banned != null) {
                        if (time == 0) {
                            Utils.bcast("&3{admin} &fзабанил игрока &3{player} &6навсегда &fпо причине: &6{reason}"
                                    .replace("{admin}", sender.getName())
                                    .replace("{player}", banned.getName())
                                    .replace("{reason}", reason));
                        } else {
                            Utils.bcast("&3{admin} &fзабанил игрока &3{player} &3на {time} &fпо причине: &6{reason}"
                                    .replace("{admin}", sender.getName())
                                    .replace("{player}", banned.getName())
                                    .replace("{time}", F.formatSecondsShort((int) TimeUnit.MILLISECONDS.toSeconds(time)))
                                    .replace("{reason}", reason));
                        }
                    } else {
                        if (time == 0) {
                            Utils.bcast("&3{admin} &fзабанил игрока &3{player} &6навсегда &fпо причине: &6{reason}"
                                    .replace("{admin}", sender.getName())
                                    .replace("{player}", args[0])
                                    .replace("{reason}", reason));
                        } else {
                            Utils.bcast("&3{admin} &fзабанил игрока &3{player} &3на {time} &fпо причине: &6{reason}"
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
                Utils.msg(sender, "&cИспользование: /unban <игрок>");
            } else {
                FlexingNetwork.INSTANCE.unban(args[0], sender.getName(), false);
            }
        }

        return false;
    }
}
