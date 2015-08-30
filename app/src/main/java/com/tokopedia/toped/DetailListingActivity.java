package com.tokopedia.toped;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.tokopedia.toped.adapter.ListViewBidder;
import com.tokopedia.toped.adapter.ListViewListing;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class DetailListingActivity extends Activity{

    private class ViewHolder{
        TextView name;
        TextView toAddress;
        LinearLayout list;
    }

    public static class Model{}

    private ArrayList<Model> models;

    GoogleMap mMap;
    MapFragment map;
    ViewHolder holder;

    private ListViewListing.Model data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_listing);
        holder = new ViewHolder();
//        map = MapFragment.newInstance();
//        getFragmentManager().beginTransaction().add(R.id.map, map).commit();
//        mMap = map.getMap();
        holder.name = (TextView)findViewById(R.id.name);
        holder.name.setText("Kulit pisang");
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlert();
            }
        });
        holder.list = (LinearLayout)findViewById(R.id.list);
        ListViewBidder.append(this, holder.list);
        ListViewBidder.append(this, holder.list);
        ListViewBidder.append(this, holder.list);
        ListViewBidder.append(this, holder.list);
//        data = getIntent().getParcelableExtra("data");
//        initView();
    }

    private void initView(){
        holder.name = (TextView)findViewById(R.id.name);
        holder.toAddress = (TextView)findViewById(R.id.address);
        holder.name.setText(data.name);
        holder.toAddress.setText(data.to);
    }

    private void createAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
        alert.setTitle("Pick carrier");
        alert.setMessage("Are you sure you want to pick this carrier?");
        alert.setNegativeButton("No", null);
        alert.setPositiveButton("Yes", null);
        alert.create().show();
    }
}
