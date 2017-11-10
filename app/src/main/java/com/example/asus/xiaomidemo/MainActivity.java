package com.example.asus.xiaomidemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.asus.xiaomidemo.download.DownLoadAcitivity;
import com.example.asus.xiaomidemo.featured.FeaturedFragment;
import com.example.asus.xiaomidemo.kind.KindFragment;
import com.example.asus.xiaomidemo.manage.LocalAppManager;
import com.example.asus.xiaomidemo.manage.ManageFragment;
import com.example.asus.xiaomidemo.rank.RankFragment;
import com.example.asus.xiaomidemo.search.SearchAcitivity;
import com.example.asus.xiaomidemo.setting.SettingActivity;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_sliding_tab_layout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.search_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        LocalAppManager.getInstance(this);

        initTabLayout();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initTabLayout() {
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FeaturedFragment());
        fragmentList.add(new RankFragment());
        fragmentList.add(new KindFragment());
        fragmentList.add(new ManageFragment());
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
        mViewPager.setOffscreenPageLimit(3);
        String titles[] = new String[]{"精品", "排行", "分类", "管理"};
        mSlidingTabLayout.setViewPager(mViewPager, titles);
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

}
