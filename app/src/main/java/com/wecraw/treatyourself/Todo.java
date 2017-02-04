package com.wecraw.treatyourself;

/**
 * Created by will_000 on 1/27/2017.
 */

public class Todo {

    private String name;
    private int value;
    private long timeCreated;
    private int id;

    public Todo() {}

    public Todo(String name, int value, int id, long timeCreated) {
        this.name = name;
        this.value = value;
        this.id = id;
        this.timeCreated = timeCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
