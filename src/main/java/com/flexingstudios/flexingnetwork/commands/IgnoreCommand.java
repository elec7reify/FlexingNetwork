package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.flexingnetwork.api.Language.Messages;
import com.flexingstudios.flexingnetwork.api.util.Notifications;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
import com.flexingstudios.flexingnetwork.impl.player.MysqlPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("ignore")) {
            if (args.length != 1) {
                Utils.msg(sender, Messages.COMMAND_IGNORE_USAGE);
                return true;
            }

            MysqlPlayer flplayer = (MysqlPlayer) FlexPlayer.get(sender.getName());

            if (args[0].equals("@all")) {
                Notifications.success(sender, "GOT IT!", "Личные сообщения больше не принимаются");
                FlexPlayer.get(sender.getName()).settings.set(1, false);
                return true;
            }

            Player player = Bukkit.getPlayerExact(args[0]);

            if (args[0].equalsIgnoreCase(sender.getName())) {
                Notifications.error(sender, "Ошибка", "Не глупи, ты не можешь игнорировать себя");
                return true;
            }

            if (player == null) {
                Utils.msg(sender, Messages.PLAYER_NOT_FOUND.replace("{player}", args[0]));
            } else if (flplayer != null) {
                if (flplayer.ignored.contains(player.getName())) {
                    Utils.msg(sender, Messages.IGNORE_ALREADY);
                } else {
                    Notifications.success(sender, "GOT IT!", "Теперь вы игнорируете игрока " + player.getName());
                    // TODO: save ignore player with user meta
                    //flplayer.setMeta("ignored", "");
                    flplayer.ignored.add(player.getName());
                }
            }
        }

        if (command.getName().equals("unignore")) {
            if (args.length != 1) {
                Utils.msg(sender, Messages.COMMAND_UNIGNORE_USAGE);
                return true;
            }

            MysqlPlayer flplayer = (MysqlPlayer) FlexPlayer.get(sender.getName());

            if (args[0].equals("@all")) {
                Notifications.success(sender, "GOT IT!", "Личные сообщения теперь принимаются");
                FlexPlayer.get(sender.getName()).settings.set(1, true);
                return true;
            }

            Player player = Bukkit.getPlayerExact(args[0]);

            if (player == null) {
                Utils.msg(sender, Messages.PLAYER_NOT_FOUND.replace("{player}", args[0]));
            } else if (flplayer != null) {
                if (!flplayer.ignored.contains(player.getName())) {
                    Utils.msg(sender, "&cВы не игнорируете " + player.getName());
                } else {
                    Notifications.success(sender, "GOT IT!", "Вы больше не игнорируете игрока " + player.getName());
                    flplayer.ignored.remove(player.getName());
                }
            }
        }

        if (command.getName().equals("msgtoggle")) {
            MysqlPlayer flplayer = (MysqlPlayer) FlexPlayer.get(sender.getName());

            if (FlexPlayer.get(sender.getName()).settings.get(1)) {
                Notifications.success(sender, "GOT IT!", "Личные сообщения больше не принимаются");
                FlexPlayer.get(sender.getName()).settings.set(1, false);
            } else {
                Notifications.success(sender, "GOT IT!", "Личные сообщения теперь принимаются");
                FlexPlayer.get(sender.getName()).settings.set(1, true);
            }
        }

        return true;
    }
}
