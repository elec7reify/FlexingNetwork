package com.flexingstudios.FlexingNetwork.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemsClickEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ItemStack item;
    private final Action action;
    private final PlayerInteractEvent interactEvent;
    private boolean cancelled = false;

    public ItemsClickEvent(PlayerInteractEvent interactEvent) {
        super(interactEvent.getPlayer());
        this.interactEvent = interactEvent;
        this.item = interactEvent.getItem();
        this.action = interactEvent.getAction();
    }
    public ItemStack getItem() {
        return this.item;
    }

    public Action getAction() {
        return this.action;
    }

    public PlayerInteractEvent getInteractEvent() {
        return this.interactEvent;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
