package com.example.asus.xiaomidemo.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.xiaomidemo.R;

/**
 * Created by asus on 2017/10/27.
 * public class CollectFragment {
 * }
 */

public class CollectFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }
}
