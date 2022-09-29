package com.flexingstudios.FlexingNetwork.api.player.gamemetric;

import com.flexingstudios.Commons.season.GameSeason;
import com.flexingstudios.FlexingNetwork.FlexingNetworkPlugin;
import com.flexingstudios.FlexingNetwork.api.FlexingNetwork;
import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class GameMetrics {
    protected final NetworkPlayer player;
    private final String game;
    private List<GameMetricValue> metrics;
    private GameSeason season;
    private boolean readOnly = false;
    private AtomicInteger loadCounter = new AtomicInteger(-1);
    private List<Consumer<GameMetrics>> callbacks = new LinkedList<>();

    public GameMetrics(String game, NetworkPlayer player) {
        this.game = game;
        this.player = player;
        metrics = new ArrayList<>();
    }

    public void addMetrics(GameMetricValue... metrics) {
        this.metrics.addAll(Arrays.asList(metrics));
    }

    public void setReadOnly(boolean flag) {
        readOnly = flag;
    }

    public void setSeason(GameSeason season) {
        this.season = season;
    }

    public List<GameMetricValue> getMetrics() {
        return metrics;
    }

    public boolean isLoaded() {
        return loadCounter.get() == 0;
    }

    public void runWhenLoaded(Consumer<GameMetrics> callback) {
        if (isLoaded()) {
            callback.accept(this);
        } else {
            callbacks.add(callback);
        }
    }

    public void load() {
        for (GameMetricValue metric : metrics)
            metric.reset();
        mysqlLoad();
    }

    public void save() {
        if (!isLoaded() || readOnly)
            return;
        beforeSave();
        mysqlSave();

        for (GameMetricValue metric : metrics)
            metric.commitChanges();
    }

    protected void afterLoad() {}

    protected void beforeSave() {}

    private void queryFinished() {
        if (loadCounter.decrementAndGet() == 0) {
            afterLoad();
            if (callbacks.isEmpty())
                return;
            Bukkit.getScheduler().scheduleSyncDelayedTask(FlexingNetworkPlugin.getInstance(), () -> {
                for (Consumer<GameMetrics> callback : callbacks)
                    callback.accept(this);
                callbacks.clear();
            });
        }
    }

    private void mysqlLoad() {
        if (season != null) {
            loadCounter.set(2);
            StringBuilder stringBuilder = new StringBuilder();
            for (GameMetricValue metric : metrics) {
                if (metric.getType() != GameMetricType.GLOBAL) {
                    if (stringBuilder.length() > 0)
                        stringBuilder.append(',');
                    stringBuilder.append(metric.getName());
                }
            }
            if (stringBuilder.length() == 0)
                stringBuilder.append('1');
            FlexingNetwork.mysql().select("SELECT " + stringBuilder + " FROM " + game + "_stats" + this.season.getTableSuffix() + " WHERE userid = " + this.player.getId(), rs -> {
                if (rs.next()) {
                    for (GameMetricValue metric : this.metrics) {
                        if (metric.getType() != GameMetricType.GLOBAL)
                            metric.seasonal().mysqlRead(rs);
                    }
                } else if (!this.readOnly) {
                    FlexingNetwork.mysql().query("INSERT INTO " + this.game + "_stats" + this.season.getTableSuffix() + " (userid) VALUES (" + this.player.getId() + ")");
                }
                queryFinished();
            });
        } else {
            this.loadCounter.set(1);
        }
        StringBuilder sb = new StringBuilder();
        for (GameMetricValue metric : this.metrics) {
            if (metric.getType() != GameMetricType.SEASONAL) {
                if (sb.length() > 0)
                    sb.append(',');
                sb.append(metric.getName());
            }
        }
        if (sb.length() == 0)
            sb.append('1');
        FlexingNetwork.mysql().select("SELECT " + sb + " FROM " + this.game + "_stats WHERE userid = " + this.player.getId(), rs -> {
            if (rs.next()) {
                for (GameMetricValue metric : this.metrics) {
                    if (metric.getType() != GameMetricType.SEASONAL)
                        metric.global().mysqlRead(rs);
                }
            } else if (!this.readOnly) {
                FlexingNetwork.mysql().query("INSERT INTO " + this.game + "_stats (userid) VALUES (" + this.player.getId() + ")");
            }
            queryFinished();
        });
    }

    private void mysqlSave() {
        StringBuilder sb = new StringBuilder();

        for (GameMetricValue metric : this.metrics) {
            if (metric.getType() != GameMetricType.SEASONAL && metric.isChanged()) {
                if (sb.length() != 0)
                    sb.append(',');
                sb.append(metric.getName()).append('=').append(metric.global().mysqlWrite());
            }
        }
        if (sb.length() > 0)
            FlexingNetwork.mysql().query("UPDATE " + this.game + "_stats SET " + sb + " WHERE userid = " + this.player.getId());
        if (this.season != null) {
            sb = new StringBuilder();
            for (GameMetricValue metric : this.metrics) {
                if (metric.getType() != GameMetricType.GLOBAL && metric.isChanged()) {
                    if (sb.length() != 0)
                        sb.append(',');
                    sb.append(metric.getName()).append('=').append(metric.seasonal().mysqlWrite());
                }
            }
            if (sb.length() > 0)
                FlexingNetwork.mysql().query("UPDATE " + this.game + "_stats" + this.season + " SET " + sb + " WHERE userid = " + this.player.getId());

        }
    }

}
