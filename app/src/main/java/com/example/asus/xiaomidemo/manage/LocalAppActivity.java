package com.example.asus.xiaomidemo.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.download.DownLoadAcitivity;
import com.example.asus.xiaomidemo.manage.local.*;
import com.example.asus.xiaomidemo.manage.local.CollectFragment;
import com.example.asus.xiaomidemo.search.SearchAcitivity;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2017/10/26.
 */

public class LocalAppActivity extends AppCompatActivity {

    @BindView(R.id.local_app_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.local_app_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.search_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localapp);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        initView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_main_search:
                SearchAcitivity.startActivity(this);
                break;
            case R.id.menu_main_refresh:
                break;
            case R.id.menu_main_download:
                DownLoadAcitivity.startActivity(this);
                break;
            case R.id.menu_main_setting:
                break;
            case R.id.menu_main_login:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new LocalAppFragment());
        fragmentList.add(new DownloadRecordFragment());
        fragmentList.add(new CollectFragment());

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        String titles[] = new String[]{"本地应用", "下载记录", "收藏"};
        mTabLayout.setViewPager(mViewPager, titles);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LocalAppActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.search_iv_back)
    public void onClick() {
        finish();
    }
}
