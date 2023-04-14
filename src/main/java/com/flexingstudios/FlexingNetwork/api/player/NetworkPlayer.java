package com.flexingstudios.FlexingNetwork.api.player;

import com.flexingstudios.Common.player.Permission;
import com.flexingstudios.Common.player.Rank;
import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import gnu.trove.set.hash.TIntHashSet;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public interface NetworkPlayer {
    String getMeta(String key);
    void setMeta(String key, String value);
    String removeMeta(String key);
    boolean hasMeta(String key);
    Map<String, String> getMetaMap();
    int addCoins(int amount);
    void addCoinsExact(int amount);
    int getCoins();
    void toLobby();
    void toServer(String id);
    void takeCoins(int amount);

    Rank getRank();

    default boolean has(Permission permission) {
        return getRank().has(permission);
    }

    default boolean has(Rank rank) {
        return getRank().has(rank);
    }

    default boolean hasAndNotify(Rank rank) {
        if (getRank().has(rank))
            return true;
        Utilities.msg(getBukkitPlayer(), Messages.NO_RANK.replace("{status}", rank.getColor() + rank.getName()));
        return false;
    }

    default boolean hasAndNotify(Permission permission) {
        if (getRank().has(permission))
            return true;
        Utilities.msg(getBukkitPlayer(), Messages.NO_PERMISSION);
        return false;
    }

    String getName();
    int getId();
    String getPrefixedName();
    String getPrefix();
    String getColoredName();
    Player getBukkitPlayer();
    boolean isOnline();
    ArrowTrail getArrowTrail();
    MessageOnJoin getMessageOnJoin();
    void setArrowTrail(ArrowTrail arrowTrail);
    void setMessageOnJoin(MessageOnJoin msg);
    void unlockArrowTrail(ArrowTrail trail);
    void unlockJoinMessage(MessageOnJoin msg);
    TIntHashSet getAvailableArrowTrails();
    TIntHashSet getAvailableJoinMessages();
    long getLoginTime();
    int getLevel();
    int getTotalExp();
    int getPartialExp();
    void giveExp(int exp);
    void giveExpExact(int exp);
    boolean getRestrict();
}
