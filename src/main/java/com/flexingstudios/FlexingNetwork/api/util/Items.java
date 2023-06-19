package com.flexingstudios.flexingnetwork.api.util;

import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class Items {

    private Items() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static ItemStack name(Material mat, String name, String... lore) {
        return name(new ItemStack(mat), name, lore);
    }

    public static ItemStack name(ItemStack is, String name, String... lore) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Utils.colored(name));

        if (lore.length > 0)
            im.setLore(Arrays.asList(Utils.colored(lore)));

        is.setItemMeta(im);

        return is;
    }

    public static ItemStack name(Material material, String name, List<String> lore) {
        return name(new ItemStack(material), name, lore);
    }

    public static ItemStack name(ItemStack is, String name, List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Utils.colored(name));

        if (!lore.isEmpty())
            im.setLore(Utils.colored(lore));

        is.setItemMeta(im);

        return is;
    }

    public static ItemStack menuTitle(Material mat, String title, String... lore) {
        return menuTitle(new ItemStack(mat), title, lore);
    }

    public static ItemStack menuTitle(ItemStack is, String title, String... lore) {
        return name(is, "&3&l" + title + "", lore);
    }

    public static String getLore(ItemStack is, int index) {
        ItemMeta im = is.getItemMeta();
        if (!im.hasLore())
            return null;

        List<String> lore = im.getLore();
        if (index < 0)
            index += lore.size();

        if (index < 0 || index >= lore.size())
            return null;

        return lore.get(index);
    }

    public static ItemStack head(String player) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setOwner(player);
        is.setItemMeta(sm);

        return is;
    }

    public static ItemStack appendLore(ItemStack is, String... lore) {
        if (lore.length == 0)
            return is;

        return appendLore(is, Arrays.asList(lore));
    }

    public static ItemStack appendLore(ItemStack is, List<String> lore) {
        ItemMeta im = is.getItemMeta();
        List<String> lore0 = im.getLore();

        if (lore0 == null) {
            lore0 = Utils.colored(lore);
        } else {
            lore0.addAll(Utils.colored(lore));
        }

        im.setLore(lore0);
        is.setItemMeta(im);

        return is;
    }

    public static NBT nbt(ItemStack is) {
        return new NBT(is);
    }

    public static ItemStack glow(Material type) {
        return glow(new ItemStack(type));
    }

    public static ItemStack glow(ItemStack is) {
        return nbt(is).set("ench", new NBTTagList().get(1)).build();
    }

    public static ItemStack enchant(Material type, Object... params) {
        return enchant(new ItemStack(type), params);
    }

    public static ItemStack enchant(ItemStack is, Object... params) {
        if (params.length % 2 == 1)
            throw new IllegalArgumentException("params must be as pairs of {Enchantment, level}");

        for (int i = 0; i < params.length; i += 2)
            is.addUnsafeEnchantment((Enchantment) params[i], (Integer) params[i + 1]);

        return is;
    }

    public static class NBT {
        private final net.minecraft.server.v1_12_R1.ItemStack nms;

        private NBT(ItemStack is) {
            nms = CraftItemStack.asNMSCopy(is);
            if (!nms.hasTag())
                nms.setTag(new NBTTagCompound());
        }

        private NBTTagCompound getTag(String path, boolean write) {
            if (path.contains(".")) {
                String[] parts = path.split("\\.");
                NBTTagCompound t = nms.getTag();

                for (int i = 0; i < parts.length; i++) {
                    NBTTagCompound t0 = t.getCompound(parts[i]);

                    if (write && t.hasKey(parts[i]))
                        t.set(parts[i], t0);
                    t = t0;
                }

                return t;
            }

            return nms.getTag();
        }

        private String getKey(String path) {
            int index = path.lastIndexOf('.');
            if (index == -1)
                return path;
            return path.substring(index + 1);
        }

        public NBT set(String path, NBTBase val) {
            getTag(path, true).set(getKey(path), val);
            return this;
        }

        public NBT setByte(String path, byte val) {
            getTag(path, true).setByte(getKey(path), val);
            return this;
        }

        public NBT setByteArray(String path, byte[] val) {
            getTag(path, true).setByteArray(getKey(path), val);
            return this;
        }

        public NBT setLong(String path, long val) {
            getTag(path, true).setLong(getKey(path), val);
            return this;
        }

        public NBT setShort(String path, short val) {
            getTag(path, true).setShort(getKey(path), val);
            return this;
        }

        public NBT setString(String path, String val) {
            getTag(path, true).setString(getKey(path), val);
            return this;
        }

        public NBT setBoolean(String path, boolean val) {
            getTag(path, true).setBoolean(getKey(path), val);
            return this;
        }

        public NBT setDouble(String path, double val) {
            getTag(path, true).setDouble(getKey(path), val);
            return this;
        }

        public NBT setFloat(String path, float val) {
            getTag(path, true).setFloat(getKey(path), val);
            return this;
        }

        public NBT setInt(String path, int val) {
            getTag(path, true).setInt(getKey(path), val);
            return this;
        }

        public NBT setIntArray(String path, int[] val) {
            getTag(path, true).setIntArray(getKey(path), val);
            return this;
        }

        public NBT setStringList(String path, List<String> val) {
            String key = getKey(path);
            NBTTagList nbtList = new NBTTagList();
            for (String s : val)
                nbtList.add(new NBTTagString(s));
            getTag(path, true).set(key, nbtList);
            return this;
        }

        public NBTBase get(String path) {
            return getTag(path, false).get(getKey(path));
        }

        public byte getByte(String path) {
            return getTag(path, false).getByte(getKey(path));
        }

        public byte[] getByteArray(String path) {
            return getTag(path, false).getByteArray(getKey(path));
        }

        public boolean getBoolean(String path) {
            return getTag(path, false).getBoolean(getKey(path));
        }

        public double getDouble(String path) {
            return getTag(path, false).getDouble(getKey(path));
        }

        public float getFloat(String path) {
            return getTag(path, false).getFloat(getKey(path));
        }

        public int getInt(String path) {
            return getTag(path, false).getInt(getKey(path));
        }

        public int[] getIntArray(String path) {
            return getTag(path, false).getIntArray(getKey(path));
        }

        public long getLong(String path) {
            return getTag(path, false).getLong(getKey(path));
        }

        public short getShort(String path) {
            return getTag(path, false).getShort(getKey(path));
        }

        public String getString(String path) {
            return getTag(path, false).getString(getKey(path));
        }

        public NBT remove(String path) {
            getTag(path, false).remove(getKey(path));
            return this;
        }

        public boolean contains(String path) {
            return getTag(path, false).hasKey(getKey(path));
        }

        public NBTTagCompound getHandle() {
            return nms.getTag();
        }

        public ItemStack build() {
            return CraftItemStack.asCraftMirror(nms);
        }
    }
}
