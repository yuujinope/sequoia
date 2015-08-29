package com.tokopedia.toped;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.tokopedia.toped.adapter.ListViewListing;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class DetailListingActivity extends Activity{

    private class ViewHolder{
        TextView name;
        TextView toAddress;
    }

    GoogleMap mMap;
    MapFragment map;
    ViewHolder holder;

    private ListViewListing.Model data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_listing);
        holder = new ViewHolder();
        map = MapFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.map, map).commit();
        mMap = map.getMap();
        data = getIntent().getParcelableExtra("data");
        initView();
    }

    private void initView(){
        holder.name = (TextView)findViewById(R.id.name);
        holder.toAddress = (TextView)findViewById(R.id.address);
        holder.name.setText(data.name);
        holder.toAddress.setText(data.to);
    }
}
