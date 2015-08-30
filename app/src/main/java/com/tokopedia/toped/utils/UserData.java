package com.tokopedia.toped.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tkpd_Eka on 8/30/2015.
 */
public class UserData {

    public Map<String, String> data;

    private static UserData instance;
    private static Context context;

    public static UserData getInstance(Context context){
        if(instance!=null)
            return instance;
        else
            return create(context);
    }

    private static UserData create(Context context){
        instance = new UserData();
        instance.context = context.getApplicationContext();
        instance.data = new HashMap<>();
        return instance;
    }

    public void addData(String id, String userName){
        data.put(id, userName);
    }

    public String getUserName(String id){
        return data.get(id);
    }

}
