package com.tokopedia.toped.utils;

import android.content.Context;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class MySession {

    private static MySession instance;
    private static Context context;

    private String loginId;
    private String userType;
    private String userName;

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
        instance.loginId = getLoginIdFromCache(context);
        instance.userType = getUserTypeFromCache(context);
        instance.userName = getUserNameFromCache(context);
        return instance;
    }


    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    private static String getLoginIdFromCache(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, "SESSION");
        return cache.getString("login_id");
    }

    private static String getUserTypeFromCache(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, "SESSION");
        return cache.getString("user_type");
    }

    private static String getUserNameFromCache(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, "SESSION");
        return cache.getString("user_name");
    }

    public static void storeLoginId(Context context, String LoginID) {
        LocalCacheHandler cache = new LocalCacheHandler(context, "SESSION");
        cache.putString("login_id", LoginID);
        cache.commitEditor();
    }

    public static void storeUserType(Context context, String userType) {
        LocalCacheHandler cache = new LocalCacheHandler(context, "SESSION");
        cache.putString("user_type", userType);
        cache.commitEditor();
    }

    public static void storeUserName(Context context, String userName) {
        LocalCacheHandler cache = new LocalCacheHandler(context, "SESSION");
        cache.putString("user_name", userName);
        cache.commitEditor();
    }

    public String getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }
}
