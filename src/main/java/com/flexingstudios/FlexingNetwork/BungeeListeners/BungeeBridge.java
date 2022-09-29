package com.flexingstudios.FlexingNetwork.BungeeListeners;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class BungeeBridge implements PluginMessageListener {
    private static final Map<String, Queue<CompletableFuture<?>>> callbackMap = new HashMap<>();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("FlexingBungee")) {
            String msg = new String(message, StandardCharsets.UTF_8);
            if (msg.startsWith("bcast"))
                Bukkit.broadcastMessage(Utilities.colored(msg.substring(6)));
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        Queue<CompletableFuture<?>> callbacks;
        String subchannel = in.readUTF();

        if (subchannel.equals("PlayerCount") || subchannel.equals("PlayerList") ||
                subchannel.equals("UUIDOther") || subchannel.equals("ServerIP")) {
            String identifier = in.readUTF(); // Server/player name
            callbacks = callbackMap.get(subchannel + "-" + identifier);

            if (callbacks == null || callbacks.isEmpty()) {
                return;
            }

            CompletableFuture<?> callback = callbacks.poll();

            switch (in.readUTF()) {
                case "PlayerCount":
                    ((CompletableFuture<Integer>) callback).complete(Integer.valueOf(in.readUTF()));
                    break;
            }
        }
    }

    public static void toLobby(Player player) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF("lobby");
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", output.toByteArray());
    }

    public static void toServer(Player player, String serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
            out.flush();
        } catch (IOException ioException) {}
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", b.toByteArray());
    }

    public static void toServerOther(String playerName, String server) {
        Player player = getFirstPlayer();
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ConnectOther");
        output.writeUTF(playerName);
        output.writeUTF(server);
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", output.toByteArray());
    }

    public void kickPlayer(String playerName, String kickMessage) {
        Player player = getFirstPlayer();
        CompletableFuture<InetSocketAddress> future = new CompletableFuture<>();

        synchronized (callbackMap) {
            callbackMap.compute("KickPlayer", this.computeQueueValue(future));
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("KickPlayer");
        output.writeUTF(playerName);
        output.writeUTF(kickMessage);
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", output.toByteArray());
    }

    public static CompletableFuture<Integer> getPlayerCount(String serverName) {
        Player player = getFirstPlayer();
        CompletableFuture<Integer> future = new CompletableFuture<>();

        synchronized (callbackMap) {
            callbackMap.compute("PlayerCount-" + serverName, computeQueueValue(future));
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("PlayerCount");
        output.writeUTF(serverName);
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", output.toByteArray());
        return future;
    }


    private static BiFunction<String, Queue<CompletableFuture<?>>, Queue<CompletableFuture<?>>> computeQueueValue(CompletableFuture<?> queueValue) {
        return (key, value) -> {
            if (value == null) value = new ArrayDeque<>();
            value.add(queueValue);

            return value;
        };
    }

    private static Player getFirstPlayer() {
        /**
         * if Bukkit Version < 1.7.10 then Bukkit.getOnlinePlayers() will return
         * a Player[] otherwise it'll return a Collection<? extends Player>.
         */
        Player firstPlayer = getFirstPlayer0(Bukkit.getOnlinePlayers());

        if (firstPlayer == null) {
            throw new IllegalArgumentException("Bungee Messaging Api requires at least one player to be online.");
        }

        return firstPlayer;
    }

    @SuppressWarnings("unused")
    private static Player getFirstPlayer0(Player[] playerArray) {
        return playerArray.length > 0 ? playerArray[0] : null;
    }

    private static Player getFirstPlayer0(Collection<? extends Player> playerCollection) {
        return Iterables.getFirst(playerCollection, null);
    }
}
