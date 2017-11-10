package com.example.asus.xiaomidemo.featured;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.detail.DetailActivity;
import com.example.asus.xiaomidemo.setting.SettingManager;
import com.example.asus.xiaomidemo.util.NetWorkUtil;
import com.example.asus.xiaomidemo.xiaomi.AppInfo;

import java.util.List;

/**
 * Created by asus on 2017/10/23.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.FeaturedViewHolder> {

    private List<AppInfo> mAppInfoList;

    private Context mContext;

    private boolean isShowOrder;

    private boolean isNotLoadBitmap = false;

    //order代表是否显示所在的位置，1.2.3.
    public AppAdapter(List<AppInfo> appInfoList, boolean isShowOrder, boolean isNotLoadBitmap) {
        this.mAppInfoList = appInfoList;
        this.isShowOrder = isShowOrder;
        this.isNotLoadBitmap = isNotLoadBitmap;
    }

    @Override
    public FeaturedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new FeaturedViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_app, parent, false));
    }

    @Override
    public void onBindViewHolder(FeaturedViewHolder holder, int position) {
        final AppInfo appInfo = mAppInfoList.get(position);
        if (!isNotLoadBitmap) {
            Glide.with(mContext).load(appInfo.getIcon()).asBitmap().placeholder(R.drawable.ic_place_holder).into(holder.ivIcon);
        } else {
            Glide.with(mContext).load(R.drawable.ic_place_holder).into(holder.ivIcon);
        }
        holder.tvCompany.setText(appInfo.getCompany());
        if (isShowOrder) {
            holder.tvName.setText((position + 1) + "." + appInfo.getAppName());
        } else {
            holder.tvName.setText(appInfo.getAppName());
        }
        holder.ratingBar.setRating((float) appInfo.getStar());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.startDetailActivity(mContext, appInfo);
            }
        });

        if (appInfo.isInstall()) {
            if (appInfo.isUpdate()) {
                holder.btnDownLoad.setText("升级");
                holder.cardView.setVisibility(View.VISIBLE);
                holder.btnDownLoad.setVisibility(View.VISIBLE);
                holder.tvInstalled.setVisibility(View.GONE);
                holder.btnDownLoad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //// TODO: 2017/11/9 ，没有apk的下载地址
//                    GetRequest<File> request = OkGo.<File>get(appInfo.getDownLoadUrl());
//                    DownloadTask downloadTask = OkDownload.request(appInfo.getDownLoadUrl(), request)//传入tag和下载请求
//                            .fileName(appInfo.getAppName() + ".apk")
//                            .extra1(appInfo.getIcon())//下载的额外数据
//                            .extra2(appInfo.getSize())
//                            .save()
//                            .register(new LogDownLoadListener());
//                    downloadTask.start();
                        if (SettingManager.getInstance(mContext).getOnlyWifi()) {
                            if (NetWorkUtil.isWifiConnected(mContext)) {
                                Toast.makeText(mContext, "开始下载，假装下载中", Toast.LENGTH_SHORT).show();
                            } else {
                                //设置了仅在Wifi状态下下载，并且wifi网络没有连接
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                            Toast.makeText(mContext, "开始下载，假装下载中", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            } else {
                holder.cardView.setVisibility(View.GONE);
                holder.tvInstalled.setText("已安装");
                holder.btnDownLoad.setVisibility(View.GONE);
                holder.tvInstalled.setVisibility(View.VISIBLE);
            }
        } else {
            holder.cardView.setVisibility(View.VISIBLE);
            holder.btnDownLoad.setText("下载");
            holder.btnDownLoad.setVisibility(View.VISIBLE);
            holder.tvInstalled.setVisibility(View.GONE);

            holder.btnDownLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// TODO: 2017/11/9 ，没有apk的下载地址
//                    GetRequest<File> request = OkGo.<File>get(appInfo.getDownLoadUrl());
//                    DownloadTask downloadTask = OkDownload.request(appInfo.getDownLoadUrl(), request)//传入tag和下载请求
//                            .fileName(appInfo.getAppName() + ".apk")
//                            .extra1(appInfo.getIcon())//下载的额外数据
//                            .extra2(appInfo.getSize())
//                            .save()
//                            .register(new LogDownLoadListener());
//                    downloadTask.start();
//                    Log.d("test", SettingManager.getInstance(mContext).getOnlyWifi() + "" + !NetWorkUtil.isWifiConnected(mContext));
                    if (SettingManager.getInstance(mContext).getOnlyWifi()) {
                        if (NetWorkUtil.isWifiConnected(mContext)) {
                            Toast.makeText(mContext, "开始下载，假装下载中", Toast.LENGTH_SHORT).show();
                        } else {
                            //设置了仅在Wifi状态下下载，并且wifi网络没有连接
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
                        Toast.makeText(mContext, "开始下载，假装下载中", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mAppInfoList.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvCompany, tvName;
        Button btnDownLoad;
        RatingBar ratingBar;
        TextView tvInstalled;
        CardView cardView;

        public FeaturedViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.app_iv_icon);
            tvCompany = (TextView) itemView.findViewById(R.id.app_tv_company);
            tvName = (TextView) itemView.findViewById(R.id.app_tv_name);
            btnDownLoad = (Button) itemView.findViewById(R.id.app_btn_download);
            ratingBar = (RatingBar) itemView.findViewById(R.id.app_rating_bar);
            tvInstalled = (TextView) itemView.findViewById(R.id.app_tv_installed);
            cardView = (CardView) itemView.findViewById(R.id.app_card_view);
        }
    }

    public void setNotLoadBitmap(boolean isNotLoadBitmap) {
        this.isNotLoadBitmap = isNotLoadBitmap;
        notifyDataSetChanged();
    }
}
