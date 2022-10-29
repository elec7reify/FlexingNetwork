package com.flexingstudios.FlexingNetwork.friends.listeners;

import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.friends.utils.Colour;
import com.flexingstudios.FlexingNetwork.friends.utils.FriendsManager;
import com.flexingstudios.FlexingNetwork.friends.utils.GUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;

public class GUIListener implements Listener {

/*    private final Inventory inv;

    public GUIListener() {
        inv = Bukkit.createInventory(this, 54, "123");
    }

    @Override
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {

    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent e) throws SQLException {
        Player p = (Player) e.getWhoClicked();

        if(e.getInventory().getTitle().equalsIgnoreCase(Colour.translate("&9&lFriends GUI"))) {
            e.setCancelled(true);
            if(e.getCurrentItem().getType() == Material.BOOK_AND_QUILL) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                p.openInventory(GUI.friendListGUIInventory(p));
            } else if(e.getCurrentItem().getType() == Material.GOLDEN_APPLE) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                p.openInventory(GUI.incomingRequestsGUIInventory(p));
            } else if(e.getCurrentItem().getType() == Material.APPLE) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                p.openInventory(GUI.outgoingRequestsGUIInventory(p));
            } else if(e.getCurrentItem().getType() == Material.BARRIER) {
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                p.closeInventory();
            } else {
            }
        } else if (e.getInventory().getTitle().equalsIgnoreCase(Colour.translate("&9&lFriends List"))) {
            e.setCancelled(true);
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Colour.translate("&9Right-Click to remove from friends list."));
            if (e.isRightClick()) {
                if(e.getCurrentItem().getItemMeta().getLore().equals(lore)) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName());
                    FriendsManager.removeFriendFromPlayer(p.getUniqueId().toString(), player.getUniqueId().toString());
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    p.openInventory(GUI.friendListGUIInventory(p));
                }
            } else if(e.isLeftClick()) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(Colour.translate("&c&lClose"))) {
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    p.closeInventory();
                }
            } else {
            }
        } else if (e.getInventory().getTitle().equalsIgnoreCase(Colour.translate("&9&lIncoming Friend Requests"))) {
            e.setCancelled(true);
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Colour.translate("&9Left-Click to accept."));
            lore.add(Colour.translate("&9Right-Click to deny."));
            if (e.isRightClick()) {
                if (e.getCurrentItem().getItemMeta().getLore().equals(lore)) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName());
                    FriendsManager.removeFriendRequest(player.getUniqueId().toString(), p.getUniqueId().toString());
                }
            } else if (e.isLeftClick()) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(Colour.translate("&c&lClose"))) {
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    p.closeInventory();
                } else if (e.getCurrentItem().getItemMeta().getLore().equals(lore)) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName());
                    FriendsManager.addFriendToPlayer(player.getUniqueId().toString(), p.getUniqueId().toString());
                    FriendsManager.addFriendToPlayer(p.getUniqueId().toString(), player.getUniqueId().toString());
                    FriendsManager.removeFriendRequest(p.getUniqueId().toString(), player.getUniqueId().toString());
                    FriendsManager.removeFriendRequest(player.getUniqueId().toString(), p.getUniqueId().toString());

                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    p.openInventory(GUI.friendListGUIInventory(p));
                }
            }
        } else if (e.getInventory().getTitle().equalsIgnoreCase(Colour.translate("&9&lOutgoing Friend Requests"))) {
            e.setCancelled(true);
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Colour.translate("&9Left-Click to delete."));
            if(e.isLeftClick()) {
                if(e.getCurrentItem().getItemMeta().getLore().equals(lore)) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(e.getCurrentItem().getItemMeta().getDisplayName());
                    FriendsManager.removeFriendRequest(p.getUniqueId().toString(), player.getUniqueId().toString());
                    FriendsManager.removeFriendRequest(player.getUniqueId().toString(), p.getUniqueId().toString());
                }
            } else if(e.isLeftClick()) {
                if(e.getCurrentItem().getItemMeta().getDisplayName().equals(Colour.translate("&c&lClose"))) {
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(Colour.translate("&9&lFriends List"))) {
            GUI.friendlistguislots = 0;
        } else if (event.getInventory().getTitle().equalsIgnoreCase(Colour.translate("&9&lIncoming Friend Requests"))) {
            GUI.irguislots = 0;
        } else if (event.getInventory().getTitle().equalsIgnoreCase(Colour.translate("&9&lOutgoing Friend Requests"))) {
            GUI.orguislots = 0;
        }
    }*/
}
