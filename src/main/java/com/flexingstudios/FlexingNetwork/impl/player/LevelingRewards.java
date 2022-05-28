package com.flexingstudios.FlexingNetwork.impl.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelingRewards {
    public static List<LevelingReward> REWARDS = new ArrayList<>();

    static {
        REWARDS.add();
    }

    public static abstract class LevelingReward {
        protected final List<String> text;

        protected LevelingReward() {
            this.text = new ArrayList<>();
        }

        protected LevelingReward(String name) {
            this.text = Collections.singletonList(name);
        }

        public List<String> getText() {
            return text;
        }

        public abstract void accept(FLPlayer flplayer);
    }

    private static class 
}
