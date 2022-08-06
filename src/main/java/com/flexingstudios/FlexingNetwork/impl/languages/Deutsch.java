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
        yml.addDefault(Messages.COMMAND_LANG_AVAILABLE, "&fVerfügbare Sprachen:");
        yml.addDefault(Messages.COMMAND_LANG_LIST_FORMAT, "&e{iso} - {name}");
        yml.addDefault(Messages.COMMAND_LANG_SELECTED_SUCCESSFULLY, "&aSprache geändert");

        yml.addDefault(Messages.COMMAND_GAMEMODE_USAGE, "&cVerwendung:");
        yml.addDefault(Messages.COMMAND_GAMEMODE_CHANGED, "Spielmodus geändert zu &a");
        yml.addDefault(Messages.COMMAND_GAMEMODE_ERROR, "&cIhr Spielmodus ist bereits ");

        yml.addDefault(Messages.COMMAND_LOBBY_SUCCESSFUL, "&cSie wurden zur Lobby weitergeleitet.");
        yml.addDefault(Messages.COMMAND_LOBBY_ERROR, "&aSie befinden sich bereits in der Lobby.");

        yml.addDefault(Messages.RESTART_BROADCAST + "4min", "4 minuten");
        yml.addDefault(Messages.RESTART_BROADCAST + "3min", "3 minuten");
        yml.addDefault(Messages.RESTART_BROADCAST + "2min", "2 minuten");
        yml.addDefault(Messages.RESTART_BROADCAST + "1min", "1 minute");
        yml.addDefault(Messages.RESTART_BROADCAST + "30sec", "30 sekunden");
        yml.addDefault(Messages.RESTART_BROADCAST + "10sec", "10 sekunden");
        yml.addDefault(Messages.RESTART_BROADCAST + "5sec", "5 sekunden");
        yml.addDefault(Messages.RESTART_BROADCAST + "4sec", "4 sekunden");
        yml.addDefault(Messages.RESTART_BROADCAST + "3sec", "3 sekunden");
        yml.addDefault(Messages.RESTART_BROADCAST + "2sec", "2 sekunden");
        yml.addDefault(Messages.RESTART_BROADCAST + "1sec", "1 sekunde");

        yml.addDefault(Messages.NO_PERMISSION, "&cZugriff verweigert: Sie haben nicht die erforderlichen Rechte, um diese Aktion auszuführen");
        yml.addDefault(Messages.NO_RANK, "&cZugriff verweigert: Diese Aktion erfordert einen Status ");

        yml.addDefault(Messages.CLOSE_DONATE_INVENTORY, "&3&lMenü schließen");

        save();
    }
}
