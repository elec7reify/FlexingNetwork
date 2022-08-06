package com.flexingstudios.FlexingNetwork.BungeeListeners;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BungeeBridge implements PluginMessageListener {
    public static final Map<String, Integer> counts = new HashMap<>();
    public static int total = 0;
    public static final Map<String, Integer> servers = new ConcurrentHashMap<>();
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("FlexingBungee")) {
            String msg = new String(message, StandardCharsets.UTF_8);
            if (msg.startsWith("bcast"))
                Bukkit.broadcastMessage(Utilities.colored(msg.substring(6)));
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        switch (in.readUTF()) {
            case "PlayerCount":
                this.counts.put(in.readUTF(), in.readInt());
                break;
        }

        DataInputStream in2 = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subChannel = in2.readUTF();
            if (subChannel.equals("PlayerCount")) {
                String server = in2.readUTF();
                if (in2.available() > 0) {
                    int count = in2.readInt();
                    if (server.equals("ALL")) {
                        this.total = count;
                    } else {
                        this.servers.put(server, count);
                    }
                }
            }
        } catch (IOException ioException) {}
    }

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

    public static Collection<Integer> getPlayers(String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);
        (Bukkit.getOnlinePlayers().iterator().next()).sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", out.toByteArray());
        ByteArrayDataInput in = ByteStreams.newDataInput(out.toByteArray());
        return counts.values();
    }
}
