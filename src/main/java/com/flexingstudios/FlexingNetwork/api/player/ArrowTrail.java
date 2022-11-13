package com.flexingstudios.FlexingNetwork.api.player;

import com.flexingstudios.FlexingNetwork.api.ItemRarity;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ArrowTrail {
    HEARTS(1, "Hearts", new ItemStack(Material.INK_SACK, 1, (short) 1), 50, ItemRarity.COMMON, false),
    DRIP_WATER(2, "Water droplets", new ItemStack(Material.WATER_BUCKET), 40, ItemRarity.COMMON, false),
    DRIP_LAVA(3, "Lava droplets", new ItemStack(Material.LAVA_BUCKET), 40, ItemRarity.COMMON, false),
    FIREWORK(4, "Firework", new ItemStack(Material.INK_SACK, 1, (short) 15), 150, ItemRarity.COMMON, false),
    NOTE(5, "Notes", new ItemStack(Material.NOTE_BLOCK, 1), 50, ItemRarity.COMMON, false),
    SLIME(6, "Slime", new ItemStack(Material.SLIME_BALL, 1), 70, ItemRarity.COMMON, false),
    VILLAGER_HAPPY(7, "Green stars", new ItemStack(Material.EMERALD, 1), 80, ItemRarity.COMMON, false),
    ANGRY_VILLAGER(8, "Broken hearts", new ItemStack(Material.IRON_SWORD, 1), 150, ItemRarity.COMMON, false),
    ENCHANTMENT_TABLE(9, "Encrypted symbols", new ItemStack(Material.ENCHANTMENT_TABLE, 1), 200, ItemRarity.SPECIAL, true),
    ;
    
    private final int id;
    private final String name;
    private final ItemStack itemStack;
    private final int price;
    private ItemRarity rarity;
    private boolean isNew;
    private static final TIntObjectHashMap<ArrowTrail> byId;

    static {
        byId = new TIntObjectHashMap<>();
        for (ArrowTrail trail : values()) {
            ArrowTrail old = byId.put(trail.id, trail);
            if (old != null)
                throw new RuntimeException("Duplicate trail id " + old + " and " + trail);
        }
    }

    ArrowTrail(int id, String name, ItemStack itemStack, int price, ItemRarity rarity, boolean isNew) {
        this.id = id;
        this.name = name;
        this.itemStack = itemStack;
        this.price = price;
        this.isNew = isNew;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public int getPrice() {
        return price;
    }

    public ItemRarity getRarity() {
        return rarity;
    }

    public ArrowTrail setRarity(ItemRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public boolean isNew() {
        return isNew;
    }

    public ArrowTrail setNew(boolean flag) {
        isNew = flag;
        return this;
    }

    public static ArrowTrail byId(int id) {
        return byId.get(id);
    }
}
