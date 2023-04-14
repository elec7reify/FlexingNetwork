package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.api.util.Notifications;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FlexPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import net.md_5.bungee.api.chat.*;
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
                    Utilities.msg(sender, "&cИспользование: /" + label + " &f<кому> <сообщение>");
                    return true;
                }

//                if (args[0].equalsIgnoreCase(sender.getName())) {
//                    Utilities.msg(sender, "&dОчень умный ход, пожалуй слишком умный для того, чтобы сервер смог это понять");
//                    return true;
//                }

                player = Bukkit.getPlayerExact(args[0]);

                if (player == null) {
                    Utilities.msg(sender, "§cИгрок " + args[0] + " не найден");
                    break;
                }
                senderInfo = (MysqlPlayer) FlexPlayer.get(sender.getName());
                recieverInfo = (MysqlPlayer) FlexPlayer.get(player);

                message = args[1];

                for (i = 2; i < args.length; i++)
                    message = message + " " + args[i];
                trySendPrivateMessage(senderInfo, recieverInfo, message);
                break;
            case "reply":
                if (args.length < 1) {
                    Utilities.msg(sender, "&cИспользование: /" + label + " &f<сообщение");
                    return true;
                }
                senderInfo = (MysqlPlayer) FlexPlayer.get(sender.getName());
                message = args[0];

                if (senderInfo.lastWriter == null || (lastWriter = (MysqlPlayer) FlexPlayer.PLAYERS.get(senderInfo.lastWriter)) == null) {
                    Utilities.msg(sender, "§cНекому ответить");
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
        if (!sender.rank.has(Rank.ADMIN) && !FlexPlayer.get(reciever.getBukkitPlayer()).settings.get(1)) {
            Utilities.msg(sender.player, reciever.getName() + " Отключил приватные сообщения");
            return;
        }

        if (!FlexPlayer.get(sender.getBukkitPlayer()).settings.get(1)) {
            Utilities.msg(sender.player, Notifications.error(sender.username, "Вы отключили личные сообщения"));
            return;
        }

        if (sender.ignored.contains(reciever.getName())) {
            Utilities.msg(sender.player, Notifications.error("FlexingWorld", "Игрок " + reciever.getName() + " у Вас в игноре."));
            return;
        }

        if (!sender.rank.has(Rank.ADMIN) && reciever.ignored.contains(sender.getName())) {
            Utilities.msg(sender.player, Notifications.error(reciever.getName(),"&cВы в игноре у этого игрока"));
            return;
        }

        TextComponent componentMessage = new TextComponent(message);

        //reply
        TextComponent reply = new TextComponent(new ComponentBuilder(Utilities.colored("&7[↩] &8⇢ "))
                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getName() + " "))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Нажмите для ответа")))
                .create());

        TextComponent part = new TextComponent(Utilities.colored("[&6" + sender.player.getDisplayName() + "&r -> &6Вы&r] "));
        BaseComponent[] components = new BaseComponent[]{ part, reply, componentMessage };

        // formatted player name
        TextComponent playerName = new TextComponent(new ComponentBuilder(Utilities.colored("&6" + reciever.getName()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/actions " + reciever.getName()))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Нажмите, чтобы открыть меню быстрых действий")))
                .create());

        TextComponent part0 = new TextComponent(Utilities.colored("[&6Вы&r -> "));
        TextComponent part1 = new TextComponent(playerName);
        TextComponent part2 = new TextComponent(Utilities.colored("&r] "));

        BaseComponent[] components1 = new BaseComponent[]{ part0, part1, part2, componentMessage };

        sender.player.spigot().sendMessage(components1);
        reciever.player.spigot().sendMessage(components);
        reciever.lastWriter = sender.getName();
        sender.lastWriter = sender.getName();
    }
}
