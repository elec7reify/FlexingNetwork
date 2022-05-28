package com.flexingstudios.FlexingNetwork.api.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class dataCommand {
    private CommandSender sender;
    private String label;
    private String sub;
    private String[] args;

    public dataCommand(CommandSender sender, String label, String sub, String[] args) {
    this.sender =sender;
    this.label =label;
    this.sub =sub;
    this.args =args;
}

    public Player getPlayer() {
        return (Player) this.sender;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public String getLabel() {
        return this.label;
    }

    public String getSub() {
        return this.sub;
    }

    public String[] getArgs() {
        return this.args;
    }

    public boolean hasArgs() {
        return (this.args.length != 0);
    }
}
