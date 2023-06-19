package com.flexingstudios.flexingnetwork.BungeeListeners;

import com.flexingstudios.flexingnetwork.FlexingNetworkPlugin;
import com.flexingstudios.flexingnetwork.api.ServerType;
import com.flexingstudios.flexingnetwork.api.util.Utils;
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
    private Map<String, ForwardConsumer> forwardListeners;
    private ForwardConsumer globalForwardListener;

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("FlexingBungee") || channel.equals("BungeeCord")) {
            String msg = new String(message, StandardCharsets.UTF_8);
            if (msg.startsWith("bcast"))
                Bukkit.broadcastMessage(Utils.colored(msg.substring(6)));
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

            try {
                switch (subchannel) {
                    case "PlayerCount":
                        ((CompletableFuture<Integer>) callback).complete(Integer.valueOf(in.readInt()));
                        break;

                    case "PlayerList":
                        ((CompletableFuture<List<String>>) callback).complete(Arrays.asList(in.readUTF().split(", ")));
                        break;

//                    case "UUIDOther":
//                        ((CompletableFuture<String>) callback).complete(input.readUTF());
//                        break;

//                    case "ServerIP": {
//                        String ip = input.readUTF();
//                        int port = input.readUnsignedShort();
//                        ((CompletableFuture<InetSocketAddress>) callback).complete(new InetSocketAddress(ip, port));
//                        break;
//                    }
                }
            } catch(Exception ex) {
                callback.completeExceptionally(ex);
            }

            return;
        }

        callbacks = callbackMap.get(subchannel);

        if (callbacks.isEmpty()) {
            return;
        }

        final CompletableFuture<?> callback = callbacks.poll();

        try {
            switch (subchannel) {
                case "GetServers":
                    ((CompletableFuture<List<String>>) callback).complete(Arrays.asList(in.readUTF().split(", ")));
                    break;

                case "GetServer":
                case "UUID":
                    ((CompletableFuture<String>) callback).complete(in.readUTF());
                    break;
                case "IP": {
                    String ip = in.readUTF();
                    int port = in.readInt();
                    ((CompletableFuture<InetSocketAddress>) callback).complete(new InetSocketAddress(ip, port));
                    break;
                }

                default:
                    break;
            }
        } catch(Exception ex) {
            callback.completeExceptionally(ex);
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

    public static void toServer(Player player, ServerType serverName) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName.getId());
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

    /**
     * Send a message (as in, a chat message) to the specified player.
     *
     * @param playerName the name of the player to send the chat message.
     * @param message the message to send to the player.
     * @throws IllegalArgumentException if there is no players online.
     */
    public static void sendMessage(String playerName, String message) {
        Player player = getFirstPlayer();
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Message");
        output.writeUTF(playerName);
        output.writeUTF(message);
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", output.toByteArray());
    }

    public static void kickPlayer(String playerName, String kickMessage) {
        Player player = getFirstPlayer();
        CompletableFuture<InetSocketAddress> future = new CompletableFuture<>();

        synchronized (callbackMap) {
            callbackMap.compute("KickPlayer", computeQueueValue(future));
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

//    private static Player getFirstPlayer() {
//        /**
//         * if Bukkit Version < 1.7.10 then Bukkit.getOnlinePlayers() will return
//         * a Player[] otherwise it'll return a Collection<? extends Player>.
//         */
//        Player firstPlayer = getFirstPlayer0(Bukkit.getOnlinePlayers());
//
//        if (firstPlayer == null) {
//            throw new IllegalArgumentException("Bungee Messaging Api requires at least one player to be online.");
//        }
//
//        return firstPlayer;
//    }
//
//    @SuppressWarnings("unused")
//    private static Player getFirstPlayer0(Player[] playerArray) {
//        return playerArray.length > 0 ? playerArray[0] : null;
//    }
//
//    private static Player getFirstPlayer0(Collection<? extends Player> playerCollection) {
//        return Iterables.getFirst(playerCollection, null);
//    }

    private static Player getFirstPlayer() {
        return Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
    }

    /**
     * Send a custom plugin message to said server. This is one of the most useful channels ever.
     * <b>Remember, the sending and receiving server(s) need to have a player online.</b>
     *
     * @param server the name of the server to send to,
     *        ALL to send to every server (except the one sending the plugin message),
     *        or ONLINE to send to every server that's online (except the one sending the plugin message).
     *
     * @param channelName Subchannel for plugin usage.
     * @param data data to send.
     * @throws IllegalArgumentException if there is no players online.
     */
    public static void forward(String server, String channelName, byte[] data) {
        Player player = getFirstPlayer();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Forward");
        output.writeUTF(server);
        output.writeUTF(channelName);
        output.writeShort(data.length);
        output.write(data);
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", output.toByteArray());
    }

    /**
     * Send a custom plugin message to specific player.
     *
     * @param playerName the name of the player to send to.
     * @param channelName Subchannel for plugin usage.
     * @param data data to send.
     * @throws IllegalArgumentException if there is no players online.
     */
    public static void forwardToPlayer(String playerName, String channelName, byte[] data) {
        Player player = getFirstPlayer();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ForwardToPlayer");
        output.writeUTF(playerName);
        output.writeUTF(channelName);
        output.writeShort(data.length);
        output.write(data);
        player.sendPluginMessage(FlexingNetworkPlugin.getInstance(), "BungeeCord", output.toByteArray());
    }

    @FunctionalInterface
    public interface ForwardConsumer {
        void accept(String channel, Player player, byte[] data);
    }
}
