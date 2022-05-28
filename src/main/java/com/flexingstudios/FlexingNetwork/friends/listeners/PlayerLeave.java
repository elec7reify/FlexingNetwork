package com.flexingstudios.FlexingNetwork.friends.listeners;

import com.flexingstudios.FlexingNetwork.friends.utils.Colour;
import com.flexingstudios.FlexingNetwork.friends.utils.FriendsManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerLeave implements Listener {

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        for (String friend : FriendsManager.getPlayerFriends(event.getPlayer().getUniqueId().toString())) {
            OfflinePlayer player1 = Bukkit.getServer().getOfflinePlayer(UUID.fromString(friend));
            if(player1.isOnline()) {
                player1.getPlayer().sendMessage(Colour.translate("&7Your friend &6" + player.getName() + " &7is now &coffline&7!"));
            } else {
                return;
            }
        }
    }
}
