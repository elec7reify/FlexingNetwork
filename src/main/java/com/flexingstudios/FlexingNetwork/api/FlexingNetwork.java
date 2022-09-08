package com.flexingstudios.FlexingNetwork.api;

import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.mysql.MysqlThread;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.BungeeListeners.BungeeBridge;
import com.google.gson.Gson;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.ZoneId;

public class FlexingNetwork {

    public static final ZoneId TZ_MOSCOW = ZoneId.of("Europe/Moscow");
    public static final Gson gson = new Gson();

    /**
     * Checks if developer mode is enabled in config
     * @return true / false
     */
    public static boolean isDevelopment() {
        return FlexingNetworkPlugin.getInstance().config.development;
    }

    public static Lobby lobby() {
        return FlexingNetworkPlugin.getInstance().lobby;
    }

    public static Features features() {
        return Features.inst;
    }

    public static MysqlThread mysql() {
        return FlexingNetworkPlugin.getInstance().mysql;
    }

    public static Metrics metrics() {
        return FlexingNetworkPlugin.getInstance().metrics;
    }

    public static boolean hasRank(CommandSender who, Rank rank, boolean notify) {
        if (who instanceof ConsoleCommandSender)
            return true;

        if (notify)
            return getPlayer(who.getName()).hasAndNotify(rank);

        return getPlayer(who.getName()).has(rank);
    }

    /**
     * Checks if the player has the specified permission
     * @param who player
     * @param permission permission
     * @param notify Whether the player needs to be notified that he does not have this right
     * @return Player's permission
     */
    public static boolean hasPermission(CommandSender who, Permission permission, boolean notify) {
        if (who instanceof ConsoleCommandSender)
            return true;

        if (notify)
            return getPlayer(who.getName()).hasAndNotify(permission);

        return getPlayer(who.getName()).has(permission);
    }

    /**
     * @param player the minecraft username of the player.
     * @return proxy player
     */
    public static NetworkPlayer getPlayer(String player) {
        return FLPlayer.get(player);
    }

    /**
     * @param player player entity
     * @return proxy player
     */
    public static NetworkPlayer getPlayer(Player player) {
        return FLPlayer.get(player);
    }

    /**
     * @param userid of the player
     * @return proxy player
     */
    public static NetworkPlayer getPlayer(int userid) {
        return FLPlayer.IDS.get(userid);
    }

    /**
     * Checks if the player is online
     *
     * @param player player entity
     * @return Online / Offline player
     */
    public static boolean isPlayerOnline(String player) {
        return FLPlayer.PLAYERS.containsKey(player);
    }

    /**
     * Checks if the player is online
     *
     * @param player player username
     * @return Online / Offline player
     */
    public static boolean isPlayerOnline(Player player) {
        return FLPlayer.PLAYERS.containsKey(player.getName());
    }

    /**
     * Checks if the player is online
     *
     * @param userid of the player
     * @return Online / Offline player
     */
    public static boolean isPlayerOnline(int userid) {
        return FLPlayer.IDS.containsKey(userid);
    }

    public static void addCommandHelp(String command, String help) {
        FlexingNetworkPlugin.getInstance().help.addCommand(command, help);
    }

    public static void addCommandHelp(String command, String help, Rank rank) {
        FlexingNetworkPlugin.getInstance().help.addCommand(command, help, rank);
    }

    public static void addCommandHelp(String command, String help, Permission permission) {
        FlexingNetworkPlugin.getInstance().help.addCommand(command, help, permission);
    }

    /**
     * Sends the player to the lobby
     * @param players entities
     */
    public static void toLobby(Player... players) {
        for (Player player : players) {
            BungeeBridge.toLobby(player);
        }
    }

    /**
     * Sends the player to the specified server
     * @param server server name
     * @param players entites
     */
    public static void toServer(String server, Player... players) {
        for (Player player : players) {
            BungeeBridge.toServer(player, server);
        }
    }
}
