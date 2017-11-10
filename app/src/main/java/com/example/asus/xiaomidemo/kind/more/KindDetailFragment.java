package com.example.asus.xiaomidemo.kind.more;

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
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2017/10/25.
 */

public class KindDetailFragment extends LazyLoadFragment implements RefreshManager.RefreshInterface {


    @BindView(R.id.detail_rv_result)
    RecyclerView mRvResult;
    @BindView(R.id.detail_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private int category;
    private int position;

    private List<AppInfo> mAppInfoList;
    private AppAdapter adapter;
    private int page = 1;

    private boolean isNotLoadBitmap;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_kind_detail;
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
        category = getArguments().getInt("category");
        position = getArguments().getInt("pagePosition");

        isNotLoadBitmap = SettingManager.getInstance(getActivity()).isSaveTraffic();

        if (position == 1) {
            mRefreshLayout.setEnableLoadmore(false);
        } else {
            mRefreshLayout.setEnableLoadmore(true);
        }

        mAppInfoList = new ArrayList<>();
        adapter = new AppAdapter(mAppInfoList, false, isNotLoadBitmap);
        mRvResult.setAdapter(adapter);
        mRvResult.setLayoutManager(new LinearLayoutManager(getActivity()));

        //1代表精品，2代表排行，3代表新品
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Observable<List<AppInfo>> observable = null;
                page = 1;
                switch (position) {
                    case 1:
                        observable = RetrofitManager.getInstance(getActivity()).getHotCatApp(category);
                        break;
                    case 2:
                        observable = RetrofitManager.getInstance(getActivity()).getCatTopList(category, page);
                        break;
                    case 3:
                        observable = RetrofitManager.getInstance(getActivity()).getCatNewList(category, page);
                        break;
                }

                if (observable != null) {
                    observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<List<AppInfo>>() {
                                @Override
                                public void onCompleted() {
                                    getDetailData(true);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.d("test", e.toString());
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(List<AppInfo> appInfos) {
                                    Log.d("test", "onNext" + appInfos.size());
                                    mAppInfoList.clear();
                                    mAppInfoList.addAll(appInfos);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Observable<List<AppInfo>> observable = null;
                switch (position) {
                    case 1:
                        observable = RetrofitManager.getInstance(getActivity()).getHotCatApp(category);
                        break;
                    case 2:
                        observable = RetrofitManager.getInstance(getActivity()).getCatTopList(category, ++page);
                        break;
                    case 3:
                        observable = RetrofitManager.getInstance(getActivity()).getCatNewList(category, ++page);
                        break;
                }
                observable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<AppInfo>>() {
                            @Override
                            public void onCompleted() {
                                getDetailData(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("test", e.toString());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<AppInfo> appInfos) {
                                mAppInfoList.addAll(appInfos);
                                adapter.notifyDataSetChanged();
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
                            adapter.notifyItemChanged(position);
                            if (mAppInfoList.size() - 1 == position) {
                                if (isRefresh) {
                                    page = 1;
                                    mRefreshLayout.finishRefresh();
                                } else {
                                    mRefreshLayout.finishLoadmore();
                                }
                                adapter.notifyDataSetChanged();
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

    public static KindDetailFragment newInstance(int category, int pagePosition) {
        KindDetailFragment fragment = new KindDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", category);
        bundle.putInt("pagePosition", pagePosition);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onRefresh() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onLoadBitMap(boolean isLoad) {
        adapter.setNotLoadBitmap(isLoad);
    }
}