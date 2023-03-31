package com.flexingstudios.FlexingNetwork.tasks;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.ServerType;
import com.flexingstudios.FlexingNetwork.api.event.ServerRestartEvent;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Restart {
    private static final int RESTART_HOUR = 2;
    private static boolean forced = false;
    private static ScheduledFuture<?> future;

    public static void schedule() {
        LocalDateTime now = LocalDateTime.now(FlexingNetwork.TZ_MOSCOW);
        int hours = now.getHour();
        int waitHours = (hours >= RESTART_HOUR) ? (23 - hours + RESTART_HOUR) : (RESTART_HOUR - hours - 1);
        int waitMins = 59 - now.getMinute();
        int waitSeconds = 59 - now.getSecond();
        if (waitHours < 2)
            waitHours += 24;
        long waitTimeMillis = ((waitHours * 3600 + waitMins * 60 + waitSeconds - 300) * 1000);
        if (future != null)
            future.cancel(false);
        future = FlexingNetworkPlugin.getInstance().scheduledExecutorService.schedule(Restart::countdown, waitTimeMillis, TimeUnit.MILLISECONDS);
        Bukkit.getPluginManager().callEvent(new ServerRestartEvent(ServerRestartEvent.State.SCHEDULED, false));
    }

    public static void countdown() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }

        Bukkit.getPluginManager().callEvent(new ServerRestartEvent(ServerRestartEvent.State.COUNTDOWN, forced));
        Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), () -> Utilities.bcast(Messages.RESTART_BROADCAST));

        ScheduledExecutorService executor = FlexingNetworkPlugin.getInstance().scheduledExecutorService;
        executor.schedule(() -> broadcast("4 минуты"), 60L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("3 минуты"), 120L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("2 минуты"), 180L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("1 минуту"), 240L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("30 секунд"), 270L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("10 секунд"), 290L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("5 секунд"), 295L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("4 секунды"), 296L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("3 секунды"), 297L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("2 секунды"), 298L, TimeUnit.SECONDS);
        executor.schedule(() -> broadcast("1 секунду"), 299L, TimeUnit.SECONDS);
        executor.schedule(() -> Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), Restart::doRestart), 300L, TimeUnit.SECONDS);
    }

    public static void restart() {
        forced = true;
        doRestart();
    }

    /**
     * Notify the player how much time is left until the restart.
     * @param time time left before restart. String Format: "0 seconds"
     */
    private static void broadcast(String time) {
        Utilities.bcast(Messages.RESTART_BROADCAST_SCHEDULED.replace("%time%", time));
    }

    private static void doRestart() {
        FlexingNetworkPlugin.getInstance().getLogger().info("Restarting server...");
        FlexingNetwork.lobby().shutdown();
        Bukkit.getPluginManager().callEvent(new ServerRestartEvent(ServerRestartEvent.State.RESTART, forced));
        if (FlexingNetwork.lobby().getServerType() == ServerType.LOBBY) {
            for (Player player : Bukkit.getOnlinePlayers())
                player.kickPlayer(Utilities.colored(Messages.RESTART_KICK_REASON));
        } else if (forced) {
            for (Player player : Bukkit.getOnlinePlayers())
                FlexingNetwork.toLobby(player);
        } else {
            for (Player player : Bukkit.getOnlinePlayers())
                player.kickPlayer(Utilities.colored(Messages.RESTART_KICK_REASON));
        }
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), Bukkit::shutdown, 5L);
    }
}
