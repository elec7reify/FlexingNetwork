package com.flexingstudios.flexingnetwork.api.player;

import com.flexingstudios.flexingnetwork.api.Language.Messages;
import com.flexingstudios.common.player.Permission;
import com.flexingstudios.common.player.Rank;
import com.flexingstudios.flexingnetwork.api.ServerType;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import gnu.trove.set.hash.TIntHashSet;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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
    void toServer(ServerType id);
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
        Utils.msg(getBukkitPlayer(), Messages.NO_RANK.replace("{status}", rank.getColor() + rank.getName()));
        return false;
    }

    default boolean hasAndNotify(Permission permission) {
        if (getRank().has(permission))
            return true;
        Utils.msg(getBukkitPlayer(), Messages.NO_PERMISSION);
        return false;
    }

    /**
     * @return The player's name.
     */
    @Nullable String getName();

    /**
     * @return player id.
     */
    int getId();
    String getPrefixedName();
    String getPrefix();
    String getColoredName();
    Player getBukkitPlayer();

    /**
     * @return true if they are online.
     */
    boolean isOnline();
    ArrowTrail getArrowTrail();
    MessageOnJoin getMessageOnJoin();
    void setArrowTrail(ArrowTrail arrowTrail);
    void setMessageOnJoin(MessageOnJoin msg);
    void unlockArrowTrail(ArrowTrail trail);
    void unlockJoinMessage(MessageOnJoin msg);
    TIntHashSet getAvailableArrowTrails();
    TIntHashSet getAvailableJoinMessages();

    /**
     * @return player login time.
     */
    long getLoginTime();

    int getLevel();
    int getTotalExp();
    int getPartialExp();
    void giveExp(int exp);
    void giveExpExact(int exp);
    void setRestrict(boolean flag);
    boolean getRestrict();
}
