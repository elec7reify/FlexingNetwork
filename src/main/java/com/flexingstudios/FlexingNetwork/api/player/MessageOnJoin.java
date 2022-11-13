package com.flexingstudios.FlexingNetwork.api.player;

import com.flexingstudios.FlexingNetwork.api.util.Items;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public enum MessageOnJoin {
    TEST(1, "132", Items.name(new ItemStack(Material.INK_SACK, 1, (short) 1), "123123", "1232"), 60);

    private final int id;
    private final String msg;
    private final ItemStack itemStack;
    private final int price;
    private static final TIntObjectHashMap<MessageOnJoin> byId;

    static {
        byId = new TIntObjectHashMap<>();
        for (MessageOnJoin message : values()) {
            MessageOnJoin old = byId.put(message.id, message);
            if (old != null)
                throw new RuntimeException("Duplicate message id " + old + " and " + message);
        }
    }

    MessageOnJoin(int id, String msg, ItemStack itemStack, int price) {
        this.id = id;
        this.msg = msg;
        this.itemStack = itemStack;
        this.price = price;
    }

    public String getText() {
        return msg;
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
