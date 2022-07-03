package com.flexingstudios.FlexingNetwork;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.conf.Configuration;
import com.flexingstudios.FlexingNetwork.api.event.PlayerLeaveEvent;
import com.flexingstudios.FlexingNetwork.api.event.PlayerUnloadEvent;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.flexingstudios.FlexingNetwork.impl.player.FLPlayer;

import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WorldProtect implements Listener {
    private final Map<String, PlayerInfo> players = new HashMap<>();
    private static final Pattern URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]{2,}\\.[a-z]{2,4})(/\\S*)?");
    private FlexingNetworkPlugin plugin;

    public WorldProtect(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
        Config config = new Config(plugin);

        String whatLang = "ru";
        File[] langs = new File(plugin.getDataFolder(), "/Languages").listFiles();
        if (langs != null) {
            for (File f : langs) {
                if (f.isFile()) {
                    if (f.getName().contains("messages_") && f.getName().contains(".yml")) {
                        String lang = f.getName().replace("messages_", "").replace(".yml", "");
                        if (lang.equalsIgnoreCase(config.language)) {
                            whatLang = f.getName().replace("messages_", "").replace(".yml", "");
                        }
                        if (Language.getLang(lang) == null) new Language(FlexingNetworkPlugin.getInstance(), lang);
                    }
                }
            }
        }

        Language def = Language.getLang(whatLang);

        if (def == null) throw new IllegalStateException("Could not found default language: " + whatLang);
        Language.setDefaultLanguage(def);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        FLPlayer networkPlayer = FLPlayer.get(player);
        plugin.mysql.addLoadPlayer(networkPlayer);
        ((CraftPlayer) player).addChannel("BungeeCord");
        ((CraftPlayer) player).addChannel("FlexingBungee");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        event.setQuitMessage(null);

        //Save preferred language
        if (Language.getLangByPlayer().containsKey(p.getUniqueId())) {
            final UUID u = p.getUniqueId();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                String iso = Language.getLangByPlayer().get(p.getUniqueId()).getIso();
                if (Language.isLanguageExist(iso)) {
                    MysqlPlayer.get(p).setLanguage(p.getUniqueId(), iso);
                }
                Language.getLangByPlayer().remove(p.getUniqueId());
            });
        }
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

        final String messageToPlayeer = checkMessage(event.getPlayer().getName(), event.getMessage());

        if (messageToPlayeer != null) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
                @Override
                public void run() {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', messageToPlayeer));
                }
            }, 1);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onChatHigh(AsyncPlayerChatEvent event) {
        NetworkPlayer player = FlexingNetwork.getPlayer(event.getPlayer());
        if ((FlexingNetwork.features()).CHANGE_CHAT.isEnabled()) {
            String msgColor = "&f";
            if (player.has(Rank.PLAYER)) {
                event.setMessage(event.getMessage());
            } else if (player.has(Rank.CHIKIBAMBONYLA)) {
                event.setMessage(Utilities.colored(event.getMessage()));
            }
            event.setFormat(Utilities.colored("&7«&r" + player.getRank().getDisplayName() + "&r&7»&r" + " %1$s&r&7: " + msgColor) + "%2$s");
        }
    }



    @EventHandler
    public void onUnload(PlayerUnloadEvent event) {
        FLPlayer player = (FLPlayer) event.getNetworkPlayer();
        plugin.coins.saveNow(player);
        plugin.expBuffer.saveNow(player);
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
        PlayerLeaveEvent event = new PlayerLeaveEvent(player, message, isKick);
        Bukkit.getPluginManager().callEvent(event);
        Bukkit.getPluginManager().callEvent(new PlayerUnloadEvent(player));
        FLPlayer.PLAYERS.remove(player.getName());
        FLPlayer.IDS.remove(Integer.valueOf(player.getId()));
        return event.getLeaveMessage();
    }

    public String checkMessage(final String name, String message){
        MessageInfo curr = new MessageInfo(message, System.currentTimeMillis());
        String returnMessage = null;

        PlayerInfo player;
        if (!players.containsKey(name)){
            player = new PlayerInfo();
            players.put(name, player);
        }else
            player = players.get(name);

        boolean checkSimilarity = message.length() > 4;
        int similar = 0;
        int fastMsgs = 0;

        while (!player.messages.isEmpty() && curr.time - player.messages.peekLast().time > 5 * 60_000)
            player.messages.removeLast();

        for (MessageInfo prev : player.messages) {
            long diff = curr.time - prev.time;
            if (checkSimilarity) {
                /*if (prev.message.length() > 4) {
                    double similarity = JaroWinkler.similarity(prev.message, curr.message);
                    double treshold = 1 - 0.02 / (0.2 + diff / (5 * 60000));
                    if (similarity >= treshold)
                        similar++;
                }*/
            }
            if (diff < 5000)
                fastMsgs++;
        }
        while (player.messages.size() > 7)
            player.messages.removeLast();
        player.messages.addFirst(curr);

        if (similar > 1 || similar == 1 && player.lastMsgIsSimilar) {
        } else if (similar == 1) {
            player.lastMsgIsSimilar = true;
            returnMessage = "boss";
        } else {
            player.lastMsgIsSimilar = false;
        }

        if (similar == 0 && fastMsgs >= 2) {
            if (!player.lastMsgIsTooFast) {
                player.lastMsgIsTooFast = true;
                returnMessage = "&fВы слишком часто отправляете сообщения в чат.";
            }
        } else {
            player.lastMsgIsTooFast = false;
        }

        return returnMessage;
    }

    public static class PlayerInfo{
        public Deque<MessageInfo> messages = new ArrayDeque<>();
        public boolean lastMsgIsSimilar;
        public boolean lastMsgIsTooFast;
        public int removeTask = -1;

        public void limitMessages(int max){
            while (messages.size() > max)
                messages.removeFirst();
        }
    }

    public static class MessageInfo{
        public String message;
        public long time;

        public MessageInfo(String message, long time) {
            this.message = message;
            this.time = time;
        }
    }
}
//VladiSlave