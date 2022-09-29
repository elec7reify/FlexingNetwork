package com.flexingstudios.FlexingNetwork.api.score;

public class Record {
    private static int counter = 0;
    private final int id = counter++;
    SideScoreboard board;
    String name;
    int value;

    Record(SideScoreboard board, String name) {
        this.board = board;
        this.name = name;
        value = 0;
    }

    public void setName(String name) {
        if (this.name.equals(name))
            return;
        board.removeScore(this.name);
        this.name = name;
        update();
    }

    public void setValue(int value) {
        if (value == this.value)
            return;

        this.value = value;
        update();
    }

    public void set(String name, int value) {
        if (!this.name.equals(name)) {
            board.removeScore(this.name);
            this.name = name;
        }

        this.value = value;
        update();
    }

    public void update() {
        board.setScore(name, value);
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void remove() {
        board.remove(this);
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object obj) {
        return obj instanceof Record && obj.hashCode() == id;
    }

    public String toString() {
        return "Record " + id + "{" + name + " = " + value + "}";
    }
}
