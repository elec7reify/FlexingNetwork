package com.flexingstudios.flexingnetwork.api

import com.flexingstudios.common.F
import com.flexingstudios.common.player.Permission
import com.flexingstudios.common.player.Rank
import com.flexingstudios.flexingnetwork.BungeeListeners.BungeeBridge
import com.flexingstudios.flexingnetwork.FlexingNetworkPlugin
import com.flexingstudios.flexingnetwork.api.Language.Messages
import com.flexingstudios.flexingnetwork.api.mysql.MysqlThread
import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer
import com.flexingstudios.flexingnetwork.api.util.Notifications
import com.flexingstudios.flexingnetwork.api.util.Utilities
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer
import kotlinx.datetime.Clock
import org.apache.commons.lang.StringEscapeUtils
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object FlexingNetwork {

    /**
     * @param username the minecraft username of the player.
     * @return proxy player
     */
    fun getPlayer(username: String): NetworkPlayer = FlexPlayer.get(username)

    /**
     * @param player player entity
     * @return proxy player
     */
    fun getPlayer(player: Player): NetworkPlayer = FlexPlayer.get(player)

    /**
     * @param userid of the player
     * @return proxy player
     */
    fun getPlayer(userid: Int): FlexPlayer? = FlexPlayer.IDS[userid]

    /**
     * Checks if the player is online
     * @param player player entity
     * @return Online / Offline player
     */
    fun isPlayerOnline(player: String): Boolean = FlexPlayer.PLAYERS.containsKey(player)

    /**
     * Checks if the player is online
     * @param player player username
     * @return Online / Offline player
     */
    fun isPlayerOnline(player: Player): Boolean = FlexPlayer.PLAYERS.containsKey(player.name)

    /**
     * Checks if the player is online
     * @param userid of the player
     * @return Online / Offline player
     */
    fun isPlayerOnline(userid: Int): Boolean = FlexPlayer.IDS.containsKey(userid)

    fun addCommandHelp(command: String, help: String) =
        FlexingNetworkPlugin.getInstance().help.addCommand(command, help)

    fun addCommandHelp(command: String, help: String, rank: Rank) =
        FlexingNetworkPlugin.getInstance().help.addCommand(command, help, rank)

    fun addCommandHelp(command: String, help: String, permission: Permission) =
        FlexingNetworkPlugin.getInstance().help.addCommand(command, help, permission)

    /**
     * @return MySQL Database connection
     */
    fun mysql(): MysqlThread = FlexingNetworkPlugin.getInstance().mysql

    fun lobby(): Lobby = FlexingNetworkPlugin.getInstance().lobby

    fun features(): Features = Features.inst

    fun metrics(): Metrics = FlexingNetworkPlugin.getInstance().metrics

    fun hasRank(who: CommandSender, rank: Rank, notify: Boolean): Boolean {
        if (who is ConsoleCommandSender) return true
        return if (notify) getPlayer(who.name).hasAndNotify(rank) else getPlayer(who.name).has(rank)
    }

    /**
     * Checks if the player has the specified permission
     * @param who player
     * @param permission permission
     * @param notify Whether the player needs to be notified that he does not have this right
     * @return Player's permission
     */
    fun hasPermission(who: CommandSender, permission: Permission, notify: Boolean): Boolean {
        if (who is ConsoleCommandSender) return true
        return if (notify) getPlayer(who.name).hasAndNotify(permission) else getPlayer(who.name).has(permission)
    }

    fun logAction(username: String, action: String) = logAction(username, action, null, null)

    fun logAction(username: String, action: String, target: String?) = logAction(username, action, target, null)

    fun logAction(username: String, action: String, target: String?, comment: String?) {
        var target = target
        var comment = comment
        target = if (target == null) "NULL" else "'" + StringEscapeUtils.escapeSql(target) + "'"
        comment = if (comment == null) "NULL" else "'" + StringEscapeUtils.escapeSql(comment) + "'"
        mysql()
            .query("INSERT INTO `user_log_actions` (`username`, `time`, `action`, `data`, `comment`) VALUES ('" + username + "', " + System.currentTimeMillis() / 1000L + ", '" + action + "', " + target + ", " + comment + ")")
    }

    fun ban(target: String, time: Long, reason: String, admin: String, shade: Boolean) {
        mysql().query(
            "INSERT INTO bans (username, banto, reason, banfrom, status, admin) VALUES('" +
                    StringEscapeUtils.escapeSql(target) + "', " +
                    (if (time == 0L) 0 else System.currentTimeMillis() + time) + ", '" +
                    StringEscapeUtils.escapeSql(reason) + "', " +
                    System.currentTimeMillis() + ", 1, '" +
                    StringEscapeUtils.escapeSql(admin) + "')"
        )

        BungeeBridge.kickPlayer(
            target, Utilities.colored(
                Notifications.banMessage()
                    .replace("{player}", target)
                    .replace("{admin}", if (shade) "&cТеневой админ" else "&3$admin")
                    .replace("{time}", F.formatSecondsShort(TimeUnit.MILLISECONDS.toSeconds(time).toInt()))
                    .replace("{reason}", reason)
                    .replace(
                        "{date}", SimpleDateFormat(Messages.DATE_FORMAT)
                            .format(Date(System.currentTimeMillis()))
                    )
            )
        )
        if (shade) logAction(admin, "shade.ban", target, reason) else logAction(admin, "ban", target, reason)
    }

    fun unban(player: String, admin: String, shadeUnban: Boolean) {
        if (shadeUnban) {
            mysql().update(
                "UPDATE bans SET status = 0 WHERE username = '$player' AND status = 1"
            ) { amount: Int ->
                if (amount > 0) {
                    for (players in Bukkit.getOnlinePlayers()) Utilities.msg(
                        players!!, "&cТеневой админ &fснял бан с игрока &3$player"
                    )
                    logAction(admin, "shade.unban", player)
                } else {
                    Utilities.msg(
                        Bukkit.getPlayer(admin),
                        "&cИгрок &f$player &cне забанен"
                    )
                }
            }
        }
        mysql().update(
            "UPDATE bans SET status = 0 WHERE username = '$player' AND status = 1"
        ) { amount: Int ->
            if (amount > 0) {
                for (players in Bukkit.getOnlinePlayers()) Utilities.msg(
                    players!!,
                    "&3$admin &fснял бан с игрока &3$player"
                )
                logAction(admin, "unban", player)
            } else {
                Utilities.msg(
                    Bukkit.getPlayer(admin),
                    "&cИгрок &f$player &cне забанен"
                )
            }
        }
    }

    fun kick(target: String, reason: String, admin: String, shade: Boolean) {
        BungeeBridge.kickPlayer(
            target, Utilities.colored(
                Notifications.kickMessage()
                    .replace("{player}", target)
                    .replace("{kicked}", if (shade) "&cТеневой админ" else "&3$admin")
                    .replace("{reason}", reason)
                    .replace(
                        "{date}", SimpleDateFormat(Messages.DATE_FORMAT)
                            .format(Date(System.currentTimeMillis()))
                    )
            )
        )
        if (shade) logAction(admin, "shade.kick", target, reason) else logAction(admin, "kick", target, reason)
    }

    fun restrict(target: String, time: Long, admin: String, shade: Boolean) {
        mysql().query(
            "INSERT INTO modrestrict (username, restrictto, restrictfrom, status, admin) VALUES('" +
                    StringEscapeUtils.escapeSql(target) + "', " +
                    (if (time == 0L) 0L else (Clock.System.now().epochSeconds + time)) + ", " +
                    Clock.System.now().epochSeconds + ", 1, '" +
                    StringEscapeUtils.escapeSql(admin) + "')"
        )
        getPlayer(target).restrict = true
        logAction(admin, "restrict", target, "Некорректный кик/бан/мут")
    }

    fun isDevelopment(): Boolean = FlexingNetworkPlugin.getInstance().config.development

    /**
     * Sends the player to the lobby
     * @param players entities
     */
    fun toLobby(vararg players: Player) {
        for (player in players) {
            BungeeBridge.toLobby(player)
        }
    }

    /**
     * Sends the player to the specified server
     * @param server server name
     * @param players entites
     */
    fun toServer(server: ServerType, vararg players: Player) {
        for (player in players)
            BungeeBridge.toServer(player, server)
    }

    /**
     * Gets the plugin that called the calling method of this method
     *
     * @return The plugin which called the method
     */
    fun getCallingPlugin(): Plugin? {
        val ex = Exception()
        return try {
            val clazz = Class.forName(ex.stackTrace[2].className)
            val plugin: Plugin = JavaPlugin.getProvidingPlugin(clazz)
            if (plugin.isEnabled) plugin else Bukkit.getPluginManager().getPlugin(plugin.name)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}