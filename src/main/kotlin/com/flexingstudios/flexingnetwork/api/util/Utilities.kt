package com.flexingstudios.flexingnetwork.api.util

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.NotNull

class Utilities {
    companion object {
        fun colored(message: String): String {
            return ChatColor.translateAlternateColorCodes('&', message)
        }

        fun colored(vararg lines: String): Array<out String> {
            for (i in lines.indices) {
                lines[i] = colored(lines[i]) as Nothing
            }
            return lines
        }

        fun colored(vararg lines: List<String>): Array<out List<String>> {
            val it: Iterator<List<String>> = lines.iterator()
            while (it.hasNext())
                it.apply { colored(it.next()) }
            return lines
        }

        fun msg(@NotNull cs: CommandSender, vararg message: String) {
            for (msg: String in message)
                cs.sendMessage(colored(msg))
        }

        fun msg(@NotNull cs: CommandSender, messages: List<String>) {
            for (message in messages)
                cs.sendMessage(colored(message))
        }

        fun broadcast(message: String) {
            Bukkit.broadcastMessage(colored(message))
        }

        fun broadcast(strings: List<String>) {
            for (player in Bukkit.getOnlinePlayers())
                msg(player, strings)
        }
    }
}