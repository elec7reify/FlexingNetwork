package com.flexingstudios.FlexingNetwork.api.player;

import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public enum MessageOnJoin {
    TEST(1, "132", new ItemStack(Material.INK_SACK, 1, (short) 1), 60);

    private static final TIntObjectHashMap<MessageOnJoin> byId;
    private final int id;
    private final String text;
    private final ItemStack itemStack;
    private final int price;

    static {
        byId = new TIntObjectHashMap<>();
        for (MessageOnJoin message : values()) {
            MessageOnJoin old = byId.put(message.id, message);
            if (old != null)
                throw new RuntimeException("Duplicate msg id " + old + " and " + message);
        }
    }

    MessageOnJoin(int id, String text, ItemStack itemStack, int price) {
        this.id = id;
        this.text = text;
        this.itemStack = itemStack;
        this.price = price;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public ItemStack getItem() {
        return itemStack.clone();
    }

    public int getPrice() {
        return price;
    }

    public static MessageOnJoin byId(int id) {
        return byId.get(id);
    }
}
