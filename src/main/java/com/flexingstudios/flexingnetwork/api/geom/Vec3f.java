package com.flexingstudios.flexingnetwork.api.geom;

import org.bukkit.Location;
import org.bukkit.util.NumberConversions;

public class Vec3f {
    public final float x;
    public final float y;
    public final float z;

    public Vec3f() {
        this(0.0F, 0.0F, 0.0F);
    }

    public Vec3f(Vec3i vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vec3f(Vec3d vec) {
        this((float)vec.x, (float)vec.y, (float)vec.z);
    }

    public Vec3f(Location loc) {
        this((float)loc.getX(), (float)loc.getY(), (float)loc.getZ());
    }

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f setX(float x) {
        return new Vec3f(x, this.y, this.z);
    }

    public Vec3f setY(float y) {
        return new Vec3f(this.x, y, this.z);
    }

    public Vec3f setZ(float z) {
        return new Vec3f(this.x, this.y, z);
    }

    public Vec3f add(float val) {
        return new Vec3f(this.x + val, this.y + val, this.z + val);
    }

    public Vec3f add(Vec3f vec) {
        return new Vec3f(this.x + vec.x, this.y + vec.y, this.z + vec.z);
    }

    public Vec3f add(float x, float y, float z) {
        return new Vec3f(this.x + x, this.y + y, this.z + z);
    }

    public Vec3f subtract(float val) {
        return new Vec3f(this.x - val, this.y - val, this.z - val);
    }

    public Vec3f subtract(Vec3f vec) {
        return new Vec3f(this.x - vec.x, this.y - vec.y, this.z - vec.z);
    }

    public Vec3f subtract(float x, float y, float z) {
        return new Vec3f(this.x - x, this.y - y, this.z - z);
    }

    public Vec3f multiply(float val) {
        return new Vec3f(this.x * val, this.y * val, this.z * val);
    }

    public Vec3f multiply(Vec3f vec) {
        return new Vec3f(this.x * vec.x, this.y * vec.y, this.z * vec.z);
    }

    public Vec3f multiply(float x, float y, float z) {
        return new Vec3f(this.x * x, this.y * y, this.z * z);
    }

    public Vec3f divide(float val) {
        return new Vec3f(this.x / val, this.y / val, this.z / val);
    }

    public Vec3f divide(Vec3f vec) {
        return new Vec3f(this.x / vec.x, this.y / vec.y, this.z / vec.z);
    }

    public Vec3f divide(float x, float y, float z) {
        return new Vec3f(this.x / x, this.y / y, this.z / z);
    }

    public Vec3f invert() {
        return new Vec3f(-x, -y, -z);
    }

    public Vec3f normalize() {
        return divide(length());
    }

    public Vec3f abs() {
        return new Vec3f(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Vec3f clone() {
        return new Vec3f(x, y, z);
    }

    public float length() {
        return (float) Math.sqrt((x * x + y * y + z * z));
    }

    public float lengthSq() {
        return x * x + y * y + z * z;
    }

    public float distance(Vec3f vec) {
        return (float)Math.sqrt(NumberConversions.square((vec.x - x)) + NumberConversions.square((vec.z - z)) + NumberConversions.square((vec.z - z)));
    }

    public float distanceSq(Vec3f vec) {
        return (float)(NumberConversions.square((vec.x - x)) + NumberConversions.square((vec.z - z)) + NumberConversions.square((vec.z - z)));
    }

    public String toString() {
        return "Vec3f[" + x + "," + y + "," + z + "]";
    }

    public int hashCode() {
        int result = (x != 0.0F) ? Float.floatToIntBits(x) : 0;

        result = 31 * result + ((y != 0.0F) ? Float.floatToIntBits(y) : 0);
        result = 31 * result + ((z != 0.0F) ? Float.floatToIntBits(z) : 0);

        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Vec3f) {
            Vec3f o = (Vec3f)obj;
            return (o.x == this.x && o.y == this.y && o.z == this.z);
        }

        return false;
    }
}
