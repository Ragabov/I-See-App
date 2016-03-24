package com.ragab.ahmed.educational.happenings.data.models;

/**
 * Created by Ragabov on 3/24/2016.
 */
public class Event {

    public String name;
    public String description;
    public String date;
    public String userName;

    public Event (String name, String description, String date, String userName)
    {
        this.name = name;
        this.description = description;
        this.date = date;
        this.userName = userName;
    }
}
