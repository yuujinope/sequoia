package com.tokopedia.toped;

import android.content.Context;
import android.content.DialogInterface;
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

import com.tokopedia.toped.adapter.ListViewMyWay;
import com.tokopedia.toped.restclient.NetworkClient;
import com.tokopedia.toped.restclient.VolleyNetwork;
import com.tokopedia.toped.utils.MySession;
import com.tokopedia.toped.utils.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 8/30/2015.
 */
public class DetailMyWayActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView address;
    View back;
    LinearLayout list;

    public static class Model {
        String userId;
        String amount;
        String listId;
        String name;
        String bidId;
    }

    String data;
    String listId;
    ArrayList<Model> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_way);
        list = (LinearLayout) findViewById(R.id.list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        address = (TextView) toolbar.findViewById(R.id.address);
        back = toolbar.findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getStringExtra("data");
        listId = getIntent().getStringExtra("listid");
        getModelsFromData();
    }

    private void getModelsFromData() {
        try {
            JSONArray datas = new JSONArray(data);
            JSONObject data;
            for (int i = 0; i < datas.length(); i++) {
                Model model = getModel(datas.getJSONObject(i));
                append(this, list, model, i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Model getModel(JSONObject item) throws JSONException {
        Model model = new Model();
        model.amount = item.getString("amount");
        model.listId = item.getString("listid");
        model.userId = item.getString("userid");
        model.name = UserData.getInstance(this).getUserName(model.userId);
        model.bidId = item.getString("id");
        return model;
    }

    private void append(Context context, LinearLayout list, Model model, final int pos) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_seller, null, false);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView bid = (TextView) view.findViewById(R.id.bid);
        name.setText(model.name);
        bid.setText("$" + model.amount);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 20);
        view.setLayoutParams(lp);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createAlert(pos);
//            }
//        });
        list.addView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (MySession.getInstance(this).isSeller())
            getMenuInflater().inflate(R.menu.menu_my_way_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.join) {
            createBatchesDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createBatchesDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        alert.setTitle("Add batch");
        final EditText amtText = new EditText(this);
        amtText.setTextColor(Color.BLACK);
        alert.setView(amtText);
        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                placeBatches(amtText.getText().toString());
            }
        });
        alert.create().show();
    }

    private void placeBatches(String amount){

        NetworkClient networkClient = new NetworkClient(this, "http://128.199.227.169:8000/batch");
        networkClient.addParam("userid", MySession.getInstance(this).getLoginId());
        networkClient.addParam("listid", listId);
        networkClient.addParam("amount", amount);
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
