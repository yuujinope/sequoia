package com.tokopedia.toped;

import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.toped.base.MainActivity;
import com.tokopedia.toped.restclient.NetworkClient;
import com.tokopedia.toped.utils.LocationHandler;

/**
 * Created by Tkpd_Eka on 8/30/2015.
 */
public class PostListingActivity extends MainActivity{
    View mainView;
    TextInputLayout nameLayout;
    TextInputLayout fromLayout;
    TextInputLayout toLayout;
    TextInputLayout timeLayout;
    EditText name;
    EditText from;
    EditText to;
    EditText time;

    LocationHandler locationHandler;

    String geoLoc;

    @Override
    protected void inflateMainView(int mainViewId) {

        FrameLayout frame = (FrameLayout)findViewById(mainViewId);
        mainView = LayoutInflater.from(this).inflate(R.layout.activity_post_listing, null, false);
        frame.addView(mainView);
        nameLayout = (TextInputLayout)mainView.findViewById(R.id.name);
        fromLayout = (TextInputLayout)mainView.findViewById(R.id.from);
        toLayout = (TextInputLayout)mainView.findViewById(R.id.to);
        timeLayout = (TextInputLayout)mainView.findViewById(R.id.time);
        name = (EditText)mainView.findViewById(R.id.name_text);
        from = (EditText)mainView.findViewById(R.id.from_text);
        to = (EditText)mainView.findViewById(R.id.to_text);
        time = (EditText)mainView.findViewById(R.id.time_text);
        nameLayout.setHint("Listing name");
        timeLayout.setHint("Maximum time");
        fromLayout.setHint("From");
        toLayout.setHint("To");

        locationHandler = new LocationHandler();
        locationHandler.init(this);
        locationHandler.setListener(new LocationHandler.LocationHandlerListener() {
            @Override
            public void onLocationUpdate(double latitude, double longitude) {
                geoLoc = latitude + "," + longitude;
                locationHandler.removeLocationUpdate();
            }
        });
        locationHandler.registerLocationUpdates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.send){
            NetworkClient network = new NetworkClient(this, "http://128.199.227.169:8000/list/new");
            network.setMethod(network.METHOD_POST);
            network.addHeader("Content-Type", "application/x-www-form-urlencoded");
            network.addParam("title", name.getText().toString());
            network.addParam("from", from.getText().toString());
            network.addParam("to", to.getText().toString());
            network.addParam("geo", geoLoc);
            network.addParam("userid", "123123");// TODO
            network.setListener(new NetworkClient.NetworkClientSuccess() {
                @Override
                public void onSuccess(String s) {
                    System.out.println("KIRISAME oke~ " + s);
                }
            });
            network.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
