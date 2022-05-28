package com.flexingstudios.FlexingNetwork.api;

public interface Metrics {
    default void add(String key) {
        add(key, 1);
    }

    void add(String paramString, int paramInt);
}
