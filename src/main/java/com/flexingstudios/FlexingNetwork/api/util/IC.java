package com.flexingstudios.FlexingNetwork.api.util;

import com.flexingstudios.FlexingNetwork.api.event.ItemClickedEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class IC {
    public static boolean isRightClick(PlayerInteractEvent event) {
        return (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
    }

    public static boolean isRightClick(ItemClickedEvent event) {
        return (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
    }

    public static boolean isLeftClick(PlayerInteractEvent event) {
        return (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK);
    }

    public static boolean isLeftClick(ItemClickedEvent event) {
        return (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK);
    }
}
