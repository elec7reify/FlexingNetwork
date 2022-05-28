package com.flexingstudios.Commons.player;

import java.util.EnumSet;

public enum Rank {
    PLAYER("&7", "Чикибамбонёнок", null, ""),
    VIP("&a&l", "Чикибамбвипка", "", ""),
    PREMIUM("&5&l", "Премиумбамбони", "", ""),
    CREATIVE("&9&l", "Чикикреатив", "", ""),
    MODERATOR("&e&l", "Чикимодератор", "", ""),
    CHIKIBAMBONI("&3&l", "Чикибамбони", "", ""),
    ADMINBAMBONI("&b&l", "Админобамбони", "", ""),
    CHIKIBAMBONYLA("&2&l", "Чикибамбонила", "", ""),
    SPONSOR("&d&l", "Спонсорбамбони", "", ""),
    OWNER("&6&l", "Чикивладелец", "", ""),
    CHIKIBOMBASTER("&c&l", "Чикибомбастер", "", ""),
    GOD("&4&l", "Чикибамбог", "", ""),
    TEAM("&9&l", "Кибербамбонёнок", "", ""),
    VADMIN("&4&l", "Младший кибербамбони", "Мл. кибербамбони", ""),
    SADMIN("&4&l", "Старший кибербамбони", "Ст. кибербамбони", ""),
    ADMIN("&4&l", "Главный кибербамбони", "Гл. кибербамбони", "");

    private String color;
    private String name;
    private String prefix;
    private String suffix;
    private EnumSet<Permission> permissions;

    static {
        CREATIVE.addPerm(Permission.SURVGAMEMODE);
        TEAM.addPerm(Permission.BUILDSERVER);
        TEAM.addPerm(Permission.VANISH);
        VADMIN.addAllPerms(TEAM);
        SADMIN.addAllPerms(VADMIN);
        ADMIN.addAllPerms(SADMIN);
    }

    Rank(String color, String name, String prefix, String suffix) {
        this.color = color;
        this.name = (name == null) ? "" : name;
        this.prefix = (prefix == null) ? "" : prefix;
        this.suffix = (suffix == null) ? "" : suffix;
        this.permissions = EnumSet.noneOf(Permission.class);
    }

    public void addPerm(Permission permission) {
        this.permissions.add(permission);
    }

    public void addAllPerms(Rank other) {
        this.permissions.addAll(other.permissions);
    }

    public boolean has(Rank rank) {
        return (compareTo(rank) >= 0);
    }

    public boolean has(Permission permission) {
        return this.permissions.contains(permission);
    }

    public String getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public String getDisplayName() {
        return this.color + this.name + "&r";
    }

    public static Rank getRank(String name) {
        if (name == null || name.isEmpty())
            return PLAYER;
        name = name.toUpperCase();
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ex) {
            return PLAYER;
        }
    }
}

