package com.ragab.ahmed.educational.happenings.data.models;

/**
 * Created by Ragabov on 3/24/2016.
 */
public class Event {

    public int id;
    public String name;
    public String description;
    public String date;
    public int userId;
    public double longitude;
    public double latitude;
    public String pic_path;
    public int type_id;
    public boolean anonymous;
    public String userName;

    public Event (String name, String description, String date, String userName, double lat, double lng)
    {
        this.name = name;
        this.description = description;
        this.date = date;
        this.userName = userName;
        this.longitude = lng;
        this.latitude = lat;
    }
    public Event (String name, String description, String date, String userName, int id)
    {
        this.name = name;
        this.description = description;
        this.date = date;
        this.userName = userName;
        this.id = id;
    }
}
