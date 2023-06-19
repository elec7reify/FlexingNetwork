package com.flexingstudios.flexingnetwork;

import com.flexingstudios.common.player.Permission;
import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.BungeeListeners.BungeeBridge;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.impl.FlexMetric;
import com.flexingstudios.flexingnetwork.impl.lobby.MysqlLobby;
import com.flexingstudios.flexingnetwork.tasks.PlayerMetaSaver;
import com.flexingstudios.flexingnetwork.api.Lobby;
import com.flexingstudios.flexingnetwork.api.updater.UpdateWatcher;
import com.flexingstudios.flexingnetwork.commands.*;
import com.flexingstudios.flexingnetwork.listeners.*;
import com.flexingstudios.flexingnetwork.impl.player.ExpBuffer;
import com.flexingstudios.flexingnetwork.impl.player.FlexCoin;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
import com.flexingstudios.flexingnetwork.impl.player.MysqlPlayer;
import com.flexingstudios.flexingnetwork.tasks.Restart;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
    public FlexCoin coins;
    public ExpBuffer expBuffer;
    public FlexMetric metrics;
    public VanishCommand vanishCommand;
    public PlayerMetaSaver metaSaver;

    private BukkitAudiences adventure;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        config = new Config(this);
        metrics = new FlexMetric(this);
        mysql = new MysqlWorker(this);
        coins = new FlexCoin(this);
        expBuffer = new ExpBuffer(this);
        help = new HelpCommand();
        lobby = new MysqlLobby(this);
        updateWatcher = new UpdateWatcher(this);
        mysql.start();
        adventure = BukkitAudiences.create(this);

        FlexPlayer.CONSTRUCTOR = MysqlPlayer::new;

        BungeeBridge bungeeBridge = new BungeeBridge();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "FlexingBungee");
        getServer().getMessenger().registerIncomingPluginChannel(this, "FlexingBungee", bungeeBridge);

        getServer().getPluginManager().registerEvents(new WorldProtect(this), this);
        getServer().getPluginManager().registerEvents(new PerWorldTablist(), this);
        getServer().getPluginManager().registerEvents(new ServiceItems(), this);
        getServer().getPluginManager().registerEvents(new ArrowTrailListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new FlexingChat(), this);
        getServer().getPluginManager().registerEvents(new ShulkerDispenseFix(), this);
        if (FlexingNetwork.INSTANCE.features().SIT.isEnabled())
            getServer().getPluginManager().registerEvents(new SitDownListener(), this);

        /*
         * Block - commands
         */
        CommandExecutor executor = new GamemodeCommand();
        IgnoreCommand ignoreCommand = new IgnoreCommand();
        getCommand("msg").setExecutor(new MessageCommand());
        getCommand("r").setExecutor(new MessageCommand());
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
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("shadekick").setExecutor(new ShadeKickCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("shadeban").setExecutor(new ShadeBanCommand());
        getCommand("unban").setExecutor(new BanCommand());
        getCommand("shadeunban").setExecutor(new ShadeBanCommand());
        getCommand("hub").setExecutor(new HubCommand());
        getCommand("stp").setExecutor(new StpCommand());
        getCommand("donate").setExecutor(new DonateCommand());
        getCommand("profile").setExecutor(new ProfileCommand());
        getCommand("actions").setExecutor(new ActionsCommand());
        getCommand("restrict").setExecutor(new RestrictCommand());
        getCommand("help").setExecutor(help);
        getCommand("firework").setExecutor(new FireworkCommand());

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

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new PlayerMetaSaver(this), 20L, 20L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            if (!FlexingNetwork.INSTANCE.isDevelopment() && FlexingNetwork.INSTANCE.features().AUTO_RESTART.isEnabled())
                Restart.Companion.schedule();
        });
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", new BungeeBridge());
        if (this.metaSaver != null)
            this.metaSaver.finish();
        coins.finish();
        expBuffer.finish();
        metrics.flush();
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
        mysql.finish();
        scheduledExecutorService.shutdownNow();
    }

    public @NotNull BukkitAudiences adventure() {
        if(adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }

    public static FlexingNetworkPlugin getInstance() {
        return instance;
    }
}
