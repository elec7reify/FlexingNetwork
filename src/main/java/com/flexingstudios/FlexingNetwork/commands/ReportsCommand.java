package com.flexingstudios.FlexingNetwork.commands;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReportsCommand implements CommandExecutor {
    private static ReportsInventory menu;

    public ReportsCommand() {
        menu = new ReportsInventory();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FlexingNetworkPlugin.getInstance(), () -> {
            if (menu.lastUsed != 0L && System.currentTimeMillis() - menu.lastUsed > 1200000L)
                menu.cleanup();
        },12000L, 12000L);
    }

    public static void onNewViolator() {
        ArrayList<Violator> violators = new ArrayList<>();
        FlexingNetwork.mysql().select("SELECT * FROM reports", rs -> {
            while (rs.next()) {
                Violator violator = new Violator(rs.getString("target"), Rank.TEAM, null);
                violator.reports.add(new Report(rs.getString("username"), rs.getString("message"), 1231231));
                violators.add(violator);
            }
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), () -> menu.load(violators), 2L);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!FlexingNetwork.hasRank(commandSender, Rank.TEAM, true)) return true;

        Player player = (Player) commandSender;
        menu.show(player);
        return true;
    }

    private static class ReportsInventory implements InvMenu {
        private Inventory inv;
        private boolean loading;
        private long lastUsed = 0L;
        private List<Violator> violators = new ArrayList<>();

        public void cleanup() {
            //inv = null;
            //violators.clear();
            //lastUsed = 0L;
        }

        public ReportsInventory() {}

        private void use() {
            lastUsed = System.currentTimeMillis();
            if (inv == null) {
                inv = Bukkit.createInventory(this, 54, "reports");
                inv.setItem(4, Items.name(Material.MAGMA_CREAM, "&aupdate"));
            }
        }

        public void load(ArrayList<Violator> violators) {
            violators.sort((v1, v2) -> {
                int res = Integer.compare(v2.reports.size(), v1.reports.size());
                return res != 0 ? res : v1.username.compareTo(v2.username);
            });
            this.violators = violators;
            for (int i = 0; i < violators.size(); i++) {
                Violator violator = violators.get(i);
                List<String> lore = new ArrayList<>();
                lore.add("&fСервер: " + violator.server);
                lore.add("&fКоличество жалоб: " + violator.reports.size());
                for (Report report : violator.reports)
                    lore.add(" &a" + report.reporter + "&f: &7" + report.details);
                lore.add("");
                lore.add("&aНажмите, чтобы открыть меню действий.");
                String prefix = "&f";
                if (violator.rank != Rank.PLAYER)
                    prefix = violator.rank.getColor() + "[" + violator.rank.getPrefix() + "] ";
                inv.setItem(getSlot(i), Items.name(Material.FEATHER, violator.username, lore));
            }
            loading = false;
        }

        private int getSlot(int index) {
            return 10 + index;
        }

        private int getIndex(int slot) {
            return slot - 9;
        }

        private void requestViolators() {
            loading = true;
            use();
            if (violators != null) {
                for (int i = 0; i < violators.size(); i++) inv.clear(getSlot(i));
            }
            ReportsCommand.onNewViolator();
        }

        @Override
        public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
            if (loading) return;
            if (slot == 4) {
                requestViolators();
                return;
            }

            int index = getIndex(slot);
            //if (index >= 0)
        }

        @Override
        public Inventory getInventory() {
            return inv;
        }

        public void show(Player player) {
            requestViolators();
            use();
            player.openInventory(inv);
        }
    }

//    private static class ViolatorMenu implements InvMenu {
//        private ReportsCommand.ReportsInventory parent;
//        private Inventory inv;
//
//        public ViolatorMenu(ReportsCommand.ReportsInventory parent) {
//            this.parent = parent;
//            this.inv = Bukkit.createInventory(this, 54, "violator");
//        }
//
//        @Override
//        public void onClick(ItemStack itemStack, Player player, int slot, ClickType clickType) {
//
//        }
//
//        @Override
//        public Inventory getInventory() {
//            return this.inv;
//        }
//    }

    private static class Violator {
        public final String username;
        public final Rank rank;
        public final String server;
        public List<Report> reports;

        public Violator(String username, Rank rank, String server) {
            this.username = username;
            this.rank = rank;
            this.server = server;
            this.reports = new LinkedList<>();
        }
    }

    private static class Report {
        public String reporter;
        public String details;
        public int time;

        public Report(String reporter, String details, int time) {
            this.reporter = reporter;
            this.details = details;
            this.time = time;
        }
    }
}
