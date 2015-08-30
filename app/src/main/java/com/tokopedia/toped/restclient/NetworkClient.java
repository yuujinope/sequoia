package com.tokopedia.toped.restclient;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tokopedia.toped.R;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class NetworkClient extends VolleyNetwork{

    public interface NetworkClientSuccess{
        void onSuccess(String s);
    }

    private NetworkClientSuccess listener;
    private Dialog loadingDialog;

    public NetworkClient(Context context, String url) {
        super(context, url);
        loadingDialog = new Dialog(context, Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.dialog_loading);
    }

    public void setListener(NetworkClientSuccess listener){
        this.listener = listener;
    }

    @Override
    public void onRequestResponse(String response) {
        listener.onSuccess(response);
        Log.i("Network Response", response);
        try {
            loadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit() {
        System.out.println("Sending " + param.toString());
        super.commit();
        try {
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Response.ErrorListener onRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    loadingDialog.dismiss();
                    Snackbar.make(null, "An connection Error has occured", Snackbar.LENGTH_SHORT).setAction("Retry?", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            restartRequest();
                        }
                    }).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void loadingProgress(){

    }
}
