package com.flexingstudios.FlexingNetwork.api.player;

import com.flexingstudios.FlexingNetwork.api.ItemRarity;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public enum MessageOnJoin {
    MESSAGE_1(1, "&8&l[&c&l+&8&l] {rank} &7{player} &aзалетел на тусу", new ItemStack(Material.INK_SACK, 1), 60, ItemRarity.COMMON),
    MESSAGE_2(2, "&f+ {rank} &7{player} &fзамёрз, но решил согреться на нашем сервере!", new ItemStack(Material.INK_SACK, 1), 60, ItemRarity.COMMON),
    MESSAGE_3(3, "{rank} &7{player} &fпришёл к своей цели: отвлечься на &9&lFlexing&f&lWorld", new ItemStack(Material.INK_SACK, 1), 60, ItemRarity.COMMON),
    MESSAGE_4(4, "{rank} &7{player} &cвышел из Dungeon! Остерегайтесь его!", new ItemStack(Material.INK_SACK, 1), 60, ItemRarity.COMMON),
    ;

    private final int id;
    private final String message;
    private final ItemStack itemStack;
    private final int price;
    private ItemRarity rarity;
    private static final TIntObjectHashMap<MessageOnJoin> byId;

    static {
        byId = new TIntObjectHashMap<>();
        for (MessageOnJoin message : values()) {
            MessageOnJoin old = byId.put(message.id, message);
            if (old != null)
                throw new RuntimeException("Duplicate message id " + old + " and " + message);
        }
    }

    MessageOnJoin(int id, String message, ItemStack itemStack, int price, ItemRarity rarity) {
        this.id = id;
        this.message = message;
        this.itemStack = itemStack;
        this.price = price;
        this.rarity = rarity;
    }

    public String getMessage() {
        return message;
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

    public ItemRarity getRarity() {
        return rarity;
    }

    public MessageOnJoin setRarity(ItemRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public static MessageOnJoin byId(int id) {
        return byId.get(id);
    }
}
