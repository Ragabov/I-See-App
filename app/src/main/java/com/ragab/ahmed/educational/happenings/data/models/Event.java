package com.ragab.ahmed.educational.happenings.data.models;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Ragabov on 3/24/2016.
 */
public class Event implements ClusterItem {

    public static String historyConfirm, historyDisConfirm, historySubmit;
    public int id;
    public String name;
    public String description;
    public String date;
    public int userID;
    public double longitude;
    public double lattitude;
    public String pic_path;
    public int typeID;
    public boolean anonymous;
    public String userName;
    public int status;

    public Event (String name, String description, String date, String userName, double lat, double lng)
    {
        this.name = name;
        this.description = description;
        this.date = date;
        this.userName = userName;
        this.longitude = lng;
        this.lattitude = lat;
    }
    public Event (String name, String description, String date, String userName, int id)
    {
        this.name = name;
        this.description = description;
        this.date = date;
        this.userName = userName;
        this.id = id;
    }


    @Override
    public String toString ()
    {
        String formatStr;
        if (status == 2)
            formatStr = historySubmit;
        else if (status == 1)
            formatStr = historyConfirm;
        else
            formatStr = historyDisConfirm;

        return String.format(formatStr, this.name, this.date);
    }

    public static boolean isValidName (String s)
    {
        return s.length() > 3;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lattitude, longitude);
    }

}
