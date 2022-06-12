package com.flexingstudios.FlexingNetwork;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.event.PlayerUnloadEvent;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Fireworks;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WorldProtect implements Listener {

    private static final Pattern URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?");
    private FlexingNetworkPlugin plugin;

    public WorldProtect(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore())
            Fireworks.launchRandom(player.getLocation());
        event.setJoinMessage(null);
        player.setGameMode(GameMode.ADVENTURE);
        FLPlayer networkPlayer = FLPlayer.get(player);
        this.plugin.mysql.addLoadPlayer(networkPlayer);
        ((CraftPlayer) player).addChannel("BungeeCord");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(fireLeaveEvent(FLPlayer.get(event.getPlayer()), event.getQuitMessage(), false));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatLowest(AsyncPlayerChatEvent event) {
        Rank rank = FlexingNetwork.getPlayer(event.getPlayer()).getRank();
        if (rank == Rank.PLAYER) {
            Matcher matcher = URL_PATTERN.matcher(event.getMessage());
            if (matcher.find()) {
                boolean replaced = false;
                StringBuffer sb = new StringBuffer();
                while (true) {
                    String host = matcher.group(2);
                    if (host == null || !host.endsWith("flexingworld.ru")) {
                        matcher.appendReplacement(sb, "<ссылка удалена>");
                        replaced = true;
                    }
                    if (!matcher.find()) {
                        if (replaced) {
                            matcher.appendTail(sb);
                            event.setMessage(sb.toString());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> Utilities.msg(event.getPlayer(), "&cЗапрещено отправлять сторонние ссылки в чат"));
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChatHigh(AsyncPlayerChatEvent event) {
        NetworkPlayer player = FlexingNetwork.getPlayer(event.getPlayer());
        if ((FlexingNetwork.features()).CHANGE_CHAT.isEnabled()) {
            String msgColor = "&f";
            if ((player.has(Rank.ADMIN) || player.has(Rank.SADMIN) || player.has(Rank.VADMIN))) {
                msgColor = "&c&l";
                event.setMessage(Utilities.colored(event.getMessage()));
            } else if (player.has(Rank.TEAM)) {
                msgColor = "&a";
                event.setMessage(Utilities.colored(event.getMessage()));
            }
            if (player.has(Rank.PLAYER)) {
                event.setMessage(event.getMessage());
            } else if (player.has(Rank.CHIKIBAMBONYLA)) {
                event.setMessage(Utilities.colored(event.getMessage()));
            }
            event.setFormat(Utilities.colored("&7«&r" + player.getRank().getDisplayName() + "&r&7»&r" + " &7%1$s&r&7: " + msgColor) + "%2$s");
        }
    }

    @EventHandler
    public void onUnload(PlayerUnloadEvent event) {
        FLPlayer player = (FLPlayer) event.getNetworkPlayer();
        plugin.coins.saveNow(player);
        if (player.has(Rank.ADMIN))
            plugin.vanishCommand.purge(player.getBukkitPlayer());
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().getName().equals(plugin.getName()))
            for (FLPlayer player : FLPlayer.PLAYERS.values())
                Bukkit.getPluginManager().callEvent(new PlayerUnloadEvent(player));
    }

    private String fireLeaveEvent(FLPlayer player, String message, boolean isKick) {
        Bukkit.getPluginManager().callEvent(new PlayerUnloadEvent(player));
        FLPlayer.PLAYERS.remove(player.getName());
        FLPlayer.IDS.remove(Integer.valueOf(player.getId()));
        return null;
    }
}
//VladiSlave