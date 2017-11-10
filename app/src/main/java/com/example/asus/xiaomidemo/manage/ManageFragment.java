package com.example.asus.xiaomidemo.manage;

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
import android.widget.TextView;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.base.LazyLoadFragment;
import com.example.asus.xiaomidemo.manage.local.LocalApp;
import com.example.asus.xiaomidemo.manage.local.LocalAppAdapter;
import com.example.asus.xiaomidemo.xiaomi.RetrofitManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2017/10/23.
 * 管理的页面
 */

public class ManageFragment extends LazyLoadFragment {

    @BindView(R.id.manage_rv_local)
    RecyclerView mRvLocal;
    @BindView(R.id.manage_tv_updateNum)
    TextView mTvUpdateNum;

    private List<LocalApp> localAppList;
    private LocalAppAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_manage;
    }

    @Override
    public void initViews(View view) {
        ButterKnife.bind(this, view);

        initView();
    }

    @Override
    public void loadData(Bundle savedInstanceState) {

    }

    private void initView() {
        localAppList = new ArrayList<>();

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

                    //判断是否需要进行更新
                    isNeedUpdate(bean, i);

                } else {
                    //系统应用
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        adapter = new LocalAppAdapter(localAppList);
        mRvLocal.setAdapter(adapter);
        mRvLocal.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @OnClick({R.id.manage_ll_localapp, R.id.manage_btn_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.manage_ll_localapp:
                LocalAppActivity.startActivity(getActivity());
                break;
            case R.id.manage_btn_update:
                break;
        }
    }

    int updateNum = 0;

    //判断某个应用是否需要升级
    private void isNeedUpdate(final LocalApp localApp, final int position) {
        RetrofitManager.getInstance(getActivity()).getDetailInfo(localApp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        localApp.setNeedUpdate(aBoolean);
                        if (aBoolean) {
                            localAppList.add(localApp);
                            ++updateNum;
                            mTvUpdateNum.setText(updateNum + "个应用可以升级");
                        }
                    }
                });
    }


}
