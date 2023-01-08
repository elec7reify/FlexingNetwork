package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.google.common.collect.Sets;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class StpCommand implements CommandExecutor {
    static final Set<String> ALLOWED_SERVER_TYPES = Sets.newHashSet("LOBBY", "BW", "SURVIVAL", "ANARCHY", "BEDWARS");

    static final Set<String> ALLOWED_STP = Sets.union(ALLOWED_SERVER_TYPES, Sets.newHashSet("BW"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

                Player player = (Player) sender;

                if (!FlexingNetwork.hasRank(sender, Rank.VADMIN, true)) return true;

                if (args.length == 0) {
                    player.sendMessage("§cИспользование /stp @<сервер>");
                    return true;
                }

                String target = args[0];

                if(target.charAt(0) == '@') {
                    tpToServer(sender, target.substring(1).toUpperCase());
                    return true;
                }
                Utilities.msg(sender, "&aТелепортация на сервер " + target);

        return true;
    }

    private static void tpToServer(CommandSender sender, String server) {
        if (server.equals(FlexingNetwork.lobby().getServerId())) {
            Utilities.msg(sender, "&6Вы уже на нужном сервере");
            return;
        }
        Set<String> allowed = FlexingNetwork.getPlayer(sender.getName()).has(Rank.VADMIN) ? ALLOWED_STP : ALLOWED_SERVER_TYPES;

        if (!allowed.contains(server.split("_")[0])) {
            Utilities.msg(sender, "&cВы не можете телепортироваться на сервер " + server + ". В доступе отказано");
            return;
        }
        FlexingNetwork.toServer(server, (Player) sender);
    }
}
