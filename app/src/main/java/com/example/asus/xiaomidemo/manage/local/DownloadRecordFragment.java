package com.example.asus.xiaomidemo.manage.local;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.xiaomidemo.R;

/**
 * Created by asus on 2017/11/2.
 * 下载记录
 */

public class DownloadRecordFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download_record, container, false);
    }
}
