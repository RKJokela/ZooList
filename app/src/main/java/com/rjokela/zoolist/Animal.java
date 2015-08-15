package com.rjokela.zoolist;

/**
 * Created by Randon K. Jokela on 8/11/2015.
 */
public class Animal {

    public final static String MAMMAL = "mammal";
    public final static String BIRD = "bird";
    public final static String REPTILE = "reptile";

    private String name="";
    private String location = "";
    private String type="";
    public String getName() {
        return(name);
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getLocation() {
        return(location);
    }

    public void setLocation(String location) {
        this.location=location;
    }

    public String getType() {
        return(type);
    }

    public void setType(String type) {
        this.type=type;
    }

    public String toString() {
        return(getName());
    }
}
