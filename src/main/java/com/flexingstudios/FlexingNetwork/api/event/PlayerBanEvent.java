package com.flexingstudios.FlexingNetwork.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerBanEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private String banMessage;

    public PlayerBanEvent(Player playerBanned, String banMessage) {
        super(playerBanned);
        this.banMessage = banMessage;
    }

    /**
     * Gets the ban message send to all online players
     *
     * @return string ban reason
     */
    public String getBanMessage() {
        return banMessage;
    }

    /**
     * Sets the ban message send to all online players
     *
     * @param banMessage ban message
     */
    public void setBanMessage(String banMessage) {
        this.banMessage = banMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
