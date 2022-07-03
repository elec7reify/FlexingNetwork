package com.flexingstudios.FlexingNetwork.impl.languages;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import org.bukkit.configuration.file.YamlConfiguration;

public class Deutsch extends Language {
    public Deutsch() {
        super(FlexingNetworkPlugin.getInstance(), "de");

        YamlConfiguration yml = getYml();
        yml.addDefault("name", "Deutsch");
        yml.addDefault(Messages.COMMAND_LANG_SELECTED_SUCCESSFULLY, "vse ok язык сменён");

        yml.addDefault(Messages.COMMAND_GAMEMODE_USAGE, "&cVerwendung:");
        yml.addDefault(Messages.COMMAND_GAMEMODE_USAGE_MODE, "<modus>");
        yml.addDefault(Messages.COMMAND_GAMEMODE_CHANGED, "Spielmodus geändert zu &a");
        yml.addDefault(Messages.COMMAND_GAMEMODE_ERROR, "&cIhr Spielmodus ist bereits ");

        yml.addDefault(Messages.COMMAND_LOBBY_SUCCESSFUL, "&cSie wurden zur Lobby weitergeleitet.");
        yml.addDefault(Messages.COMMAND_LOBBY_ERROR, "&aSie befinden sich bereits in der Lobby.");

        save();
    }
}
