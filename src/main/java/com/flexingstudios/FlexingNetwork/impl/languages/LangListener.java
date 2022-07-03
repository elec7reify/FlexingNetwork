package com.flexingstudios.FlexingNetwork.impl.languages;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.event.PlayerLangChangeEvent;
import com.flexingstudios.FlexingNetwork.impl.player.MysqlPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class LangListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLanguageChangeEvent(PlayerLangChangeEvent e) {
        if (e == null) return;
        if (e.isCancelled()) return;
            Bukkit.getScheduler().runTaskLater(FlexingNetworkPlugin.getInstance(), () -> {
                // save to db
                MysqlPlayer.get(e.getPlayer()).setLanguage(e.getPlayer().getUniqueId(), e.getNewLang());
                //Bukkit.getScheduler().runTaskAsynchronously(FlexingNetworkPlugin.getInstance(), () -> MysqlPlayer.get(e.getPlayer()).setLanguage(e.getPlayer(), e.getNewLang()));
            }, 10L);
    }
}
