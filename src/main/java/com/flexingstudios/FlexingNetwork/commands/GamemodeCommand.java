package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.common.player.Permission;
import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.Language.Messages;
import com.flexingstudios.flexingnetwork.api.ServerType;
import com.flexingstudios.flexingnetwork.api.util.Notifications;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class GamemodeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (FlexingNetwork.INSTANCE.lobby().getServerType() == ServerType.BUILD) {
                if (!FlexingNetwork.INSTANCE.hasPermission(sender, Permission.BUILDSERVER, true))
                    return true;
            } else if (!FlexingNetwork.INSTANCE.hasRank(sender, Rank.ADMIN, true)) {
                return true;
            }

            Player player = (Player) sender;
            switch (cmd.getName()) {
                case "gamemode":
                    if (args.length == 0) {
                        Utils.msg(player, Messages.COMMAND_GAMEMODE_USAGE.replace("{command}", label));
                        break;
                    }
                    switch (args[0].toLowerCase()) {
                        case "0":
                        case "s":
                        case "survival":
                            changeGamemode(player, GameMode.SURVIVAL);
                            break;
                        case "1":
                        case "c":
                        case "creative":
                            changeGamemode(player, GameMode.CREATIVE);
                            break;
                        case "2":
                        case "a":
                        case "adventure":
                            changeGamemode(player, GameMode.ADVENTURE);
                            break;
                        case "3":
                        case "sp":
                        case "spectator":
                            changeGamemode(player, GameMode.SPECTATOR);
                            break;
                    }
                    break;
                case "gmc":
                    changeGamemode(player, GameMode.CREATIVE);
                    break;
                case "gma":
                    changeGamemode(player, GameMode.ADVENTURE);
                    break;
                case "gms":
                    changeGamemode(player, GameMode.SURVIVAL);
                    break;
                case "gmsp":
                    changeGamemode(player, GameMode.SPECTATOR);
                    break;
            }
        } else {
            switch (cmd.getName()) {
                case "gamemode":
                    if (args.length == 0 || args.length == 1) {
                        Utils.msg(sender, Messages.COMMAND_GAMEMODE_USAGE.replace("{command}", label));
                        break;
                    }
                    switch (args[0].toLowerCase()) {
                        case "0":
                        case "s":
                        case "survival":
                            changeGamemode(args[1], GameMode.SURVIVAL);
                            break;
                        case "1":
                        case "c":
                        case "creative":
                            changeGamemode(args[1], GameMode.CREATIVE);
                            break;
                        case "2":
                        case "a":
                        case "adventure":
                            changeGamemode(args[1], GameMode.ADVENTURE);
                            break;
                        case "3":
                        case "sp":
                        case "spectator":
                            changeGamemode(args[1], GameMode.SPECTATOR);
                            break;
                    }
                    break;
                case "gmc":
                    if (args.length == 0) {
                        Utils.msg(sender, Messages.COMMAND_GAMEMODE_USAGE.replace("{command}", label));
                        break;
                    }
                    changeGamemode(args[0], GameMode.CREATIVE);
                    break;
                case "gma":
                    if (args.length == 0) {
                        Utils.msg(sender, Messages.COMMAND_GAMEMODE_USAGE.replace("{command}", label));
                        break;
                    }
                    changeGamemode(args[0], GameMode.ADVENTURE);
                    break;
                case "gms":
                    if (args.length == 0) {
                        Utils.msg(sender, Messages.COMMAND_GAMEMODE_USAGE.replace("{command}", label));
                        break;
                    }
                    changeGamemode(args[0], GameMode.SURVIVAL);
                    break;
                case "gmsp":
                    if (args.length == 0) {
                        Utils.msg(sender, Messages.COMMAND_GAMEMODE_USAGE.replace("{command}", label));
                        break;
                    }
                    changeGamemode(args[0], GameMode.SPECTATOR);
                    break;
            }
        }

            return true;
    }

    private void changeGamemode(Player player, GameMode mode) {
        if (player.getGameMode() == mode) {
            Notifications.error(player, "Ошибка", Messages.COMMAND_GAMEMODE_ERROR.replace("{mode}", mode.name()));
            return;
        }

        player.setGameMode(mode);
        Notifications.success(player, "GOT IT!", Messages.COMMAND_GAMEMODE_CHANGED.replace("{mode}", mode.name()));
    }

    private void changeGamemode(String player, GameMode mode) {
        Player target = Bukkit.getPlayer(player);
        if (player != null) {
            if (target.getGameMode() == mode) {
                //Utilities.msg(player, T.error("Error", Language.getMsg(player, Messages.COMMAND_GAMEMODE_ERROR).replace("{mode}", mode.name())));
                return;
            }

            target.setGameMode(mode);
            //Utilities.msg(player, T.success("GOT IT!", Language.getMsg(player, Messages.COMMAND_GAMEMODE_CHANGED).replace("{mode}", mode.name())));
        } else {
            Logger.getGlobal().info("Player doesn't exists!");
        }
    }
}
