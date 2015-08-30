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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ricoharisin on 8/30/15.
 */
public class GCMHandler extends IntentService {

    public static String TAG = "Reg";
    private Boolean isRegistered = false;
    private String Token;
    private String IDPush;

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
        Token = id.getToken("51092408192", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        Log.i("Cool Log", Token);
        checkGCMId();
    }

    private void updateToBackEnd(String IDPush) {
        NetworkClient networkClient = new NetworkClient(this, "http://128.199.227.169:8000/push/"+IDPush);
        networkClient.addParam("userid", MySession.getInstance(this).getLoginId());
        networkClient.addParam("regid", Token);
        networkClient.addParam("type", MySession.getInstance(this).getUserType());
        networkClient.setMethod(NetworkClient.METHOD_PUT);
        networkClient.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {

            }
        });
        networkClient.commit();
    }

    private void sendToBackEnd() {
        NetworkClient networkClient = new NetworkClient(this, "http://128.199.227.169:8000/push");
        networkClient.addParam("userid", MySession.getInstance(this).getLoginId());
        networkClient.addParam("regid", Token);
        networkClient.addParam("type", MySession.getInstance(this).getUserType());
        networkClient.setMethod(NetworkClient.METHOD_POST);
        networkClient.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {

            }
        });
        networkClient.commit();
    }

    private void checkGCMId() {
        NetworkClient networkClient = new NetworkClient(this, "http://128.199.227.169:8000/push");
        networkClient.setMethod(NetworkClient.METHOD_GET);
        networkClient.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {
                String IDPush = "-1";
                try {
                    JSONArray jArray = new JSONArray(s);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = new JSONObject(jArray.getString(i));
                        if (json.getString("userid").equals(MySession.getInstance(getBaseContext()).getLoginId())) {
                            isRegistered = true;
                            IDPush = json.getString("id");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isRegistered) {
                    updateToBackEnd(IDPush);
                } else {
                    sendToBackEnd();
                }
            }
        });
        networkClient.commit();
    }


}
