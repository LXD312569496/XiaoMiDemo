package com.example.asus.xiaomidemo.setting;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by asus on 2017/11/9.
 */

public class SettingManager {

    private static final String SP_SETTING = "setting";

    private static SettingManager instance;
    private Context mContext;
    private boolean isSaveTraffic;//是否开启省流量模式
    private boolean isOnlyWifi;//是否仅在WLAN连接的时候下载

    private SharedPreferences sharedPreferences;

    private SettingManager(Context context) {
        mContext = context;
    }

    public static SettingManager getInstance(Context context) {
        if (instance == null) {
            synchronized (SettingManager.class) {
                if (instance == null) {
                    instance = new SettingManager(context);
                }
            }
        }
        return instance;
    }

    private SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = mContext.getSharedPreferences(SP_SETTING, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }


    /**
     * 是否开启省流量模式
     *
     * @param isSaveTraffic
     */
    public void setSaveTraffic(boolean isSaveTraffic) {
        getSharedPreferences().edit().putBoolean("isSaveTraffic", isSaveTraffic).apply();
    }

    public boolean isSaveTraffic() {
        return getSharedPreferences().getBoolean("isSaveTraffic", false);
    }

    /**
     * 是否仅在WLAN连接的时候下载
     */
    public void setOnlyWifi(boolean isOnlyWifi) {
        getSharedPreferences().edit().putBoolean("isOnlyWifi", isOnlyWifi).apply();
    }

    public boolean getOnlyWifi() {
        return getSharedPreferences().getBoolean("isOnlyWifi", true);
    }


}
