package com.wecraw.treatyourself;

import java.util.UUID;

/**
 * Created by will_000 on 1/3/2017.
 */

public class Event {
    private String name;
    private boolean timed;
    private int value;
    private boolean earns;
    private long timeCreated;
    private int id;

    //ctor for NEW EVENT - doesn't take ID as arg
    public Event(String name, boolean timed, boolean earns, int value, long timeCreated){
        this.name=name;
        this.timed=timed;
        this.earns=earns;
        this.value=value;
        this.timeCreated=timeCreated;
    }
    //ctor for UPDATING EVENT - takes ID as arg
    public Event(int id, String name, boolean timed, boolean earns, int value, long timeCreated){
        this.name=name;
        this.timed=timed;
        this.earns=earns;
        this.value=value;
        this.timeCreated=timeCreated;
        this.id=id;
    }

    //default ctor
    public Event(){}

    //getters and setters

    public void setName(String name){
        this.name=name;
    }
    public void setTimed(boolean timed){
        this.timed=timed;
    }
    public void setEarns(boolean earns){
        this.earns=earns;
    }
    public void setValue(int value){
        this.value=value;
    }
    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public boolean isTimed(){
        return this.timed;
    }
    public boolean isEarns() { return this.earns; }
    public int getValue(){
        return this.value;
    }
    public long getTimeCreated() {return this.timeCreated;}
    public int getId() {
        return id;
    }
}
