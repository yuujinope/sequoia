package com.tokopedia.toped.fragment;

import android.view.View;

import com.tokopedia.toped.R;
import com.tokopedia.toped.base.BaseFragment;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class FragmentOnMyWay extends BaseFragment{

    private class ViewHolder{

    }

    private ViewHolder holder;

    @Override
    protected int getViewId() {
        return R.layout.layout_onmyway;
    }

    @Override
    protected Object createViewHolder(View view) {
        holder = new ViewHolder();
        return holder;
    }

    @Override
    protected void bindViewHolder(Object viewHolder) {
        holder = (ViewHolder)viewHolder;
    }

    @Override
    protected void onCreateView() {

    }
}
