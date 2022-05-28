package com.flexingstudios.FlexingNetwork;

import com.flexingstudios.FlexingNetwork.api.conf.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Config {

    private final FlexingNetworkPlugin plugin;
    public String mysqlUrl;
    public String mysqlUsername;
    public String mysqlPassword;
    public boolean lobbyEnabled;
    public String lobbyServerId;
    public String lobbyServerHost;
    public boolean updaterEnabled;
    public boolean development;

    public Config(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration config = this.plugin.getConfig();
        this.mysqlUrl = "jdbc:mysql://" + config.getString("mysql.host", "localhost") + ":" + config.getString("mysql.port", "3306") + "/" + config.getString("mysql.database", "minecraft");
        this.mysqlUsername = config.getString("mysql.username", "root");
        this.mysqlPassword = config.getString("mysql.password", "");
        this.lobbyEnabled = config.getBoolean("lobby.enabled", false);
        this.lobbyServerHost = config.getString("lobby.serverHost", "localhost");
        this.lobbyServerId = config.getString("lobby.serverId");
        this.development = config.getBoolean("dev", false);
        this.updaterEnabled = config.getBoolean("updater.enabled", true);
    }
}
