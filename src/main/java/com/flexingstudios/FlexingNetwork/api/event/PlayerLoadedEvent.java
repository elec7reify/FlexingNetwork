package com.flexingstudios.flexingnetwork.api.event;


import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerLoadedEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private NetworkPlayer player;

    public PlayerLoadedEvent(NetworkPlayer player) {
        super(player.getBukkitPlayer());
        this.player = player;
    }

    public NetworkPlayer getNetworkPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
