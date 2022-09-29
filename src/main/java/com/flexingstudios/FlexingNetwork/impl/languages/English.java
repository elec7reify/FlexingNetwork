package com.flexingstudios.FlexingNetwork.impl.languages;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;

public class English extends Language {
    public English() {
        super(FlexingNetworkPlugin.getInstance(), "en");
        YamlConfiguration yml = getYml();

        yml.addDefault("name", "English");
        yml.addDefault(Messages.COMMAND_LANG_AVAILABLE, "&fAvailable languages:");
        yml.addDefault(Messages.COMMAND_LANG_LIST_FORMAT, "&e{iso} - {name}");
        yml.addDefault(Messages.COMMAND_LANG_SELECTED_SUCCESSFULLY, "&aLanguage changed");

        yml.addDefault(Messages.PLAYER_NOT_FOUND, "&cPlayer {targetPlayer} not found");

        yml.addDefault(Messages.COMMAND_GAMEMODE_USAGE, "&cUsage: /{label} <mode>");
        yml.addDefault(Messages.COMMAND_GAMEMODE_CHANGED, "Your game mode was set to &a{mode}");
        yml.addDefault(Messages.COMMAND_GAMEMODE_ERROR, "&cYour game mode is already {mode}");

        yml.addDefault(Messages.COMMAND_LOBBY_SUCCESSFUL, "&cYou have been sent to the lobby.");
        yml.addDefault(Messages.COMMAND_LOBBY_ERROR, "&aYou are already in the lobby.");

        yml.addDefault(Messages.COMMAND_IGNORE_USAGE, "&cUsage: /ignore <username>");
        yml.addDefault(Messages.IGNORE_ALREADY, "&cYou are already ignoring this player");
        yml.addDefault(Messages.COMMAND_UNIGNORE_USAGE, "&cUsage: /unignore <username>");

        yml.addDefault(Messages.RESTART_BROADCAST, Arrays.asList(
                "&b------------------------------",
                "&fThe Server will restart in &35 minutes&f!",
                "&b------------------------------"
        ));
        yml.addDefault(Messages.RESTART_BROADCAST_SCHEDULED, "&fThe Server will restart in &3{time}&f!");
        yml.addDefault(Messages.RESTART_TIME + "4min", "4 minutes");
        yml.addDefault(Messages.RESTART_TIME + "3min", "3 minutes");
        yml.addDefault(Messages.RESTART_TIME + "2min", "2 minutes");
        yml.addDefault(Messages.RESTART_TIME + "1min", "1 minute");
        yml.addDefault(Messages.RESTART_TIME + "30sec", "30 seconds");
        yml.addDefault(Messages.RESTART_TIME + "10sec", "10 seconds");
        yml.addDefault(Messages.RESTART_TIME + "5sec", "5 seconds");
        yml.addDefault(Messages.RESTART_TIME + "4sec", "4 seconds");
        yml.addDefault(Messages.RESTART_TIME + "3sec", "3 seconds");
        yml.addDefault(Messages.RESTART_TIME + "2sec", "2 seconds");
        yml.addDefault(Messages.RESTART_TIME + "1sec", "1 second");

        addDefault(Messages.COMMAND_KICK_USAGE, "&cUsage: /kick <username> [reason]");
        addDefault(Messages.COMMAND_SHADEKICK_USAGE, "&cUsage: /shadekick <username> [reason]");
        addDefault(Messages.REASON_NOT_SPECIFIED, "Not specified");
//        addDefault(Messages.KICKED_BY_SHADEADMIN, "&c&lТеневой админ &fкикнул игрока &3{target} &fпо причине: &6{reason}");
//        addDefault(Messages.KICKED_BY_ADMIN, "&3{kicker} &fкикнул игрока &3{target} &fпо причине: &6{reason}");

        save();
    }
}
