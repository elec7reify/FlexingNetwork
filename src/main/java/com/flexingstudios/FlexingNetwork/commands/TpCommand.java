package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.ServerType;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Spectators;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        NetworkPlayer networkPlayer = FlexingNetwork.getPlayer(sender.getName());

        if (FlexingNetwork.lobby().getServerType() == ServerType.BUILD) {
            if (!networkPlayer.hasAndNotify(Permission.BUILDSERVER))
                return true;
        } else {
            if (!networkPlayer.hasAndNotify(Permission.VANISH))
                return true;

            if (!networkPlayer.has(Rank.TEAM) && !Spectators.instance().contains((Player) sender)) {
                Utilities.msg(sender, "&cВы можете использовать телепорт только в режиме наблюдателя");
                return true;
            }
        }

        if (args.length == 1) {
            if (args[0].contains(",")) {
                teleport(sender, args[0].split(","));
            } else {
                Player target = Bukkit.getPlayerExact(args[0]);
                if (target != null) {
                    teleport(sender, target);
                } else {
                    Utilities.msg(sender, "&cИгрок &f" + args[0] + "&c не найден");
                    }
                }
                return true;
            }

            if (args.length == 2 && networkPlayer.getRank().has(Rank.ADMIN)) {
                Player player1 = Bukkit.getPlayerExact(args[0]);
                if (player1 == null) {
                    Utilities.msg(sender, "&cИгрок &f" + args[0] + "&c не найден");
                    return true;
                }
                Player player2 = Bukkit.getPlayerExact(args[1]);
                if (player2 == null) {
                    Utilities.msg(sender, "&cИгрок &f" + args[1] + "&c не найден");
                    return true;
                }
                teleport(player1, player1);
                return true;
            }
            if (args.length > 2 && !args[0].contains(",")) {
                teleport(sender, args);
                return true;
            }

            teleport(sender, Joiner.on("").join(args).split(","));
            return true;
    }

    private void help(CommandSender sender) {
        List<String> list = new ArrayList<>();
        list.add("&cИспользование: /tp <игрок>");
        Utilities.msg(sender, list);
    }

    private void teleport(CommandSender sender, String[] loc) {
        if (loc.length < 3) {
            help(sender);
            return;
        }
        try {
            Location parsed = new Location(((Player)sender).getWorld(), Double.parseDouble(loc[0].trim()), Double.parseDouble(loc[1].trim()), Double.parseDouble(loc[2].trim()));
            if (loc.length > 3)
                parsed.setYaw(Float.parseFloat(loc[3].trim()) + 1.0E-4F);
            if (loc.length > 4)
                parsed.setPitch(Float.parseFloat(loc[4].trim()) + 1.0E-4F);
            if (parsed.getX() % 1.0D < 1.0E-4D && parsed.getZ() % 1.0D < 1.0E-4D) {
                parsed.setX(parsed.getX() + 0.5D);
                parsed.setZ(parsed.getZ() + 0.5D);
            }
            teleport(sender, parsed);
        } catch (Exception ex) {
            help(sender);
        }
    }

    private void teleport(CommandSender sender, Player entity) {
        teleport(sender, entity.getLocation());
        Utilities.msg(sender, T.success("GOT IT!", "&fВы телепортированы к игркоку &a" + entity.getName()));
    }

    private void teleport(CommandSender sender, Location loc) {
        Player player = (Player)sender;
        if (loc.getYaw() == 0.0F)
            loc.setYaw(player.getLocation().getYaw());
        if (loc.getPitch() == 0.0F)
            loc.setPitch(player.getLocation().getPitch());
        player.teleport(loc);
    }
}
