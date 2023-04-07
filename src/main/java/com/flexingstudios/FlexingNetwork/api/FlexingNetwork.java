package com.flexingstudios.FlexingNetwork.api;

import com.flexingstudios.Common.F;
import com.flexingstudios.Common.player.Permission;
import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.mysql.MysqlThread;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.T;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FlexPlayer;
import com.flexingstudios.FlexingNetwork.BungeeListeners.BungeeBridge;
import com.google.gson.Gson;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FlexingNetwork {

    public static final ZoneId TZ_MOSCOW = ZoneId.of("Europe/Moscow");
    public static final Gson gson = new Gson();

    /**
     * Checks if developer mode is enabled in config
     * @return true if developer mode is enabled
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
        if (who instanceof ConsoleCommandSender) return true;

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
        if (who instanceof ConsoleCommandSender) return true;

        if (notify)
            return getPlayer(who.getName()).hasAndNotify(permission);

        return getPlayer(who.getName()).has(permission);
    }

    public static void restrict(String target, long time, String admin, boolean shadeRestrict) {
        Player player = Bukkit.getPlayer(target);
        FlexPlayer flPlayer = FlexPlayer.get(target);

        if (player != null) {
            if (shadeRestrict) {
                FlexingNetwork.mysql().query("INSERT INTO modrestrict (username, restrictto, restrictfrom, status, admin) VALUES('" +
                        StringEscapeUtils.escapeSql(target) + "', " +
                        (time == 0 ? 0 : (System.currentTimeMillis() + time)) + ", " +
                        System.currentTimeMillis() + ", 1, '" +
                        StringEscapeUtils.escapeSql(admin) + "')");
                flPlayer.setRestrict(true);

                logAction(admin, "restrict", target, "Некорректный кик/бан/мут");
            }
            FlexingNetwork.mysql().query("INSERT INTO modrestrict (username, restrictto, restrictfrom, status, admin) VALUES('" +
                    StringEscapeUtils.escapeSql(target) + "', " +
                    (time == 0 ? 0 : (System.currentTimeMillis() + time)) + ", " +
                    System.currentTimeMillis() + ", 1, '" +
                    StringEscapeUtils.escapeSql(admin) + "')");
            flPlayer.setRestrict(true);

            logAction(admin, "restrict", target, "Некорректный кик/бан/мут");
        } else {
            if (shadeRestrict) {
                FlexingNetwork.mysql().query("INSERT INTO modrestrict (username, restrictto, restrictfrom, status, admin) VALUES('" +
                        StringEscapeUtils.escapeSql(target) + "', " +
                        (time == 0 ? 0 : (System.currentTimeMillis() + time)) + ", " +
                        System.currentTimeMillis() + ", 1, '" +
                        StringEscapeUtils.escapeSql(admin) + "')");

                logAction(admin, "restrict", target, "Некорректный кик/бан/мут");
            }
            FlexingNetwork.mysql().query("INSERT INTO modrestrict (username, restrictto, restrictfrom, status, admin) VALUES('" +
                    StringEscapeUtils.escapeSql(target) + "', " +
                    (time == 0 ? 0 : (System.currentTimeMillis() + time)) + ", " +
                    System.currentTimeMillis() + ", 1, '" +
                    StringEscapeUtils.escapeSql(admin) + "')");

            logAction(admin, "restrict", target, "Некорректный кик/бан/мут");
        }
    }

    public static void ban(String target, long time, String reason, String admin, boolean shadeBan) {
        Player player = Bukkit.getPlayer(target);

        if (player != null) {
            if (shadeBan) {
                FlexingNetwork.mysql().query("INSERT INTO bans (username, banto, reason, banfrom, status, admin) VALUES('" +
                        StringEscapeUtils.escapeSql(target) + "', " +
                        (time == 0 ? 0 : (System.currentTimeMillis() + time)) + ", '" +
                        StringEscapeUtils.escapeSql(reason) + "', " +
                        System.currentTimeMillis() + ", 1, '" +
                        StringEscapeUtils.escapeSql(admin) + "')");

                player.kickPlayer(Utilities.colored(T.BanMessage(player)
                        .replace("{player}", target)
                        .replace("{admin}", "&cТеневой админ")
                        .replace("{time}", F.formatSecondsShort((int) TimeUnit.MILLISECONDS.toSeconds(time)))
                        .replace("{reason}", reason)
                        .replace("{date}", new SimpleDateFormat(Messages.DATE_FORMAT)
                                .format(new Date(System.currentTimeMillis())))));
                logAction(admin, "shade.ban", target, reason);
            } else {
                FlexingNetwork.mysql().query("INSERT INTO bans (username, banto, reason, banfrom, status, admin) VALUES('" +
                        StringEscapeUtils.escapeSql(target) + "', " +
                        (time == 0 ? 0 : (System.currentTimeMillis() + time)) + ", '" +
                        StringEscapeUtils.escapeSql(reason) + "', " +
                        System.currentTimeMillis() + ", 1, '" +
                        StringEscapeUtils.escapeSql(admin) + "')");

                player.kickPlayer(Utilities.colored(T.BanMessage(player)
                        .replace("{player}", target)
                        .replace("{admin}", "&3" + admin)
                        .replace("{time}", F.formatSecondsShort((int) TimeUnit.MILLISECONDS.toSeconds(time)))
                        .replace("{reason}", reason)
                        .replace("{date}", new SimpleDateFormat(Messages.DATE_FORMAT)
                                .format(new Date(System.currentTimeMillis())))));
                logAction(admin, "ban", target, reason);
            }
        } else {
            if (shadeBan) {
                FlexingNetwork.mysql().query("INSERT INTO bans (username, banto, reason, banfrom, status, admin) VALUES('" +
                        StringEscapeUtils.escapeSql(target) + "', " +
                        (time == 0 ? 0 : (System.currentTimeMillis() + time)) + ", '" +
                        StringEscapeUtils.escapeSql(reason) + "', " +
                        System.currentTimeMillis() + ", 1, '" +
                        StringEscapeUtils.escapeSql(admin) + "')");

                BungeeBridge.kickPlayer(target, Utilities.colored(T.BanMessage(player)
                        .replace("{player}", target)
                        .replace("{admin}", "&cnТеневой админ")
                        .replace("{time}", F.formatSecondsShort((int) TimeUnit.MILLISECONDS.toSeconds(time)))
                        .replace("{reason}", reason)
                        .replace("{date}", new SimpleDateFormat(Messages.DATE_FORMAT)
                                .format(new Date(System.currentTimeMillis())))));
                logAction(admin, "shade.ban", target, reason);
            } else {
                FlexingNetwork.mysql().query("INSERT INTO bans (username, banto, reason, banfrom, status, admin) VALUES('" +
                        StringEscapeUtils.escapeSql(target) + "', " +
                        (time == 0 ? 0 : (System.currentTimeMillis() + time)) + ", '" +
                        StringEscapeUtils.escapeSql(reason) + "', " +
                        System.currentTimeMillis() + ", 1, '" +
                        StringEscapeUtils.escapeSql(admin) + "')");

                BungeeBridge.kickPlayer(target, Utilities.colored(T.BanMessage(player)
                        .replace("{player}", target)
                        .replace("{admin}", "&3" + admin)
                        .replace("{time}", F.formatSecondsShort((int) TimeUnit.MILLISECONDS.toSeconds(time)))
                        .replace("{reason}", reason)
                        .replace("{date}", new SimpleDateFormat(Messages.DATE_FORMAT)
                                .format(new Date(System.currentTimeMillis())))));
                logAction(admin, "ban", target, reason);
            }
        }
    }

    public static void unban(String player, String admin, boolean shadeUnban) {
        if (shadeUnban) {
            mysql().update("UPDATE bans SET status = 0 WHERE username = '" + player + "' AND status = 1", amount -> {
                if (amount > 0) {
                    for (Player players : Bukkit.getOnlinePlayers())
                        Utilities.msg(players, "&cТеневой админ &fснял бан с игрока &3" + player);
                    logAction(admin, "shade.unban", player);
                } else {
                    Utilities.msg(Bukkit.getPlayer(admin), "&cИгрок &f" + player + " &cне забанен");
                }
            });
        }

        mysql().update("UPDATE bans SET status = 0 WHERE username = '" + player + "' AND status = 1", amount -> {
            if (amount > 0) {
                for (Player players : Bukkit.getOnlinePlayers())
                    Utilities.msg(players, "&3" + admin + " &fснял бан с игрока &3" + player);
                logAction(admin, "unban", player);
            } else {
                Utilities.msg(Bukkit.getPlayer(admin), "&cИгрок &f" + player + " &cне забанен");
            }
        });
    }

    public static void kick(String target, String reason, String kicked, boolean shadeKick) {
        Player player = Bukkit.getPlayer(target);
        if (player != null) {
            if (shadeKick) {
                player.kickPlayer(Utilities.colored(T.formattedKickMessage(player)
                        .replace("{player}", target)
                        .replace("{kicked}", "&cТеневой админ")
                        .replace("{reason}", reason)
                        .replace("{date}", new SimpleDateFormat(Messages.DATE_FORMAT)
                                .format(new Date(System.currentTimeMillis())))));
            logAction(kicked, "shade.kick", target, reason);
            } else {
            player.kickPlayer(Utilities.colored(T.formattedKickMessage(player)
                    .replace("{player}", target)
                    .replace("{kicked}", "&cТеневой админ")
                    .replace("{reason}", reason)
                    .replace("{date}", new SimpleDateFormat(Messages.DATE_FORMAT)
                            .format(new Date(System.currentTimeMillis())))));
            logAction(kicked, "kick", target, reason);
            }
        } else {
            if (shadeKick) {
                BungeeBridge.kickPlayer(target, Utilities.colored(T.formattedKickMessage(player)
                        .replace("{player}", target)
                        .replace("{kicked}", "&cТеневой админ")
                        .replace("{reason}", reason)
                        .replace("{date}", new SimpleDateFormat(Messages.DATE_FORMAT)
                                .format(new Date(System.currentTimeMillis())))));
                logAction(kicked, "shade.kick", target, reason);
            } else {
                BungeeBridge.kickPlayer(target, Utilities.colored(T.formattedKickMessage(player)
                        .replace("{player}", target)
                        .replace("{kicked}", "&3" + kicked)
                        .replace("{reason}", reason)
                        .replace("{date}", new SimpleDateFormat(Messages.DATE_FORMAT)
                                .format(new Date(System.currentTimeMillis())))));
                logAction(kicked, "kick", target, reason);
            }
        }
    }

    public static void logAction(String username, String action) {
        logAction(username, action, null, null);
    }

    public static void logAction(String username, String action, String target) {
        logAction(username, action, target, null);
    }

    public static void logAction(String username, String action, String target, String comment) {
        if (action == null || username == null) return;
        target = target == null ? "NULL" : "'" + StringEscapeUtils.escapeSql(target) + "'";
        comment = comment == null ? "NULL" : "'" + StringEscapeUtils.escapeSql(comment) + "'";
        mysql().query("INSERT INTO `user_log_actions` (`username`, `time`, `action`, `data`, `comment`) VALUES ('" + username + "', " + System.currentTimeMillis() / 1000L + ", '" + action + "', " + target + ", " + comment + ")");
    }

    /**
     * @param player the minecraft username of the player.
     * @return proxy player
     */
    public static NetworkPlayer getPlayer(String player) {
        return FlexPlayer.get(player);
    }

    /**
     * @param player player entity
     * @return proxy player
     */
    public static NetworkPlayer getPlayer(Player player) {
        return FlexPlayer.get(player);
    }

    /**
     * @param userid of the player
     * @return proxy player
     */
    public static NetworkPlayer getPlayer(int userid) {
        return FlexPlayer.IDS.get(userid);
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

    /**
     * Checks if the player is online
     *
     * @param player player entity
     * @return Online / Offline player
     */
    public static boolean isPlayerOnline(String player) {
        return FlexPlayer.PLAYERS.containsKey(player);
    }

    /**
     * Checks if the player is online
     *
     * @param player player username
     * @return Online / Offline player
     */
    public static boolean isPlayerOnline(Player player) {
        return FlexPlayer.PLAYERS.containsKey(player.getName());
    }

    /**
     * Checks if the player is online
     *
     * @param userid of the player
     * @return Online / Offline player
     */
    public static boolean isPlayerOnline(int userid) {
        return FlexPlayer.IDS.containsKey(userid);
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

    /**
     * Gets the plugin that called the calling method of this method
     *
     * @return The plugin which called the method
     */
    public static Plugin getCallingPlugin() {
        Exception ex = new Exception();
        try {
            Class<?> clazz = Class.forName(ex.getStackTrace()[2].getClassName());
            Plugin plugin = JavaPlugin.getProvidingPlugin(clazz);
            return plugin.isEnabled() ? plugin : Bukkit.getPluginManager().getPlugin(plugin.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
