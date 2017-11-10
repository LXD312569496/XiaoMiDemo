package com.example.asus.xiaomidemo.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.xiaomidemo.R;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.List;

/**
 * Created by asus on 2017/10/31.
 */

public class DownLoadFinishAdapter extends RecyclerView.Adapter<DownLoadFinishAdapter.DownLoadViewHolder> {

    private List<DownloadTask> taskList;

    private Context context;

    public DownLoadFinishAdapter(List<DownloadTask> taskList) {
        this.taskList = taskList;
    }

    @Override
    public DownLoadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new DownLoadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donwloadfinish, parent, false));
    }

    @Override
    public void onBindViewHolder(DownLoadViewHolder holder, int position) {
        final DownloadTask task = taskList.get(position);
        Progress progress = task.progress;

        //extra1是图标，extra2是软件大小
        Glide.with(context).load(progress.extra1).asBitmap().into(holder.ivIcon);
        holder.tvName.setText(progress.fileName);
        String size = (String) progress.extra2;
        holder.tvSize.setText(size);

        holder.btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(task.progress.filePath)),
                        "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class DownLoadViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvSize;
        Button btnInstall;

        public DownLoadViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.item_finish_iv_icon);
            tvName = (TextView) itemView.findViewById(R.id.item_finish_tv_name);
            tvSize = (TextView) itemView.findViewById(R.id.item_finish_tv_size);
            btnInstall = (Button) itemView.findViewById(R.id.item_finish_btn_install);
        }
    }
}
