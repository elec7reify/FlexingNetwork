package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.flexingnetwork.api.util.Notifications;
import com.flexingstudios.flexingnetwork.api.util.Utils;
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
            Utils.msg(sender, "§cНет прав");
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
            Notifications.success(sender, "GOT IT!", "&fСкорость &aполёта &fустановлен на &a" + speed);
        } else {
            player.setWalkSpeed(0.2F + 0.08F * (speed - 1));
            Notifications.success(sender, "GOT IT!", "&fСкорость &aходьбы &fустановлен на &a" + speed);
        }

        return true;
    }

    private void help(CommandSender sender) {
        Utils.msg(sender, "&cИспользование: /speed <скорость ходьбы/полёта>");
    }
}
