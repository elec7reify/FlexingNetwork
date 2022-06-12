package com.flexingstudios.FlexingNetwork.api.player.goals;

import com.flexingstudios.FlexingNetwork.api.player.NetworkPlayer;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

public abstract class ExpCoinsGoal extends Goal {
    public int rewardCoins;
    public int rewardExp;

    public ExpCoinsGoal(int rewardCoins, int rewardExp) {
        this.rewardCoins = rewardCoins;
        this.rewardExp = rewardExp;
    }

    public void write(JsonObject json) {
        super.write(json);
        json.addProperty("c", Integer.valueOf(this.rewardCoins));
        json.addProperty("e", Integer.valueOf(this.rewardExp));
    }

    public void read(JsonObject json) {
        super.read(json);
        this.rewardCoins = json.get("c").getAsInt();
        this.rewardExp = json.get("e").getAsInt();
    }

    public void complete(NetworkPlayer player) {
        player.addCoinsExact(this.rewardCoins);
        player.giveExpExact(this.rewardExp);
    }

    public List<String> getRewardText() {
        return Arrays.asList(new String[] { "&7+ &e" +
                Utilities.pluralsCoins(this.rewardCoins), "&7+ &9" + this.rewardExp + " опыта"});
    }
}
