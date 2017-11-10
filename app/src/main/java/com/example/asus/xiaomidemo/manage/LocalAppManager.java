package com.example.asus.xiaomidemo.manage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.example.asus.xiaomidemo.manage.local.LocalApp;
import com.example.asus.xiaomidemo.xiaomi.AppInfo;
import com.example.asus.xiaomidemo.xiaomi.RetrofitManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2017/10/27.
 */

public class LocalAppManager {
    private static LocalAppManager instance;

    private HashMap<String, LocalApp> hashMap;
    //key为包名，value为实体类，get方法为空说明没安装，对比版本号判断是否需要升级

    private LocalAppManager(Context context) {
        hashMap = new HashMap<>();
        List<LocalApp> list = getLocalAppList(context);
        for (int i = 0; i < list.size(); i++) {
            hashMap.put(list.get(i).getPackageName(), list.get(i));
        }
    }

    public static LocalAppManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocalAppManager(context);
        }
        return instance;
    }

    //是否已经安装
    public boolean isInstall(AppInfo appInfo) {
        String packageName = appInfo.getPackageName();
        if (hashMap.get(packageName) == null) {
            return false;
        } else {
            return true;
        }
    }

    //判断是否需要更新
    public synchronized boolean isNeedUpdate(AppInfo appInfo) {
        //已安装
        if (isInstall(appInfo)) {
//            Log.d("test",appInfo.getAppName());
            String packageName = appInfo.getPackageName();
            String versionCode = appInfo.getVersion();
            String currentVersion = hashMap.get(packageName).getVersion();
            if (versionCode.compareTo(currentVersion) > 0) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }


    //判断是否需要更新
    public boolean isNeedUpdate(Context context, LocalApp localApp) {

        final boolean[] flag = new boolean[1];
        RetrofitManager.getInstance(context).getDetailInfo(localApp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        flag[0] = aBoolean;
                    }
                });
        return flag[0];
    }


    //获取本地的全部应用
    public List<LocalApp> getLocalAppList(Context context) {
        List<LocalApp> localAppList = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);

        for (int i = 0; i < resolveInfoList.size(); i++) {
            LocalApp bean = new LocalApp();
            bean.setAppName(resolveInfoList.get(i).loadLabel(context.getPackageManager()).toString());


            String packageName = resolveInfoList.get(i).activityInfo.packageName;
            bean.setPackageName(packageName);
            Drawable icon = resolveInfoList.get(i).loadIcon(context.getPackageManager());
            bean.setIcon(icon);

            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
                String versionCode = context.getPackageManager()
                        .getPackageInfo(packageName, 0).versionName;
                bean.setVersion(versionCode);

                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    //第三方应用
                    localAppList.add(bean);
                } else {
                    //系统应用
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return localAppList;
    }


}
