package com.flexingstudios.FlexingNetwork.impl.lobby;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Lobby;
import com.flexingstudios.FlexingNetwork.api.ServerType;
import com.google.common.base.Joiner;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
            task = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) plugin, this, 5L, 30L);
            plugin.mysql.query("INSERT IGNORE INTO servers (id, port) VALUES ('" + plugin.config.lobbyServerId + "', " + Bukkit.getPort() + ")");
        }
        String[] split = getServerId().split("_", 2);
        this.typeId = split[0];
        this.type = ServerType.byId(split[0]);
        this.number = Integer.parseInt(split[1]);
    }

    @Override
    public void run() {
        send(System.currentTimeMillis() / 1000L);
    }

    @Override
    public int getMaxPlayers() {
        if (this.maxPlayers != -1)
            return this.maxPlayers;
        return 0;
    }

    private int getOnlinePlayers() {
        return Bukkit.getOnlinePlayers().toArray().length;
    }

    private void send(long updateTime) {
        this.plugin.mysql.query("UPDATE servers SET updated = " + updateTime + ",max = " +
                getMaxPlayers() + ",online = " +
                getOnlinePlayers() + ",menu_status = " + this.menuInfo + ",connectable = " + this.state
                .getId() + " WHERE id = '" + this.plugin.config.lobbyServerId + "'");
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
    public void setMenuText(String... lines) {
        if (lines == null || lines.length == 0) {
            this.menuInfo = "NULL";
        } else {
            this.menuInfo = "'" + StringEscapeUtils.escapeSql(Joiner.on("^").join(lines)) + "'";
        }
    }

    @Override
    public void setConnectableState(Lobby.State state) {
        this.state = state;
    }

    @Override
    public void setMaxPlayers(int max) {
        this.maxPlayers = max;
    }

    @Override
    public String getServerId() {
        return this.plugin.config.lobbyServerId;
    }

    @Override
    public ServerType getServerType() {
        return this.type;
    }

    @Override
    public String getServerTypeId() {
        return this.typeId;
    }

    @Override
    public int getServerNumber() {
        return this.number;
    }

    @Override
    public String getHost() {
        return this.plugin.config.lobbyServerHost;
    }

    @Override
    public int getPort() {
        return Bukkit.getPort();
    }
}
