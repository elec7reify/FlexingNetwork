package com.flexingstudios.FlexingNetwork.api.util;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleManager {
    /**
     * Send a actionbar.
     *
     * @param player The player.
     * @param message The message that will appear.
     */
    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent chatActionBar = IChatBaseComponent.ChatSerializer.a(Utilities.colored("{\"text\": \"" + message + "\"}"));

        PacketPlayOutTitle packetActionBar = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, chatActionBar);

        getPlayerNMS(player).playerConnection.sendPacket(packetActionBar);
    }

    /**
     * Get EntityPlayer of the player.
     *
     * @param player the player.
     * @return A EntityPlayer of the player.
     */
    private static EntityPlayer getPlayerNMS(Player player){
        return ((CraftPlayer) player).getHandle();
    }
}
