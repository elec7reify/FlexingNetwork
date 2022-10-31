package com.flexingstudios.FlexingNetwork.friends.utils;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FriendsManager {

    public static void addFriendToPlayer(String sender, String targetName) {
        FlexingNetwork.mysql().query("INSERT INTO friends (user, target) VALUES ('" + sender + "', '" + targetName + "')");
    }

    public static void removeFriendFromPlayer(String user, String targetName) {
        FlexingNetwork.mysql().query("DELETE FROM friends WHERE user'" + user + "' AND target='" + targetName + "'");
    }

    public static ArrayList<String> getPlayerFriends(String sender) {
        ArrayList<String> playerFriends = new ArrayList<>();
        FlexingNetwork.mysql().select("SELECT * FROM friends WHERE user='" + sender + "'", rs -> {
            while (rs.next()) {
                playerFriends.add(rs.getString("target"));
            }
        });

        return playerFriends;
    }

    public static void addFriendRequest(String sender, String targetName) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String dateString = formatter.format(date);
        FlexingNetwork.mysql().query("INSERT INTO friends_requests (user, target) VALUES ('" + sender + "', '" + targetName + "')");
    }

    public static void removeFriendRequest(String sender, String targetName) {
        FlexingNetwork.mysql().query("DELETE FROM friends_requests WHERE user='" + sender + "' AND target='" + targetName + "'");
    }

//    public static ArrayList<String> incomingRequests(String player) throws SQLException {
//        ArrayList<String> requestsReceived = new ArrayList<>();
//        PreparedStatement st = FlexingNetworkPlugin.connection.prepareStatement("SELECT * FROM friends_requests WHERE target='" + player + "'");
//        ResultSet rs = st.executeQuery();
//        while (rs.next()) {
//            requestsReceived.add(rs.getString("user"));
//        }
//        rs.close();
//        st.close();
//        return requestsReceived;
//    }
//
//    public static ArrayList<String> outgoingRequests(String player) throws SQLException {
//        ArrayList<String> requestsSent = new ArrayList<>();
//        PreparedStatement st = FlexingNetworkPlugin.connection.prepareStatement("SELECT * FROM friends_requests WHERE user='" + player + "'");
//        ResultSet rs = st.executeQuery();
//        while (rs.next()) {
//            requestsSent.add(rs.getString("target"));
//        }
//        rs.close();
//        st.close();
//        return requestsSent;
//    }
}
