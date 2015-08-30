package com.tokopedia.toped.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.tokopedia.toped.R;
import com.tokopedia.toped.adapter.ListViewMyWay;
import com.tokopedia.toped.base.BaseFragment;
import com.tokopedia.toped.restclient.NetworkClient;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class FragmentOnMyWay extends BaseFragment{

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
        return R.layout.layout_onmyway;
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

    }

    private void getOnMyWayList(){
        NetworkClient network = new NetworkClient(getActivity(), "http://128.199.227.169:8000/list");
    }
}
