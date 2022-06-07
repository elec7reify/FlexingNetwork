package com.flexingstudios.FlexingNetwork.api.player.gamemetric;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class GameMetricEntry {
    protected final GameMetricValue parent;

    GameMetricEntry(GameMetricValue parent) {
        this.parent = parent;
    }

    abstract String mysqlWrite();

    abstract void mysqlRead(ResultSet paramResultSet) throws SQLException;

    abstract Object coreWrite();

    abstract void coreRead(Object paramObject);

    public abstract boolean isChanged();

    abstract void commitChanges();

    abstract void reset();
}
