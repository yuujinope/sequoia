package com.tokopedia.toped;

import android.content.Intent;
import android.util.Log;

import com.tokopedia.toped.base.MainActivity;
import com.tokopedia.toped.utils.GCMHandler;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class CarrierActivity extends MainActivity{

    @Override
    protected void inflateMainView(int mainViewId) {
        Log.i("COOOL", "cool cool");
        startService(new Intent(this, GCMHandler.class));
    }
}
