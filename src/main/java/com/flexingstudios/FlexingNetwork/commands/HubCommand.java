package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.BungeeListeners.BungeeBridge;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.ServerType;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (FlexingNetwork.lobby().getServerType() == ServerType.LOBBY) {
            Utilities.msg(player, "&aВы уже находитесь в лобби.");
        } else {
            BungeeBridge.toLobby(player);
            Utilities.msg(player, "&cВы были перенаправленны в лобби.");
        }

        return true;
    }
}
