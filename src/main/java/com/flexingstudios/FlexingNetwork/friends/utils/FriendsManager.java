package com.flexingstudios.FlexingNetwork.friends.utils;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FriendsManager {

    public static void addFriendToPlayer(String playera, String playerb) throws SQLException {
        FlexingNetworkPlugin.connection.createStatement().execute("INSERT INTO friends (playera, playerb) VALUES ('" + playera + "', '" + playerb + "')");
    }

    public static void removeFriendFromPlayer(String playera, String playerb) throws SQLException {
        FlexingNetworkPlugin.connection.createStatement().execute("DELETE FROM friends WHERE playera='" + playera + "' AND playerb='" + playerb + "'");
    }

    public static ArrayList<String> getPlayerFriends(String playeruuid) throws SQLException {
        ArrayList<String> playerFriends = new ArrayList<String>();
        PreparedStatement st = FlexingNetworkPlugin.connection.prepareStatement("SELECT * FROM friends WHERE playera='" + playeruuid + "'");
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            playerFriends.add(rs.getString("playerb"));
        }
        rs.close();
        st.close();
        return playerFriends;
    }

    public static void addFriendRequest(String triggeredRequest, String receivedRequest) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String dateString = formatter.format(date);
        FlexingNetworkPlugin.connection.createStatement().execute("INSERT INTO requests (thatrequested, other, whenrequested) VALUES ('" + triggeredRequest + "', '" + receivedRequest + "', '" + dateString + "')");
    }

    public static void removeFriendRequest(String playera, String playerb) throws SQLException {
        FlexingNetworkPlugin.connection.createStatement().execute("DELETE FROM requests WHERE thatrequested='" + playera + "' AND other='" + playerb + "'");
    }

    public static ArrayList<String> incomingRequests(String player) throws SQLException {
        ArrayList<String> requestsReceived = new ArrayList<String>();
        PreparedStatement st = FlexingNetworkPlugin.connection.prepareStatement("SELECT * FROM requests WHERE other='" + player + "'");
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            requestsReceived.add(rs.getString("thatrequested"));
        }
        rs.close();
        st.close();
        return requestsReceived;
    }

    public static ArrayList<String> outgoingRequests(String player) throws SQLException {
        ArrayList<String> requestsSent = new ArrayList<String>();
        PreparedStatement st = FlexingNetworkPlugin.connection.prepareStatement("SELECT * FROM requests WHERE thatrequested='" + player + "'");
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            requestsSent.add(rs.getString("other"));
        }
        rs.close();
        st.close();
        return requestsSent;
    }
}
