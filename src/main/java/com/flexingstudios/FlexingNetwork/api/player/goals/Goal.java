package com.flexingstudios.flexingnetwork.api.player.goals;

import com.flexingstudios.flexingnetwork.api.player.NetworkPlayer;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Goal {
    private int progress = 0;
    private int goal = 1;
    public String game = null;
    public long finishTime = 0L;
    public boolean needSave = false;

    public boolean isApplicable(NetworkPlayer player, GoalQuery query) {
        return true;
    }

    public void write(JsonObject json) {
        json.addProperty(".p", Integer.valueOf(this.progress));
        json.addProperty(".g", Integer.valueOf(this.goal));
        if (this.game != null)
            json.addProperty(".e", this.game);
    }

    public void read(JsonObject json) {
        this.progress = json.get(".p").getAsInt();
        this.goal = json.get(".g").getAsInt();
        this.game = json.get(".e").getAsString();
    }

    public void setDuration(int seconds) {
        this.finishTime = System.currentTimeMillis() / 1000L + seconds;
    }

    public abstract void complete(NetworkPlayer paramNetworkPlayer);

    public abstract ItemStack getItem();

    public List<String> getText(boolean addGame) {
        return new ArrayList<>();
    }

    public List<String> getRewardText() {
        return null;
    }

    public int getGoal() {
        return this.goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
