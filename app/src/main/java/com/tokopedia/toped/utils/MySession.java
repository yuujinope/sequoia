package com.tokopedia.toped.utils;

import android.content.Context;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class MySession {

    private static MySession instance;
    private static Context context;

    private String loginId;

    public static MySession getInstance(Context context){
        if(instance!=null) {
            return instance;
        }
        else{
            return createNewInstance(context.getApplicationContext());
        }
    }

    private static MySession createNewInstance(Context context){
        MySession instance = new MySession();
        instance.context = context;
        return instance;
    }

}
