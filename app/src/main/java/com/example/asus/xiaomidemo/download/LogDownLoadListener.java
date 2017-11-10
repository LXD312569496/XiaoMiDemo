package com.example.asus.xiaomidemo.download;

import android.util.Log;

import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;

import java.io.File;

/**
 * Created by asus on 2017/10/31.
 */

public class LogDownLoadListener extends DownloadListener{

    public LogDownLoadListener() {
        super("LogDownLoadListener");
    }

    @Override
    public void onStart(Progress progress) {
        Log.d("test","onStart:"+progress);
    }

    @Override
    public void onProgress(Progress progress) {
        Log.d("test","onProgress:"+progress);
    }

    @Override
    public void onError(Progress progress) {
        Log.d("test","onError:"+progress);
    }

    @Override
    public void onFinish(File file, Progress progress) {
        Log.d("test","onFinish:"+progress);
    }

    @Override
    public void onRemove(Progress progress) {
        Log.d("test","onRemove:"+progress);
    }
}
