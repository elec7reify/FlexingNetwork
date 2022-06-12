package com.flexingstudios.FlexingNetwork.api.player;

import com.flexingstudios.Commons.player.Permission;
import com.flexingstudios.Commons.player.Rank;
import com.flexingstudios.FlexingNetwork.api.util.Utilities;
import org.bukkit.entity.Player;

import java.util.Map;

public interface NetworkPlayer {
    String getMeta(String paramString);
    void setMeta(String paramString1, String paramString2);
    String removeMeta(String paramString);
    boolean hasMeta(String paramString);
    Map<String, String> getMetaMap();
    int addCoins(int paramInt);
    void addCoinsExact(int paramInt);
    int getCoins();
    void toLobby();
    void toServer(String paramString);
    void takeCoins(int paramInt);
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
        Utilities.msg(getBukkitPlayer(), "&cОшибка в доступе: Для этого действия необходим статус " + rank.getColor() + rank.getName());
        return false;
    }

    default boolean hasAndNotify(Permission permission) {
        if (getRank().has(permission))
            return true;
        Utilities.msg(getBukkitPlayer(), "&cОшибка в доступе: У вас недостаточно прав для выполнения этого действия");
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
    void setArrowTrail(ArrowTrail paramArrowTrail);
    void unlockArrowTrail(ArrowTrail paramArrowTrail);
    long getLoginTime();
    int getLevel();
    int getTotalExp();
    int getPartialExp();
    void giveExp(int paramInt);
    void giveExpExact(int paramInt);
}
