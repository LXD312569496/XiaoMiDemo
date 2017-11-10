package com.example.asus.xiaomidemo.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.download.DownLoadAcitivity;
import com.example.asus.xiaomidemo.featured.AppAdapter;
import com.example.asus.xiaomidemo.manage.LocalAppManager;
import com.example.asus.xiaomidemo.setting.SettingActivity;
import com.example.asus.xiaomidemo.xiaomi.AppInfo;
import com.example.asus.xiaomidemo.xiaomi.RetrofitManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by asus on 2017/10/31.
 */

public class SearchAcitivity extends AppCompatActivity {
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.search_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_rv_result)
    RecyclerView mRvResult;
    @BindView(R.id.search_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.search_rv_record)
    ListView mLvRecord;

    private List<AppInfo> mAppInfoList;
    private AppAdapter adapter;

    private List<String> recordList;
    private ArrayAdapter<String> arrayAdapter;
    private StringBuilder stringBuilder;

    private boolean isNotLoadBitmap = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        initView();
        initData();

    }

    private void initData() {

    }

    private void initView() {

        mAppInfoList = new ArrayList<>();
        final AppAdapter adapter = new AppAdapter(mAppInfoList, false, isNotLoadBitmap);
        mRvResult.setAdapter(adapter);
        mRvResult.setLayoutManager(new LinearLayoutManager(this));

        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadmore(false);

        stringBuilder = new StringBuilder();
        recordList = new ArrayList<>();
        String record = getSharedPreferences("record", MODE_PRIVATE).getString("record", "");
        stringBuilder.append(record);
        String[] temp = record.split(",");
        for (int i = 0; i < temp.length; i++) {
            recordList.add(temp[i]);
        }
        recordList = getNewList(recordList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recordList);
        mLvRecord.setAdapter(arrayAdapter);
        mLvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search(recordList.get(position));
            }
        });


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {


                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(SearchAcitivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
//                    search(query);
                    //进行搜索操作
                    mRefreshLayout.autoRefresh();

                    stringBuilder.append("," + query);
                    getSharedPreferences("record", MODE_PRIVATE).edit()
                            .putString("record", stringBuilder.toString()).apply();
                    recordList.add(query);
                    arrayAdapter.notifyDataSetChanged();

                    mLvRecord.setVisibility(View.GONE);
                    mRvResult.setVisibility(View.VISIBLE);

                    RetrofitManager.getInstance(SearchAcitivity.this).search(query)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<List<AppInfo>>() {
                                @Override
                                public void onCompleted() {
                                    for (int i = 0; i < mAppInfoList.size(); i++) {
                                        final int position = i;
                                        RetrofitManager.getInstance(SearchAcitivity.this).getDetailInfo(mAppInfoList.get(i))
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Subscriber<AppInfo>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        Log.d("test", "onCompleted");
                                                        adapter.notifyItemChanged(position);
                                                        if (mAppInfoList.size() - 1 == position) {
                                                            adapter.notifyDataSetChanged();
                                                            mRefreshLayout.finishRefresh();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Log.d("test", "onError:" + e.toString());
                                                        e.printStackTrace();
                                                    }

                                                    @Override
                                                    public void onNext(AppInfo appInfo) {
                                                        boolean isInstall = LocalAppManager.getInstance(SearchAcitivity.this).isInstall(appInfo);
                                                        appInfo.setInstall(isInstall);
                                                        //判断是否需要升级
                                                        if (isInstall) {
                                                            boolean isUpdate = LocalAppManager.getInstance(SearchAcitivity.this).isNeedUpdate(appInfo);
                                                            appInfo.setUpdate(isUpdate);
                                                        }
                                                        mAppInfoList.set(position, appInfo);
                                                    }
                                                });
                                    }

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(List<AppInfo> appInfos) {
                                    mAppInfoList.clear();
                                    mAppInfoList.addAll(appInfos);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }

                return true;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void search(String query) {
        mLvRecord.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.VISIBLE);

        //进行搜索操作
        mRefreshLayout.autoRefresh();

        stringBuilder.append("," + query);
        getSharedPreferences("record", MODE_PRIVATE).edit()
                .putString("record", stringBuilder.toString()).apply();
        recordList.add(query);
        recordList = getNewList(recordList);
        arrayAdapter.notifyDataSetChanged();

        RetrofitManager.getInstance(SearchAcitivity.this).search(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AppInfo>>() {
                    @Override
                    public void onCompleted() {
                        for (int i = 0; i < mAppInfoList.size(); i++) {
                            final int position = i;
                            RetrofitManager.getInstance(SearchAcitivity.this).getDetailInfo(mAppInfoList.get(i))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<AppInfo>() {
                                        @Override
                                        public void onCompleted() {
                                            Log.d("test", "onCompleted");
                                            adapter.notifyItemChanged(position);
                                            if (mAppInfoList.size() - 1 == position) {
                                                adapter.notifyDataSetChanged();
                                                mRefreshLayout.finishRefresh();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.d("test", "onError:" + e.toString());
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onNext(AppInfo appInfo) {
                                            boolean isInstall = LocalAppManager.getInstance(SearchAcitivity.this).isInstall(appInfo);
                                            appInfo.setInstall(isInstall);
                                            //判断是否需要升级
                                            if (isInstall) {
                                                boolean isUpdate = LocalAppManager.getInstance(SearchAcitivity.this).isNeedUpdate(appInfo);
                                                appInfo.setUpdate(isUpdate);
                                            }
                                            mAppInfoList.set(position, appInfo);
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<AppInfo> appInfos) {
                        mAppInfoList.clear();
                        mAppInfoList.addAll(appInfos);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_main_download:
                DownLoadAcitivity.startActivity(this);
                break;
            case R.id.menu_main_setting:
                SettingActivity.startActivity(this);
                break;
            case R.id.menu_main_login:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchAcitivity.class);
        context.startActivity(intent);
    }


    @OnClick(R.id.search_iv_back)
    public void onClick() {
        finish();
    }

    //set集合去重，不打乱顺序
    public List<String> getNewList(List<String> stringList) {

        Set set = new HashSet();
        List<String> newList = new ArrayList<>();
        for (String cd : stringList) {
            if (set.add(cd)) {
                newList.add(cd);
            }
        }
        return newList;
    }


}
