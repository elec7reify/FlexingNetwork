package com.flexingstudios.FlexingNetwork.tasks

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork
import com.flexingstudios.FlexingNetwork.api.Language.Messages
import com.flexingstudios.FlexingNetwork.api.ServerType
import com.flexingstudios.FlexingNetwork.api.event.ServerRestartEvent
import com.flexingstudios.FlexingNetwork.api.utils.Utilities
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.bukkit.Bukkit
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class Restart {
    companion object {
        private val RESTART_HOUR: Int = 2
        private var forced: Boolean = false
        private lateinit var future: ScheduledFuture<Any>

        fun schedule() {
            val now: LocalDateTime = Instant.DISTANT_FUTURE.toLocalDateTime(TimeZone.of("Europe/Moscow"))
            val hours: Int = now.hour
            var waitHours: Int = if (hours >= RESTART_HOUR)
                23 - hours + RESTART_HOUR
            else
                RESTART_HOUR - hours - 1
            val waitMins: Int = 59 - now.minute
            val waitSeconds: Int = 59 - now.second
            if (waitHours < 2)
                waitHours += 24
            val waitTimeMillis: Long = (waitHours * 3600 + waitMins * 60 + waitSeconds - 300 * 1000).toLong()
            if (future != null)
                future.cancel(false)
            future = FlexingNetworkPlugin.getInstance().scheduledExecutorService.schedule(Restart::countdown, waitTimeMillis, TimeUnit.MILLISECONDS) as ScheduledFuture<Any>
            Bukkit.getPluginManager().callEvent(ServerRestartEvent(ServerRestartEvent.State.SCHEDULED, false))
        }

        fun countdown() {
            if (future != null) {
                future.cancel(true)
            }

            Bukkit.getPluginManager().callEvent(ServerRestartEvent(ServerRestartEvent.State.COUNTDOWN, forced))
            Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance()) {
                Utilities.broadcast(
                    Messages.RESTART_BROADCAST
                )
            }

            val executor = FlexingNetworkPlugin.getInstance().scheduledExecutorService
            executor.schedule({ broadcast("4 минуты") }, 60L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("3 минуты") }, 120L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("2 минуты") }, 180L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("1 минуту") }, 240L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("30 секунд") }, 270L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("10 секунд") }, 290L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("5 секунд") }, 295L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("4 секунды") }, 296L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("3 секунды") }, 297L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("2 секунды") }, 298L, TimeUnit.SECONDS)
            executor.schedule({ broadcast("1 секунду") }, 299L, TimeUnit.SECONDS)
            executor.schedule<Int>({
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    FlexingNetworkPlugin.getInstance()
                ) { doRestart() }
            }, 300L, TimeUnit.SECONDS)
        }

        fun restart() {
            forced = true;
            doRestart()
        }

        /**
         * Notify the player how much time is left until the restart.
         * @param time time left before restart. String Format: "0 seconds"
         */
        private fun broadcast(time: String) {
            Utilities.broadcast(Messages.RESTART_BROADCAST_SCHEDULED.replace("%time%", time));
        }

        private fun doRestart() {
            FlexingNetworkPlugin.getInstance().getLogger().info("Restarting server...")
            FlexingNetwork.lobby().shutdown()
            Bukkit.getPluginManager().callEvent(ServerRestartEvent(ServerRestartEvent.State.RESTART, forced))
            if (FlexingNetwork.lobby().getServerType() == ServerType.LOBBY) {
                for (player in Bukkit.getOnlinePlayers())
                    player.kickPlayer(Utilities.colored(Messages.RESTART_KICK_REASON))
            } else if (forced) {
                for (player in Bukkit.getOnlinePlayers())
                    FlexingNetwork.toLobby(player)
            } else {
                for (player in Bukkit.getOnlinePlayers())
                    player.kickPlayer(Utilities.colored(Messages.RESTART_KICK_REASON))
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), Bukkit::shutdown, 5L)
        }

    }
}