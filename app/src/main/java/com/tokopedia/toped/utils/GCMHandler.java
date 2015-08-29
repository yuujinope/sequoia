package com.tokopedia.toped.utils;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.tokopedia.toped.restclient.NetworkClient;

import java.io.IOException;

/**
 * Created by ricoharisin on 8/30/15.
 */
public class GCMHandler extends IntentService {

    public static String TAG = "Reg";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GCMHandler() {
        super(TAG);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
            try {
                GetGCMId();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }


    private void GetGCMId() throws IOException {
        Log.i("STarting...", "STARTING NEEH");
        InstanceID id = InstanceID.getInstance(this);
        String token = id.getToken("51092408192", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        Log.i("Cool Log", token);
        sendToBackEnd(token);
    }

    private void sendToBackEnd(String token) {
        NetworkClient networkClient = new NetworkClient(this, "http://128.199.227.169:8000/push");
        networkClient.addParam("userid", MySession.getInstance(this).getLoginId());
        networkClient.addParam("regid", token);
        networkClient.addParam("type", MySession.getInstance(this).getUserType());
        networkClient.setMethod(NetworkClient.METHOD_POST);
        networkClient.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {

            }
        });
        networkClient.commit();
    }


}
