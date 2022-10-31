package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.google.common.base.Joiner;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Player player = (Player) commandSender;

        if (args.length == 0) {
            Utilities.msg(player, "report <username> [причина]");
            return true;
        }

        String msg;
        if (args.length > 1) {
            msg = Joiner.on(' ').join(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
        } else {
            msg = "-";
        }
        sendReport(player, args[0], msg);

        return true;
    }

    private void sendReport(CommandSender sender, String target, String message) {
        FlexingNetwork.mysql().query("INSERT INTO reports (username, target, message) VALUES('" +
                StringEscapeUtils.escapeSql(sender.getName()) + "', '" +
                StringEscapeUtils.escapeSql(target) + "', '" +
                StringEscapeUtils.escapeSql(message) + "')");
    }
}
