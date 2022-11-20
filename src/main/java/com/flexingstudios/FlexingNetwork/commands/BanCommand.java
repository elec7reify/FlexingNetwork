package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.F;
import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.BungeeListeners.BungeeBridge;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.ParsedTime;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!FlexingNetwork.hasPermission(commandSender, Permission.BAN, true))
            return true;

        Player sender = (Player) commandSender;

        if (command.getName().equals("ban")) {
            if (args.length == 0) {
                Utilities.msg(sender, "Использование: /ban <username> [time] [reason]");
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

                    if (banned != null) {
                        banned.kickPlayer(T.BanMessage(banned)
                                .replace("{username}", args[0])
                                .replace("{admin}", sender.getName())
                                .replace("{time}", new ParsedTime((int) TimeUnit.MILLISECONDS.toSeconds(time)).format())
                                .replace("{reason}", reason));

//                        FlexingNetwork.mysql().query("INSERT INTO bans (username, banto, reason, banfrom, status, admin) VALUES('" +
//                                StringEscapeUtils.escapeSql(args[0]) + "', " +
//                                (time == 0 ? 0 : System.currentTimeMillis() + time) + ", '" +
//                                StringEscapeUtils.escapeSql(reason) + "', " +
//                                System.currentTimeMillis() + ", 1, '" +
//                                StringEscapeUtils.escapeSql(sender.getName()) + "')");

                        // Immunity to kick
                        if (flPlayer.has(Rank.ADMIN)) {
                            FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), false);
                        } else if (flPlayer.has(Rank.SADMIN)) {
                            if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN)) {
                                FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), false);
                            }
                        } else if (flPlayer.has(Rank.VADMIN)) {
                            if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN)) {
                                FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), false);
                            }
                        } else if (flPlayer.has(Rank.TEAM)) {
                            if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN)) {
                                FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), false);
                            }
                        } else if (flPlayer.has(Rank.GOD) && !FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.TEAM)) {
                            FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), false);
                        }
                    } else {
                        BungeeBridge.kickPlayer(args[0], T.BanMessage(banned)
                                .replace("{username}", args[0])
                                .replace("{admin}", sender.getName())
                                .replace("{time}", new ParsedTime((int) TimeUnit.MILLISECONDS.toSeconds(time)).format())
                                .replace("{reason}", reason));

//                        FlexingNetwork.mysql().query("INSERT INTO bans (username, banto, reason, banfrom, status, admin) VALUES('" +
//                                StringEscapeUtils.escapeSql(args[0]) + "', " +
//                                (time == 0 ? 0 : System.currentTimeMillis() + time) + ", '" +
//                                StringEscapeUtils.escapeSql(reason) + "', " +
//                                System.currentTimeMillis() + ", 1, '" +
//                                StringEscapeUtils.escapeSql(sender.getName()) + "')");

                        // Immunity to kick
                        if (flPlayer.has(Rank.ADMIN)) {
                            FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), true);
                        } else if (flPlayer.has(Rank.SADMIN)) {
                            if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN)) {
                                FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), true);
                            }
                        } else if (flPlayer.has(Rank.VADMIN)) {
                            if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN)) {
                                FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), true);
                            }
                        } else if (flPlayer.has(Rank.TEAM)) {
                            if (!FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN)) {
                                FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), true);
                            }
                        } else if (flPlayer.has(Rank.GOD) && !FlexingNetwork.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.VADMIN) && !FlexingNetwork.getPlayer(args[0]).has(Rank.TEAM)) {
                            FlexingNetwork.ban(banned.getName(), System.currentTimeMillis() + time, reason, sender.getName(), true);
                        }
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
