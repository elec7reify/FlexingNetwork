package com.flexingstudios.FlexingNetwork.api;

import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.mysql.MysqlThread;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;
import com.flexingstudios.FlexingNetwork.BungeeListeners.BungeeBridge;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.ZoneId;

public class FlexingNetwork {

    public static final ZoneId TZ_MOSCOW = ZoneId.of("Europe/Moscow");

    public static Lobby lobby() {
        return (FlexingNetworkPlugin.getInstance()).lobby;
    }

    public static Features features() {
        return Features.inst;
    }

    public static MysqlThread mysql() {
        return (FlexingNetworkPlugin.getInstance()).mysql;
    }

    public static Metrics metrics() {
        return (FlexingNetworkPlugin.getInstance()).metrics;
    }

    public static boolean hasRank(CommandSender who, Rank rank, boolean notify) {
        if (who instanceof ConsoleCommandSender)
            return true;
        if (notify)
            return getPlayer(who.getName()).hasAndNotify(rank);
        return getPlayer(who.getName()).has(rank);
    }

    public static boolean hasPermission(CommandSender who, Permission permission, boolean notify) {
        if (who instanceof ConsoleCommandSender)
            return true;
        if (notify)
            return getPlayer(who.getName()).hasAndNotify(permission);
        return getPlayer(who.getName()).has(permission);
    }

    public static NetworkPlayer getPlayer(String player) {
        return FLPlayer.get(player);
    }

    public static NetworkPlayer getPlayer(Player player) {
        return FLPlayer.get(player);
    }

    public static NetworkPlayer getPlayer(int userid) {
        return FLPlayer.IDS.get(Integer.valueOf(userid));
    }

    public static boolean isPlayerOnline(String player) {
        return FLPlayer.PLAYERS.containsKey(player);
    }

    public static boolean isPlayerOnline(Player player) {
        return FLPlayer.PLAYERS.containsKey(player.getName());
    }

    public static boolean isPlayerOnline(int userid) {
        return FLPlayer.IDS.containsKey(Integer.valueOf(userid));
    }

    public static void toLobby(Player... players) {
        for (Player player : players) {
            BungeeBridge.toLobby(player);
        }
    }

    public static void toServer(String server, Player... players) {
        for (Player player : players) {
            BungeeBridge.toServer(player, server);
        }
    }
}
