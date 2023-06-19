package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.flexingnetwork.BungeeListeners.BungeeBridge;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.Language.Messages;
import com.flexingstudios.flexingnetwork.api.ServerType;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (FlexingNetwork.INSTANCE.lobby().getServerType() == ServerType.LOBBY) {
            Utils.msg(player, Messages.COMMAND_LOBBY_ERROR);
        } else {
            BungeeBridge.toLobby(player);
            Utils.msg(player, Messages.COMMAND_LOBBY_SUCCESSFUL);
        }

        return true;
    }
}
