package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }
}
