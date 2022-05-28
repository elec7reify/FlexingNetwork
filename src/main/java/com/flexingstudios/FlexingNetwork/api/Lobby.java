package com.flexingstudios.FlexingNetwork.api;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

public interface Lobby {
    default void setMenuText(List<String> lines) {
        setMenuText(lines.<String>toArray(new String[lines.size()]));
    }

    void setMenuText(String... paramVarArgs);
    void setMaxPlayers(int paramInt);
    int getMaxPlayers();
    void setConnectableState(State paramState);
    String getServerId();
    ServerType getServerType();
    String getServerTypeId();
    int getServerNumber();
    String getHost();
    int getPort();
    void shutdown();
    void forceSend();

    public enum State {
        ALLOW_SPECTATORS(0),
        ALLOW_ALL(1),
        ALLOW_VIP(2),
        DENY_ALL(10),
        OFFLINE(11);

        private static final TIntObjectMap<State> byId = (TIntObjectMap<State>) new TIntObjectHashMap(16);

        private int id;

        static {
            for (State s : values())
                byId.put(s.id, s);
        }

        State(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static State byId(int id) {
            return (State)byId.get(id);
        }
    }
}
