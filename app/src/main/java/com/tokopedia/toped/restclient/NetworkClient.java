package com.tokopedia.toped.restclient;

import android.content.Context;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class NetworkClient extends VolleyNetwork{
    public NetworkClient(Context context, String url) {
        super(context, url);
    }

    @Override
    public void onRequestResponse(String response) {
        System.out.println("KIRISAME" + response);
    }

}
