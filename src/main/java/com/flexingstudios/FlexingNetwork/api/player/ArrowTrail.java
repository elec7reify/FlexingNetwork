package com.flexingstudios.FlexingNetwork.api.player;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ArrowTrail {
    HEARTS(1, "Сердечки", new ItemStack(Material.INK_SACK, 1, (short) 1), 50),
    DRIP_WATER(2, "Капельки воды", new ItemStack(Material.WATER_BUCKET), 180),
    DRIP_LAVA(3, "Капельки лавы", new ItemStack(Material.LAVA_BUCKET), 40),
    FIREWORK(4, "Фейерверк", new ItemStack(Material.INK_SACK, 1, (short) 15), 150),
    NOTE(5, "Ноты", new ItemStack(Material.NOTE_BLOCK, 1), 50),
    SLIME(6, "Слизь", new ItemStack(Material.SLIME_BALL, 1), 70),
    VILLAGER_HAPPY(7, "Зелёные звёзды", new ItemStack(Material.EMERALD, 1), 80),
    ANGRY_VILLAGER(8, "Разбитые сердца", new ItemStack(Material.IRON_SWORD, 1), 150),
    ENCHANTMENT_TABLE(9, "Зашифрованные символы", new ItemStack(Material.ENCHANTMENT_TABLE, 1), 200),
    ;

    private static final TIntObjectHashMap<ArrowTrail> byId;
    private final int id;
    private String name;
    private final ItemStack itemStack;
    private final int price;

    static {
        byId = new TIntObjectHashMap<>();
        for (ArrowTrail trail : values()) {
            ArrowTrail old = byId.put(trail.id, trail);
            if (old != null)
                throw new RuntimeException("Duplicate trail id " + old + " and " + trail);
        }
    }

    ArrowTrail(int id, String name, ItemStack itemStack, int price) {
        this.id = id;
        this.name = name;
        this.itemStack = itemStack;
        this.price = price;
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

    public static ArrowTrail byId(int id) {
        return byId.get(id);
    }
}
