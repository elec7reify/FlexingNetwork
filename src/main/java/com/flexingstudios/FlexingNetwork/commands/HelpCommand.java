package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.common.player.Permission;
import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements CommandExecutor {
    private ArrayList<Desc> commands = new ArrayList<>(20);

    public void addCommand(String command, String help) {
        addCommand(command, help, Rank.PLAYER);
    }

    public void addCommand(String command, String help, Rank rank) {
        for (Desc desc : this.commands) {
            if (desc.command.equals(command))
                return;
        }
        this.commands.add(new RankDesc(command, help, rank));
    }

    public void addCommand(String command, String help, Permission permission) {
        for (Desc desc : this.commands) {
            if (desc.command.equals(command))
                return;
        }
        this.commands.add(new PermissionDesc(command, help, permission));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            Utils.msg(sender, "&cPlayer only commands!");
            return true;
        }

        List<Desc> cmds = getAllowedCommands(FlexPlayer.get(sender.getName()));
        int page = 0;
        int pages = (cmds.size() - 1) / 9;

        if (args.length == 1)
            try {
                page = Integer.parseInt(args[0]) - 1;
                if (page > pages)
                    page = pages;
                if (page < 0)
                    page = 0;
            } catch (NumberFormatException numberFormatException) {}
        Utils.msg(sender, "&3============== &fПомощь &7[" + (page + 1) + "&8/&7" + (pages + 1) + "] &3==============");
        int index = page * 9;

        for (int i = index; i < cmds.size() && i < index + 9; i++) {
            Desc cmd = cmds.get(i);
            sender.sendMessage(ChatColor.AQUA + "/" + cmd.command + ChatColor.WHITE + " - " + cmd.help);
        }

        return true;
    }

    private List<Desc> getAllowedCommands(NetworkPlayer player) {
        List<Desc> cmds = new ArrayList<>(this.commands.size());
        this.commands.stream()
                .filter(cmd -> cmd.canUse(player))
                .forEachOrdered(cmds::add);
        return cmds;
    }

    private static abstract class Desc {
        String command;

        String help;

        Desc(String command, String help) {
            this.command = Utils.colored(command);
            this.help = Utils.colored(help);
        }

        public abstract boolean canUse(NetworkPlayer param1NetworkPlayer);

        public String toString() {
            return this.command;
        }
    }

    private static class RankDesc extends Desc {
        Rank rank;

        RankDesc(String command, String help, Rank rank) {
            super(command, help);
            this.rank = rank;
        }

        public boolean canUse(NetworkPlayer player) {
            return player.getRank().has(this.rank);
        }
    }

    private static class PermissionDesc extends Desc {
        Permission permission;

        PermissionDesc(String command, String help, Permission permission) {
            super(command, help);
            this.permission = permission;
        }

        public boolean canUse(NetworkPlayer player) {
            return player.getRank().has(this.permission);
        }
    }
}
