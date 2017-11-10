package com.example.asus.xiaomidemo.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.RefreshManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2017/11/1.
 */

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.search_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting_switch_load_bitmap)
    Switch mSwitchLoadBitmap;
    @BindView(R.id.setting_switch_only_wifi)
    Switch mSwitchOnlyWifi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mSwitchLoadBitmap.setChecked(SettingManager.getInstance(SettingActivity.this).isSaveTraffic());
        mSwitchOnlyWifi.setChecked(SettingManager.getInstance(SettingActivity.this).getOnlyWifi());

        mSwitchLoadBitmap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingManager.getInstance(SettingActivity.this).setSaveTraffic(isChecked);
                RefreshManager.getInstance().LoadBitMap(isChecked);
            }
        });
        mSwitchOnlyWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingManager.getInstance(SettingActivity.this).setOnlyWifi(isChecked);
            }
        });

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.search_iv_back)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.search_iv_back, R.id.setting_ll_clear_history, R.id.setting_ll_clear_cache})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.setting_ll_clear_history:
                //清楚历史搜索记录
                clearHistory();
                break;
            case R.id.setting_ll_clear_cache:
                //清楚Glide缓存
                clearCache();
                break;
        }
    }

    private void clearCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setMessage("是否清除缓存内容（图片，安装包等）？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //清楚磁盘缓存和内存缓存
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.get(SettingActivity.this).clearDiskCache();
                            }
                        }).start();
                        Toast.makeText(SettingActivity.this, "缓存内容已清除", Toast.LENGTH_SHORT).show();
                        Glide.get(SettingActivity.this).clearMemory();
                    }
                })
                .create().show();
    }

    private void clearHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setMessage("是否清除历史搜索记录？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getSharedPreferences("record", MODE_PRIVATE).edit()
                                .putString("record", "").apply();
                        Toast.makeText(SettingActivity.this, "历史记录已清除", Toast.LENGTH_SHORT).show();
                    }
                })
                .create().show();
    }
}
