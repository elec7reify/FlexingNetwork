package com.flexingstudios.FlexingNetwork.impl.languages;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;

public class Ukrainian extends Language {
    public Ukrainian() {
        super(FlexingNetworkPlugin.getInstance(), "uk");
        YamlConfiguration yml = getYml();

        yml.addDefault(Messages.LANGUAGE_NAME, "Українська");

        yml.addDefault(Messages.MENU_LANGUAGE_LORE_COMMING_SOON, Arrays.asList(
                "&7Український зараз знаходиться в розробці",
                "&7та поки що не доступний.",
                "",
                "&7Якщо ви бажаєте допомогти нам у",
                "&7завантаженні цієї мови на FlexingWorld, то",
                "&aНатисніть, щоб отримати більше інформації!"));

        yml.addDefault(Messages.NEW, "НОВЕ");
        yml.addDefault(Messages.COMMING_SOON, "В РОЗРОБЦІ");

        save();
    }
}
