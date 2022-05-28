package com.flexingstudios.FlexingNetwork.BungeeListeners;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BungeeBridge implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {}

    public static void toLobby(Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF("lobby");
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", output.toByteArray());
    }

    public static void toServer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
            out.flush();
        } catch (IOException ioException) {}
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", b.toByteArray());
    }
}
