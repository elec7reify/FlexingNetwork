package com.flexingstudios.FlexingNetwork.api.event;

import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerUnloadEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private NetworkPlayer player;

    public PlayerUnloadEvent(NetworkPlayer player) {
        super(false);
        this.player = player;
    }

    public Player getPlayer() {
        return player.getBukkitPlayer();
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
