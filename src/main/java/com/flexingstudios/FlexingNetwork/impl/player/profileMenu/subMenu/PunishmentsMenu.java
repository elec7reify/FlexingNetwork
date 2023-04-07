package com.flexingstudios.FlexingNetwork.impl.player.profileMenu.subMenu;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.impl.player.profileMenu.FlexPlayerMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

public class PunishmentsMenu implements InvMenu {
    private final Inventory inv;
    private final Player player;
    private final FlexPlayerMenu parent;
    private ArrayList<Punishment> punishments = new ArrayList<>();

    public PunishmentsMenu(Player player, FlexPlayerMenu parent) {
        inv = Bukkit.createInventory(this, 54, "История наказаний");
        this.player = player;
        this.parent = parent;

        inv.setItem(40, Items.name(Material.FEATHER, "&aВернуться назад &7(Мой профиль)"));
    }

    private void requestPunishments() {
        FlexingNetwork.mysql().select("SELECT * FROM `user_log_actions` WHERE `data` = '" + player.getName() + "'", rs -> {
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                int time = rs.getInt("time");
                String action = rs.getString("action");
                String data = rs.getString("data");
                String comment = rs.getString("comment");

                if (action.equals("ban") || action.equals("shade.ban") || action.equals("unban") ||
                        action.equals("shade.unban") || action.equals("kick") || action.equals("shade.kick") ||
                        action.equals("mute") || action.equals("shade.mute") || action.equals("restrict")) {
                    Punishment punishment = new Punishment(id, username, time, action, data, comment);
                    punishments.add(punishment);
                }
            }
        });

        Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), () -> load(punishments), 2L);
    }

    private void load(ArrayList<Punishment> punishments) {
        String admin;
        for (int i = 0; i < punishments.size(); i++) {
            Punishment punishment = punishments.get(i);

            if (punishment == null) {
                inv.setItem(13, Items.name(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), "&aУ вас нет наказаний!"));
            } else {
                if (punishment.action.startsWith("shade")) {
                    admin = "&cТеневой админ";
                } else {
                    admin = punishment.username;
                }
                inv.setItem(getSlot(i), Items.name(PunishmentType.byId(punishment.action).is, "&e#" + i,
                        "&fИдентификатор лога &7" + punishment.id,
                        "&fАдмининистратор: &c" + admin,
                        "&fДата и время: &6" + new SimpleDateFormat(Messages.DATE_FORMAT)
                                .format(new Date(punishment.time * 1000L)),
                        "&fДействие: &3" + PunishmentType.byId(punishment.action).action,
                        "&fНарушитель: &3" + punishment.data,
                        "&fПричина: &6" + punishment.comment));
            }
        }
    }

    private int getSlot(int index) {
        return 9 + index;
    }

    private int getIndex(int slot) {
        return slot - 9;
    }

    @Override
    public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
        if (slot == 40) player.openInventory(parent.getInventory());
    }

    @Override
    public Inventory getInventory() {
        requestPunishments();
        return inv;
    }

    private static class Punishment {
        private final int id;
        public final String username;
        public final int time;
        public final String action;
        public final String data;
        public final String comment;

        public Punishment(int id, String username, int time, String action, String data, String comment) {
            this.id = id;
            this.username = username;
            this.time = time;
            this.action = action;
            this.data = data;
            this.comment = comment;
        }
    }

    private enum PunishmentType {
        BAN("ban", "Бан", new ItemStack(Material.BARRIER, 1)),
        SHADEBAN("shade.ban", "Теневой бан", new ItemStack(Material.FIREBALL, 1)),
        UNBAN("unban", "Разбан", new ItemStack(Material.NETHER_STAR, 1)),
        SHADEUNBAN("shade.unban", "Теневой разбан", new ItemStack(Material.FIREBALL, 1)),
        KICK("kick", "Кик", new ItemStack(Material.LAVA_BUCKET, 1)),
        SHADEKICK("shade.kick", "Теневой кик", new ItemStack(Material.FIREBALL, 1)),
        MUTE("mute", "Мут", new ItemStack(Material.REDSTONE, 1)),
        SHADEMUTE("shade.mute", "Теневой мут", new ItemStack(Material.FIREBALL, 1)),
        RESTRICT("restrict", "Запрет на выдачу наказаний", new ItemStack(Material.BARRIER, 1)),
        ;

        private final String id;
        private final String action;
        private final ItemStack is;
        private static final Map<String, PunishmentType> byId;

        static {
            byId = new HashMap<>();
            for (PunishmentType type : values()) {
                PunishmentType old = byId.put(type.id, type);
                if (old != null)
                    throw new RuntimeException("Duplicate punishment id " + old + " and " + type);
            }
        }

        PunishmentType(String id, String action, ItemStack is) {
            this.id = id;
            this.action = action;
            this.is = is;
        }

        public static PunishmentType byId(String id) {
            return byId.get(id);
        }
    }
}
