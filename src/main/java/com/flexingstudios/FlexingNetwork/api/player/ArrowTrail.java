package com.flexingstudios.FlexingNetwork.api.player;

import com.flexingstudios.FlexingNetwork.api.Attributes;
import com.flexingstudios.FlexingNetwork.api.ItemRarity;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ArrowTrail {
    HEARTS(1, "Сердца", new ItemStack(Material.INK_SACK, 1, (short) 1), 50, ItemRarity.COMMON, null),
    DRIP_WATER(2, "Капли воды", new ItemStack(Material.WATER_BUCKET), 40, ItemRarity.COMMON, null),
    DRIP_LAVA(3, "Капли лавы", new ItemStack(Material.LAVA_BUCKET), 40, ItemRarity.COMMON, null),
    FIREWORK(4, "Фейерверк", new ItemStack(Material.INK_SACK, 1, (short) 15), 150, ItemRarity.COMMON, null),
    NOTE(5, "Ноты", new ItemStack(Material.NOTE_BLOCK, 1), 50, ItemRarity.COMMON, null),
    SLIME(6, "Слизь", new ItemStack(Material.SLIME_BALL, 1), 70, ItemRarity.COMMON, null),
    VILLAGER_HAPPY(7, "Зелёные звёзды", new ItemStack(Material.EMERALD, 1), 80, ItemRarity.COMMON, Attributes.NEW),
    ANGRY_VILLAGER(8, "Разбитые сердца", new ItemStack(Material.IRON_SWORD, 1), 150, ItemRarity.COMMON, Attributes.NEW),
    ENCHANTMENT_TABLE(9, "Зашифрованные символы", new ItemStack(Material.ENCHANTMENT_TABLE, 1), 200, ItemRarity.SPECIAL, Attributes.NEW, Attributes.UPDATED),
    ;
    
    private final int id;
    private final String name;
    private final ItemStack itemStack;
    private final int price;
    private ItemRarity rarity;
    private final Attributes[] attributes;
    private static final TIntObjectHashMap<ArrowTrail> byId;

    static {
        byId = new TIntObjectHashMap<>();
        for (ArrowTrail trail : values()) {
            ArrowTrail old = byId.put(trail.id, trail);
            if (old != null)
                throw new RuntimeException("Duplicate trail id " + old + " and " + trail);
        }
    }

    ArrowTrail(int id, String name, ItemStack itemStack, int price, ItemRarity rarity, Attributes... attributes) {
        this.id = id;
        this.name = name;
        this.itemStack = itemStack;
        this.price = price;
        this.rarity = rarity;
        this.attributes = attributes;
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

    public Attributes[] getAttributes() {
        return attributes;
    }

    public static ArrowTrail byId(int id) {
        return byId.get(id);
    }
}
