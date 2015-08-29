package com.tokopedia.toped;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.toped.restclient.NetworkClient;
import com.tokopedia.toped.restclient.VolleyNetwork;
import com.tokopedia.toped.utils.GCMHandler;
import com.tokopedia.toped.utils.MySession;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class LauncherActivity extends Activity {

    private EditText vEmail;
    private EditText vPassword;
    private TextView vButtonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MySession.getInstance(this).getLoginId() != null) {
            startActivity(new Intent(this, ListingActivity.class));
            finish();
        }
        setContentView(R.layout.activity_launcher);
        initView();
        initListener();

    }

    private void initView() {
        vEmail = (EditText) findViewById(R.id.email_text);
        vPassword = (EditText) findViewById(R.id.password_text);
        vButtonSubmit = (TextView) findViewById(R.id.login_button);
    }

    private void initListener() {
        vButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(vEmail.getText().toString(), vPassword.getText().toString());
            }
        });
    }

    private void doLogin(String userID, String password) {
        NetworkClient networkClient = new NetworkClient(this, "http://128.199.227.169:8000/user/login");
        networkClient.addParam("user", userID);
        networkClient.addParam("password", password);
        networkClient.setMethod(VolleyNetwork.METHOD_POST);
        networkClient.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject json = new JSONObject(s);
                    MySession.storeLoginId(getBaseContext(), json.getString("id"));
                    MySession.storeUserType(getBaseContext(), json.getString("type"));
                    MySession.storeUserName(getBaseContext(), json.getString("user"));
                    startService(new Intent(getBaseContext(), GCMHandler.class));
                    startActivity(new Intent(getBaseContext(), ListingActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        networkClient.commit();

    }
}
