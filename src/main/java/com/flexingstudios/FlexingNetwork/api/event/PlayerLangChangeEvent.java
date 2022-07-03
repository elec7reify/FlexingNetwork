package com.flexingstudios.FlexingNetwork.api.event;

import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLangChangeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private Player player;
    private String oldLang, newLang;

    public PlayerLangChangeEvent(Player player, String oldLang, String newLang) {
        this.player = player;
        this.oldLang = oldLang;
        this.newLang = newLang;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public String getOldLang() {
        return oldLang;
    }

    public String getNewLang() {
        return newLang;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
