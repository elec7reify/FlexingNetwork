package com.flexingstudios.FlexingNetwork.api.event;


import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerLoadedEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    private NetworkPlayer player;

    public PlayerLoadedEvent(NetworkPlayer player) {
        super(player.getBukkitPlayer());
        this.player = player;
    }

    public NetworkPlayer getNetworkPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
