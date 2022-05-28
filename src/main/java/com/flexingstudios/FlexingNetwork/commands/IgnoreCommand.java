package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.mes;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
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
                mes.msg(sender, "&cИспользование /ignore <ник>");
                return true;
            }

            MysqlPlayer flplayer = (MysqlPlayer) FLPlayer.get(sender.getName());

            if (args[0].equals("@all")) {
                mes.msg(sender, T.success("GOT IT!", "Личные сообщения больше не принимаются"));
                //flplayer.ignoreAll = true;
                (FLPlayer.get(sender.getName())).settings.set(1, false);
                return true;
            }

            Player player = Bukkit.getPlayerExact(args[0]);

            if (player == sender) {
                mes.msg(sender, T.error("Ошибка", "Не глупи, ты не можешь игнорировать себя"));
                return true;
            }

            if (player == null) {
                mes.msg(sender, "&cИгрок " + args[0] + " не найден");
            } else if (flplayer != null) {
                if (flplayer.ignored.contains(player.getName())) {
                    mes.msg(sender, "Вы уже игнорируете этого игрока");
                } else {
                    mes.msg(sender, T.success("GOT IT!", "Теперь вы игнорируете игрока " + player.getName()));
                    flplayer.ignored.add(player.getName());
                }
            }
        }

        if (command.getName().equals("unignore")) {
            if (args.length != 1) {
                mes.msg(sender, "&cИспользование /unignore <ник>");
                return true;
            }

            MysqlPlayer flplayer = (MysqlPlayer) FLPlayer.get(sender.getName());

            if (args[0].equals("@all")) {
                mes.msg(sender, T.success("GOT IT!", "Личные сообщения теперь принимаются"));
                //flplayer.ignoreAll = false;
                (FLPlayer.get(sender.getName())).settings.set(1, true);
                return true;
            }

            Player player = Bukkit.getPlayerExact(args[0]);

            if (player == null) {
                mes.msg(sender, "&cИгрок " + args[0] + " не найден");
            } else if (flplayer != null) {
                if (!flplayer.ignored.contains(player.getName())) {
                    mes.msg(sender, "Вы не игнорируете " + player.getName());
                } else {
                    mes.msg(sender, T.success("GOT IT!", "Вы больше не игнорируете игрока " + player.getName()));
                    flplayer.ignored.remove(player.getName());
                }
            }
        }

        if (command.getName().equals("msgtoggle")) {
            MysqlPlayer flplayer = (MysqlPlayer) FLPlayer.get(sender.getName());

            if (!flplayer.ignoreAll) {
                mes.msg(sender, T.success("GOT IT!", "Личные сообщения больше не принимаются"));
                flplayer.ignoreAll = true;
            } else {
                mes.msg(sender, T.success("GOT IT!", "Личные сообщения теперь принимаются"));
                flplayer.ignoreAll = false;
            }
        }
        return true;
    }
}
