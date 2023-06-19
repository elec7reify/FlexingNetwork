package com.flexingstudios.flexingnetwork.api.player.goals;

import com.flexingstudios.flexingnetwork.api.ServerType;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class BedBreakGoal extends ExpCoinsGoal {
    public BedBreakGoal() {
        this(0, 0);
    }

    public BedBreakGoal(int rewardCoins, int rewardExp) {
        super(rewardCoins, rewardExp);
    }

    public ItemStack getItem() {
        return new ItemStack(Material.BED);
    }

    public List<String> getText(boolean addGame) {
        String text = "&fРазрушить &e" + getGoal() + Utils.plurals(getGoal(), " кровать" , " кровати", " кроватей");
        if (addGame)
            text = text + "&f на " + ServerType.byId(this.game).getName();
        return Collections.singletonList(text);
    }
}
