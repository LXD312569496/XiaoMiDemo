package com.example.asus.xiaomidemo.kind.more;

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
import android.widget.TextView;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.RefreshManager;
import com.example.asus.xiaomidemo.download.DownLoadAcitivity;
import com.example.asus.xiaomidemo.search.SearchAcitivity;
import com.example.asus.xiaomidemo.setting.SettingActivity;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2017/10/25.
 */

public class KindDetailActivity extends AppCompatActivity {
    @BindView(R.id.kind_sliding_tab_layout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.kind_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.search_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.detail_tv_title)
    TextView mTvTitle;

    private int category;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kind_detail);
        ButterKnife.bind(this);

        category = getIntent().getIntExtra("category", 0);
        title = getIntent().getStringExtra("title");

        setSupportActionBar(mToolbar);
        mTvTitle.setText(title);

        initView();
    }

    public static void startActivity(Context context, int category, String title) {
        Intent intent = new Intent(context, KindDetailActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("title", title);
        context.startActivity(intent);
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
                RefreshManager.getInstance().refreshAll();
                break;
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

    private void initView() {
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(KindDetailFragment.newInstance(category, 1));
        fragmentList.add(KindDetailFragment.newInstance(category, 2));
        fragmentList.add(KindDetailFragment.newInstance(category, 3));

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
        String titles[] = new String[]{"精品", "排行", "新品"};
        mSlidingTabLayout.setViewPager(mViewPager, titles);

    }


    @OnClick(R.id.search_iv_back)
    public void onClick() {
        finish();
    }

}
