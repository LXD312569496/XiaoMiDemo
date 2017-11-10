package com.example.asus.xiaomidemo.download;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.base.BaseFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus on 2017/10/31.
 * 下载中
 */

public class DownloadingFragment extends BaseFragment {
    @BindView(R.id.downloading_rv_downloading)
    RecyclerView mRvDownloading;

    private List<Progress> downloading;
    private List<DownloadTask> taskList;
    private DownLoadingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_downloading, container, false);
        ButterKnife.bind(this, view);

        initView();
        initData();
        return view;
    }

    private void initView() {
//        downloading = DownloadManager.getInstance().getDownloading();
//        taskList = OkDownload.restore(downloading);
//        Log.d("test","下载中元素个数:"+taskList.size());
        /**
         * todo：因为爬不到下载地址，所以先用假数据
         */
        taskList=new ArrayList<>();
        adapter=new DownLoadingAdapter(taskList);
        ((SimpleItemAnimator)mRvDownloading.getItemAnimator()).setSupportsChangeAnimations(false);
        mRvDownloading.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvDownloading.setAdapter(adapter);

    }

    private void initData() {
        //假数据
        GetRequest<File> request1= OkGo.<File>get("http://bmob-cdn-14815.b0.upaiyun.com/2017/10/31/78d7415e40f9ee0b80666ffb9a99383f.apk");
        DownloadTask task1= OkDownload.request("http://bmob-cdn-14815.b0.upaiyun.com/2017/10/31/78d7415e40f9ee0b80666ffb9a99383f.apk",request1)
                .extra1("http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0987a84321a3845b71932e80576498067a0d681e2")
                .extra2("27.55 M")
                .fileName("王者荣耀助手")
                .save();
        task1.progress.status=Progress.PAUSE;
        taskList.add(task1);

        GetRequest<File> request2= OkGo.<File>get("http://bmob-cdn-14815.b0.upaiyun.com/2017/10/31/69bf1c2b4097c1e7809f39fc3c3a5dfa.apk");
        DownloadTask task2= OkDownload.request("http://bmob-cdn-14815.b0.upaiyun.com/2017/10/31/69bf1c2b4097c1e7809f39fc3c3a5dfa.apk",request2)
                .extra1("http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0cbc45cde805bc27da84e2f8cf6874487fd40ac7e")
                .extra2("46.22 M")
                .fileName("作业帮")
                .save();
        task2.progress.status=Progress.PAUSE;
        taskList.add(task2);
        adapter.notifyDataSetChanged();



    }
}
