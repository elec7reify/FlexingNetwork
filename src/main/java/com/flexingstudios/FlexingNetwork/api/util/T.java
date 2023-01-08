package com.flexingstudios.FlexingNetwork.api.util;

import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class T {
    public static String system(String title, String text) {
        return title + " &3> &f" + text;
    }

    public static String success(String title, String text) {
        return system("&9&l" + title, "&f" + text);
    }

    public static String error(String title, String text) {
        return system("&3" + title, "&c" + text);
    }

    public static String BanMessage(Player banned) {
        String message = Language.getMsg(banned, Messages.BAN_MESSAGE);
        return message;
    }

    public static String formattedKickMessage(Player player) {
        String message = Language.getMsg(player, Messages.KICK_MESSAGE);
        return message;
    }
}
