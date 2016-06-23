package com.ragab.ahmed.educational.happenings.ui.map.cache;

import android.support.v7.util.SortedList;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

/**
 * Created by Ragabov on 6/16/2016.
 */
public class EventsCacheManager {

    ArrayList<LatLngBounds> loadedRegions;

    public EventsCacheManager ()
    {
        loadedRegions = new ArrayList<>();
    }

    public boolean addRegion (LatLngBounds newRegion)
    {
        LatLng southEast = new LatLng(newRegion.southwest.latitude, newRegion.northeast.longitude);
        LatLng northWest = new LatLng(newRegion.northeast.latitude, newRegion.southwest.longitude);
        boolean isLoaded = false;

        for (int i = 0; i < loadedRegions.size(); i++) {
            LatLngBounds region = loadedRegions.get(i);

            if (region.contains(newRegion.northeast) || region.contains(newRegion.southwest) ||
                    region.contains(southEast) || region.contains(northWest)) {
                loadedRegions.set(i,
                        region.including(newRegion.northeast)
                                .including(newRegion.southwest)
                                .including(southEast)
                                .including(northWest)
                );
                if (loadedRegions.get(i).getCenter() == region.getCenter())
                    isLoaded = true;
            }
        }
        return isLoaded;
    }

    public void clusterRegion ()
    {
        for (int i = 0 ; i < loadedRegions.size(); i++)
        {
            LatLngBounds region = loadedRegions.get(i);
            LatLng southEast = new LatLng(region.southwest.latitude, region.northeast.longitude);
            LatLng northWest = new LatLng(region.southwest.latitude, region.northeast.longitude);


        }
    }
}
