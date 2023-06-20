package com.flexingstudios.flexingnetwork.impl.lobby;

import com.flexingstudios.flexingnetwork.api.ServerType;
import com.flexingstudios.flexingnetwork.FlexingNetworkPlugin;
import com.flexingstudios.flexingnetwork.api.Lobby;
import com.google.common.base.Joiner;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class MysqlLobby implements Lobby, Runnable {
    private final FlexingNetworkPlugin plugin;
    private static int task = -1;
    private int maxPlayers = -1;
    private String menuInfo = "NULL";
    private Lobby.State state = Lobby.State.ALLOW_ALL;
    private String typeId;
    private ServerType type;
    private int number;

    public MysqlLobby(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
        if (task != -1) {
            Bukkit.getScheduler().cancelTask(task);
            task = -1;
        }
        if (plugin.config.lobbyEnabled) {
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 5L, 30L);
            plugin.mysql.query("INSERT IGNORE INTO servers (id, port) VALUES ('" + plugin.config.lobbyServerId + "', " + Bukkit.getPort() + ")");
        }
        String[] split = getServerId().split("_", 2);
        typeId = split[0];
        type = ServerType.byId(split[0]);
        number = Integer.parseInt(split[1]);
    }

    @Override
    public void run() {
        send(System.currentTimeMillis() / 1000L);
    }

    @Override
    public int getMaxPlayers() {
        if (maxPlayers != -1)
            return maxPlayers;
        return 0;
    }

    private int getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().toArray().length;
    }

    private void send(long updateTime) {
        plugin.mysql.query("UPDATE servers SET updated = " + updateTime + ",max = " +
           getMaxPlayers() + ",online = " +
           getOnlinePlayers() + ",menu_status = " + menuInfo + ",connectable = " + state
           .getId() + " WHERE id = '" + plugin.config.lobbyServerId + "'");
    }

    @Override
    public void shutdown() {
        Bukkit.getScheduler().cancelTask(task);
        MysqlLobby.task = -1;
        send(0L);
    }

    @Override
    public void forceSend() {
        send(System.currentTimeMillis());
    }

    @Override
    public void setMenuText(String... menuText) {
        if (menuText == null || menuText.length == 0) {
            menuInfo = "NULL";
        } else {
            menuInfo = "'" + StringEscapeUtils.escapeSql(Joiner.on("^").join(menuText)) + "'";
        }
    }

    @Override
    public void setConnectableState(Lobby.@NotNull State state) {
        this.state = state;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public @NotNull String getServerId() {
        return plugin.config.lobbyServerId;
    }

    @Override
    public @NotNull ServerType getServerType() {
        return type;
    }

    @Override
    public @NotNull String getServerTypeId() {
        return typeId;
    }

    @Override
    public int getServerNumber() {
        return number;
    }

    @Override
    public @NotNull String getHost() {
        return plugin.config.lobbyServerHost;
    }

    @Override
    public int getPort() {
        return Bukkit.getPort();
    }
}
