package com.tokopedia.toped.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tokopedia.toped.DetailMyWayActivity;
import com.tokopedia.toped.R;
import com.tokopedia.toped.adapter.ListViewMyWay;
import com.tokopedia.toped.base.BaseFragment;
import com.tokopedia.toped.restclient.NetworkClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class FragmentOnMyWay extends BaseFragment {

    private ListView list;
    private ListViewMyWay adapter;
    private ArrayList<ListViewMyWay.Model> models = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ListViewMyWay(getActivity(), models);
    }

    @Override
    protected int getViewId() {
        return R.layout.list_order;
    }

    @Override
    protected Object createViewHolder(View view) {
        list = (ListView) findViewById(R.id.list);
        return list;
    }

    @Override
    protected void bindViewHolder(Object viewHolder) {
        list = (ListView) viewHolder;
    }

    @Override
    protected void onCreateView() {
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DetailMyWayActivity.class);
                intent.putExtra("data", models.get(i).batches);
                intent.putExtra("listid", models.get(i).ListID);
                startActivity(intent);
            }
        });
        getOnMyWayList();
    }

    private void getOnMyWayList() {
        NetworkClient network = new NetworkClient(getActivity(), "http://128.199.227.169:8000/list");
        network.setMethod(network.METHOD_GET);
        network.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {
                try {
                    getResultToModels(new JSONArray(s));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        network.commit();
    }

    private void getResultToModels(JSONArray array) throws JSONException {
        int total = array.length();
        for (int i = 0; i < total; i++) {
            ListViewMyWay.Model model = getModel(array.getJSONObject(i));
            if (model != null)
                models.add(model);
        }
        adapter.notifyDataSetChanged();
    }

    private ListViewMyWay.Model getModel(JSONObject item) throws JSONException {
        ListViewMyWay.Model model = new ListViewMyWay.Model();
        boolean myway = item.getBoolean("onmyway");
        if (!myway)
            return null;
        JSONArray batches = item.getJSONArray("batches");
        model.name = item.optString("title", "Botol aqua");
        model.to = item.getString("to");
        model.from = item.getString("from");
        model.userId = item.getString("userid");
        model.batches = item.getString("batches");
        model.ListID = item.getString("id");
        String geo = item.getString("geo");
        String lat = geo.substring(0, geo.indexOf(','));
        String lon = geo.substring(geo.indexOf(',') + 1);
        model.latitude = Double.valueOf(lat);
        model.longitude = Double.valueOf(lon);
        model.time = "50";
        model.batchesNumber = batches.length();
        return model;
    }
}
