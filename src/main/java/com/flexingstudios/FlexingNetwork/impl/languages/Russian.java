package com.flexingstudios.FlexingNetwork.impl.languages;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.ArrowTrail;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;

import java.util.Arrays;

public class Russian extends Language {
    public Russian() {
        super(FlexingNetworkPlugin.getInstance(), "ru");
        YamlConfiguration yml = getYml();

        yml.addDefault(Messages.LANGUAGE_NAME, "Русский");
//        yml.addDefault(Messages.MENU_LANGUAGE_TITLE, "Сменить язык");
//        yml.addDefault(Messages.MENU_LANGUAGE_LORE , Arrays.asList(
//                "&7После применения: язык",
//                "&7интерфейса сервера будет",
//                "&7изменён на Русский.",
//                "",
//                "&aНажмите, чтобы изменить язык",
//                "&aна Русский."));
        yml.addDefault(Messages.PLAYER_NOT_FOUND, "&cИгрок {targetPlayer} не найден");

        yml.addDefault(Messages.COMMAND_GAMEMODE_USAGE, "&cИспользование: /{label} <режим>");
        yml.addDefault(Messages.COMMAND_GAMEMODE_CHANGED, "Ваш режим игры изменён на &a{mode}");
        yml.addDefault(Messages.COMMAND_GAMEMODE_ERROR, "&cВаш режим игры уже является {mode}");

        yml.addDefault(Messages.COMMAND_LOBBY_SUCCESSFUL, "&cВы были перенаправленны в лобби.");
        yml.addDefault(Messages.COMMAND_LOBBY_ERROR, "&aВы уже находитесь в лобби.");

        yml.addDefault(Messages.COMMAND_IGNORE_USAGE, "&cИспользование: /ignore <ник>");
        yml.addDefault(Messages.IGNORE_ALREADY, "&cВы уже игнорируете этого игрока");
        yml.addDefault(Messages.COMMAND_UNIGNORE_USAGE, "&cИспользование: /unignore <ник>");

        yml.addDefault(Messages.RESTART_BROADCAST, Arrays.asList(
                "&b------------------------------",
                "&fСервер будет перезагружен через &35 минут&f!",
                "&b------------------------------"
        ));
        yml.addDefault(Messages.RESTART_BROADCAST_SCHEDULED, "&fСервер будет перезагружен через &3{time}&f!");
        yml.addDefault(Messages.RESTART_TIME + "4min", "4 минуты");
        yml.addDefault(Messages.RESTART_TIME + "3min", "3 минуты");
        yml.addDefault(Messages.RESTART_TIME + "2min", "2 минуты");
        yml.addDefault(Messages.RESTART_TIME + "1min", "1 минуту");
        yml.addDefault(Messages.RESTART_TIME + "30sec", "30 секунд");
        yml.addDefault(Messages.RESTART_TIME + "10sec", "10 секунд");
        yml.addDefault(Messages.RESTART_TIME + "5sec", "5 секунд");
        yml.addDefault(Messages.RESTART_TIME + "4sec", "4 секунды");
        yml.addDefault(Messages.RESTART_TIME + "3sec", "3 секунды");
        yml.addDefault(Messages.RESTART_TIME + "2sec", "2 секунды");
        yml.addDefault(Messages.RESTART_TIME + "1sec", "1 секунду");
        yml.addDefault(Messages.RESTART_KICK_REASON, "&cПерезагрузка сервера");

        yml.addDefault(Messages.NO_PERMISSION, "&cОтказ в доступе: У вас недостаточно прав для выполнения этого действия");
        yml.addDefault(Messages.NO_RANK, "&cОтказ в доступе: Для этого действия необходим статус ");

        yml.addDefault(Messages.CLOSE_DONATE_INVENTORY, "&3&lЗакрыть меню");

        /** Rank Inventory */
        yml.addDefault(Messages.RANK_SPONSOR_TITLE, "&fПривилегия &a&lЧикибамбвипка &f- &a&l29 Руб.&f/мес.");
        yml.addDefault(Messages.RANK_VIP_LORE_SURVIVAL, Arrays.asList(
                "",
                " &a• &fПрефикс в чат и таб: &7«&a&lЧикибамбвипка&7» &7{player_name}",
                " &a• &fПример сообщения в чате:",
                "&7«&a&lЧикибамбвипка&7» &7{player_name} &8→ &7Hello World!",
                "",
                " &a◆ &fВозможность получить набор &a&lVIP: &a/kit vip",
                " &a◆ &fВозможность включить режим полёта: &a/fly",
                " &a◆ &fВозможность установить &a&l3 &fточки дома: &a/sethome",
                " &a◆ &fВозможность открыть эндер сундук: &a/echest",
                " &a◆ &fВозможность открыть верстак: &a/workbench",
                " &a◆ &fВозможность телепортация к месту смерти: &a/back",
                " &a◆ &fВозможность узнать рецепт крафта: &a/recipe",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n60&9&l Flex&f&lCoin &fза покупку",
                "",
                "&fПокупать донат на сайте: &ewww.FlexingWorld.net"));

        yml.addDefault(Messages.RANK_SPONSOR_TITLE, "&fПривилегия &d&lСпонсорбамбони &f- &a&l299 Руб.");
        yml.addDefault(Messages.RANK_SPONSOR_LORE_SURVIVAL, Arrays.asList(
                "&d• &fПрефикс в чат и таб: &7«&d&lСпонсорбамбони&7» &7{player_name}",
                "&d• &fПример сообщения в чате:",
                "",
                "&7«&d&lСпонсорбамбони&7» &7{player_name} &8→ &7Hello World!",
                "",
                "&c▪ Донат выдается на &c&lвсе &cсервера.",
                "&6▪ Возможности &6&lпредыдущих &6донатов.",
                "&a▪ Донат &a&lостается &aпосле вайпа.",
                "",
                "&fПокупать донат на сайте: &ewww.FlexingWorld.ru"));

        addDefault(Messages.COMMAND_KICK_USAGE, "&cИспользование: {command} <игрок> [причина]");
        addDefault(Messages.COMMAND_BAN_USAGE, "&cИспользование: {command} <игрок> [время] [причина]");
        addDefault(Messages.REASON_NOT_SPECIFIED, "Не указана");
        addDefault(Messages.KICKED_BY_SHADEADMIN, "&c&lТеневой админ &fкикнул игрока &3{player} &fпо причине: &6{reason}");
        addDefault(Messages.KICKED_BY_ADMIN, "&3{admin} &fкикнул игрока &3{player} &fпо причине: &6{reason}");
        addDefault(Messages.BANNED_BY_ADMIN, "&3{admin} &fзабанил игрока &3{player} &6навсегда &fпо причине: &6{reason}");
        addDefault(Messages.TEMPBANNED_BY_ADMIN, "&3{admin} &fзабанил игрока &3{player} &3на {time} &fпо причине: &6{reason}");
        addDefault(Messages.KICK_MESSAGE, Arrays.asList(
                "&fСлужба безопастности &9&lFlexing&f&lWorld",
                "&fВы были кикнуты",
                "",
                "&fВаш ник: &3{player}",
                "&fВас кикнул: {admin}",
                "&fПо причине: &6{reason}",
                "",
                "&fПожалуйста, &3&lпрочитайте правила сервера&r&f, чтобы избежать дальнейших наказаний",
                "",
                "&fНе согласны с нашим наказанием? Подайте аппеляцию",
                "&fVK: &3https://vk.com/flexingworld",
                "&fDS: &3https://discord.gg/9X6QpjqSvG",
                "",
                "&8{date}"));
        addDefault(Messages.BAN_MESSAGE, Arrays.asList(
                "&fСлужба безопастности &9&lFlexing&f&lWorld",
                "&fВы были забанены",
                "",
                "&fВаш ник: &3{player}",
                "&fВас забанил: {admin}",
                "&fВремя бана: &6{time}",
                "&fПо причине: &6{reason}",
                "",
                "&fПожалуйста, &3&lпрочитайте правила сервера&r&f, чтобы избежать дальнейших наказаний",
                "",
                "&fНе согласны с нашим наказанием? Подайте аппеляцию",
                "&fVK: &3https://vk.com/flexingworld",
                "&fDS: &3https://discord.gg/9X6QpjqSvG",
                "",
                "&8{date}"));
        yml.addDefault(Messages.OFFICIAL_DATE_FORMAT, "yyyy-MM-dd HH:mm");
        yml.addDefault(Messages.PROFILE_MENU_TITLE, "Профиль: {name}");
        yml.addDefault(Messages.ARROWTRAIL_HEARTS, "Сердца");
        yml.addDefault(Messages.ARROWTRAIL_DRIP_WATER, "Капли воды");
        yml.addDefault(Messages.ARROWTRAIL_DRIP_LAVA, "Капли лавы");
        yml.addDefault(Messages.ARROWTRAIL_FIREWORK, "Фейерверк");
        yml.addDefault(Messages.ARROWTRAIL_NOTE, "Ноты");
        yml.addDefault(Messages.ARROWTRAIL_SLIME, "Слизь");
        yml.addDefault(Messages.ARROWTRAIL_VILLAGER_HAPPY, "Зелёные звёзды");
        yml.addDefault(Messages.ARROWTRAIL_ANGRY_VILLAGER, "Разбитые сердца");
        yml.addDefault(Messages.ARROWTRAIL_ENCHANTMENT_TABLE, "Зашифрованные символы");
        yml.addDefault(Messages.ARROWTRAIL_SELECTED, "&fВы выбрали &6след от стрелы&f: &a{trail_name}");

        save();
    }
}
