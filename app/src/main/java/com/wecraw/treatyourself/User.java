package com.wecraw.treatyourself;

/**
 * Created by will_000 on 1/12/2017.
 */

public class User {

    private String name;
    private long points;
    private long timeCreated;
    private int id;

    public User(String name, long points, long timeCreated) {
        this.name = name;
        this.points = points;
        this.timeCreated = timeCreated;
        this.id = 0;
    }

    public User() {
        this.name = "Default Name";
        this.points = 0;
        this.timeCreated = 0;
        this.id = 0;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
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


