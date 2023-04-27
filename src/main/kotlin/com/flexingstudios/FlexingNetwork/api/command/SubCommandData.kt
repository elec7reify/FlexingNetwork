package com.flexingstudios.FlexingNetwork.api.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SubCommandData(
    val sender: CommandSender,
    val label: String,
    val sub: String,
    vararg val args: String
) {
    fun getPlayer(): Player = sender as Player

    fun hasArgs(): Boolean = args.isNotEmpty()
}