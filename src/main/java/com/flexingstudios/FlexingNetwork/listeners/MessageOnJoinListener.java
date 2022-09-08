package com.flexingstudios.FlexingNetwork.listeners;

import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.player.ArrowTrail;
import com.flexingstudios.FlexingNetwork.api.player.MessageOnJoin;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Particles;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MessageOnJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        NetworkPlayer player = FlexingNetwork.getPlayer(event.getPlayer());

        if (player.getMessageOnJoin() != null) {
            Message msg = new Message(event.getPlayer(), player.getMessageOnJoin());

        }
    }

    private static class Message implements Runnable {
        Player player;
        MessageOnJoin messages;
        int task = -1;
        int tick = 0;

        public Message(Player player, MessageOnJoin message) {
            this.player = player;
            this.messages = message;
        }

        @Override
        public void run() {
            switch (messages) {
                case TEST:
                    Utilities.msg(player, "123");
                    break;
            }
        }
    }
}
