package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.F;
import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.FlexingNetwork.BungeeListeners.BungeeBridge;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import net.minecraft.server.v1_12_R1.PacketPlayInWindowClick;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!FlexingNetwork.hasPermission(commandSender, Permission.BAN, true))
            return true;

        Player sender = (Player) commandSender;

        if (command.getName().equals("ban")) {
            if (args.length == 0) {
                Utilities.msg(sender, "Usage");
            } else {
                int time;
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

                    if (time < 0)
                        time = 0;

                    if (reason.isEmpty()) {
                        reason = "Не указана";
                    } else {
                        reason = reason.substring(0, reason.length() - 1);
                    }

                    Player banned = Bukkit.getPlayerExact(args[0]);

                    BungeeBridge.kickPlayer(banned.getName(), T.BanMessage(banned)
                            .replace("{username}", args[0])
                            .replace("{admin}", sender.getName())
                            .replace("{time}", F.formatSecondsShort((int) TimeUnit.MILLISECONDS.toSeconds(time)))
                            .replace("{reason}", reason));

                    FlexingNetwork.mysql().query("INSERT INTO bans (username, banto, reason, banfrom, status, admin) VALUES('" +
                            StringEscapeUtils.escapeSql(args[0]) + "', " + (time == 0 ? 0 :
                            (System.currentTimeMillis() + (time))) + ", '" +
                            StringEscapeUtils.escapeSql(reason) + "', " +
                            System.currentTimeMillis() + ", 1, '" +
                            StringEscapeUtils.escapeSql(sender.getName()) + "')");
                }

                return true;
            }
        }

        if (command.getName().equals("unban")) {
            if (args.length == 0) {
                Utilities.msg(sender, "");
            } else {

            }
        }

        return false;
    }
}
