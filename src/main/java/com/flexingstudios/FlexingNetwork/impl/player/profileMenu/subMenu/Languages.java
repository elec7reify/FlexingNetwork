package com.flexingstudios.FlexingNetwork.impl.player.profileMenu.subMenu;

import com.flexingstudios.FlexingNetwork.api.util.ItemBuilder;
import com.flexingstudios.FlexingNetwork.api.util.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public enum Languages {
    RUSSIAN("ru", "https://textures.minecraft.net/texture/16eafef980d6117dabe8982ac4b4509887e2c4621f6a8fe5c9b735a83d775ad", new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/16eafef980d6117dabe8982ac4b4509887e2c4621f6a8fe5c9b735a83d775ad")).build(), false),
    ENGLISH("en", "https://textures.minecraft.net/texture/4cac9774da1217248532ce147f7831f67a12fdcca1cf0cb4b3848de6bc94b4", new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/4cac9774da1217248532ce147f7831f67a12fdcca1cf0cb4b3848de6bc94b4")).build(), true),
    UKRAINIAN("uk", "https://textures.minecraft.net/texture/28b9f52e36aa5c7caaa1e7f26ea97e28f635e8eac9aef74cec97f465f5a6b51", new ItemStack(Material.BOOK_AND_QUILL, 1), false),
    GERMAN("de", "https://textures.minecraft.net/texture/5e7899b4806858697e283f084d9173fe487886453774626b24bd8cfecc77b3f", new ItemStack(Material.BOOK_AND_QUILL, 1), true),
    ;

    private final String id;
    private final String skinURL;
    private final ItemStack is;
    private final boolean isNew;
    private static final Map<String, Languages> byId;

    static {
        byId = new HashMap<>();
        for (Languages lang : values()) {
            Languages old = byId.put(lang.getId(), lang);
            if (old != null)
                throw new RuntimeException("Duplicate language id " + old + " and " + lang);
        }
    }

    Languages(String id, String skinURL, ItemStack is, boolean isNew) {
        this.id = id;
        this.skinURL = skinURL;
        this.is = is;
        this.isNew = isNew;
    }

    public String getId() {
        return id;
    }

    public String getSkinURL() {
        return skinURL;
    }

    public ItemStack getItem() {
        return is;
    }

    public boolean isNew() {
        return isNew;
    }

    public static Languages byId(String id) {
        return byId.get(id);
    }
}
