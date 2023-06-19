package com.flexingstudios.flexingnetwork.api;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Lobby {
    default void setMenuText(List<String> lines) {
        setMenuText(lines.toArray(new String[lines.size()]));
    }

    void setMenuText(String... menuText);

    /**
     * Set the maximum amount of players which can log in to this server.
     *
     * @param maxPlayers the amount of players this server allows.
     */
    void setMaxPlayers(int maxPlayers);

    /**
     * Get the maximum amount of players which can log in to this server.
     *
     * @return the amount of players this server allows.
     */
    int getMaxPlayers();

    void setConnectableState(@NotNull Lobby.State state);

    @Nullable
    String getServerId();

    @NotNull
    ServerType getServerType();

    @NotNull
    String getServerTypeId();

    int getServerNumber();

    @NotNull
    String getHost();

    /**
     * Get the game port that the server runs on.
     *
     * @return the port number of this server.
     */
    int getPort();

    void shutdown();

    void forceSend();

    enum State {
        ALLOW_SPECTATORS(0),
        ALLOW_ALL(2),
        ALLOW_VIP(3),
        ALLOW_PREMIUM(4),
        DENY_ALL(5),
        OFFLINE(9);

        private static final TIntObjectMap<State> byId = new TIntObjectHashMap<>(16);
        private final int id;

        static {
            for (State s : values())
                byId.put(s.id, s);
        }

        State(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static State byId(int id) {
            return byId.get(id);
        }
    }
}
