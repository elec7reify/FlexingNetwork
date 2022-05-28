package com.flexingstudios.FlexingNetwork.api.score;

import com.flexingstudios.FlexingNetwork.api.util.Reflect;
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
    private Set<Record> records;
    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective objective;
    private ScoreboardObjective nmsObjective;
    private Scoreboard nmsScoreboard;

    public SideScoreboard(String name) {
        this.records = (Set<Record>) new ConcurrentSet();
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("score", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.nmsObjective = Reflect.get(this.objective, "objective");
        this.nmsScoreboard = ((CraftScoreboard) this.scoreboard).getHandle();
        setDisplayName(name);
    }

    public void setDisplayName(String name) {
        this.objective.setDisplayName(name);
    }

    public void unbind(Player player) {
        try {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        } catch (IllegalStateException illegalStateException) {}
    }

    public void bind(Player player) {
        try {
            player.setScoreboard(this.scoreboard);
        } catch (IllegalStateException illegalStateException) {}
    }

    public Record create() {
        return create("", 0);
    }

    public Record create(String name) {
        return create(name, 0);
    }

    public Record create(String name, int value) {
        if (name == null)
            throw new IllegalArgumentException("Name cannot be null");
        Record rec = new Record(this, name);
        rec.value = value;
        this.records.add(rec);
        return rec;
    }

    public void remove(Record record) {
        this.records.remove(record);
        removeScore(record.name);
    }

    public void reset() {
        for (Record record : this.records)
            removeScore(record.name);
        this.records.clear();
    }

    void setScore(String name, int score) {
        this.nmsScoreboard.getPlayerScoreForObjective(name, this.nmsObjective).setScore(score);
    }

    void removeScore(String name) {
        this.nmsScoreboard.resetPlayerScores(name, nmsObjective);
    }

    public Scoreboard getScoreboard() {
        return (Scoreboard) this.scoreboard;
    }
}
