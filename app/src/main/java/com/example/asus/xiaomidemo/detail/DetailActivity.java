package com.example.asus.xiaomidemo.detail;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.RefreshManager;
import com.example.asus.xiaomidemo.download.DownLoadAcitivity;
import com.example.asus.xiaomidemo.search.SearchAcitivity;
import com.example.asus.xiaomidemo.setting.SettingActivity;
import com.example.asus.xiaomidemo.setting.SettingManager;
import com.example.asus.xiaomidemo.util.NetWorkUtil;
import com.example.asus.xiaomidemo.xiaomi.AppInfo;
import com.hymane.expandtextview.ExpandTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2017/10/23.
 */

public class DetailActivity extends AppCompatActivity implements RefreshManager.RefreshInterface {


    AppInfo appInfo;
    @BindView(R.id.detail_iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.detail_tv_name)
    TextView mTvName;
    @BindView(R.id.detail_tv_company)
    TextView mTvCompany;
    @BindView(R.id.detail_tv_decription)
    ExpandTextView mTvDecription;
    @BindView(R.id.detail_tv_updateLog)
    ExpandTextView mTvUpdateLog;
    @BindView(R.id.detail_rv_image)
    RecyclerView mRvImage;
    @BindView(R.id.detail_tv_download)
    TextView mTvDownload;
    @BindView(R.id.detail_tv_title)
    TextView mTvTitle;
    @BindView(R.id.search_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.detail_tv_commentNum)
    TextView mTvCommentNum;
    @BindView(R.id.detail_tv_permission)
    TextView mTvPermission;

    private String appName;
    private String detailUrl;
    private ArrayList<AppInfo> sameAppList;

    private boolean isNotLoadBitmap;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        RefreshManager.getInstance().register(this);
        isNotLoadBitmap = SettingManager.getInstance(this).isSaveTraffic();

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

        setSupportActionBar(mToolbar);

        final AppInfo appInfo = getIntent().getParcelableExtra("appInfo");
        mTvName.setText(appInfo.getAppName());

        appName = appInfo.getAppName();
        detailUrl = appInfo.getDetailUrl();
        sameAppList = appInfo.getSameAppList();

        mTvTitle.setText(appInfo.getAppName());

        if (!isNotLoadBitmap) {
            Glide.with(this).load(appInfo.getIcon()).asBitmap().into(mIvIcon);
        } else {
            Glide.with(this).load(R.drawable.ic_place_holder).asBitmap().into(mIvIcon);
        }

        mTvCompany.setText(appInfo.getSize() + "|" + appInfo.getCompany());
        mTvCommentNum.setText(appInfo.getCommentNum());
        if (appInfo.getPermissionList() != null && appInfo.getPermissionList().size() != 0) {
            mTvPermission.setText("权限详情（" + appInfo.getPermissionList().size() + ")");
        }

        imageAdapter = new ImageAdapter(appInfo.getImgs(), isNotLoadBitmap);
        mRvImage.setAdapter(imageAdapter);
        mRvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CustomSnapHelper snapHelper = new CustomSnapHelper();
        snapHelper.attachToRecyclerView(mRvImage);

        mTvDecription.setContent(appInfo.getDescription());
        /**
         * 需要判断更新内容是否为空
         */
        if (TextUtils.isEmpty(appInfo.getUpdateLog())) {
            mTvUpdateLog.setVisibility(View.GONE);
        } else {
            mTvUpdateLog.setContent(appInfo.getUpdateLog());
        }

        mTvDownload.setBackgroundColor(Color.GREEN);
        if (appInfo.isInstall()) {
            if (appInfo.isUpdate()) {
                mTvDownload.setText("升级");
            } else {
                mTvDownload.setText("启动");
                mTvDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(appInfo.getPackageName());
                        startActivity(LaunchIntent);
                    }
                });
//                doStartApplicationWithPackageName(appInfo.getPackageName());

            }
        } else {
            mTvDownload.setText("下载");
        }
    }


    public static void startDetailActivity(Context context, AppInfo appInfo) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("appInfo", appInfo);
        context.startActivity(intent);
    }

    @OnClick({R.id.search_iv_back, R.id.detail_iv_collect, R.id.detail_tv_download, R.id.detail_iv_share,
            R.id.detail_tv_same_app})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.detail_iv_collect:
                break;
            case R.id.detail_tv_download:
                if (SettingManager.getInstance(DetailActivity.this).getOnlyWifi()) {
                    if (NetWorkUtil.isWifiConnected(DetailActivity.this)) {
                        download();
                    } else {
                        //设置了仅在Wifi状态下下载，并且wifi网络没有连接
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                        builder.setTitle("安装应用")
                                .setMessage("您在下载管理中开启了[仅在WLAN下下载]设置，暂时无法在线安装应用，请" +
                                        "连接WLAN网络后重试")
                                .setNeutralButton("知道了", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create()
                                .show();
                    }
                } else {
                    //假装在下载
                    download();
                }
                break;
            case R.id.detail_iv_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, "在小米应用商店发现一个有趣的APP：" + appName
                        + "，分享一下！传送门:" + detailUrl);
                startActivity(Intent.createChooser(intent, "分享给好友"));
                break;
            case R.id.detail_tv_same_app:
//                SameAppActivity.startActivity(this,appInfo.getSameAppList());
                Log.d("test", "同开发者应用：" + sameAppList.size());
                break;
        }
    }

    private void download() {
        Toast.makeText(DetailActivity.this, "开始下载，假装下载中", Toast.LENGTH_SHORT).show();
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                for (int i = 0; i <= 10; i++) {
                    try {
                        Thread.sleep(1000);
                        publishProgress(i * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                mTvDownload.setText(values[0] + "%");
                mTvDownload.setBackgroundColor(Color.GRAY);
                if (values[0] == 100) {
                    mTvDownload.setText("安装");
                    mTvDownload.setBackgroundColor(Color.GREEN);
                }
            }
        }.execute();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadBitMap(boolean isLoad) {
        imageAdapter.setNotLoadBitmap(isLoad);
    }


    /**
     * 通过包名启动应用
     *
     * @param packagename
     */
    private void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);
        }
    }
}
