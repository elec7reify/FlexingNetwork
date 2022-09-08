package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int speed;

        Player player = (Player)sender;
        if (!player.hasPermission("speed")) {
            Utilities.msg(sender, "§cНет прав");
            return true;
        }

        if (args.length == 0) {
            help(sender);
            return true;
        }

        try {
            speed = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            help(sender);
            return true;
        }

        if (speed < 1)
            speed = 1;
        if (speed > 10)
            speed = 10;

        if (player.isFlying()) {
            player.setFlySpeed(0.1F + 0.05F * (speed - 1));
            Utilities.msg(sender, T.success("GOT IT!", "&fСкорость &aполёта &fустановлен на &a" + speed));
        } else {
            player.setWalkSpeed(0.2F + 0.08F * (speed - 1));
            Utilities.msg(sender, T.success("GOT IT!", "&fСкорость &aходьбы &fустановлен на &a" + speed));
        }

        return true;
    }

    private void help(CommandSender sender) {
        Utilities.msg(sender, "&cИспользование: /speed <скорость ходьбы/полёта>");
    }
}
