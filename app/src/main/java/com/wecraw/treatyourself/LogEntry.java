package com.wecraw.treatyourself;

/**
 * Created by will_000 on 1/26/2017.
 */

public class LogEntry {
    private int id;
    private String name;
    private long timeCreated;
    private Boolean earns;
    private Boolean timed;
    private Boolean todo;
    private int value;
    private int duration; //in minutes

    public LogEntry(String name, long timeCreated, Boolean earns, Boolean timed, int duration, int value, Boolean todo) {
        this.name = name;
        this.timeCreated = timeCreated;
        this.earns = earns;
        this.timed = timed;
        this.todo = todo;
        this.duration = duration;
        this.value = value;
    }
    public LogEntry(String name, long timeCreated, Boolean earns, Boolean timed, int duration, int value) {
        this.name = name;
        this.timeCreated = timeCreated;
        this.earns = earns;
        this.timed = timed;
        this.duration = duration;
        this.value = value;
        this.todo = false;
    }

    public LogEntry() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Boolean isEarns() {
        return earns;
    }

    public void setEarns(Boolean earns) {
        this.earns = earns;
    }

    public Boolean isTodo() {
        return todo;
    }

    public void setTodo(Boolean todo) {
        this.todo = todo;
    }

    public Boolean isTimed() {
        return timed;
    }

    public void setTimed(Boolean timed) {
        this.timed = timed;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
