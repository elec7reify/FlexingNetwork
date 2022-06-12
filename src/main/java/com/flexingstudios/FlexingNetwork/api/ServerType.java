package com.flexingstudios.FlexingNetwork.api;

import java.util.HashMap;
import java.util.Map;

public enum ServerType {
    AUTH("AUTH", "Авторизация"),
    BED_WARS("BW", "BedWars"),
    LOBBY("LOBBY", "Лобби"),
    BUILD("BUILD", "Билд"),
    SURVIVAL("SURVIVAL", "Выживание"),
    ANARCHY("ANARCHY", "Анархия"),
    UNKNOWN("UNKNOWN", "UNKNOWN");

    private static final Map<String, ServerType> byId;
    private String name;
    private String id;

    static {
        byId = new HashMap<>();
        for (ServerType serverType : values()) {
            ServerType old = byId.put(serverType.getId(), serverType);
            if (old != null)
                throw new RuntimeException("Duplicate ServerType id " + old + " and " + serverType);
        }
    }

    ServerType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return The official name of the ServerType
     */
    public String getName() {
        return name;
    }

    /**
     * @return The internal ID
     */
    public String getId() {
        return id;
    }


    public static ServerType byId(String id) {
        return byId.getOrDefault(id.toUpperCase(), UNKNOWN);
    }
}
