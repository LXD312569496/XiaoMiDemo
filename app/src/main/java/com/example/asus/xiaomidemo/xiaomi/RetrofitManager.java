package com.example.asus.xiaomidemo.xiaomi;

import android.content.Context;

import com.example.asus.xiaomidemo.manage.local.LocalApp;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by asus on 2017/10/23.
 */

public class RetrofitManager {


    private static RetrofitManager INSTANCE;

    private static Context mContext;
    private OkHttpClient client;
    private Retrofit retrofit;
    private XiaoMiInterface mXiaoMiInterface;

    private RetrofitManager(Context context) {
        mContext = context;
        client = new OkHttpClient.Builder()
//                .cache(new Cache(new File(mContext
//                        .getCacheDir().getAbsolutePath(), "CaseCache"), 20 * 1024 * 1024))
//                .addInterceptor(interceptor)
//                .addNetworkInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(XiaoMiInterface.BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mXiaoMiInterface = retrofit.create(XiaoMiInterface.class);

    }

    public static RetrofitManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitManager(context);
        }
        return INSTANCE;
    }


    /**
     * 精品推荐
     *
     * @return
     */
    public Observable<List<AppInfo>> getAllFeaturedList() {
        return mXiaoMiInterface.getAllFeaturedList()
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return JsoupUtil.getAPPInfoList(s);
                    }
                });
    }

    /**
     * 排行
     */
    public Observable<List<AppInfo>> getRankList(int page) {

        return mXiaoMiInterface.getRankList(page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return JsoupUtil.getAPPInfoList(s);
                    }
                });
    }


    /**
     * 获取详细信息
     */
    public Observable<AppInfo> getDetailInfo(final AppInfo info) {
        String detailUrl = info.getDetailUrl();
        //解决不同网页的兼容问题，只需要读取其中的包名
        if (detailUrl.contains("&")) {
            detailUrl = detailUrl.substring(detailUrl.indexOf("=") + 1, detailUrl.indexOf("&"));
        } else {
            detailUrl = detailUrl.substring(12, detailUrl.length());
        }
        return mXiaoMiInterface.getDetailInfo(detailUrl)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, AppInfo>() {
                    @Override
                    public AppInfo call(String s) {
                        return JsoupUtil.getDetailInfo(s, info);
                    }
                });

    }

    /**
     * 获取详细信息
     */
    public Observable<Boolean> getDetailInfo(final LocalApp localApp) {
        String packageName = localApp.getPackageName();
        return mXiaoMiInterface.getDetailInfo(packageName)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return JsoupUtil.isNeedUpdate(s, localApp);
                    }
                });
    }


    /**
     * 分类模块的热门应用
     */
    public Observable<List<AppInfo>> getHotCatApp(int category) {
        return mXiaoMiInterface.getHotCatApp(category)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return JsoupUtil.getAPPInfoList(s);
                    }
                });
    }

    /**
     * 分类模块的排行
     */
    public Observable<List<AppInfo>> getCatTopList(int category, int page) {
        return mXiaoMiInterface.getCatTopList(category, page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return JsoupUtil.getAPPInfoList(s);
                    }
                });
    }

    /**
     * 分类模块的新品
     */
    public Observable<List<AppInfo>> getCatNewList(int category, int page) {
        return mXiaoMiInterface.getCatNewApp(category, page)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return JsoupUtil.getNewInfoList(s);
                    }
                });
    }

    /**
     * 搜索
     */
    public Observable<List<AppInfo>> search(String keywords) {
        return mXiaoMiInterface.search(keywords)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return JsoupUtil.getAPPInfoList(s);
                    }
                });
    }

}
