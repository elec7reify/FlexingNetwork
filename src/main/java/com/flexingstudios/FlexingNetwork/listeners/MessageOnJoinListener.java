package com.flexingstudios.FlexingNetwork.listeners;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
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

import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageOnJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        NetworkPlayer nplayer = FlexingNetwork.getPlayer(event.getPlayer());

        Logger.getGlobal().log(Level.INFO, nplayer.getMessageOnJoin().getText());
        Utilities.bcast(nplayer.getMessageOnJoin().getText());
        if (nplayer.getMessageOnJoin() != null) {
            //Message msg = new Message(event.getPlayer(), nplayer.getMessageOnJoin());
            Logger.getGlobal().log(Level.INFO, nplayer.getMessageOnJoin().getText());
            Utilities.bcast(nplayer.getMessageOnJoin().getText());
        }
    }
//
//    private static class Message implements Runnable {
//        Player player;
//        MessageOnJoin messages;
//
//        public Message(Player player, MessageOnJoin message) {
//            this.player = player;
//            this.messages = message;
//        }
//
//        @Override
//        public void run() {
//            switch (messages) {
//                case TEST:
//                    for (Player player : Bukkit.getOnlinePlayers())
//                        Utilities.msg(player, "123");
//                    break;
//            }
//        }
//    }
}
