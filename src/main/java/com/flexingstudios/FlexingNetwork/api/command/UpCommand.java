package com.flexingstudios.FlexingNetwork.api.command;

import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

public abstract class UpCommand implements CommandExecutor, TabCompleter {
    private final Map<String, RegisteredSub> subs = new HashMap<>();
    private final List<PublicSub> publicSubs = new ArrayList<>();

    public UpCommand() {
        BiConsumer<String, RegisteredSub> adsb = (cmd, sub) -> {
            if (subs.put(cmd, sub) != null)
                throw new IllegalArgumentException("[" + getClass().getSimpleName() + "] Sub " + cmd + " is already registered for " + sub.method.getName());
        };
        Class<?> clazz = getClass();
        do {
            for (Method method : clazz.getDeclaredMethods()) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(CmdSub.class)) {
                    RegisteredSub rsub = new RegisteredSub(method);
                    CmdSub asub = method.getAnnotation(CmdSub.class);

                    rsub.hidden = asub.hidden();
                    for (String name : asub.value()) {
                        if (!rsub.hidden)
                            publicSubs.add(new PublicSub(name.toLowerCase(), rsub));
                        adsb.accept(name.toLowerCase(), rsub);
                    }
                    for (String alias : asub.aliases())
                        adsb.accept(alias.toLowerCase(), rsub);
                    if ((asub.ranks()).length == 0) {
                        rsub.rankExact = false;
                        rsub.ranks = new Rank[] { asub.rank() };
                    } else {
                        rsub.rankExact = true;
                        rsub.ranks = asub.ranks();
                    }
                }
            }
        clazz = clazz.getSuperclass();
    } while (clazz != UpCommand.class);
        Collections.sort(publicSubs);
    }

    protected void runCommand(Runnable action, CommandSender sender, Command cmd, String label, String[] args) {
        action.run();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            RegisteredSub rsub = subs.get(args[0].toLowerCase());
        if (rsub != null) {
            if (!rsub.isAvailableFor(getRank(sender), sender))
                return true;
            String[] str = new String[args.length - 1];
            if (str.length != 0)
                System.arraycopy(args, 1, str, 0, str.length);

            runCommand(() -> {
                try {
                    rsub.method.invoke(this, new dataCommand(sender, label, args[0].toLowerCase(), str));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, sender, cmd, label, args);

            return true;
            }
        }
        runCommand(() -> main(sender, cmd, label, args), sender, cmd, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!publicSubs.isEmpty() && args.length == 1) {
            Rank rank = getRank(sender);
            List<String> list = new ArrayList<>(publicSubs.size());
            String begin = args[0].toLowerCase();
            for (PublicSub sub : publicSubs) {
                if (sub.cmd.startsWith(begin) && sub.sub.isAvailableFor(rank, null))
                    list.add(sub.cmd);
            }

            return list;
        }

        return null;
    }

    protected Rank getRank(CommandSender sender) {
        if (sender instanceof org.bukkit.command.ConsoleCommandSender) return Rank.ADMIN;
        return FlexingNetwork.getPlayer(sender.getName()).getRank();
    }

    protected List<PublicSub> getPublicSubs() {
        return publicSubs;
    }

    protected abstract boolean main(CommandSender sender, Command command, String label, String[] args);

    protected static class RegisteredSub {
        Method method;
        Rank[] ranks = new Rank[0];
        boolean rankExact = false;
        boolean hidden = false;

        public RegisteredSub(Method method) {
            this.method = method;
        }

        public boolean isAvailableFor(Rank rank, CommandSender inform) {
            if (rankExact) {
                for (Rank rank1 : ranks) {
                    if (rank1 == rank) return true;
                }
                if (inform != null)
                    Utilities.msg(inform, "&cОтказ в доступе: Необходим статус " + ranks[0].getDisplayName());
                return false;
            }
            if (rank.has(ranks[0])) return true;
            if (inform != null)
                Utilities.msg(inform, "&cОтказ в доступе: Необходим статус " + ranks[0].getDisplayName());
            return false;
        }
    }

    protected static class PublicSub implements Comparable<PublicSub> {
        public String cmd;
        public RegisteredSub sub;

        public PublicSub(String cmd, RegisteredSub sub) {
            this.cmd = cmd;
            this.sub = sub;
        }

        public int compareTo(PublicSub sb) {
            return cmd.compareToIgnoreCase(sb.cmd);
        }
    }
}
