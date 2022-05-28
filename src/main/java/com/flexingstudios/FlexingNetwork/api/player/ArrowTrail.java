package com.flexingstudios.FlexingNetwork.api.player;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ArrowTrail {
    HEARTS(1, "Сердечки", new ItemStack(Material.INK_SACK, 1, (short) 1));

    private static final TIntObjectHashMap<ArrowTrail> byId;
    private final int id;
    private final String name;
    private final ItemStack itemStack;

    static {
        byId = new TIntObjectHashMap<>();
        for (ArrowTrail trail : values()) {
            ArrowTrail old = byId.put(trail.id, trail);
            if (old != null)
                throw new RuntimeException("Duplicate trail id " + old + " and " + trail);
        }
    }

    ArrowTrail(int id, String name, ItemStack itemStack) {
        this.id = id;
        this.name = name;
        this.itemStack = itemStack;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public ItemStack getItem() {
        return this.itemStack.clone();
    }

    public static ArrowTrail byId(int id) {
        return byId.get(id);
    }
}
