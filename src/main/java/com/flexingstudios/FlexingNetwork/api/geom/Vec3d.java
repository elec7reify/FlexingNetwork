package com.flexingstudios.FlexingNetwork.api.geom;

import org.bukkit.Location;
import org.bukkit.util.NumberConversions;

public class Vec3d {
    public final double x;
    public final double y;
    public final double z;

    public Vec3d() {
        this(0.0D, 0.0D, 0.0D);
    }

    public Vec3d(Vec3i vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vec3d(Vec3f vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vec3d(Location loc) {
        this(loc.getX(), loc.getY(), loc.getZ());
    }

    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d setX(double x) {
        return new Vec3d(x, y, z);
    }

    public Vec3d setY(double y) {
        return new Vec3d(x, y, z);
    }

    public Vec3d setZ(double z) {
        return new Vec3d(x, y, z);
    }

    public Vec3d add(double val) {
        return new Vec3d(x + val, y + val, z + val);
    }

    public Vec3d add(Vec3d vec) {
        return new Vec3d(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vec3d add(double x, double y, double z) {
        return new Vec3d(this.x + x, this.y + y, this.z + z);
    }

    public Vec3d subtract(double val) {
        return new Vec3d(x - val, y - val, z - val);
    }

    public Vec3d subtract(Vec3d vec) {
        return new Vec3d(x - vec.x, y - vec.y, z - vec.z);
    }

    public Vec3d subtract(double x, double y, double z) {
        return new Vec3d(this.x - x, this.y - y, this.z - z);
    }

    public Vec3d multiply(double val) {
        return new Vec3d(x * val, y * val, z * val);
    }

    public Vec3d multiply(Vec3d vec) {
        return new Vec3d(x * vec.x, y * vec.y, z * vec.z);
    }

    public Vec3d multiply(double x, double y, double z) {
        return new Vec3d(this.x * x, this.y * y, this.z * z);
    }

    public Vec3d divide(double val) {
        return new Vec3d(x / val, y / val, z / val);
    }

    public Vec3d divide(Vec3d vec) {
        return new Vec3d(x / vec.x, y / vec.y, z / vec.z);
    }

    public Vec3d divide(double x, double y, double z) {
        return new Vec3d(this.x / x, this.y / y, this.z / z);
    }

    public Vec3d invert() {
        return new Vec3d(-x, -y, -z);
    }

    public Vec3d normalize() {
        return divide(length());
    }

    public Vec3d abs() {
        return new Vec3d(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    public Vec3d clone() {
        return new Vec3d(x, y, z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double lengthSq() {
        return x * x + y * y + z * z;
    }

    public float distance(Vec3f vec) {
        return (float)Math.sqrt(NumberConversions.square(vec.x - x) + NumberConversions.square(vec.z - z) + NumberConversions.square(vec.z - z));
    }

    public float distanceSq(Vec3f vec) {
        return (float)(NumberConversions.square(vec.x - x) + NumberConversions.square(vec.z - z) + NumberConversions.square(vec.z - z));
    }

    public String toString() {
        return "Vec3d{" +
                x + "," +
                y + "," +
                z + "}";
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(x);
        int result = (int)(temp ^ temp >>> 32L);

        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int)(temp ^ temp >>> 32L);
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int)(temp ^ temp >>> 32L);

        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Vec3d) {
            Vec3d o = (Vec3d)obj;
            return (o.x == x && o.y == y && o.z == z);
        }

        return false;
    }
}
