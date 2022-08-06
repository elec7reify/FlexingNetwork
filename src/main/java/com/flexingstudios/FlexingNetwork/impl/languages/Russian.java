package com.flexingstudios.FlexingNetwork.impl.languages;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;

public class Russian extends Language {
    public Russian() {
        super(FlexingNetworkPlugin.getInstance(), "ru");
        YamlConfiguration yml = getYml();

        yml.addDefault("name", "Русский");
        yml.addDefault(Messages.COMMAND_LANG_AVAILABLE, "&fДоступные языки:");
        yml.addDefault(Messages.COMMAND_LANG_LIST_FORMAT, "&e{iso} - {name}");
        yml.addDefault(Messages.COMMAND_LANG_SELECTED_SUCCESSFULLY, "&aЯзык изменён");

        yml.addDefault(Messages.PLAYER_NOT_FOUND, "&cИгрок {targetPlayer} не найден");

        yml.addDefault(Messages.COMMAND_GAMEMODE_USAGE, "&cИспользование: /{label} <режим>");
        yml.addDefault(Messages.COMMAND_GAMEMODE_CHANGED, "Ваш режим игры изменён на &a{modename}");
        yml.addDefault(Messages.COMMAND_GAMEMODE_ERROR, "&cВаш режим игры уже является {mode}");

        yml.addDefault(Messages.COMMAND_LOBBY_SUCCESSFUL, "&cВы были перенаправленны в лобби.");
        yml.addDefault(Messages.COMMAND_LOBBY_ERROR, "&aВы уже находитесь в лобби.");

        yml.addDefault(Messages.COMMAND_IGNORE_USAGE, "&cИспользование /ignore <ник>");
        yml.addDefault(Messages.IGNORE_ALREADY, "&cВы уже игнорируете этого игрока");
        yml.addDefault(Messages.COMMAND_UNIGNORE_USAGE, "&cИспользование /unignore <ник>");

        yml.addDefault(Messages.RESTART_BROADCAST, Arrays.asList(
                "&b------------------------------",
                "&fСервер будет перезагружен через &35 минут&f!",
                "&b------------------------------"
        ));
        yml.addDefault(Messages.RESTART_BROADCAST_SCHEDULED, "&fСервер будет перезагружен через &3{time}&f!");
        yml.addDefault(Messages.RESTART_BROADCAST + "4min", "4 минуты");
        yml.addDefault(Messages.RESTART_BROADCAST + "3min", "3 минуты");
        yml.addDefault(Messages.RESTART_BROADCAST + "2min", "2 минуты");
        yml.addDefault(Messages.RESTART_BROADCAST + "1min", "1 минуту");
        yml.addDefault(Messages.RESTART_BROADCAST + "30sec", "30 секунд");
        yml.addDefault(Messages.RESTART_BROADCAST + "10sec", "10 секунд");
        yml.addDefault(Messages.RESTART_BROADCAST + "5sec", "5 секунд");
        yml.addDefault(Messages.RESTART_BROADCAST + "4sec", "4 секунды");
        yml.addDefault(Messages.RESTART_BROADCAST + "3sec", "3 секунды");
        yml.addDefault(Messages.RESTART_BROADCAST + "2sec", "2 секунды");
        yml.addDefault(Messages.RESTART_BROADCAST + "1sec", "1 секунду");

        yml.addDefault(Messages.NO_PERMISSION, "&cОтказ в доступе: У вас недостаточно прав для выполнения этого действия");
        yml.addDefault(Messages.NO_RANK, "&cОтказ в доступе: Для этого действия необходим статус ");

        yml.addDefault(Messages.CLOSE_DONATE_INVENTORY, "&3&lЗакрыть меню");

        save();
    }
}
