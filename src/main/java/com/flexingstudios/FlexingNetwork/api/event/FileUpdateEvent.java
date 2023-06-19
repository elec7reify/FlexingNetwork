package com.flexingstudios.flexingnetwork.api.event;

import com.flexingstudios.flexingnetwork.api.updater.WatchedDir;
import com.flexingstudios.flexingnetwork.api.updater.WatchedEntry;
import com.flexingstudios.flexingnetwork.api.updater.WatchedFile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FileUpdateEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private Action action = Action.IGNORE;
    private WatchedEntry old;
    private WatchedEntry curr;

    public FileUpdateEvent(WatchedEntry old, WatchedEntry curr) {
        this.old = old;
        this.curr = curr;
    }

    public boolean isDir() {
        return old instanceof WatchedDir;
    }

    public boolean isFile() {
        return old instanceof WatchedFile;
    }

    public WatchedEntry getOld() {
        return old;
    }

    public WatchedEntry getCurrent() {
        return curr;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public enum Action {
        RESTART, IGNORE;
    }
}
