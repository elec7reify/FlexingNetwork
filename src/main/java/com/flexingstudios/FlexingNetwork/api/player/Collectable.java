package com.flexingstudios.FlexingNetwork.api.player;

public class Collectable {
    protected final NetworkPlayer player;
    private final String key;
    protected boolean[] data;

    public Collectable(NetworkPlayer player, String key, int size) {
        this(player, key, new boolean[size]);
    }

    public Collectable(NetworkPlayer player, String key, boolean[] defaults) {
        this.player = player;
        this.key = key;
        this.data = defaults;
    }

    public boolean get(int index) {
        if (index < 0 || index >= data.length)
            throw new IndexOutOfBoundsException("size=" + data.length);
        return data[index];
    }

    public boolean set(int index, boolean state) {
        if (index < 0 || index >= data.length)
            throw new IndexOutOfBoundsException("size=" + data.length);
        boolean old = data[index];
        if (old != state) {
            data[index] = state;
            save();
        }

        return old;
    }

    public int getTrueCount() {
        int count = 0;
        for (boolean b : data) {
            if (b)
                count++;
        }

        return count;
    }

    public int getFalseCount() {
        int count = 0;
        for (boolean b : data) {
            if (!b)
                count++;
        }

        return count;
    }

    public int getSize() {
        return data.length;
    }

    public void save() {
        char[] chars = new char[data.length];
        for (int i = 0; i < chars.length; i++)
            chars[i] = data[i] ? '1' : '0';
        player.setMeta(key, new String(chars));
    }

    public void load() {
        String meta = player.getMeta(key);
        if (meta != null) {
            char[] chars = meta.toCharArray();
            int size = (chars.length > data.length) ? data.length : chars.length;
            for (int i = 0; i < size; i++)
                this.data[i] = (chars[i] == '1');
        }
    }
}
