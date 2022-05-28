package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.ServerType;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.mes;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (FlexingNetwork.lobby().getServerType() == ServerType.BUILD) {
            if (!FlexingNetwork.hasPermission(sender, Permission.BUILDSERVER, true))
                return true;
        } else if (!FlexingNetwork.hasRank(sender, Rank.ADMIN, true)) {
            return true;
        }

        Player player = (Player) sender;
        switch (cmd.getName()) {
            case "gamemode":
                if (args.length == 0) {
                    mes.msg(player, "&cИспользование: /" + label + " <режим>");
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
            return true;
    }

    private void changeGamemode(Player player, GameMode mode) {
        if (player.getGameMode() == mode) {
            mes.msg(player, T.error("Error","&cВаш режим игры уже является " + mode.name()));
            return;
        }
        player.setGameMode(mode);
        mes.msg(player, T.success("GOT IT!", "Режим игры изменён на &a" + mode.name()));
    }
}