package com.tokopedia.toped.adapter;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tokopedia.toped.R;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 8/30/2015.
 */
public class ListViewMyWay extends BaseAdapter{

    public static class Model{
        public String ListID;
        public String userId;
        public String name;
        public String from;
        public String to;
        public String time;
        public double longitude;
        public double latitude;
        public String batches;
        public int batchesNumber;

    }

    private class ViewHolder{
        TextView name;
        TextView address;
        TextView bidder;
    }

    private ArrayList<Model> models;
    private ViewHolder holder;
    private Context context;

    public ListViewMyWay(Context context, ArrayList<Model> models){
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_myway, viewGroup, false);
            holder = new ViewHolder();
            holder.name = (TextView)view.findViewById(R.id.name);
            holder.address = (TextView)view.findViewById(R.id.address);
            holder.bidder = (TextView)view.findViewById(R.id.bidder);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        bindView(models.get(i));
        return view;
    }

    private void bindView(Model model){
        holder.name.setText(model.name);
        holder.address.setText(model.to);
        holder.bidder.setText("" + model.batchesNumber);
    }
}
