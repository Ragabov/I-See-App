package com.ragab.ahmed.educational.happenings.data.models;

/**
 * Created by Ragabov on 6/13/2016.
 */
public class FavouriteLocation {
    public long id;
    public String name;
    public double lattitude;
    public double longitude;

    public FavouriteLocation(String name, double lat, double lng)
    {
        this.name = name;
        this.lattitude = lat;
        this.longitude = lng;
    }
    @Override
    public String toString(){
        return name;
    }
}
