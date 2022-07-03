package com.flexingstudios.FlexingNetwork.impl.languages;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import org.bukkit.configuration.file.YamlConfiguration;

public class Russian extends Language {
    public Russian() {
        super(FlexingNetworkPlugin.getInstance(), "ru");

        YamlConfiguration yml = getYml();
        yml.addDefault("name", "Русский");
        yml.addDefault(Messages.COMMAND_LANG_SELECTED_SUCCESSFULLY, "vse ok язык сменён");

        yml.addDefault(Messages.COMMAND_GAMEMODE_USAGE, "&cИспользование:");
        yml.addDefault(Messages.COMMAND_GAMEMODE_USAGE_MODE, "<режим>");
        yml.addDefault(Messages.COMMAND_GAMEMODE_CHANGED, "Режим игры изменён на &a");
        yml.addDefault(Messages.COMMAND_GAMEMODE_ERROR, "&cВаш режим игры уже явлвяется ");

        yml.addDefault(Messages.COMMAND_LOBBY_SUCCESSFUL, "&cВы были перенаправленны в лобби.");
        yml.addDefault(Messages.COMMAND_LOBBY_ERROR, "&aВы уже находитесь в лобби.");

        save();
    }
}
