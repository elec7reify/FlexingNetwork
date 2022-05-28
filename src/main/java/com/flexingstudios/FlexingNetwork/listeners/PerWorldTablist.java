package com.flexingstudios.FlexingNetwork.listeners;

import com.flexingstudios.FlexingNetwork.api.util.Spectators;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PerWorldTablist implements Listener {

    @EventHandler
    private void onPlayreChangeWorld(PlayerChangedWorldEvent event) {
        updatePlayer(event.getPlayer());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayer(event.getPlayer());
    }

    private void updatePlayer(Player player) {
        boolean spectator = isSpectator(player);
        World world = player.getWorld();

        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other != player)
                if (other.getWorld() != world) {
                    if (!spectator)
                        other.hidePlayer(player);
                    player.hidePlayer(other);
                } else {
                    if (!isSpectator(other))
                        player.showPlayer(other);
                    if (!spectator)
                        other.showPlayer(player);
                }
        }
    }


    private static boolean isSpectator(Player player) {
        return (Spectators.isEnabled() && Spectators.instance().contains(player));
    }
}
