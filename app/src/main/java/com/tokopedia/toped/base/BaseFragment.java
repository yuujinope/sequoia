package com.tokopedia.toped.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public abstract class BaseFragment extends Fragment{

    protected View view;

    protected abstract int getViewId();
    protected abstract Object createViewHolder(View view);
    protected abstract void bindViewHolder(Object viewHolder);
    protected abstract void onCreateView();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(getViewId(), container, false);
            view.setTag(createViewHolder(view));
        }else{
            bindViewHolder(view.getTag());
        }
        onCreateView();
        return view;
    }

    public View findViewById(int i){
        return view.findViewById(i);
    }
}
