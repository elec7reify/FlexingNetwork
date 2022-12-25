package com.flexingstudios.FlexingNetwork.api.util;

import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import io.netty.util.internal.MathUtil;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Utilities {

    private Utilities() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static String plurals(int n, String form1, String form2, String form3) {
        if (n == 0)
            return form3;

        n = Math.abs(n) % 100;
        if (n > 10 && n < 20)
            return form3;

        n %= 10;
        if (n > 1 && n < 5)
            return form2;
        if (n == 1)
            return form1;

        return form3;
    }

    public static String pluralsCoins(int coins) {
        return coins + plurals(coins, " коин", " коина", " коинов");
    }

    public static void bcast(String msg) {
        Bukkit.broadcastMessage(colored(msg));
    }

    public static void bcast(List<String> msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Utilities.msg(player, msg);
        }
    }

    /**
     * Format a string to contain Bukkit colour codes
     *
     * @param msg The message to colour
     * @return The formatted colour coded string
     */
    public static String colored(String msg) {
        if (msg == null)
            return null;

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String[] colored(String... lines) {
        if (lines == null)
            return null;
        for (int i = 0; i < lines.length; i++)
            lines[i] = colored(lines[i]);

        return lines;
    }

    public static List<String> colored(List<String> lines) {
        ListIterator<String> it = lines.listIterator();
        while (it.hasNext())
            it.set(colored(it.next()));

        return lines;
    }

    public static void msg(CommandSender cs, String... msg) {
        cs.sendMessage(colored(msg));
    }

    public static void msg(CommandSender cs, List<String> msg) {
        for (String str : msg)
            cs.sendMessage(colored(str));
    }

    public static void sendPacket(Player player, Packet packet) {
        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(packet);
    }

    public static Location parseLocation(World world, String loc) {
        try {
            String[] data = loc.split(",");
            Location bloc = new Location(world, Double.parseDouble(data[0].trim()), Double.parseDouble(data[1].trim()), Double.parseDouble(data[2].trim()));
            if (data.length > 3)
                bloc.setYaw(Float.parseFloat(data[3].trim()));
            if (data.length > 4)
                bloc.setPitch(Float.parseFloat(data[4].trim()));

            return bloc;
        } catch (Exception e) {
            FlexingNetworkPlugin.getInstance().getLogger().log(Level.SEVERE, null, e);
            return new Location(world, 0.0D, 0.0D, 0.0D);
        }
    }

    public static List<Location> parseLocations(World world, List<String> locs) {
        return locs.stream()
                .map(loc -> parseLocation(world, loc))
                .collect(Collectors.toList());
    }

    public static Location center(Collection<Location> locs) {
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        World w = null;
        for (Location loc : locs) {
            if (w == null)
                w = loc.getWorld();

            x += loc.getX();
            y += loc.getY();
            z += loc.getZ();
        }

        return new Location(w, x / locs.size(), y / locs.size(), z / locs.size());
    }

    public static String genBar(int length, float progress, char c, String background, String filled) {
        StringBuilder sb = new StringBuilder(length + 4);
        sb.append(colored(filled));

        boolean filled0 = false;
        for (int i = 0; i < length; i++) {
            if (!filled0 && length * progress <= i) {
                sb.append(colored(background));
                filled0 = true;
            }
            sb.append(c);
        }

        return sb.toString();
    }
}
