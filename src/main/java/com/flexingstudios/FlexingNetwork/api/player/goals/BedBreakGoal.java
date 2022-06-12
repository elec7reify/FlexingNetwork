package com.flexingstudios.FlexingNetwork.api.player.goals;

import com.flexingstudios.FlexingNetwork.api.ServerType;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
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
        String text = "&fРазрушить &e" + getGoal() + Utilities.plurals(getGoal(), " кровать" , " кровати", " кроватей");
        if (addGame)
            text = text + "&f на " + ServerType.byId(this.game).getName();
        return Collections.singletonList(text);
    }
}
