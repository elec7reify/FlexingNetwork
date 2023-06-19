package com.flexingstudios.flexingnetwork.api.menu

import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer
import com.flexingstudios.flexingnetwork.api.util.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

 class InvMenuImpl(override val inventory: Inventory) : InvMenuKt {

    override fun addButton(slot: Int, item: ItemStack) {
        TODO("Not yet implemented")
    }

    override fun addButton(item: ItemStack, slot: Int) {
        TODO("Not yet implemented")
    }

    override fun onClick(item: ItemStack, player: NetworkPlayer, slot: Int, clickType: ClickType) {
        TODO("Not yet implemented")
    }

}