package com.tokopedia.toped;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Tkpd_Eka on 8/30/2015.
 */
public class DetailMyWayActivity extends AppCompatActivity{

    Toolbar toolbar;
    TextView address;
    View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_way);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        address = (TextView)toolbar.findViewById(R.id.address);
        back = toolbar.findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
