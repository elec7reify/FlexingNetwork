package com.flexingstudios.FlexingNetwork.api.player.gamemetric;

public abstract class GameMetricValue {
    protected final String name;

    protected GameMetricType type;

    public GameMetricValue(String name, GameMetricType type) {
        this.name = name;
        this.type = type;
    }

    abstract GameMetricEntry global();

    abstract GameMetricEntry seasonal();

    public String getName() {
        return this.name;
    }

    public GameMetricType getType() {
        return this.type;
    }

    void commitChanges() {
        GameMetricEntry global = global();
        if (global != null)
            global.commitChanges();
        GameMetricEntry seasonal = seasonal();
        if (seasonal != null)
            seasonal.commitChanges();
    }

    public boolean isChanged() {
        GameMetricEntry global = global();
        if (global != null && global.isChanged())
            return true;
        GameMetricEntry seasonal = seasonal();
        if (seasonal != null && seasonal.isChanged())
            return true;
        return false;
    }

    public void reset() {
        GameMetricEntry global = global();
        if (global != null)
            global.reset();
        GameMetricEntry seasonal = seasonal();
        if (seasonal != null)
            seasonal.reset();
    }
}
