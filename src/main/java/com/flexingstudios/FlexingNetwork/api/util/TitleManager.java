package com.flexingstudios.FlexingNetwork.api.util;

import com.flexingstudios.FlexingNetwork.api.entity.NMSEntityUtils;
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

        NMSEntityUtils.getNMSPlayer(player).playerConnection.sendPacket(packetActionBar);
    }
}
