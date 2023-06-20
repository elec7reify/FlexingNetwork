package com.flexingstudios.flexingnetwork.api.score;

import com.flexingstudios.flexingnetwork.api.util.Reflect;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.v1_12_R1.Scoreboard;
import net.minecraft.server.v1_12_R1.ScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.Set;

public class SideScoreboard {
    private Set<java.lang.Record> records;
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective objective;
    private ScoreboardObjective nmsObjective;
    private Scoreboard nmsScoreboard;

    public SideScoreboard(String name) {
        records = new ConcurrentSet<>();
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("score", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        nmsObjective = Reflect.get(objective, "objective");
        nmsScoreboard = ((CraftScoreboard) scoreboard).getHandle();
        setDisplayName(name);
    }

    public void setDisplayName(String name) {
        objective.setDisplayName(name);
    }

    public void unbind(Player player) {
        try {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        } catch (IllegalStateException ex) {}
    }

    public void bind(Player player) {
        try {
            player.setScoreboard(scoreboard);
        } catch (IllegalStateException ex) {}
    }

    public java.lang.Record create() {
        return create("", 1);
    }

    public java.lang.Record create(String name) {
        return create(name, 1);
    }

    public java.lang.Record create(String name, int value) {
        if (name == null)
            throw new IllegalArgumentException("Name cannot be null");

        java.lang.Record rec = new java.lang.Record(this, name);
        rec.value = value;
        records.add(rec);

        return rec;
    }

    public void remove(java.lang.Record record) {
        records.remove(record);
        removeScore(record.name);
    }

    public void reset() {
        for (java.lang.Record record : records)
            removeScore(record.name);
        records.clear();
    }

    void setScore(String name, int score) {
        nmsScoreboard.getPlayerScoreForObjective(name, nmsObjective).setScore(score);
    }

    void removeScore(String name) {
        nmsScoreboard.resetPlayerScores(name, nmsObjective);
    }

    public Scoreboard getScoreboard() {
        return (Scoreboard) scoreboard;
    }
}
