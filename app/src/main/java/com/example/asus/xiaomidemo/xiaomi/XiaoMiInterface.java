package com.example.asus.xiaomidemo.xiaomi;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by asus on 2017/10/23.
 */

public interface XiaoMiInterface {

    public static final String BASE_URL = "http://app.mi.com";

    /**
     * 精品推荐
     *
     * @return
     */
    @GET("/allFeaturedList")
    Observable<String> getAllFeaturedList();


    /**
     * 应用的详情页面
     */
    @GET("/details")
    Observable<String> getDetailInfo(@Query("id") String detailUrl);


    /**
     * 应用排行
     *
     * @return
     */
    @GET("/topList")
    Observable<String> getRankList(@Query("page") int page);


    /**
     * 获取分类的排行
     */
    @GET("/catTopList/{category}")
    Observable<String> getCatTopList(@Path("category")int category,@Query("page") int page);

    /**
     * 获取分类的精品
     */
    @GET("/hotCatApp/{category}")
    Observable<String> getHotCatApp(@Path("category") int category);

    /**
     * 获取分类的新品
     */
    @GET("/category/{category}")
    Observable<String> getCatNewApp(@Path("category") int category,
                                    @Query("page") int page);


    /**
     * 搜索
     */
    @GET("/search")
    Observable<String> search(@Query("keywords") String keywords);


    /**
     * 应用的下载地址
     */
    @GET("/download/{id}")
    Observable<String> donwLoad(@Path("id") int id);

}
