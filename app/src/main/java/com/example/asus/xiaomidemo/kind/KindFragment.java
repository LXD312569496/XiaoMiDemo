package com.example.asus.xiaomidemo.kind;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.xiaomidemo.R;
import com.example.asus.xiaomidemo.RefreshManager;
import com.example.asus.xiaomidemo.base.BaseFragment;
import com.example.asus.xiaomidemo.setting.SettingManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus on 2017/10/23.
 * 分类的页面
 */

public class KindFragment extends BaseFragment implements RefreshManager.RefreshInterface {
    @BindView(R.id.kind_rv_kind)
    RecyclerView mRvKind;

    private boolean isNotLoadBitmap;

    private KindAdapter adapter;
    private List<Kind> kindList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kind, container, false);
        ButterKnife.bind(this, view);
        RefreshManager.getInstance().register(this);

        initView();
        return view;
    }

    private void initView() {
        kindList = new ArrayList<>();
        kindList.add(new Kind("游戏", 15, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("实用工具", 5, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("影音视听", 27, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("聊天社交", 2, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("图书阅读", 7, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("学习教育", 12, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("效率办公", 10, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("时尚购物", 9, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("居家生活", 4, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("旅行交通", 3, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("摄影摄像", 6, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("医疗健康", 14, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("体育运动", 8, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("新闻资讯", 11, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("娱乐消遣", 13, R.mipmap.ic_launcher_round));
        kindList.add(new Kind("金融理财", 1, R.mipmap.ic_launcher_round));

        isNotLoadBitmap = SettingManager.getInstance(getActivity()).isSaveTraffic();

        adapter = new KindAdapter(kindList, isNotLoadBitmap);
        mRvKind.setAdapter(adapter);
        mRvKind.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadBitMap(boolean isLoad) {
        adapter.setNotLoadBitmap(isLoad);
//        isNotLoadBitmap=isLoad;
//        adapter = new KindAdapter(kindList,isNotLoadBitmap);
//        mRvKind.setAdapter(adapter);
//        mRvKind.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
