package com.flexingstudios.FlexingNetwork;

import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.Lobby;
import com.flexingstudios.FlexingNetwork.api.updater.UpdateWatcher;
import com.flexingstudios.FlexingNetwork.api.util.Reflect;
import com.flexingstudios.FlexingNetwork.commands.*;
import com.flexingstudios.FlexingNetwork.friends.commands.FriendCommand;
import com.flexingstudios.FlexingNetwork.friends.listeners.GUIListener;
import com.flexingstudios.FlexingNetwork.impl.FMetrics;
import com.flexingstudios.FlexingNetwork.impl.languages.Deutsch;
import com.flexingstudios.FlexingNetwork.impl.languages.English;
import com.flexingstudios.FlexingNetwork.impl.languages.LangListener;
import com.flexingstudios.FlexingNetwork.impl.languages.Russian;
import com.flexingstudios.FlexingNetwork.impl.lobby.MysqlLobby;
import com.flexingstudios.FlexingNetwork.impl.player.*;
import com.flexingstudios.FlexingNetwork.listeners.*;
import com.flexingstudios.FlexingNetwork.BungeeListeners.BungeeBridge;
import com.flexingstudios.FlexingNetwork.tasks.PlayerMetaSaver;
import com.flexingstudios.FlexingNetwork.tasks.Restart;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class FlexingNetworkPlugin extends JavaPlugin {

    static FlexingNetworkPlugin instance;
    public ScheduledExecutorService scheduledExecutorService;
    public HelpCommand help;
    public Config config;
    public Lobby lobby;
    public MysqlWorker mysql;
    public UpdateWatcher updateWatcher;
    public FCoins coins;
    public ExpBuffer expBuffer;
    public FMetrics metrics;
    public VanishCommand vanishCommand;
    public PlayerMetaSaver metaSaver;
    public static Connection connection;

    @Override
    public void onLoad() {
        instance = this;

        Class<?> cEntity = Reflect.findClass("net.minecraft.server.v1_12_R1.Entity");
        if (((Integer)Reflect.get(cEntity, "entityCount")).intValue() == 0)
            Reflect.set(cEntity, "entityCount", Integer.valueOf(1));
        String vmname = ManagementFactory.getRuntimeMXBean().getName();
        String pid = vmname.split("@")[0];
        File pidfile = new File("pid");
        if (pidfile.exists())
            pidfile.delete();
        try {
            Files.write(pidfile.toPath(), pid.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(pidfile::delete, "FlexingNetwork pid deleter"));

        new English();
        new Russian();
        new Deutsch();
    }

    @Override
    public void onEnable() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        config = new Config(this);
        metrics = new FMetrics(this);
        mysql = new MysqlWorker(this);
        coins = new FCoins(this);
        expBuffer = new ExpBuffer(this);
        help = new HelpCommand();
        lobby = new MysqlLobby(this);
        updateWatcher = new UpdateWatcher(this);
        mysql.start();
        FLPlayer.CONSTRUCTOR = MysqlPlayer::new;
        BungeeBridge bungeeBridge = new BungeeBridge();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "FlexingBungee");
        getServer().getMessenger().registerIncomingPluginChannel(this, "FlexingBungee", bungeeBridge);
        getServer().getPluginManager().registerEvents(new WorldProtect(this), this);
        getServer().getPluginManager().registerEvents(new PerWorldTablist(), this);
        getServer().getPluginManager().registerEvents(new ServiceItems(), this);
        getServer().getPluginManager().registerEvents(new ArrowTrailListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new LangListener(), this);

        MessageCommand messageCommand = new MessageCommand();
        CommandExecutor executor = new GamemodeCommand();
        IgnoreCommand ignoreCommand = new IgnoreCommand();
        getCommand("msg").setExecutor(messageCommand);
        getCommand("r").setExecutor(messageCommand);
        getCommand("ignore").setExecutor(ignoreCommand);
        getCommand("unignore").setExecutor(ignoreCommand);
        getCommand("msgtoggle").setExecutor(ignoreCommand);
        getCommand("gamemode").setExecutor(executor);
        getCommand("gms").setExecutor(executor);
        getCommand("gmc").setExecutor(executor);
        getCommand("gma").setExecutor(executor);
        getCommand("gmsp").setExecutor(executor);
        getCommand("vanish").setExecutor(this.vanishCommand = new VanishCommand());
        getCommand("speed").setExecutor(new SpeedCommand());
        getCommand("tp").setExecutor(new TpCommand());
        getCommand("flexingnetwork").setExecutor(new FlexingNetworkCommand(this));
        getCommand("hub").setExecutor(new HubCommand());
        getCommand("stp").setExecutor(new StpCommand());
        getCommand("donate").setExecutor(new DonateCommand());
        getCommand("profile").setExecutor(new ProfileCommand());
        getCommand("friend").setExecutor(new FriendCommand());
        getCommand("language").setExecutor(new LanguageCommand());
        getCommand("help").setExecutor(help);

        //getServer().getScheduler().scheduleSyncRepeatingTask(this, new MemoryFix(), 100L, 100L);
        //getServer().getScheduler().scheduleSyncRepeatingTask(this, this.metaSaver = new PlayerMetaSaver(this), 20L, 20L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            help.addCommand("banlist", "Банлист", Permission.BAN);
            help.addCommand("history <игрок>", "История мутов и банов у игрока", Rank.VADMIN);
            help.addCommand("checkban <игрок>", "Проверить статус бана игрока", Permission.BAN);
            help.addCommand("checkmute <игрок>", "Проверить статус мута игрока", Permission.MUTE);
            help.addCommand("ban <игрок> [время] [причина]", "Бан игрока", Permission.BAN);
            help.addCommand("unban <IP/игрок>", "Разбанить игрока", Permission.BAN);
            help.addCommand("ipban <IP/игрок> [время] [причина]", "Бан по IP", Permission.IPBAN);
            help.addCommand("mute <игрок> [время] [причина]", "Замутить игрока", Permission.MUTE);
            help.addCommand("unmute <игрок>", "Размутить игрока", Permission.MUTE);
            help.addCommand("kick <игрок> [причина]", "Кик игрока", Rank.CHIKIBAMBONYLA);
            help.addCommand("vanish", "Режим наблюдателя", Permission.VANISH);
            help.addCommand("tp <игрок>", "Телепортация к игроку", Permission.VANISH);
            help.addCommand("speed <скорость>", "Установить скорость ходьбы или полета", Rank.TEAM);
            help.addCommand("hub", "Телепорт в лобби");
            help.addCommand("msg <игрок> <сообщение>", "Отправить личное сообщение игроку");
            help.addCommand("r <сообщение>", "Ответить игроку");
            help.addCommand("me", "Ваш профиль");
            help.addCommand("ignore <игрок>", "Игнорирование игрока");
            help.addCommand("unignore <игрок>", "Снять запрет");
            help.addCommand("msgtoggle", "Переключить личные сообщения");
            help.addCommand("donate", "Игровое донат меню");
            help.addCommand("gm <режим>", "Изменение игрового режима", Rank.SADMIN);
            help.addCommand("flex", "Админские команды", Rank.ADMIN);
        });

        /*getLogger().info("Starting Up...");
        try {
            openConnection();
            checkTables();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            if (!FlexingNetwork.isDevelopment() && (FlexingNetwork.features()).AUTO_RESTART.isEnabled())
                Restart.schedule();
        });

    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", new BungeeBridge());
        coins.finish();
        expBuffer.finish();
        metrics.flush();
        mysql.finish();
        scheduledExecutorService.shutdownNow();
        /*try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    public static FlexingNetworkPlugin getInstance() {
        return instance;
    }

    /*public static void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:test.db";
        Connection conn = DriverManager.getConnection(url);
        connection = conn;
        getPlugin().getLogger().info("MySQL connected.");
    }

    public static void checkTables() throws SQLException {
        Statement st = connection.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS friends (playera TEXT NOT NULL, playerb TEXT NOT NULL)");
        getPlugin().getLogger().info("Checked table \"friends\"!");
        st.execute("CREATE TABLE IF NOT EXISTS requests (thatrequested TEXT NOT NULL, other TEXT NOT NULL, whenrequested TEXT NOT NULL)");
        getPlugin().getLogger().info("Checked table \"requests\"!");
        }*/
}
