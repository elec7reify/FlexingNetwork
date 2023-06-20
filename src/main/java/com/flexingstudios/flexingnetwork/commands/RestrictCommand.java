package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.common.F;
import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestrictCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!FlexingNetwork.INSTANCE.hasRank(commandSender, Rank.ADMIN, true))
            return true;

        Player sender = (Player) commandSender;

        if (command.getName().equals("restrict")) {
            if (args.length == 0) {
                Utils.msg(sender, "Usage: restrict <username> [time]");
            } else {
                long time;

                if (args.length > 1) {
                    try {
                        time = F.toMilliSec(args[1]);
                    } catch (NumberFormatException e) {
                        time = 0;
                    }

                    Player banned = Bukkit.getPlayerExact(args[0]);
                    FlexPlayer flPlayer = FlexPlayer.get(sender);

                    // Immunity to restrict another admins
                    if (flPlayer.has(Rank.ADMIN)) {
                        FlexingNetwork.INSTANCE.restrict(args[0], time, sender.getName(), false);
                    } else if (flPlayer.has(Rank.SADMIN)) {
                        if (!FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN)) {
                            FlexingNetwork.INSTANCE.restrict(args[0], time, sender.getName(), false);
                        }
                    } else if (flPlayer.has(Rank.VADMIN)) {
                        if (!FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.SADMIN)) {
                            FlexingNetwork.INSTANCE.restrict(args[0], time, sender.getName(), false);
                        }
                    } else if (flPlayer.has(Rank.TEAM)) {
                        if (!FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.ADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.SADMIN) && !FlexingNetwork.INSTANCE.getPlayer(args[0]).has(Rank.VADMIN)) {
                            FlexingNetwork.INSTANCE.restrict(args[0], time, sender.getName(), false);
                        }
                    }

//                    if (banned != null) {
////                        for (Player players : Bukkit.getOnlinePlayers())
////                            Utilities.msg(players, Language.getMsg(players, Messages.KICKED_BY_ADMIN)
////                                    .replace("{kicked}", sender.getName())
////                                    .replace("{targetName}", banned.getName())
////                                    .replace("{reason}", reason));
//                    } else {
////                        for (Player players : Bukkit.getOnlinePlayers())
////                            Utilities.msg(players, Language.getMsg(players, Messages.KICKED_BY_ADMIN)
////                                    .replace("{kicked}", sender.getName())
////                                    .replace("{targetName}", args[0])
////                                    .replace("{reason}", reason));
//                    }
                }

                return true;
            }
        }

//        if (command.getName().equals("unban")) {
//            if (args.length == 0) {
//                Utilities.msg(sender, "&cИспользование: /unban <username>");
//            } else {
//                FlexingNetwork.unban(args[0], sender.getName(), false);
//            }
//        }

        return true;
    }
}
