package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.apache.commons.codec.language.bm.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.flexingstudios.FlexingNetwork.api.player.Language.getMsg;

public class LanguageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            Utilities.msg(p, getMsg(p, Messages.COMMAND_LANG_AVAILABLE));
            Utilities.msg(p, Language.getLanguages().listIterator().toString().toUpperCase());
            for (Language lf : Language.getLanguages()) {

            }
        } else if (Language.isLanguageExist(args[0])) {
            Language.setPlayerLanguage(p.getUniqueId(), args[0]);
            //p.sendMessage(getMsg(p, Messages.COMMAND_LANG_SELECTED_SUCCESSFULLY));
            Bukkit.getScheduler().runTaskLater(FlexingNetworkPlugin.getInstance(), () -> p.sendMessage(getMsg(p, Messages.COMMAND_LANG_SELECTED_SUCCESSFULLY)), 3L);
        }

        return true;
    }
}

