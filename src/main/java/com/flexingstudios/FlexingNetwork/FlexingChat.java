package com.flexingstudios.FlexingNetwork;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Iterator;

public class FlexingChat implements Listener {

        @EventHandler
        private void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        event.setCancelled(false);

        Iterator iter = event.getRecipients().iterator();
        while (iter.hasNext()) {
            Object o = (iter.next());
            Player p = o instanceof Player ? ((Player) o) : null;
            // p.spigot().sendMessage(main);

        }

            event.setFormat(":" + message);

    }
}
/*
    public Chat getChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        return rsp != null ? rsp.getProvider() : null;
    }

    public void setPrefix(UUID uuid, String s) {
        getChat().setPlayerPrefix(Bukkit.getPlayer(uuid), s);
    }
 */