package com.example.asus.xiaomidemo.manage.local;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.detail.DetailActivity;
import com.example.asus.xiaomidemo.xiaomi.AppInfo;
import com.example.asus.xiaomidemo.xiaomi.RetrofitManager;

import java.util.List;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2017/10/26.
 */

public class LocalAppAdapter extends RecyclerView.Adapter<LocalAppAdapter.LocalAppViewHolder> {

    private Context context;

    private List<LocalApp> localAppList;

    public LocalAppAdapter(List<LocalApp> localAppList) {
        this.localAppList = localAppList;

    }

    @Override
    public LocalAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new LocalAppViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local_app, parent, false));
    }

    @Override
    public void onBindViewHolder(LocalAppViewHolder holder, int position) {
        final LocalApp localApp = localAppList.get(position);

        holder.ivIcon.setBackgroundDrawable(localApp.getIcon());
        holder.tvCompany.setText(localApp.getCompany());
        holder.tvName.setText(localApp.getAppName());
        holder.tvVersion.setText(localApp.getVersion());

        if (localApp.isNeedUpdate()) {
            holder.btnDownLoad.setText("升级");
            holder.btnDownLoad.setVisibility(View.VISIBLE);
            holder.tvUpdate.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.VISIBLE);
        } else {
            holder.tvUpdate.setVisibility(View.VISIBLE);
            holder.tvUpdate.setText("已安装");
            holder.btnDownLoad.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!localApp.isNeedUpdate()) {
                    Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    String pkg = "com.android.settings";
                    String cls = "com.android.settings.applications.InstalledAppDetails";
                    i.setComponent(new ComponentName(pkg, cls));
                    i.setData(Uri.parse("package:" + localApp.getPackageName()));
                    context.startActivity(i);
                }else {
                            AppInfo appInfo = new AppInfo();
                            appInfo.setUpdate(true);
                            appInfo.setDetailUrl("/details?id="+localApp.getPackageName());
                            RetrofitManager.getInstance(context).getDetailInfo(appInfo)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<AppInfo>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(AppInfo appInfo) {
                                            DetailActivity.startDetailActivity(context,appInfo);
                                        }
                                    });



                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return localAppList.size();
    }

    public static class LocalAppViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvCompany, tvName, tvVersion;
        TextView tvUpdate;
        Button btnDownLoad;
        CardView cardView;

        public LocalAppViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.local_app_iv_icon);
            tvCompany = (TextView) itemView.findViewById(R.id.local_app_tv_company);
            tvName = (TextView) itemView.findViewById(R.id.local_app_tv_name);
            tvVersion = (TextView) itemView.findViewById(R.id.local_app_version);
            tvUpdate = (TextView) itemView.findViewById(R.id.local_app_update);
            btnDownLoad = (Button) itemView.findViewById(R.id.local_app_btn_download);
            cardView = (CardView) itemView.findViewById(R.id.local_app_card_view);
        }
    }
}
