package com.flexingstudios.FlexingNetwork.api.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Notifications {
    @Contract(pure = true)
    public static @NotNull String system(String title, String text) {
        return title + " → &f" + text;
    }

    public static String success(String title, String text) {
        return system("&a" + title, "&f" + text);
    }

    public static String error(String title, String text) {
        return system("&c" + title, "&f" + text);
    }

    public static String banMessage() {
        return "&fСлужба безопастности &9&lFlexing&f&lWorld" +
                "\n&fВы были забанены" +
                "\n" +
                "\n&fВаш ник: &3{player}" +
                "\n&fВас забанил: {admin}" +
                "\n&fВремя бана: &6{time}" +
                "\n&fПо причине: &6{reason}" +
                "\n" +
                "\n&fПожалуйста, &3&lпрочитайте правила сервера&r&f, чтобы избежать дальнейших наказаний" +
                "\n" +
                "\n&fНе согласны с нашим наказанием? Подайте аппеляцию" +
                "\n&fVK: &3https://vk.com/flexingworld" +
                "\n&fDS: &3https://discord.gg/9X6QpjqSvG" +
                "\n" +
                "\n&8{date}";
    }

    public static String kickMessage() {
        return "\n&fСлужба безопастности &9&lFlexing&f&lWorld" +
        "\n&fВы были кикнуты" +
        "\n" +
        "\n&fВаш ник: &3{player}" +
        "\n&fВас кикнул: {admin}" +
        "\n&fПо причине: &6{reason}" +
        "\n" +
        "\n&fПожалуйста, &3&lпрочитайте правила сервера&r&f, чтобы избежать дальнейших наказаний" +
        "\n" +
        "\n&fНе согласны с нашим наказанием? Подайте аппеляцию" +
        "\n&fVK: &3https://vk.com/flexingworld" +
        "\n&fDS: &3https://discord.gg/9X6QpjqSvG" +
        "\n" +
        "\n&8{date}";
    }
}
