package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.BungeeListeners.BungeeBridge;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KickCommand implements CommandExecutor {
    private SimpleDateFormat dateFormat;

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (!FlexingNetwork.hasRank(commandSender, Rank.CHIKIBAMBONYLA, true)) {
            return true;
        }

        Player sender = (Player) commandSender;

        if (cmd.getName().equals("kick")) {
            if (args.length == 0) {
                Utilities.msg(commandSender, Language.getMsg(sender, Messages.COMMAND_KICK_USAGE));
            } else {
                String kickmessage = null;

                if (args.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++)
                        sb.append(args[i]).append(' ');
                    kickmessage = sb.substring(0, sb.length() - 1);
                } else {
                    for (Player players : Bukkit.getOnlinePlayers())
                        kickmessage = Language.getMsg(players, Messages.REASON_NOT_SPECIFIED);
                }

                Player targetPlayer = Bukkit.getPlayerExact(args[0]);
                dateFormat = new SimpleDateFormat(Language.getMsg(targetPlayer, Messages.OFFICIAL_DATE_FORMAT));

                if (targetPlayer != null) {
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            Utilities.msg(players, Language.getMsg(players, Messages.KICKED_BY_ADMIN)
                                    .replace("{kicker}", sender.getName())
                                    .replace("{target}", targetPlayer.getName())
                                    .replace("{reason}", kickmessage));
                        }
                        targetPlayer.kickPlayer(Utilities.colored(T.formattedKickMessage(targetPlayer)
                                .replace("{username}", targetPlayer.getName())
                                .replace("{kicker}", "&3" + sender.getName())
                                .replace("{reason}", kickmessage)
                                .replace("{date}", dateFormat.format(new Date(System.currentTimeMillis())))));
                    }
                }
        }

        return true;
    }
}
