package com.tokopedia.toped;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.tokopedia.toped.adapter.ListViewListing;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class DetailListingActivity extends Activity{

    GoogleMap mMap;
    MapFragment map;

    private ListViewListing.Model data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_listing);
        map = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map, map).commit();
        mMap = map.getMap();
        data = getIntent().getParcelableExtra("data");
    }
}
