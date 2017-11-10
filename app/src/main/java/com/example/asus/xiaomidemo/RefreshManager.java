package com.example.asus.xiaomidemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/2.
 * 刷新所有界面的管理器
 */

public class RefreshManager {

    public interface RefreshInterface {
        void onRefresh();
        void onLoadBitMap(boolean isLoad);
    }

    private static RefreshManager instance;

    private List<RefreshInterface> interfaceList;

    private RefreshManager() {
        interfaceList = new ArrayList<>();
    }

    public static RefreshManager getInstance() {
        if (instance == null) {
            synchronized (RefreshManager.class) {
                if (instance == null) {
                    instance = new RefreshManager();
                }
            }
        }
        return instance;
    }

    public void register(RefreshInterface refreshInterface) {
        if (!interfaceList.contains(refreshInterface)) {
            interfaceList.add(refreshInterface);
        }
    }

    public void refreshAll() {
        for (RefreshInterface refreshInterface : interfaceList) {
            refreshInterface.onRefresh();
        }
    }

    //省流量模式，不加载图片
    public void LoadBitMap(boolean isLoad) {
        for (RefreshInterface refreshInterface : interfaceList) {
            refreshInterface.onLoadBitMap(isLoad);
        }
    }


}
