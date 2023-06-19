package com.flexingstudios.flexingnetwork.api.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum  Materials {
    BED_BLOCK("BED_BLOCK", "RED_BED"),
    EYE_OF_ENDER("EYE_OF_ENDER", "ENDER_EYE"),
    WOOD_PICKAXE("WOOD_PICKAXE", "WOODEN_PICKAXE"),
    PLAYER_HEAD("SKULL_ITEM:3", "SKULL_ITEM:3");

    private String material_legacy;
    private String material;

    Materials(String material_legacy, String material) {
        this.material_legacy = material_legacy;
        this.material = material;
    }

    public ItemBuilder getStack() {
        String materialName;
        materialName = this.material;
        ItemStack stack;
        boolean hasDamage = materialName.contains(":");
        Material material;
        if(hasDamage) {
            short damage = Short.parseShort(materialName.split(":")[1]);
            material = Material.valueOf(materialName.split(":")[0]);
            stack = new ItemStack(material, 1, damage);
        }else {
            material = Material.valueOf(materialName);
            stack = new ItemStack(material);
        }
        return new ItemBuilder(stack);
    }

    public Material getMaterial() {
        String materialName;
        materialName = this.material;
        boolean hasDamage = materialName.contains(":");
        Material material;
        if(hasDamage) {
            material = Material.valueOf(materialName.split(":")[0]);
        }else {
            material = Material.valueOf(materialName);
        }
        return material;
    }
}
