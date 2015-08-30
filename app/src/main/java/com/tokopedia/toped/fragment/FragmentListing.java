package com.tokopedia.toped.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tokopedia.toped.DetailListingActivity;
import com.tokopedia.toped.R;
import com.tokopedia.toped.adapter.ListViewListing;
import com.tokopedia.toped.base.BaseFragment;
import com.tokopedia.toped.restclient.NetworkClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class FragmentListing extends BaseFragment{

    private ListView list;
    private ListViewListing adapter;
    private ArrayList<ListViewListing.Model> models = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListViewListing(getActivity(), models);
    }

    @Override
    protected int getViewId() {
        return R.layout.list_order;
    }

    @Override
    protected Object createViewHolder(View view) {
        list = (ListView)findViewById(R.id.list);
        return list;
    }

    @Override
    protected void bindViewHolder(Object viewHolder) {
        list = (ListView)viewHolder;
    }

    @Override
    protected void onCreateView() {
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailListingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("bid_obj", models.get(i).bids);
                bundle.putString("list_id", models.get(i).ListID);
                bundle.putString("status", models.get(i).status);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        getList();
    }

    private void getList(){
        NetworkClient client = new NetworkClient(getActivity(), "http://128.199.227.169:8000/list");
        client.setMethod(client.METHOD_GET);
        client.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {
                try {
                    getResultToModels(new JSONArray(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        client.commit();
    }

    private void getResultToModels(JSONArray array)throws JSONException{
        int total = array.length();
        for(int i = 0; i<total; i++){
            models.add(getModel(array.getJSONObject(i)));
        }
        adapter.notifyDataSetChanged();
    }

    private ListViewListing.Model getModel(JSONObject item)throws JSONException{
        ListViewListing.Model model = new ListViewListing.Model();
        model.name = item.optString("title", "Botol aqua");
        model.to = item.getString("to");
        model.from = item.getString("from");
        model.userId = item.getString("userid");
        model.bids = item.getString("bids");
        model.ListID = item.getString("id");
        model.status = item.getString("status");
        String geo = item.getString("geo");
        String lat = geo.substring(0, geo.indexOf(','));
        String lon = geo.substring(geo.indexOf(',') + 1);
        model.latitude = Double.valueOf(lat);
        model.longitude = Double.valueOf(lon);
        model.time = "50";
        return model;
    }
}
