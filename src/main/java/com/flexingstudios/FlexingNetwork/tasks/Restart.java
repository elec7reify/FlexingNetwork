package com.flexingstudios.FlexingNetwork.tasks;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.ServerType;
import com.flexingstudios.FlexingNetwork.api.event.ServerRestartEvent;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
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
            Utilities.bcast("&b------------------------------"); // 30
            Utilities.bcast("&fСервер будет перезагружен через &35 минут&f!");
            Utilities.bcast("&b------------------------------"); // 30
        });

        ScheduledExecutorService executor = (FlexingNetworkPlugin.getInstance()).scheduledExecutorService;
        executor.schedule(() -> bcast("3 минуты"), 120L, TimeUnit.SECONDS);
        executor.schedule(() -> bcast("1 минуту"), 240L, TimeUnit.SECONDS);
        executor.schedule(() -> bcast("30 секунд"), 270L, TimeUnit.SECONDS);
        executor.schedule(() -> bcast("10 секунд"), 290L, TimeUnit.SECONDS);
        executor.schedule(() -> bcast("5 секунд"), 295L, TimeUnit.SECONDS);
        executor.schedule(() -> bcast("4 секунды"), 296L, TimeUnit.SECONDS);
        executor.schedule(() -> bcast("3 секунды"), 297L, TimeUnit.SECONDS);
        executor.schedule(() -> bcast("2 секунды"), 298L, TimeUnit.SECONDS);
        executor.schedule(() -> bcast("1 секунду"), 299L, TimeUnit.SECONDS);
        executor.schedule(() -> Integer.valueOf(Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), Restart::doRestart)), 300L, TimeUnit.SECONDS);
    }

    public static void restart() {
        forced = true;
        doRestart();
    }

    private static void bcast(String time) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), () -> Utilities.bcast("&fСервер будет перезагружен через &3" + time + "&f!"));
    }

    private static void doRestart() {
        FlexingNetworkPlugin.getInstance().getLogger().info("Restarting server...");
        FlexingNetwork.lobby().shutdown();
        Bukkit.getPluginManager().callEvent(new ServerRestartEvent(ServerRestartEvent.State.RESTART, forced));
        if (FlexingNetwork.lobby().getServerType() == ServerType.LOBBY) {
            for (Player player : Bukkit.getOnlinePlayers())
                player.kickPlayer(ChatColor.RED + "Перезагрузка сервера");
        } else if (forced) {
            FlexingNetwork.toLobby((Player) Bukkit.getOnlinePlayers());
        } else {
            for (Player player : Bukkit.getOnlinePlayers())
                player.kickPlayer(ChatColor.RED + "Перезагрузка сервера");
        }
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), Bukkit::shutdown, 5L);
    }
}
