package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.F;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.Debug;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.command.CmdSub;
import com.flexingstudios.FlexingNetwork.api.command.UpCommand;
import com.flexingstudios.FlexingNetwork.api.command.dataCommand;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.ParsedTime;
import com.flexingstudios.FlexingNetwork.api.util.mes;
import com.flexingstudios.FlexingNetwork.tasks.Restart;
import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class FlexingNetworkCommand extends UpCommand {
    private final FlexingNetworkPlugin plugin;

    public FlexingNetworkCommand(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void runCommand(Runnable action, CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof  Player)) {
            super.runCommand(action, sender, cmd, label, args);
            return;
        }
        NetworkPlayer player = FlexingNetwork.getPlayer(sender.getName());
        if (player.has(Rank.ADMIN) || player.has(Rank.SADMIN) || player.has(Rank.VADMIN)) {
            super.runCommand(action, sender, cmd, label, args);
        }
    }

    @Override
    protected boolean main(CommandSender sender, Command command, String label, String[] args) {
        help(new dataCommand(sender, label, "help", new String[0]));
        return false;
    }

    @CmdSub(value = {"tolobby"}, rank = Rank.VADMIN)
    private void toLobby(dataCommand data) {
        if ((data.getArgs().length == 0)) {
            mes.msg(data.getSender(), "&cИспользование /" + data.getLabel() + " " + data.getSub() + " <игрок>");
            return;
        }
        Player player = this.plugin.getServer().getPlayerExact(data.getArgs()[0]);
        if (player != null) {
            FlexingNetwork.toLobby(player);
        } else {
            mes.msg(data.getSender(), "&cИгрок " + data.getArgs()[0] + " не найден");
        }
    }

    @CmdSub(value = {"tolobbyall"}, rank = Rank.ADMIN)
    private void toLobbyAll(dataCommand data) {
        mes.msg(data.getSender(), "&aВсе игроки отправлены в лобби ");
        FlexingNetwork.toLobby((Player) Bukkit.getOnlinePlayers());
    }

    @CmdSub(value = {"toserver"}, rank = Rank.ADMIN)
    private void toServer(dataCommand data) {
        if((data.getArgs().length != 2)) {
            mes.msg(data.getSender(), "&cusage /" + data.getLabel() + " " + data.getSub() + " <игрок> <сервер>");
            return;
        }
        Player player = this.plugin.getServer().getPlayerExact(data.getArgs()[0]);
        if (player != null) {
            FlexingNetwork.toServer(data.getArgs()[1], player);
        } else {
            mes.msg(data.getSender(), "&cИгрок " + data.getArgs()[0] + " не найден");
        }
    }

    @CmdSub(value = {"toserverall"}, rank = Rank.ADMIN)
    private void toServerAll(dataCommand data) {
        if ((data.getArgs()).length != 1) {
            mes.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <сервер>");
            return;
        }
        mes.msg(data.getSender(), "&aВсе игроки отправлены на сервер " + data.getArgs()[0]);
        for (Player player : Bukkit.getOnlinePlayers())
            FlexingNetwork.toServer(data.getArgs()[0], player);
    }

    @CmdSub(value = {"addcoins"}, rank = Rank.ADMIN)
    private void addCoins(dataCommand data) {
        if ((data.getArgs()).length != 2) {
            mes.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <игрок> <количество>");
            return;
        }
        int coins = Integer.parseInt(data.getArgs()[1]);
        if (data.getArgs()[0].equals("@all")) {
            for (Player player : Bukkit.getOnlinePlayers())
                FlexingNetwork.getPlayer(player).addCoins(coins);
        } else {
            Player player = this.plugin.getServer().getPlayerExact(data.getArgs()[0]);
            if (player != null)
                FlexingNetwork.getPlayer(player).addCoins(coins);
        }
    }

    @CmdSub(value = {"giveexp"}, rank = Rank.SADMIN)
    private void giveExp(dataCommand data) {
        if ((data.getArgs()).length != 2) {
            mes.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <игрок> <количество>");
            return;
        }
        int exp = Integer.parseInt(data.getArgs()[1]);
        if (data.getArgs()[0].equals("@all")) {
            for (Player player : Bukkit.getOnlinePlayers())
                FlexingNetwork.getPlayer(player).giveExp(exp);
        } else {
            Player player = this.plugin.getServer().getPlayerExact(data.getArgs()[0]);
            if (player != null) {
                FlexingNetwork.getPlayer(player).giveExp(exp);
            }
        }
    }


    @CmdSub(value = {"givestatus"}, rank = Rank.ADMIN)
    private void givestatus(dataCommand data) {
        if ((data.getArgs().length != 2)) {
            mes.msg(data.getSender(), "&c/" + data.getLabel() + " " + data.getSub() + " <игрок> [Статус]");
            return;
        }
        if (data.getArgs()[1] == null) {
            mes.msg(data.getSender(), "&aВсе доступные статусы: &fPLAYER, VIP, PREMIUM, CREATIVE, MODERATOR, CHIKIBAMBONI,", " ADMINOBAMBONI, CHIKIBAMBONYLA, SPONSOR, OWNER, CHIKIBOMBASTER,", " GOD, TEAM, VADMIN, SADMIN, ADMIN");
        } else {
            FlexingNetwork.mysql().query("UPDATE authme SET status='" + data.getArgs()[1] + "' WHERE username = '" + data.getArgs()[0] + "'");
            data.getSender().sendMessage(mes.colored("&aСтатус &f" + data.getArgs()[1] + " &aвыдан игроку &f" + data.getArgs()[0] + "&a!"));
        }
    }

    @CmdSub(value = {"stats"}, aliases = {"status"}, rank = Rank.VADMIN)
    private void stats(dataCommand data) {
        Runtime runtime = Runtime.getRuntime();
        List<String> lines = new ArrayList<>();
        lines.add("&e------------ &6Статистика &f&e------------");
        lines.add("&eВремя работы: &f" + (new ParsedTime(System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime())).format());
        lines.add("&eПамять: &f" + ((runtime.totalMemory() - runtime.freeMemory()) / 1024L / 1024L) + " MB / " + (runtime.totalMemory() / 1024L / 1024L) + " MB up to " + (runtime.maxMemory() / 1024L / 1024L) + " MB");
        lines.add("&eПодключение к бд: " + (this.plugin.mysql.isConnected() ? "&aактивно" : "&cразорвано"));
        lines.add("&eЗапросов к бд: &f" + this.plugin.mysql.getExecutedQueries());
        mes.msg(data.getSender(), lines);
    }

    @CmdSub(value = {"gc"}, rank = Rank.ADMIN)
    private void gc(dataCommand data) {
        long start = System.nanoTime();
        System.gc();
        mes.msg(data.getSender(), "System " + F.formatFloat((float) (System.nanoTime() - start) / 1000000.0F, 2) + " мс.");
    }

    @CmdSub(value = {"debug"}, rank = Rank.ADMIN)
    private void debug(dataCommand data) {
        try {
            Debug group = Debug.valueOf(data.getArgs()[0].toUpperCase());
            if (group.isEnabled()) {
                mes.msg(data.getSender(), "&e" + group.name() + " дебаг &cвыключен.");
                group.setEnabled(false);
                } else {
                    mes.msg(data.getSender(), "&e" + group.name() + " дебаг &aвключен.");
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
                    mes.msg(data.getSender(), "&e/" + data.getLabel() + " debug " + str);
                }
            }


     @CmdSub(value = {"restart"}, rank = Rank.ADMIN)
     private void restart(dataCommand data) {
         if (data.hasArgs()) {
             Restart.restart();
         } else {
             Restart.countdown();
         }
    }

    @CmdSub(value = {"help"}, ranks = {Rank.ADMIN, Rank.SADMIN, Rank.VADMIN}, hidden = true)
    private void help(dataCommand data) {
        List<String> cmds = new ArrayList<>();
        Rank rank = getRank(data.getSender());
        for (PublicSub sub : getPublicSubs()) {
            if (sub.sub.isAvailableFor(rank, null))
                cmds.add(sub.cmd);
        }
        data.getSender().sendMessage(Joiner.on(", ").join(cmds));
    }
}
