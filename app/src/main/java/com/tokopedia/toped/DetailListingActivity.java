package com.tokopedia.toped;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.tokopedia.toped.adapter.ListViewBidder;
import com.tokopedia.toped.adapter.ListViewListing;
import com.tokopedia.toped.restclient.NetworkClient;
import com.tokopedia.toped.restclient.VolleyNetwork;
import com.tokopedia.toped.utils.MySession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class DetailListingActivity extends AppCompatActivity {

    private Boolean isSeller = false;
    private String ListID;

    private class ViewHolder{
        TextView name;
        TextView toAddress;
        LinearLayout list;
    }

    public static class Model{
        String UserID;
        String UserName;
        String BidAmt;
        String BidID;
    }

    private ArrayList<Model> models = new ArrayList<>();

    GoogleMap mMap;
    MapFragment map;
    ViewHolder holder;

    private ListViewListing.Model data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSeller = MySession.getInstance(this).isSeller();
        String bidObj = getIntent().getExtras().getString("bid_obj");
        ListID = getIntent().getExtras().getString("list_id");
        Log.i("TAGGG", bidObj);
        setContentView(R.layout.activity_detail_listing);
        holder = new ViewHolder();
//        map = MapFragment.newInstance();
//        getFragmentManager().beginTransaction().add(R.id.map, map).commit();
//        mMap = map.getMap();
        holder.name = (TextView)findViewById(R.id.name);
        holder.name.setText("Kulit pisang");
        /*holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlert();
            }
        });*/
        holder.list = (LinearLayout)findViewById(R.id.list);
        parseBid(bidObj);
        createListView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        data = getIntent().getParcelableExtra("data");
//        initView();

    }

    private void initView(){
        holder.name = (TextView)findViewById(R.id.name);
        holder.toAddress = (TextView)findViewById(R.id.address);
        holder.name.setText(data.name);
        holder.toAddress.setText(data.to);
    }

    private void parseListId(String data) {
        try {
            JSONObject json = new JSONObject(data);
            ListID = json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseBid(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                Model temp = new Model();
                JSONObject tempJson = new JSONObject(jsonArray.getString(i));
                temp.UserID = tempJson.getString("userid");
                temp.BidAmt = tempJson.getString("amount");
                temp.BidID = tempJson.getString("id");
                models.add(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createListView() {
        for (int i = 0; i < models.size(); i++) {
            append(this, holder.list, models.get(i), i);
        }
    }

    private void createAlert(final int pos){
        if (isSeller) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
            alert.setTitle("Pick carrier");
            alert.setMessage("Are you sure you want to pick this carrier?");
            alert.setNegativeButton("No", null);
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    takeBid(pos);
                    //startActivity(new Intent(getBaseContext(), PickCarrierActivity.class));
                }
            });
            alert.create().show();
        }
    }

    private void createBidDialog(){
        if (!isSeller) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
            alert.setTitle("Place a bid");
            final EditText amtText = new EditText(this);
            amtText.setTextColor(Color.BLACK);
            alert.setView(amtText);
            alert.setPositiveButton("Place", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    placeBid(amtText.getText().toString());
                }
            });
            alert.create().show();
        }
    }

    private void append(Context context, LinearLayout list, Model model, final int pos){
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_bidder, null, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView bid = (TextView) view.findViewById(R.id.bid);
        name.setText(model.UserID);
        bid.setText("$"+model.BidAmt);
        Log.i("Append List", model.UserID);
        Log.i("Append List", model.BidAmt);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,10);
        view.setLayoutParams(lp);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlert(pos);
            }
        });
        list.addView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!isSeller) {
            getMenuInflater().inflate(R.menu.menu_detail_listing, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.bid) {
           createBidDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void placeBid(String amt) {
        NetworkClient networkClient = new NetworkClient(this, "http://128.199.227.169:8000/bid/new");
        networkClient.addParam("userid", MySession.getInstance(this).getLoginId());
        networkClient.addParam("listid", ListID);
        networkClient.addParam("amount", amt);
        networkClient.setMethod(VolleyNetwork.METHOD_POST);
        networkClient.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {
                Log.i("Network Response", s);
            }
        });
        networkClient.commit();
    }

    private void takeBid(int pos) {
        NetworkClient networkClient = new NetworkClient(this, "http://128.199.227.169:8000/order/complete");
        networkClient.addParam("carrierid", models.get(pos).UserID);
        networkClient.addParam("listid", ListID);
        networkClient.addParam("amount", models.get(pos).BidAmt);
        networkClient.addParam("bidid", models.get(pos).BidID);
        networkClient.setMethod(VolleyNetwork.METHOD_POST);
        networkClient.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {
                Log.i("Network Response", s);
            }
        });
        networkClient.commit();
    }
}
