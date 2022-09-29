package com.flexingstudios.FlexingNetwork.api.util;

import com.flexingstudios.Commons.F;
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

    public static String BanMessage(Player banned, String reason, int timeSeconds, String admin) {
        String bantime;

        if (timeSeconds == 0) {
            bantime = "навсегда";
        } else {
            bantime = F.formatSecondsShort(timeSeconds);
        }
        List<String> message = Language.getList(banned, Messages.BAN_MESSAGE);

        return String.valueOf(message);
    }

    public static String formattedKickMessage(Player player) {
        List<String> message = Language.getList(player, Messages.KICK_MESSAGE);
//                "&fСлужба безопастности &9&lFlexing&f&lWorld" +
//                "\n&fВы были кикнуты" +
//                "\n" +
//                "\n&fВаш ник &3{username}" +
//                "\n&fВас кикнул &3{kicker}" +
//                "\n&fПо причине: &6{reason}" +
//                "\n" +
//                "\n&fПожалуйста, &3&lпрочитайте правила сервера&r&f, чтобы избежать дальнейших наказаний" +
//                "\n" +
//                "\n&fНе согласны с нашим наказанием? Подайте аппеляцию" +
//                "\n&fVK: &3https://vk.com/flexingworld" +
//                "\nDS: &3https://discord.gg/9X6QpjqSvG" +
//                "\n" +
//                "\n&8{date}";

        return String.valueOf(message);
    }
}
