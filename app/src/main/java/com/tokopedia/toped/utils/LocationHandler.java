package com.tokopedia.toped.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class LocationHandler implements LocationListener {

    public interface LocationHandlerListener{
        void onLocationUpdate(double latitude, double longitude);
    }

    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5; // 5 minute TODO FIX THIS

    private LocationManager locationManager;
    private LocationHandlerListener listener;

    public boolean isRegistered = false;

    private Context context;

    public void init(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void setListener(LocationHandlerListener listener){
        this.listener = listener;
    }

    public void removeLocationUpdate() {
        try {
            locationManager.removeUpdates(this);
            isRegistered = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            isRegistered = true;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert();
        }

    }

    private void showAlert() { // TODO this is useless in prototype
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        System.out.println("KIRISAME Long: " + longitude + " Lat: " + latitude);
        listener.onLocationUpdate(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
