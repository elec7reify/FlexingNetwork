package com.flexingstudios.flexingnetwork.api.util

import net.md_5.bungee.api.ChatColor

enum class Attributes(
    val color: ChatColor,
    val tag: String,
) {
    NEW(ChatColor.RED, "НОВОЕ"),
    UPDATE(ChatColor.AQUA, "ОБНОВЛЕНИЕ"),
    UPDATED(ChatColor.GREEN, "ОБНОВЛЕНО"),
    DISCOUNT(ChatColor.GOLD, "СКИДКА"),
    RELEASED(ChatColor.YELLOW, "ВЫПУЩЕНА"),
    FEATURED(ChatColor.GREEN, "ПОПУЛЯРНОЕ"),
    ;

    fun addAttribute(color: ChatColor, tag: String): String {
        return Utilities.colored("$color $tag")
    }
}