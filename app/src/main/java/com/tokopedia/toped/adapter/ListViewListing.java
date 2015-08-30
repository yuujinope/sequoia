package com.tokopedia.toped.adapter;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.toped.R;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class ListViewListing extends BaseAdapter{

    public static class Model implements Parcelable{
        public String ListID;
        public String userId;
        public String name;
        public String from;
        public String to;
        public String time;
        public double longitude;
        public double latitude;
        public String bids;

        public Model(){}

        protected Model(Parcel in) {
            userId = in.readString();
            name = in.readString();
            from = in.readString();
            to = in.readString();
            time = in.readString();
            longitude = in.readDouble();
            latitude = in.readDouble();
            bids = in.readString();
        }

        public static final Creator<Model> CREATOR = new Creator<Model>() {
            @Override
            public Model createFromParcel(Parcel in) {
                return new Model(in);
            }

            @Override
            public Model[] newArray(int size) {
                return new Model[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(userId);
            parcel.writeString(name);
            parcel.writeString(from);
            parcel.writeString(to);
            parcel.writeString(time);
            parcel.writeDouble(longitude);
            parcel.writeDouble(latitude);
            parcel.writeString(bids);
        }
    }

    private class ViewHolder{
        TextView name;
        TextView address;
        TextView time;
    }

    private Context context;
    private ArrayList<Model> models;
    private ViewHolder holder;

    public ListViewListing(Context context, ArrayList<Model> models){
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item_listing, viewGroup, false);
            holder = new ViewHolder();
            holder.name = (TextView)view.findViewById(R.id.name);
            holder.address = (TextView)view.findViewById(R.id.address);
            holder.time = (TextView)view.findViewById(R.id.time);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }
        bindView(models.get(i));
        return view;
    }

    private void bindView(Model model){
        holder.name.setText(model.name);
        holder.time.setText(model.time + "m");
        holder.address.setText(model.to);
    }
}
