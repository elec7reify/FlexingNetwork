package com.flexingstudios.FlexingNetwork.api.player;

import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.conf.Configuration;
import com.flexingstudios.FlexingNetwork.api.event.PlayerLangChangeEvent;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Language extends Configuration {
    private final String iso;
    private String prefix = "";
    private static final HashMap<UUID, Language> langByPlayer = new HashMap<>();
    private static final List<Language> languages = new ArrayList<>();
    private static Language defaultLanguage;

    public Language(Plugin plugin, String iso) {
        super(plugin, "messages_" + iso, plugin.getDataFolder().getPath() + "/Languages");
        this.iso = iso;
        languages.add(this);
    }

    /**
     * Set chat prefix.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Get scoreboard strings.
     */
    public static List<String> getScoreboard(Player p, String path, String alternative) {
        Language language = getPlayerLanguage(p);
        if (language.exists(path)) {
            return language.l(path);
        } else {
            if (path.split("\\.").length == 3) {
                String[] sp = path.split("\\.");
                String path2 = sp[1];
                path2 = String.valueOf(path2.charAt(0)).toUpperCase() + path2.substring(1).toLowerCase();
                path2 = sp[0] + "." + path2 + "." + sp[2];
                if (language.exists(path2)) {
                    return language.l(path);
                } else if (language.exists(sp[0] + "." + sp[1].toUpperCase() + "." + sp[2])) {
                    return language.l(sp[0] + "." + sp[1].toUpperCase() + "." + sp[2]);
                }
            }
        }
        return language.l(alternative);
    }

    /**
     * Get language display name.
     */
    public String getLangName() {
        return getString(Messages.LANGUAGE_NAME);
    }

    /**
     * Get message in player's language.
     */
    public static String getMsg(Player player, String path) {
        if (player == null) return getDefaultLanguage().m(path);
        return langByPlayer.getOrDefault(player.getUniqueId(), getDefaultLanguage()).m(path);
    }

    /**
     * Get message in player's language.
     */
    public static String getMsg(NetworkPlayer player, String path) {
        if (player == null) return getDefaultLanguage().m(path);
        Player flPlayer = player.getBukkitPlayer();
        return langByPlayer.getOrDefault(flPlayer.getUniqueId(), getDefaultLanguage()).m(path);
    }

    /**
     * Retrieve a player language.
     */
    public static Language getPlayerLanguage(Player p) {
        return langByPlayer.getOrDefault(p.getUniqueId(), getDefaultLanguage());
    }

    public static Language getPlayerLanguage(UUID p) {
        return langByPlayer.getOrDefault(p, getDefaultLanguage());
    }

    /**
     * Check if a message was set.
     */
    public boolean exists(String path) {
        return get(path) != null;
    }

    /**
     * Get a string list in player's language.
     */
    public static List<String> getList(Player p, String path) {
        return langByPlayer.getOrDefault(p.getUniqueId(), getDefaultLanguage()).l(path);
    }

    /**
     * Get a color translated message.
     */
    public String m(String path) {
        String message = getString(path);
        if (message == null) {
            System.err.println("Missing message key " + path + " in language " + getIso());
            message = Language.getDefaultLanguage().getString(path);
        }
        return Utilities.colored(message.replace("{prefix}", prefix));
    }

    /**
     * Get a color translated list.
     */
    public List<String> l(String path) {
        List<String> result = new ArrayList<>();
        List<String> lines = getStringList(path);
        if (lines == null) {
            System.err.println("Missing message list key " + path + " in language " + getIso());
            lines = Collections.emptyList();
        }
        for (String line : lines) {
            result.add(Utilities.colored(line));
        }

        return result;
    }

    public static HashMap<UUID, Language> getLangByPlayer() {
        return langByPlayer;
    }

    /**
     * Check if a language exists.
     */
    public static boolean isLanguageExist(String iso) {
        for (Language l : languages) {
            if (l.iso.equalsIgnoreCase(iso)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get language with given info.
     *
     * @return null if could not find.
     */
    public static Language getLang(String iso) {
        for (Language l : languages) {
            if (l.iso.equalsIgnoreCase(iso)) {
                return l;
            }
        }
        return getDefaultLanguage();
    }

    /**
     * Get language iso code.
     */
    public String getIso() {
        return iso;
    }

    /**
     * Get loaded languages list.
     */
    public static List<Language> getLanguages() {
        return languages;
    }

    /**
     * Change a player language
     */
    public static boolean setPlayerLanguage(UUID uuid, String iso) {

        if (iso == null) {
            if (langByPlayer.containsKey(uuid)) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    PlayerLangChangeEvent e = new PlayerLangChangeEvent(player, langByPlayer.get(uuid).iso, getDefaultLanguage().iso);
                    Bukkit.getPluginManager().callEvent(e);
                    if (e.isCancelled()) return false;
                }
            }
            langByPlayer.remove(uuid);
            return true;
        }

        Language newLang = Language.getLang(iso);
        if (newLang == null) return false;
        Language oldLang = Language.getPlayerLanguage(uuid);
        if (oldLang.getIso().equals(newLang.getIso())) return false;

        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            PlayerLangChangeEvent e = new PlayerLangChangeEvent(player, oldLang.getIso(), newLang.getIso());
            Bukkit.getPluginManager().callEvent(e);
            if (e.isCancelled()) return false;
        }

        if (Language.getDefaultLanguage().getIso().equals(newLang.getIso())) {
            langByPlayer.remove(uuid);
            return true;
        }

        if (langByPlayer.containsKey(uuid)) {
            langByPlayer.replace(uuid, newLang);
        } else {
            langByPlayer.put(uuid, newLang);
        }
        return true;
    }

    /**
     * Change server default language.
     */
    public static void setDefaultLanguage(Language defaultLanguage) {
        Language.defaultLanguage = defaultLanguage;
    }

    /**
     * Get server default language.
     */
    public static Language getDefaultLanguage() {
        return defaultLanguage;
    }
}

