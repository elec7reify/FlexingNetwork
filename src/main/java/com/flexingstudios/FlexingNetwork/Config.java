package com.flexingstudios.FlexingNetwork;

import com.flexingstudios.FlexingNetwork.api.conf.Configuration;

import java.util.List;

public class Config extends Configuration {
    private final FlexingNetworkPlugin plugin;
    public String mysqlUrl;
    public String mysqlUsername;
    public String mysqlPassword;
    public boolean lobbyEnabled;
    public String lobbyServerId;
    public String lobbyServerHost;
    public boolean updaterEnabled;
    public boolean development;
    public String language;
    public List<String> onDevCanJoin;

    public Config(FlexingNetworkPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfig();
    }

    private void loadConfig() {
        mysqlUrl = "jdbc:mysql://" + getString("mysql.host", "localhost") + ":" + getString("mysql.port", "3306") + "/" + getString("mysql.database", "minecraft");
        mysqlUsername = getString("mysql.username", "root");
        mysqlPassword = getString("mysql.password", "");
        lobbyEnabled = getBoolean("lobby.enabled", false);
        lobbyServerHost = getString("lobby.serverHost", "localhost");
        lobbyServerId = getString("lobby.serverId");
        development = getBoolean("development", false);
        updaterEnabled = getBoolean("updater.enabled", true);
        language = getString("language", "ru");
        onDevCanJoin = getStringList("ifDevCanJoin");
    }
}
