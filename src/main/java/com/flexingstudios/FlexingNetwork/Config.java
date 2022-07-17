package com.flexingstudios.FlexingNetwork;

import com.flexingstudios.FlexingNetwork.api.conf.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

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
    public String language;
    public List<String> onDevCanJoin;

    public Config(FlexingNetworkPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfig();
    }

    private void loadConfig() {
        Configuration config = new Configuration(this.plugin);
        mysqlUrl = "jdbc:mysql://" + config.getString("mysql.host", "localhost") + ":" + config.getString("mysql.port", "3306") + "/" + config.getString("mysql.database", "minecraft");
        mysqlUsername = config.getString("mysql.username", "root");
        mysqlPassword = config.getString("mysql.password", "");
        lobbyEnabled = config.getBoolean("lobby.enabled", false);
        lobbyServerHost = config.getString("lobby.serverHost", "localhost");
        lobbyServerId = config.getString("lobby.serverId");
        development = config.getBoolean("development", false);
        updaterEnabled = config.getBoolean("updater.enabled", true);
        language = config.getString("language", "ru");
        onDevCanJoin = config.getStringList("ifDevCanJoin");
    }
}
