package com.flexingstudios.FlexingNetwork.friends.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class GUI {

    public static int friendlistguislots = 0;
    public static int orguislots = 0;
    public static int irguislots = 0;

    public static Inventory firstGUIInventory(Player player) {
        Inventory firstguiinv = Bukkit.getServer().createInventory(player, 36, ChatColor.translateAlternateColorCodes('&', "&9&lFriends GUI"));

        ItemStack currentFriends = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta currentFriendsMeta = currentFriends.getItemMeta();
        ArrayList<String> cfLore = new ArrayList<>();

        currentFriendsMeta.setDisplayName(Colour.translate("&6&lFriends List"));
        cfLore.add(Colour.translate("&9Click to view/manage your friends list."));
        currentFriendsMeta.setLore(cfLore);
        currentFriends.setItemMeta(currentFriendsMeta);

        firstguiinv.setItem(13, currentFriends);

        ItemStack incomingRequests = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta incomingRequestsMeta = incomingRequests.getItemMeta();
        ArrayList<String> irLore = new ArrayList<>();

        incomingRequestsMeta.setDisplayName(Colour.translate("&6&lIncoming Friend Requests"));
        irLore.add(Colour.translate("&9Click to view/manage your incoming friend requests."));
        incomingRequestsMeta.setLore(irLore);
        incomingRequests.setItemMeta(incomingRequestsMeta);

        ItemStack outgoingRequests = new ItemStack(Material.APPLE);
        ItemMeta outgoingRequestsMeta = incomingRequests.getItemMeta();
        ArrayList<String> orLore = new ArrayList<>();

        outgoingRequestsMeta.setDisplayName(Colour.translate("&6&lOutgoing Friend Requests"));
        orLore.add(Colour.translate("&9Click to view/manage your outgoing friend requests."));
        outgoingRequestsMeta.setLore(orLore);
        outgoingRequests.setItemMeta(outgoingRequestsMeta);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();

        closeMeta.setDisplayName(Colour.translate("&c&lClose"));
        close.setItemMeta(closeMeta);

        firstguiinv.setItem(13, currentFriends);
        firstguiinv.setItem(22, incomingRequests);
        firstguiinv.setItem(31, outgoingRequests);
        firstguiinv.setItem(35, close);

        return firstguiinv;
    }

    public static Inventory friendListGUIInventory(Player player) throws SQLException {
        Inventory friendlistguiinv = Bukkit.getServer().createInventory(player, 45, Colour.translate("&9&lFriends List"));
        for(String frienduuid : FriendsManager.getPlayerFriends(player.getUniqueId().toString())) {
            OfflinePlayer player1 = Bukkit.getServer().getOfflinePlayer(UUID.fromString(frienduuid));

            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            ArrayList<String> skullLore = new ArrayList<String>();

            meta.setDisplayName(Colour.translate(player1.getName()));
            skullLore.add(Colour.translate("&9Right-Click to remove from friends list."));
            meta.setLore(skullLore);
            meta.setOwningPlayer(player1);
            skull.setItemMeta(meta);

            friendlistguiinv.setItem(friendlistguislots, skull);
            friendlistguislots = friendlistguislots + 1;
        }

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();

        closeMeta.setDisplayName(Colour.translate("&c&lClose"));
        close.setItemMeta(closeMeta);

        friendlistguiinv.setItem(44, close);

        return friendlistguiinv;
    }

    public static Inventory incomingRequestsGUIInventory(Player player) throws SQLException {
        Inventory incominglistguiinv = Bukkit.getServer().createInventory(player, 36, Colour.translate("&9&lIncoming Friend Requests"));

        for(String frienduuid : FriendsManager.incomingRequests(player.getUniqueId().toString())) {
            OfflinePlayer player1 = Bukkit.getServer().getOfflinePlayer(UUID.fromString(frienduuid));

            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            ArrayList<String> skullLore = new ArrayList<>();

            meta.setDisplayName(Colour.translate("&6" + player1.getName()));
            skullLore.add(Colour.translate("&9Left-Click to accept."));
            skullLore.add(Colour.translate("&9Right-Click to deny."));
            meta.setLore(skullLore);
            meta.setOwningPlayer(player1);
            skull.setItemMeta(meta);

            incominglistguiinv.setItem(irguislots, skull);
            irguislots = irguislots + 1;
        }

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();

        closeMeta.setDisplayName(Colour.translate("&c&lClose"));
        close.setItemMeta(closeMeta);

        incominglistguiinv.setItem(35, close);

        return incominglistguiinv;
    }

    public static Inventory outgoingRequestsGUIInventory(Player player) throws SQLException {
        Inventory outgoinglistguiinv = Bukkit.getServer().createInventory(player, 36, Colour.translate("&9&lOutgoing Friend Requests"));

        for(String frienduuid : FriendsManager.outgoingRequests(player.getUniqueId().toString())) {
            OfflinePlayer player1 = Bukkit.getServer().getOfflinePlayer(UUID.fromString(frienduuid));

            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            ArrayList<String> skullLore = new ArrayList<String>();

            meta.setDisplayName(Colour.translate(player1.getName()));
            skullLore.add(Colour.translate("&9Left-Click to delete."));
            meta.setLore(skullLore);
            meta.setOwningPlayer(player1);
            skull.setItemMeta(meta);

            outgoinglistguiinv.setItem(orguislots, skull);
            orguislots = orguislots + 1;
        }

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();

        closeMeta.setDisplayName(Colour.translate("&c&lClose"));
        close.setItemMeta(closeMeta);

        outgoinglistguiinv.setItem(35, close);

        return outgoinglistguiinv;
    }
}


