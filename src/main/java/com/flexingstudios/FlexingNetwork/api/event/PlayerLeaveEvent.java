package com.flexingstudios.FlexingNetwork.api.event;

import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final boolean isKick;
    private final NetworkPlayer player;
    private String message;

    public PlayerLeaveEvent(NetworkPlayer player, String message, boolean isKick) {
        this.player = player;
        this.isKick = isKick;
        this.message = message;
    }

    public Player getPlayer() {
        return player.getBukkitPlayer();
    }

    public NetworkPlayer getNetworkPlayer() {
        return player;
    }

    public String getLeaveMessage() {
        return message;
    }

    public void setLeaveMessage(String message) {
        this.message = message;
    }

    public boolean isKick() {
        return isKick;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
