package com.flexingstudios.flexingnetwork.api.player.goals;

import java.util.Map;

public interface Goals {
    void add(String string, Goal goal);

    void addCustom(String string, Goal goal);

    boolean remove(String string);

    boolean contains(String string);

    void trigger(String string, GoalQuery goalQuery);

    void triggerAmount(String string, int paramInt, GoalQuery goalQuery);

    void openInventory();

    Map<String, Goal> getActiveGoals();

    Map<String, Goal> getCustomGoals();
}
