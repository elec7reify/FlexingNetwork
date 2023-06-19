package com.flexingstudios.flexingnetwork.api.menu

import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer
import com.flexingstudios.flexingnetwork.api.util.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface InvMenuKt {
    val inventory: Inventory

    fun addButton(
        slot: Int,
        item: ItemStack,
    )

    fun addButton(
        item: ItemStack,
        slot: Int,
    )

    fun onClick(
        item: ItemStack,
        player: NetworkPlayer,
        slot: Int,
        clickType: ClickType
    )
}