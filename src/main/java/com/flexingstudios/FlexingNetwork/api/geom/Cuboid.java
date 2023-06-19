package com.flexingstudios.flexingnetwork.api.geom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;

public class Cuboid implements Iterable<Vec3i> {
    protected Vec3i min;
    protected Vec3i max;

    public Cuboid() {
        min = new Vec3i();
        max = new Vec3i();
    }

    public Cuboid(Vec3i p1, Vec3i p2) {
        setBounds(p1, p2);
    }

    public void setBounds(Vec3i p1, Vec3i p2) {
        min = new Vec3i(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.min(p1.z, p2.z));
        max = new Vec3i(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y), Math.max(p1.z, p2.z));
    }

    public void shift(Vec3i change) {
        min = min.add(change);
        max = max.add(change);
    }

    public void expand(Vec3i change) {
        if (change.x > 0) {
            max = max.setX(Math.max(max.x + change.x, min.x));
        } else if (change.x < 0) {
            min = min.setX(Math.min(min.x + change.x, max.x));
        }

        if (change.y > 0) {
            max = max.setY(Math.max(max.y + change.y, min.y));
        } else if (change.y < 0) {
            min = min.setY(Math.min(min.y + change.y, max.y));
        }

        if (change.z > 0) {
            max = max.setZ(Math.max(max.z + change.z, min.z));
        } else if (change.z < 0) {
            min = min.setZ(Math.min(min.z + change.z, max.z));
        }
    }

    public void contract(Vec3i change) {
        if (change.x > 0) {
            min = min.setX(Math.min(min.x + change.x, max.x));
        } else if (change.x < 0) {
            max = max.setX(Math.max(max.x + change.x, min.x));
        }

        if (change.y > 0) {
            min = min.setY(Math.min(min.y + change.y, max.y));
        } else if (change.y < 0) {
            max = max.setY(Math.max(max.y + change.y, min.y));
        }

        if (change.z > 0) {
            min = min.setZ(Math.min(min.z + change.z, max.z));
        } else if (change.z < 0) {
            max = max.setZ(Math.max(max.z + change.z, min.z));
        }
    }

    public void inset(Vec3i change) {
        min = min.add(change);
        max = max.subtract(change);
        fixOverlap();
    }

    public void outset(Vec3i change) {
        max = max.add(change);
        min = min.subtract(change);
        fixOverlap();
    }

    public Vec3i getMin() {
        return min;
    }

    public Vec3i getMax() {
        return max;
    }

    public Vec3f getCenter() {
        return new Vec3f(min.add(max)).divide(2.0F);
    }

    public int getWidth() {
        return max.x - min.x + 1;
    }

    public int getHeight() {
        return max.y - min.y + 1;
    }

    public int getLength() {
        return max.z - min.z + 1;
    }

    public int getArea() {
        return (max.x - min.x + 1) * (max.y - min.y + 1) * (max.z - min.z + 1);
    }

    public boolean contains(Vec3i vec) {
        return vec.x >= min.x && vec.x <= max.x && vec.y >= min.y && vec.y <= max.y && vec.z >= min.z && vec.z <= max.z;
    }

    public boolean contains(Vec3f vec) {
        return vec.x >= min.x && vec.x < (max.x + 1) && vec.y >= min.y && vec.y < (max.y + 1) && vec.z >= min.z && vec.z < (max.z + 1);
    }

    public boolean contains(Vec3d vec) {
        return vec.x >= min.x && vec.x < (max.x + 1) && vec.y >= min.y && vec.y < (max.y + 1) && vec.z >= min.z && vec.z < (max.z + 1);
    }

    public Vec3i size() {
        return max.add(min.invert());
    }

    public Cuboid asFlatCuboid() {
        return new Cuboid(min, max.setY(min.y));
    }

    public Cuboid clone() {
        return new Cuboid(min, max);
    }

    public Iterator<Vec3i> iterator() {
        return new Iterator<Vec3i>() {
            private Vec3i min = Cuboid.this.getMin();
            private Vec3i max = Cuboid.this.getMax();
            private int nextX = min.x;
            private int nextY = min.y;
            private int nextZ = min.z;

            public boolean hasNext() {
                return nextX != Integer.MIN_VALUE;
            }

            public Vec3i next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                Vec3i answer = new Vec3i(nextX, nextY, nextZ);
                nextX = min.x;
                nextY = min.y;

                if (++nextX > max.x && ++nextY > max.y && ++nextZ > max.z)
                    nextX = Integer.MIN_VALUE;

                return answer;
            }
        };
    }

    public Spliterator<Vec3i> spliterator() {
        return Spliterators.spliterator(iterator(), getArea(), 0);
    }

    public String toString() {
        return "Cuboid[" +
                min.x + " " +
                min.y + " " +
                min.z + " to " +
                max.x + " " +
                max.y + " " +
                max.z
                + "]";
    }

    public boolean equals(Object obj) {
        if (obj instanceof Cuboid) {
            Cuboid other = (Cuboid)obj;
            return other.min.equals(min) && other.max.equals(max);
        }

        return false;
    }

    private void fixOverlap() {
        if (min.x > max.x) {
            int val = min.x + max.x / 2;

            min.setX(val);
            max.setX(val);
        }

        if (min.y > max.y) {
            int val = min.y + max.y / 2;

            min.setY(val);
            max.setY(val);
        }

        if (min.z > max.z) {
            int val = min.z + max.z / 2;

            min.setZ(val);
            max.setZ(val);
        }
    }
}

