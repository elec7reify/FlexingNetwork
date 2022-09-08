package com.flexingstudios.FlexingNetwork.api.geom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.NumberConversions;

public class Vec3i {
    public final int x;
    public final int y;
    public final int z;

    public Vec3i() {
        this(0, 0, 0);
    }

    public Vec3i(Vec3d vec) {
        x = (int)vec.x;
        y = (int)vec.y;
        z = (int)vec.z;
    }

    public Vec3i(Vec3f vec) {
        x = (int)vec.x;
        y = (int)vec.y;
        z = (int)vec.z;
    }

    public Vec3i(Location loc) {
        x = loc.getBlockX();
        y = loc.getBlockY();
        z = loc.getBlockZ();
    }

    public Vec3i(Block bloc) {
        x = bloc.getX();
        y = bloc.getY();
        z = bloc.getZ();
    }

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i setX(int x) {
        return new Vec3i(x, y, z);
    }

    public Vec3i setY(int y) {
        return new Vec3i(x, y, z);
    }

    public Vec3i setZ(int z) {
        return new Vec3i(x, y, z);
    }

    public Vec3i add(int val) {
        return new Vec3i(x + val, y + val, z + val);
    }

    public Vec3i add(Vec3i vec) {
        return new Vec3i(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vec3i add(int x, int y, int z) {
        return new Vec3i(this.x + x, this.y + y, this.z + z);
    }

    public Vec3i subtract(int val) {
        return new Vec3i(x - val, y - val, z - val);
    }

    public Vec3i subtract(Vec3i vec) {
        return new Vec3i(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vec3i subtract(int x, int y, int z) {
        return new Vec3i(this.x - x, this.y - y, this.z - z);
    }

    public Vec3i multiply(int val) {
        return new Vec3i(x * val, y * val, z * val);
    }

    public Vec3i multiply(Vec3i vec) {
        return new Vec3i(x * vec.x, y * vec.y, z * vec.z);
    }

    public Vec3i multiply(int x, int y, int z) {
        return new Vec3i(this.x * x, this.y * y, this.z * z);
    }

    public Vec3f divide(int val) {
        return new Vec3f((x / val), (y / val), (z / val));
    }

    public Vec3f divide(float val) {
        return new Vec3f(x / val, y / val, z / val);
    }

    public Vec3f divide(Vec3i vec) {
        return new Vec3f((x / vec.x), (y / vec.y), (z / vec.z));
    }

    public Vec3f divide(Vec3f vec) {
        return new Vec3f(x / vec.x, y / vec.y, z / vec.z);
    }

    public Vec3f divide(int x, int y, int z) {
        return new Vec3f((this.x / x), (this.y / y), (this.z / z));
    }

    public Vec3i invert() {
        return new Vec3i(-x, -y, -z);
    }

    public Vec3f normalize() {
        return divide(length());
    }

    public Vec3i abs() {
        return new Vec3i(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Vec3i clone() {
        return new Vec3i(x, y, z);
    }

    public float length() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float lengthSq() {
        return x * x + y * y + z * z;
    }

    public float distance(Vec3f vec) {
        return (float) Math.sqrt(NumberConversions.square((vec.x - x)) + NumberConversions.square((vec.z - z)) + NumberConversions.square((vec.z - z)));
    }

    public float distanceSq(Vec3f vec) {
        return (float) (NumberConversions.square((vec.x - x)) + NumberConversions.square((vec.z - z)) + NumberConversions.square((vec.z - z)));
    }

    public String toString() {
        return "Vec3i["
                + x + ", "
                + y + ", "
                + z + "]";
    }

    public int hashCode() {
        int result = x;

        result = 31 * result + y;
        result = 31 * result + z;

        return result;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj instanceof Vec3i) {
            Vec3i o = (Vec3i) obj;
            return (o.x == x && o.y == y && o.z == z);
        }

        return false;
    }
}
