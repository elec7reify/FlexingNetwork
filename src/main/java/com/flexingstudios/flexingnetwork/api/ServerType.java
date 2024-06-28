package com.flexingstudios.flexingnetwork.api;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum ServerType {
    AUTH("AUTH", "Авторизация"),
    BED_WARS("BW", "BedWars"),
    LOBBY("LOBBY", "Лобби"),
    BUILD("BUILD", "Билд"),
    SURVIVAL("SURVIVAL", "Выживание"),
    ANARCHY("ANARCHY", "Анархия"),
    UNKNOWN("UNKNOWN", "UNKNOWN")
    ;

    private static final Map<String, ServerType> byId;
    private final String name;
    private final String id;

    static {
        byId = new HashMap<>();
        for (ServerType serverType : values()) {
            ServerType old = byId.put(serverType.getId(), serverType);
            if (old != null)
                throw new RuntimeException("Duplicate ServerType id " + old + " and " + serverType);
        }
    }

    ServerType(@NotNull String id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return The official name of the ServerType
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * @return The internal ID
     */
    public @NotNull String getId() {
        return id;
    }

    public static ServerType byId(String id) {
        return byId.getOrDefault(id.toUpperCase(), UNKNOWN);
    }
}
