package com.flexingstudios.flexingnetwork.api.util;

import com.flexingstudios.flexingnetwork.api.entity.NMSEntityUtils;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import org.bukkit.entity.Player;

public class TitleManager {
    /**
     * Send a actionbar.
     *
     * @param player The player.
     * @param message The message that will appear.
     */
    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent chatActionBar = IChatBaseComponent.ChatSerializer.a(Utils.colored("{\"text\": \"" + message + "\"}"));

        PacketPlayOutTitle packetActionBar = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, chatActionBar);

        NMSEntityUtils.getNMSPlayer(player).playerConnection.sendPacket(packetActionBar);
    }
}
