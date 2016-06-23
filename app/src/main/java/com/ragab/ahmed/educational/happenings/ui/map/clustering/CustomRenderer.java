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

/**
 * Created by Ragabov on 6/16/2016.
 */
public class CustomRenderer extends DefaultClusterRenderer<Event> {

    private MainMapFragment fragment;
    private Context mContext;
    public CustomRenderer(Context context, GoogleMap map, ClusterManager<Event> clusterManager, MainMapFragment fragment) {
        super(context, map, clusterManager);
        this.mContext = context;
        this.fragment = fragment;
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<Event> cluster)
    {
        return (fragment.currentZoom < 19 && cluster.getSize() > 4);
    }

    @Override
    protected void onBeforeClusterItemRendered(Event item, MarkerOptions markerOptions) {
        markerOptions.title(item.name);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.accident_1));
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}