package com.flexingstudios.flexingnetwork.commands;

import com.flexingstudios.common.F;
import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.Debug;
import com.flexingstudios.flexingnetwork.BungeeListeners.BungeeBridge;
import com.flexingstudios.flexingnetwork.Config;
import com.flexingstudios.flexingnetwork.FlexingNetworkPlugin;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.ServerType;
import com.flexingstudios.flexingnetwork.api.command.SubCommand;
import com.flexingstudios.flexingnetwork.api.command.SubCommandData;
import com.flexingstudios.flexingnetwork.api.command.UpCommand;
import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer;
import com.flexingstudios.flexingnetwork.api.util.ChatUtil;
import com.flexingstudios.flexingnetwork.api.util.ParsedTime;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
import com.flexingstudios.flexingnetwork.tasks.Restart;
import com.google.common.base.Joiner;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlexingNetworkCommand extends UpCommand {
    private final FlexingNetworkPlugin plugin;

    public FlexingNetworkCommand(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void runCommand(Runnable action, CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            super.runCommand(action, sender, cmd, label, args);
            return;
        }
        NetworkPlayer player = FlexingNetwork.INSTANCE.getPlayer(sender.getName());
        if (player.has(Rank.ADMIN) || player.has(Rank.SADMIN) || player.has(Rank.VADMIN)) {
            super.runCommand(action, sender, cmd, label, args);
        }
    }

    @Override
    protected boolean main(CommandSender sender, Command command, String label, String[] args) {
        help(new SubCommandData(sender, label, "help", args));
        return false;
    }

    @SubCommand(name = "tolobby", rank = Rank.VADMIN)
    private void toLobby(SubCommandData data) {
        if (data.getArgs().length == 0) {
            Utils.msg(data.getSender(), "&cИспользование /" + data.getLabel() + " " + data.getSub() + " <игрок>");
            return;
        }
        Player player = plugin.getServer().getPlayerExact(data.getArgs()[0]);
        if (player != null) {
            FlexingNetwork.INSTANCE.toLobby(player);
        } else {
            Utils.msg(data.getSender(), "&cИгрок " + data.getArgs()[0] + " не найден");
        }
    }

    @SubCommand(name = "tolobbyall", rank = Rank.ADMIN)
    private void toLobbyAll(SubCommandData data) {
        Utils.msg(data.getSender(), "&aВсе игроки отправлены в лобби ");
        FlexingNetwork.INSTANCE.toLobby((Player) Bukkit.getOnlinePlayers());
    }

    @SubCommand(name = "toserver", rank = Rank.ADMIN)
    private void toServer(SubCommandData data) {
        if (data.getArgs().length != 2) {
            Utils.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <игрок> <сервер>");
            return;
        }

        Player player = plugin.getServer().getPlayerExact(data.getArgs()[0]);
        if (player != null) {
            FlexingNetwork.INSTANCE.toServer(ServerType.valueOf(data.getArgs()[1]), player);
        } else {
            BungeeBridge.toServerOther(data.getArgs()[0], data.getArgs()[1]);
            Utils.msg(data.getSender(), "&cИгрок " + data.getArgs()[0] + " не найден");
        }
    }

    @SubCommand(name = "toserverall", rank = Rank.ADMIN)
    private void toServerAll(SubCommandData data) {
        if (data.getArgs().length != 1) {
            Utils.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <сервер>");
            return;
        }
        Utils.msg(data.getSender(), "&aВсе игроки отправлены на сервер " + data.getArgs()[0]);
        for (Player player : Bukkit.getOnlinePlayers())
            FlexingNetwork.INSTANCE.toServer(ServerType.valueOf(data.getArgs()[0]), player);
    }

    @SubCommand(name = "addcoins", rank = Rank.ADMIN)
    private void addCoins(SubCommandData data) {
        if (data.getArgs().length != 2) {
            Utils.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <игрок> <количество>");
            return;
        }
        int coins = Integer.parseInt(data.getArgs()[1]);
        if (data.getArgs()[0].equals("@all")) {
            for (Player player : Bukkit.getOnlinePlayers())
                FlexingNetwork.INSTANCE.getPlayer(player).addCoins(coins);
        } else {
            Player player = plugin.getServer().getPlayerExact(data.getArgs()[0]);
            if (player != null)
                FlexingNetwork.INSTANCE.getPlayer(player).addCoins(coins);
        }
    }

    @SubCommand(name = "takecoins", rank = Rank.ADMIN)
    private void takeCoins(SubCommandData data) {
        if (data.getArgs().length != 2) {
            Utils.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <игрок> <количество>");
            return;
        }
        int coins = Integer.parseInt(data.getArgs()[1]);
        if (data.getArgs()[0].equals("@all")) {
            for (Player player : Bukkit.getOnlinePlayers())
                FlexingNetwork.INSTANCE.getPlayer(player).takeCoins(coins);
        } else {
            Player player = plugin.getServer().getPlayerExact(data.getArgs()[0]);
            if (player != null)
                FlexingNetwork.INSTANCE.getPlayer(player).takeCoins(coins);
        }
    }

    @SubCommand(name = "giveexp", rank = Rank.SADMIN)
    private void giveExp(SubCommandData data) {
        if (data.getArgs().length != 2) {
            Utils.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <игрок> <количество>");
            return;
        }
        int exp = Integer.parseInt(data.getArgs()[1]);
        if (data.getArgs()[0].equals("@all")) {
            for (Player player : Bukkit.getOnlinePlayers())
                FlexingNetwork.INSTANCE.getPlayer(player).giveExp(exp);
        } else {
            Player player = plugin.getServer().getPlayerExact(data.getArgs()[0]);
            if (player != null) {
                FlexingNetwork.INSTANCE.getPlayer(player).giveExp(exp);
            }
        }
    }


    @SubCommand(name = "givestatus", rank = Rank.ADMIN)
    private void givestatus(SubCommandData data) {
        if (data.getArgs().length != 2) {
            Utils.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <игрок> [Статус]");
            return;
        }
        if (data.getArgs().length != 2) {
            Utils.msg(data.getSender(), "&aВсе доступные статусы: &f" + Arrays.toString(Rank.values()));
        } else {
            FlexPlayer flexPlayer = FlexPlayer.get(data.getArgs()[0]);
            flexPlayer.rank = Rank.getRank(data.getArgs()[1]);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + data.getArgs()[0] + " parent set " + data.getArgs()[1]);
            FlexingNetwork.INSTANCE.mysql().query("UPDATE authme SET status='" + data.getArgs()[1].toUpperCase() + "' WHERE username = '" + data.getArgs()[0] + "'");
            data.getSender().sendMessage(Utils.colored("&aСтатус &f" + data.getArgs()[1].toUpperCase() + " &aвыдан игроку &f" + data.getArgs()[0] + "&a!"));
        }
    }

    @SubCommand(name = "stats", aliases = "status", rank = Rank.SADMIN)
    private void stats(SubCommandData data) {
        Runtime runtime = Runtime.getRuntime();
        List<String> lines = new ArrayList<>();
        lines.add("&3------------ &fСтатистика &3------------");
        lines.add("&bВремя работы: &f" + new ParsedTime(System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()).format());
        lines.add("&bПамять: &f" + ((runtime.totalMemory() - runtime.freeMemory()) / 1024L / 1024L) + " MB / " + (runtime.totalMemory() / 1024L / 1024L) + " MB up to " + (runtime.maxMemory() / 1024L / 1024L) + " MB");
        lines.add("&bПодключение к бд: " + (plugin.mysql.isConnected() ? "&aактивно" : "&cразорвано"));
        lines.add("&bЗапросов к бд: &f" + plugin.mysql.getExecutedQueries());
        Utils.msg(data.getSender(), lines);
    }

    @SubCommand(name = "gc", rank = Rank.ADMIN)
    private void gc(SubCommandData data) {
        long start = System.nanoTime();
        System.gc();
        Utils.msg(data.getSender(), "System " + F.formatFloat((float) (System.nanoTime() - start) / 1000000.0F, 2) + " ms.");
    }

    @SubCommand(name = "debug", rank = Rank.ADMIN)
    private void debug(SubCommandData data) {
        try {
            Debug group = Debug.valueOf(data.getArgs()[0].toUpperCase());
            if (group.isEnabled()) {
                Utils.msg(data.getSender(), "&e" + group.name() + " дебаг &cвыключен.");
                group.setEnabled(false);
            } else {
                    Utils.msg(data.getSender(), "&e" + group.name() + " дебаг &aвключен.");
                    group.setEnabled(true);
            }
        } catch (Exception e) {
            StringBuilder str = new StringBuilder("<");
            for (Debug group : Debug.values()) {
                if (str.length() != 1)
                    str.append("&e, ");
                if (group.isEnabled()) {
                    str.append("&a").append(group.name());
                } else {
                    str.append("&c").append(group.name());
                }
            }
            str.append("&e>");
            Utils.msg(data.getSender(), "&e/" + data.getLabel() + " debug " + str);
        }
    }

     @SubCommand(name = "restart", rank = Rank.ADMIN)
     private void restart(SubCommandData data) {
         if (data.hasArgs()) {
             Restart.Companion.restart();
         } else {
             Restart.Companion.countdown();
         }
    }

    @SubCommand(name = "reload", rank = Rank.ADMIN)
    private void reload(SubCommandData data) {
        new Config(FlexingNetworkPlugin.getInstance()).reload();
    }

    @SubCommand(name = "help", ranks = {Rank.ADMIN, Rank.SADMIN, Rank.VADMIN}, hidden = true)
    private void help(SubCommandData data) {
        List<String> cmds = new ArrayList<>();
        Rank rank = getRank(data.getSender());
        for (PublicSub sub : getPublicSubs()) {
            if (sub.sub.isAvailableFor(rank, null)) {
                cmds.add(sub.cmd);
                ChatUtil.createMessage(
                        sub.cmd,
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, data.getLabel() + " " + sub.cmd),
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("&7Нажмите, чтобы ввести команду"))
                );
            }
        }
        data.getSender().sendMessage(Joiner.on(", ").join(cmds));
    }
}
