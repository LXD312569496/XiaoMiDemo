package com.example.asus.xiaomidemo.download;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.base.BaseFragment;
import com.lzy.okserver.download.DownloadTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus on 2017/10/31.
 * 已下载
 */

public class DownloadFinishFragment extends BaseFragment {
    @BindView(R.id.downloadfinish_rv_downloadfinish)
    RecyclerView mRvDownloadfinish;

    private static List<DownloadTask> taskList;
    private static DownLoadFinishAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_downloadfinish, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        taskList=new ArrayList<>();
        adapter=new DownLoadFinishAdapter(taskList);
        mRvDownloadfinish.setAdapter(adapter);
        mRvDownloadfinish.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public static void addFinishTask(DownloadTask downTask){
        taskList.add(downTask);
        adapter.notifyDataSetChanged();
    }
}
