package com.flexingstudios.flexingnetwork.api.Language;

import java.util.Arrays;
import java.util.List;

public class Messages {

    /**  */
    public static final String NEW = "НОВОЕ";
    public static final String PROFILE_MENU_TITLE = "Профиль: {name}";
    public static final String PLAYER_NOT_FOUND = "&cИгрок {player} не найден";
    public static final String ARROWTRAIL_SELECTED = "&fВы выбрали &6след от стрелы&f: &a{trail_name}";

    /** BAN/KICK/MUTE Messages + SHADE */
    public static final String REASON_NOT_SPECIFIED = "Не указана";
    public static final String COMMAND_KICK_USAGE = "&cИспользование: /{command} <игрок> [причина]";
    public static final String COMMAND_BAN_USAGE = "&cИспользование: /{command} [игрок] <время> <причина>";
    public static final String KICKED_BY_SHADEADMIN = "&c&lТеневой админ &fкикнул игрока &3{player} &fпо причине: &6{reason}";
    public static final String KICKED_BY_ADMIN = "&3{admin} &fкикнул игрока &3{player} &fпо причине: &6{reason}";
    public static final String BANNED_BY_ADMIN = "&3{admin} &fзабанил игрока &3{player} &6навсегда &fпо причине: &6{reason}";
    public static final String TEMPBANNED_BY_ADMIN = "&3{admin} &fзабанил игрока &3{player} &3на {time} &fпо причине: &6{reason}";
    public static final String BAN_ME = "&cВы не можете заблокировать самого себя!";
    public static final String KICK_ME = "&cВы не можете кикнуть самого себя!";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    /** Gamemode Command Messages */
    public static final String COMMAND_GAMEMODE_USAGE = "&cИспользование: /{command} <режим>";
    public static final String COMMAND_GAMEMODE_CHANGED = "Ваш режим игры изменён на &a{mode}";
    public static final String COMMAND_GAMEMODE_ERROR = "&cВаш режим игры уже является {mode}";

    /** Lobby Command Messages */
    public static final String COMMAND_LOBBY_SUCCESSFUL = "&cВы были перенаправленны в лобби";
    public static final String COMMAND_LOBBY_ERROR = "&aВы уже находитесь в лобби.";

    /** Ignore/Unignore Command Messages */
    public static final String COMMAND_IGNORE_USAGE = "&cИспользование: /ignore <ник>";
    public static final String IGNORE_ALREADY = "&cВы уже игнорируете этого игрока";
    public static final String COMMAND_UNIGNORE_USAGE = "&cИспользование: /unignore <ник>";

    /** Restart task */
    public static final List<String> RESTART_BROADCAST = Arrays.asList(
            "&b------------------------------",
            "&fСервер будет перезагружен через &35 минут&f!",
            "&b------------------------------");
    public static final String RESTART_BROADCAST_SCHEDULED = "&fСервер будет перезагружен через &3%time%&f!";
    public static final String RESTART_KICK_REASON = "&cПерезагрузка сервера";

    /** Permission & Status */
    public static final String NO_PERMISSION = "&cОтказано в доступе: у вас недостаточно прав для выполнения этого действия";
    public static final String NO_RANK = "&cОтказано в доступе: для этого действия необходим ранг {rank}";

    /** ONLY Console AND ONLY in ENGLISH */
    public static final String CONSOLE_COMMAND_GAMEMODE_USAGE = "console-cmd-gamemode-usage";
    rus

//                "&7После применения: язык",
//                "&7интерфейса сервера будет",
//                "&7изменён на Русский.",
//                "",
//                "&aНажмите, чтобы изменить язык",
//                "&aна Русский."
}
