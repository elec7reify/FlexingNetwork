package com.flexingstudios.FlexingNetwork.api;

import org.bukkit.ChatColor;

public enum Attributes {
    NEW(ChatColor.RED, "НОВОЕ"),
    UPDATE(ChatColor.AQUA, "ОБНОВЛЕНИЕ"),
    UPDATED(ChatColor.GREEN, "ОБНОВЛЕНО"),
    DISCOUNT(ChatColor.GOLD, "СКИДКА"),
    RELEASED(ChatColor.YELLOW, "ВЫПУЩЕНА"),
    FEATURED(ChatColor.GREEN, "ПОПУЛЯРНОЕ")
    ;

    private final ChatColor color;
    private final String tag;

    Attributes(ChatColor color, String tag) {
        this.color = color;
        this.tag = tag;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getTag() {
        return tag;
    }
}
