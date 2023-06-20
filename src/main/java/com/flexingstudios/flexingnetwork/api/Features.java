package com.flexingstudios.flexingnetwork.api;

import com.flexingstudios.flexingnetwork.FlexingNetworkPlugin;

public class Features {
    static final Features inst = new Features();
    public final Feature CHANGE_CHAT = new Feature("Change Chat", true);
    public final Feature CHANGE_PLAYER_LIST_NAMES = new Feature("Change Player List Names", false);
    public final Feature AUTO_RESTART = new Feature("Auto Restart", true);
    public final Feature SIT = new Feature("Sit down", false);

    public static class Feature {
        private final String name;
        private boolean enabled;

        Feature(String name, boolean enabled) {
            this.name = name;
            this.enabled = enabled;
        }

        public void setEnabled(boolean enabled) {
            if (this.enabled != enabled) {
                this.enabled = enabled;
                FlexingNetworkPlugin.getInstance().getLogger().info("Feature [" + name + "] is " + (enabled ? "enabled" : "disabled"));
            }
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getName() {
            return name;
        }
    }
}
