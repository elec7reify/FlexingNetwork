package com.flexingstudios.FlexingNetwork;

public enum Debug {
    MYSQL;

    private boolean enabled;

    Debug() {
        this.enabled = false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
