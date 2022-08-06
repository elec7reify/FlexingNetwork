package com.flexingstudios.FlexingNetwork.tasks;

import com.flexingstudios.FlexingNetwork.Config;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.ServerType;
import com.flexingstudios.FlexingNetwork.api.conf.Configuration;
import com.flexingstudios.FlexingNetwork.api.event.ServerRestartEvent;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Restart {
    private static final int RESTART_HOUR = 48;
    private static boolean forced = false;
    private static ScheduledFuture<?> future;

    public static void schedule() {
        LocalDateTime now = LocalDateTime.now(FlexingNetwork.TZ_MOSCOW);
        int hours = now.getHour();
        int waitHours = (hours >= 3) ? (23 - hours + 3) : (3 - hours - 1);
        int waitMins= 59 - now.getMinute();
        int waitSeconds = 59 - now.getSecond();
        if (waitHours < 2)
            waitHours += 24;
        long waitTimeMillis = ((waitHours * 3600 + waitMins * 60 + waitSeconds - 300) * 1000);
        if (future != null)
            future.cancel(false);
        future = (FlexingNetworkPlugin.getInstance()).scheduledExecutorService.schedule(Restart::countdown, waitTimeMillis, TimeUnit.MILLISECONDS);
        Bukkit.getPluginManager().callEvent(new ServerRestartEvent(ServerRestartEvent.State.SCHEDULED, false));
    }

    public static void countdown() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }

        Bukkit.getPluginManager().callEvent(new ServerRestartEvent(ServerRestartEvent.State.COUNTDOWN, forced));
        Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers())
                Utilities.bcast(Language.getList(player, Messages.RESTART_BROADCAST));
        });

        ScheduledExecutorService executor = FlexingNetworkPlugin.getInstance().scheduledExecutorService;
        for (Player player : Bukkit.getOnlinePlayers()) {
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "4min")), 60L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "3min")), 120L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "2min")), 180L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "1min")), 240L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "30sec")), 270L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "10sec")), 290L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "5sec")), 295L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "4sec")), 296L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "3sec")), 297L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "2sec")), 298L, TimeUnit.SECONDS);
            executor.schedule(() -> bcast(Language.getMsg(player, Messages.RESTART_BROADCAST + "1sec")), 299L, TimeUnit.SECONDS);
        }
        executor.schedule(() -> Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), Restart::doRestart), 300L, TimeUnit.SECONDS);
    }

    public static void restart() {
        forced = true;
        doRestart();
    }

    private static void bcast(String time) {
        for (Player player : Bukkit.getOnlinePlayers())
            Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), () -> Utilities.bcast(Language.getMsg(player, Messages.RESTART_BROADCAST_SCHEDULED).replace("{time}", time)));
    }

    private static void doRestart() {
        FlexingNetworkPlugin.getInstance().getLogger().info("Restarting server...");
        FlexingNetwork.lobby().shutdown();
        Bukkit.getPluginManager().callEvent(new ServerRestartEvent(ServerRestartEvent.State.RESTART, forced));
        if (FlexingNetwork.lobby().getServerType() == ServerType.LOBBY) {
            for (Player player : Bukkit.getOnlinePlayers())
                player.kickPlayer(ChatColor.RED + "Перезагрузка сервера");
        } else if (forced) {
            for (Player player : Bukkit.getOnlinePlayers())
                FlexingNetwork.toLobby(player);
        } else {
            for (Player player : Bukkit.getOnlinePlayers())
                player.kickPlayer(ChatColor.RED + "Перезагрузка сервера");
        }
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), Bukkit::shutdown, 5L);
    }
}
