package com.flexingstudios.FlexingNetwork.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemClickedEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ItemStack item;
    private final Action action;
    private final PlayerInteractEvent interactEvent;
    private boolean cancelled = false;

    public ItemClickedEvent(PlayerInteractEvent interactEvent) {
        super(interactEvent.getPlayer());
        this.interactEvent = interactEvent;
        item = interactEvent.getItem();
        action = interactEvent.getAction();
    }
    public ItemStack getItem() {
        return item;
    }

    public Action getAction() {
        return action;
    }

    public PlayerInteractEvent getInteractEvent() {
        return interactEvent;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
