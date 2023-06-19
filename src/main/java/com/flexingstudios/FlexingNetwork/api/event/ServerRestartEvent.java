package com.flexingstudios.flexingnetwork.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerRestartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum State {
        SCHEDULED, COUNTDOWN, RESTART;
    }
}

