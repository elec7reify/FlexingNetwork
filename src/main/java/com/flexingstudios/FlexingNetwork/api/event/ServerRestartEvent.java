package com.flexingstudios.FlexingNetwork.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerRestartEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private State state;
    private boolean forced;

    public ServerRestartEvent(State state, boolean forced) {
        this.state = state;
        this.forced = forced;
    }

    public boolean isForced() {
        return forced;
    }

    public State getState() {
        return state;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public enum State {
        SCHEDULED, COUNTDOWN, RESTART;
    }
}

