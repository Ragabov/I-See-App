package com.ragab.ahmed.educational.happenings.ui.map.clustering;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.Event;
import com.ragab.ahmed.educational.happenings.ui.map.MainMapFragment;

import java.lang.reflect.Field;

/**
 * Created by Ragabov on 6/16/2016.
 */
public class CustomRenderer extends DefaultClusterRenderer<Event> {

    private MainMapFragment fragment;
    private Context mContext;
    int[] iconIds = new int[150];
    public CustomRenderer(Context context, GoogleMap map, ClusterManager<Event> clusterManager, MainMapFragment fragment) {
        super(context, map, clusterManager);
        this.mContext = context;
        this.fragment = fragment;
        initializeIds();
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<Event> cluster)
    {
        return (fragment.currentZoom < 19 && cluster.getSize() > 4);
    }

    @Override
    protected void onBeforeClusterItemRendered(Event item, MarkerOptions markerOptions) {
        markerOptions.title(item.name);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(iconIds[item.typeID]));
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    public void initializeIds()
    {
        String name = "";
        for (int i = 0 ; i < 100 ; i++) {
            name = "cat_"+i/10+"_"+i%10;
            final Field field;
            try {
                field = R.mipmap.class.getField(name);
                int id = field.getInt(null);
                iconIds[i] = id;
            } catch (NoSuchFieldException e) {

            } catch (IllegalAccessException e) {

            }
        }
    }
}