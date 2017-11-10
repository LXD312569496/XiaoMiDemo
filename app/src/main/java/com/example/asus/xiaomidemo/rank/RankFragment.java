package com.example.asus.xiaomidemo.rank;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.RefreshManager;
import com.example.asus.xiaomidemo.base.LazyLoadFragment;
import com.example.asus.xiaomidemo.featured.AppAdapter;
import com.example.asus.xiaomidemo.manage.LocalAppManager;
import com.example.asus.xiaomidemo.setting.SettingManager;
import com.example.asus.xiaomidemo.xiaomi.AppInfo;
import com.example.asus.xiaomidemo.xiaomi.RetrofitManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2017/10/23.
 * 排行的页面
 */

public class RankFragment extends LazyLoadFragment implements RefreshManager.RefreshInterface {
    @BindView(R.id.rank_rv_result)
    RecyclerView mRvResult;
    @BindView(R.id.rank_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private int page = 1;
    private List<AppInfo> mAppInfoList;
    private AppAdapter mAppAdapter;

    private boolean isNotLoadBitmap;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_rank;
    }

    @Override
    public void initViews(View view) {
        ButterKnife.bind(this, view);
        RefreshManager.getInstance().register(this);
        initView();
    }

    @Override
    public void loadData(Bundle savedInstanceState) {
        mRefreshLayout.autoRefresh();
    }


    private void initView() {
        isNotLoadBitmap = SettingManager.getInstance(getActivity()).isSaveTraffic();

        mAppInfoList = new ArrayList<>();
        mAppAdapter = new AppAdapter(mAppInfoList, true, isNotLoadBitmap);
        mRvResult.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvResult.setAdapter(mAppAdapter);

        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadmore(true);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                RetrofitManager.getInstance(getActivity()).getRankList(1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<AppInfo>>() {
                            @Override
                            public void onCompleted() {
                                getDetailData(true);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("test", e.toString());
                            }

                            @Override
                            public void onNext(List<AppInfo> appInfos) {

                                mAppInfoList.clear();
                                mAppInfoList.addAll(appInfos);
                                mAppAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                RetrofitManager.getInstance(getActivity()).getRankList(++page)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<AppInfo>>() {
                            @Override
                            public void onCompleted() {
                                getDetailData(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("test", e.toString());
                            }

                            @Override
                            public void onNext(List<AppInfo> appInfos) {
                                mAppInfoList.addAll(appInfos);
                                mAppAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });
    }

    private void getDetailData(final boolean isRefresh) {
        for (int i = 0; i < mAppInfoList.size(); i++) {
            final int position = i;
            RetrofitManager.getInstance(getActivity()).getDetailInfo(mAppInfoList.get(i))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AppInfo>() {
                        @Override
                        public void onCompleted() {
                            mAppAdapter.notifyItemChanged(position);
                            mAppAdapter.notifyDataSetChanged();
                            if (mAppInfoList.size() - 1 == position) {
                                if (isRefresh) {
                                    page = 1;
                                    mRefreshLayout.finishRefresh();
                                } else {
                                    mRefreshLayout.finishLoadmore();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("test", "onError:" + e.toString());
                        }

                        @Override
                        public void onNext(AppInfo appInfo) {
                            boolean isInstall = LocalAppManager.getInstance(getActivity()).isInstall(appInfo);
                            appInfo.setInstall(isInstall);
                            //判断是否需要升级
                            if (isInstall) {
                                boolean isUpdate = LocalAppManager.getInstance(getActivity()).isNeedUpdate(appInfo);
                                appInfo.setUpdate(isUpdate);
                            }
                            mAppInfoList.set(position, appInfo);
                        }
                    });

        }
    }


    @Override
    public void onRefresh() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onLoadBitMap(boolean isLoad) {
        mAppAdapter.setNotLoadBitmap(isLoad);
    }
}
