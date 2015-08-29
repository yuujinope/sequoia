package com.tokopedia.toped.restclient;

import android.content.Context;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class NetworkClient extends VolleyNetwork{

    public interface NetworkClientSuccess{
        void onSuccess(String s);
    }

    private NetworkClientSuccess listener;

    public NetworkClient(Context context, String url) {
        super(context, url);
    }

    public void setListener(NetworkClientSuccess listener){
        this.listener = listener;
    }

    @Override
    public void onRequestResponse(String response) {
        listener.onSuccess(response);
    }

    @Override
    public void commit() {
        System.out.println("Sending " + param.toString());
        super.commit();
    }
}
