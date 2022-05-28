package com.flexingstudios.FlexingNetwork.friends.utils.enums;

import org.bukkit.ChatColor;

public enum Error {

    PLAYERS_ONLY(ChatColor.RED + "This command can't be used by Console!"),
    PLAYER_NOT_FOUND(ChatColor.RED + "That player was not found."),
    PLAYER_NOT_ONLINE(ChatColor.RED + "That player isn't online."),
    NO_PERMISSION(ChatColor.RED + "You do not have permission to execute this command!");

    private String error;

    Error(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
