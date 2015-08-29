package com.tokopedia.toped.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tokopedia.toped.R;

/**
 * Created by Tkpd_Eka on 8/30/2015.
 */
public class ListViewBidder {

    Context context;

    public static void append(Context context, LinearLayout list){
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_bidder, null, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,10);
        view.setLayoutParams(lp);
        list.addView(view);
    }

}
