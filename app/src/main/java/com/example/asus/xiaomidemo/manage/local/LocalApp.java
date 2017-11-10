package com.example.asus.xiaomidemo.manage.local;

import android.graphics.drawable.Drawable;

/**
 * Created by asus on 2017/10/26.
 */

public class LocalApp {

    String appName;//名字
    Drawable icon;//图片
    String company;//公司
    String size;//软件的大小
    String version;//软件的版本号
    String packageName;//软件的包名

    boolean isNeedUpdate=false;

    public LocalApp() {
    }

    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
