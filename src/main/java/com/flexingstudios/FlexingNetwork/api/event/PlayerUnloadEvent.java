package com.flexingstudios.FlexingNetwork.api.event;

import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUnloadEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private NetworkPlayer player;

    public PlayerUnloadEvent(NetworkPlayer player) {
        super(false);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player.getBukkitPlayer();
    }

    public NetworkPlayer getNetworkPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
