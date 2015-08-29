package com.tokopedia.toped;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.toped.base.MainActivity;
import com.tokopedia.toped.utils.LocationHandler;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class MapActivity extends MainActivity {

    GoogleMap mMap;
    MapFragment map;
    LocationHandler locationHandler;

    @Override
    protected void inflateMainView(int mainViewId) {
        if (map == null) {
            map = MapFragment.newInstance();
            getFragmentManager().beginTransaction().add(mainViewId, map).commit();
        }
        setUpMapIfNeeded();
        if(locationHandler == null) {
            locationHandler = new LocationHandler();
            locationHandler.init(this);
            locationHandler.setListener(new LocationHandler.LocationHandlerListener() {
                @Override
                public void onLocationUpdate(double latitude, double longitude) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Location!"));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10);
                    mMap.animateCamera(cameraUpdate);
                    locationHandler.removeLocationUpdate();
                }
            });
            locationHandler.registerLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = map.getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
    }

    private void getLocation() {
    }
}
