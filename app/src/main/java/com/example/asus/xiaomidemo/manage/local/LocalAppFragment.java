package com.example.asus.xiaomidemo.manage.local;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.RefreshManager;
import com.example.asus.xiaomidemo.base.LazyLoadFragment;
import com.example.asus.xiaomidemo.xiaomi.RetrofitManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2017/10/26.
 */

public class LocalAppFragment extends LazyLoadFragment implements RefreshManager.RefreshInterface {

    @BindView(R.id.local_app_rv_app)
    RecyclerView mRvApp;
    @BindView(R.id.local_app_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private List<LocalApp> localAppList;
    private LocalAppAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_localapp;
    }

    @Override
    public void initViews(View view) {
        ButterKnife.bind(this, view);
        RefreshManager.getInstance().register(this);
        initView();
    }

    @Override
    public void loadData(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        for (int i = 0; i < resolveInfoList.size(); i++) {
            LocalApp bean = new LocalApp();
            bean.setAppName(resolveInfoList.get(i).loadLabel(getActivity().getPackageManager()).toString());


            String packageName = resolveInfoList.get(i).activityInfo.packageName;
            bean.setPackageName(packageName);
            Drawable icon = resolveInfoList.get(i).loadIcon(getActivity().getPackageManager());
            bean.setIcon(icon);

            try {
                PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(packageName, 0);
                String versionCode = getActivity().getPackageManager()
                        .getPackageInfo(packageName, 0).versionName;
                bean.setVersion(versionCode);

                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                    //第三方应用
                    localAppList.add(bean);

                    //判断是否需要进行更新
                    isNeedUpdate(bean, i);


                } else {
                    //系统应用
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        localAppList = new ArrayList<>();

        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
            }
        });
        mRefreshLayout.autoRefresh();


        adapter = new LocalAppAdapter(localAppList);
        mRvApp.setAdapter(adapter);
        mRvApp.setLayoutManager(new LinearLayoutManager(getActivity()));


    }


    //判断某个应用是否需要升级
    private void isNeedUpdate(final LocalApp localApp, final int position) {
        RetrofitManager.getInstance(getActivity()).getDetailInfo(localApp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        adapter.notifyItemChanged(position);

                        if (position == localAppList.size() - 1) {
                            mRefreshLayout.finishRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        localApp.setNeedUpdate(aBoolean);
                        localAppList.set(position, localApp);
                    }
                });

    }


    @Override
    public void onRefresh() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onLoadBitMap(boolean isLoad) {

    }
}
