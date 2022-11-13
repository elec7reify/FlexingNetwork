package com.flexingstudios.FlexingNetwork.api;

public enum ItemRarity {
    COMMON("Обычный"),
    UNCOMMON("Необычный"),
    CREATIVE("Креативный"),
    SPECIAL("Особый"),
    SEASON("Сезонный"),
    ;

    private final String tag;

    ItemRarity(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
