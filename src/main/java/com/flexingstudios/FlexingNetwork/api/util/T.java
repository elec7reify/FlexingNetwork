package com.flexingstudios.FlexingNetwork.api.util;

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
}
