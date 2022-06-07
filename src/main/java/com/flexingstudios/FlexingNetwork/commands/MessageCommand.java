package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.mes;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;
        MysqlPlayer senderInfo;
        MysqlPlayer recieverInfo;
        MysqlPlayer lastWriter;
        String message;
        int i;

        switch (command.getName().toLowerCase()) {
            case "msg":
                if (args.length < 2) {
                    mes.msg(sender, "&cИспользование: /" + label + " &f<кому> <сообщение>");
                    return true;
                }

                if (args[0].equalsIgnoreCase(sender.getName())) {
                    mes.msg(sender, "boy next door");
                    return true;
                }

                player = Bukkit.getPlayerExact(args[0]);

                if (player == null) {
                    mes.msg(sender, "§cИгрок " + args[0] + " не найден");
                    break;
                }
                senderInfo = (MysqlPlayer) FLPlayer.get(sender.getName());
                recieverInfo = (MysqlPlayer) FLPlayer.get(player);

                message = args[1];

                for (i = 2; i < args.length; i++)
                    message = message + " " + args[i];
                trySendPrivateMessage(senderInfo, recieverInfo, message);
                break;
            case "reply":
                if (args.length < 1) {
                    mes.msg(sender, "&cИспользование: /" + label + " &f<сообщение");
                    return true;
                }
                senderInfo = (MysqlPlayer) FLPlayer.get(sender.getName());
                message = args[0];

                if (senderInfo.lastWriter == null || (lastWriter = (MysqlPlayer) FLPlayer.PLAYERS.get(senderInfo.lastWriter)) == null) {
                    mes.msg(sender, "§cНекому ответить");
                    return true;
                }

                for (i = 1; i < args.length; i++)
                    message = message + " " + args[i];
                trySendPrivateMessage(senderInfo, lastWriter, message);
                break;
        }
        return true;
    }

    private void trySendPrivateMessage(MysqlPlayer sender, MysqlPlayer reciever, String message) {
        if (!sender.rank.has(Rank.ADMIN) && reciever.ignoreAll) {
            mes.msg(sender.player, reciever.getName() + " Отключил приватные сообщения");
            return;
        }

        if (sender.ignoreAll) {
            mes.msg(sender.player, T.error(sender.username, "Вы отключили личные сообщения"));
            return;
        }

        if (sender.ignored.contains(reciever.getName())) {
            mes.msg(sender.player, T.error("FlexingWorld", "Игрок " + reciever.getName() + " у вас в игноре."));
            return;
        }

        if (!sender.rank.has(Rank.ADMIN) && reciever.ignored.contains(sender.getName())) {
            mes.msg(sender.player, T.error(reciever.getName(),"&cВы в игноре у этого игрока"));
            return;
        }
        sender.player.sendMessage(mes.colored("[&6Вы&r -> &6" + reciever.player.getDisplayName() + "&r] ") + message);
        reciever.player.sendMessage(mes.colored("[&6" + sender.player.getDisplayName() + "&r -> &6Вы&r] ") + message);
        reciever.lastWriter = sender.getName();
        sender.lastWriter = sender.getName();
    }
}
