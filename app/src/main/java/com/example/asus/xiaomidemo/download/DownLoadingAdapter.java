package com.example.asus.xiaomidemo.download;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.xiaomidemo.R;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.List;

/**
 * Created by asus on 2017/10/31.
 */

public class DownLoadingAdapter extends RecyclerView.Adapter<DownLoadingAdapter.DownLoadViewHolder> {

    private List<DownloadTask> mDownLoadTaskList;

    private Context context;

    public DownLoadingAdapter(List<DownloadTask> mDownLoadTaskList) {
        this.mDownLoadTaskList = mDownLoadTaskList;
    }

    @Override
    public DownLoadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new DownLoadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donwloading, parent, false));
    }

    @Override
    public void onBindViewHolder(final DownLoadViewHolder holder, final int position) {
        final DownloadTask task = mDownLoadTaskList.get(position);
        Progress progress = task.progress;

        //extra1是图标，extra2是软件大小
        Glide.with(context).load(progress.extra1).asBitmap().into(holder.ivIcon);
        holder.tvName.setText(progress.fileName);
        String size = (String) progress.extra2;
        holder.tvSize.setText(size);

        int status = progress.status;
//        public static final int NONE = 0;         //无状态
//        public static final int WAITING = 1;      //等待
//        public static final int LOADING = 2;      //下载中
//        public static final int PAUSE = 3;        //暂停
//        public static final int ERROR = 4;        //错误
//        public static final int FINISH = 5;       //完成
        switch (status) {
            case Progress.NONE:
                holder.tvState.setText("");
                break;
            case Progress.WAITING:
//                holder.tvState.setText("等待");
                break;
            case Progress.LOADING:
//                holder.tvState.setText("下载中");
                holder.btnDownload.setText("暂停");
                holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task.pause();
                    }
                });
                break;
            case Progress.PAUSE:
                holder.tvState.setText("暂停");
                holder.btnDownload.setText("开始");
                holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        task.register(new DownloadListener(task.progress.url) {
                            @Override
                            public void onStart(Progress progress) {

                            }

                            @Override
                            public void onProgress(Progress progress) {

                                int percent = (int) (progress.currentSize * 100 / progress.totalSize);
                                holder.progressBar.setProgress(percent);
                                holder.tvState.setText(percent + "%");
                                notifyItemChanged(position);
                            }

                            @Override
                            public void onError(Progress progress) {

                            }

                            @Override
                            public void onFinish(File file, Progress progress) {
                                mDownLoadTaskList.remove(position);
                                notifyItemRemoved(position);
                                DownloadFinishFragment.addFinishTask(task);
                            }

                            @Override
                            public void onRemove(Progress progress) {

                            }
                        });
                        task.start();
                    }
                });
                break;
            case Progress.ERROR:
                holder.tvState.setText("错误");
                break;
            case Progress.FINISH:
                holder.tvState.setText("完成");
                break;
        }


    }

    @Override
    public int getItemCount() {
        return mDownLoadTaskList.size();
    }

    public static class DownLoadViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvSize, tvState;
        ProgressBar progressBar;
        Button btnDownload;

        public DownLoadViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.item_downloading_iv_icon);
            tvName = (TextView) itemView.findViewById(R.id.item_downloading_tv_name);
            tvSize = (TextView) itemView.findViewById(R.id.item_downloading_tv_size);
            tvState = (TextView) itemView.findViewById(R.id.item_downloading_tv_state);
            progressBar = (ProgressBar) itemView.findViewById(R.id.item_downloading_progress_bar);
            btnDownload = (Button) itemView.findViewById(R.id.item_downloading_btn_donwload);
        }
    }


}
