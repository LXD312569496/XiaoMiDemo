package com.example.asus.xiaomidemo.xiaomi;

import com.example.asus.xiaomidemo.manage.local.LocalApp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.example.asus.xiaomidemo.xiaomi.XiaoMiInterface.BASE_URL;

/**
 * Created by asus on 2017/10/23.
 */

public class JsoupUtil {

    /**
     * 精品推荐
     **/
    public static List<AppInfo> getAPPInfoList(String result) {

//        Log.d("test", "getAPPInfoList:" + result);
        List<AppInfo> appInfoList = new ArrayList<>();
        Document document = Jsoup.parse(result);
        Elements elements = document.select("div.applist-wrap").first().select("ul.applist").select("li");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            AppInfo appInfo = new AppInfo();

            appInfo.setAppName(element.select("h5").text());
            appInfo.setIcon(element.select("img").attr("data-src"));
            appInfo.setDetailUrl(element.select("a").attr("href"));
            appInfo.setCategory(element.select("p.app-desc").first().text());
//            Log.d("test", appInfo.toString());
            appInfoList.add(appInfo);
        }
        return appInfoList;
    }

    /**
     *
     */
    public static List<AppInfo> getNewInfoList(String result) {

        List<AppInfo> appInfoList = new ArrayList<>();
        Document document = Jsoup.parse(result);
        Elements elements = document.select("ul#all-applist.applist").select("li");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            AppInfo appInfo = new AppInfo();

            appInfo.setAppName(element.select("h5").text());
            appInfo.setIcon(element.select("img").attr("data-src"));
            appInfo.setDetailUrl(element.select("a").attr("href"));
            appInfo.setCategory(element.select("p.app-desc").first().text());
//            Log.d("test", appInfo.toString());
            appInfoList.add(appInfo);
        }
        return appInfoList;
    }

    /**
     * 获取应用的详细信息
     */
    public static AppInfo getDetailInfo(String result, AppInfo appInfo) {

        AppInfo bean = appInfo;
        Document document = Jsoup.parse(result);

        String appName = document.select("div.intro-titles").first().select("h3").first().text();
//        Log.d("test", "名字:" + appName);
        String company = document.select("div.intro-titles").first().select("p").first().text();//公司
//        Log.d("test", "公司:" + company);
        String icon = document.select("img.yellow-flower").first().attr("src");
//        Log.d("test", "图标:" + icon);

        String star = document.select("div.star1-empty").first().select("div").get(1).attr("class");//星星的等级
        double resultStar = Double.valueOf(star.substring(star.lastIndexOf("-") + 1, star.length())) / 2;

        String size = document.select("div.details.preventDefault").select("ul.cf").first().select("li").get(1).text();//软件的大小
//        Log.d("test", "软件大小:" + size);
        Elements elements = document.select("div.img-list.height-auto").select("img");
//        Log.d("test", "缩略图：个数" + elements.size());
        List<String> imgs = new ArrayList<>();//软件的几张缩略图
//        Log.d("test","名字："+appInfo.getAppName()+elements.size());

        for (int i = 0; i < elements.size(); i++) {
            imgs.add(elements.get(i).select("img").attr("src"));
        }
        String description = document.select("div.app-text").first().select("p.pslide").first().text();//软件的功能介绍
//        Log.d("test", "介绍:" + description);
        /**
         * 需要判断是否有更新的内容
         */
        String updateLog = "";
        if (document.select("div.app-text").first().select("p.pslide").size() == 2) {
            updateLog = document.select("div.app-text").first().select("p.pslide").get(1).text();//软件的更新日志
        }
//        Log.d("test", "更新：" + updateLog);
        String commentNum = document.select("div.intro-titles").first().select("span.app-intro-comment").first().text();//评分的人数
        commentNum = commentNum.substring(1, commentNum.length() - 1);
//        Log.d("test","评论："+commentNum);
        String updateTime = document.select("div.details.preventDefault").select("ul.cf").select("li").get(5).text();
//        Log.d("test", "更新时间:" + updateTime);
        String packageName = document.select("div.details.preventDefault").select("ul.cf").select("li.special-li").first().text();
//        Log.d("test", "包名：" + packageName);
        String versionCode = document.select("div.details.preventDefault").select("ul.cf").select("li").get(3).text();
//          Log.d("test","版本号："+versionCode);
        List<String> permissionList = new ArrayList<>();//所需要的权限
        Elements elements2 = document.select("div.details.preventDefault").select("ul.second-ul").select("li");
        for (int i = 0; i < elements2.size(); i++) {
            permissionList.add(elements2.get(i).text());
//            Log.d("test", elements2.get(i).text());
        }
        String downloadUrl = BASE_URL + document.select("div.app-info-down").select("a").attr("href");
//        Log.d("test", "下载地址: "+BASE_URL+downloadUrl);

        ArrayList<AppInfo> sameAppList = new ArrayList<>();
        if (!document.select("div.second-imgbox").toString().isEmpty()){
            Elements elements3 = document.select("div.second-imgbox").select("ul").first().select("li");
            for (int i = 0; i < elements3.size(); i++) {
                Element element = elements3.get(i);
//            Log.d("test", elements3.get(i).toString());
                AppInfo appInfo1 = new AppInfo();

                appInfo1.setAppName(element.select("h5").text());
                appInfo1.setIcon(element.select("img").attr("data-src"));
                appInfo1.setDetailUrl(element.select("a").attr("href"));
//            appInfo1.setCategory(element.select("p.app-desc").first().text());
                sameAppList.add(appInfo1);
            }

        }


        bean.setSameAppList(sameAppList);

        bean.setAppName(appName);
        bean.setCompany(company);
        bean.setIcon(icon);
        bean.setSize(size);
        bean.setImgs(imgs);
        bean.setDescription(description);
        bean.setUpdateLog(updateLog);
        bean.setCommentNum(commentNum);
        bean.setUpdateTime(updateTime);
        bean.setPackageName(packageName);
        if (permissionList.size()!=0) {
            bean.setPermissionList(permissionList);
        }
        bean.setStar(resultStar);
        bean.setVersion(versionCode);
        bean.setDownLoadUrl(downloadUrl);
//        Log.d("test","名字："+appInfo.getAppName()+"公司："+appInfo.getCompany());
        return bean;
    }


    /**
     * 判断某个应用是否需要升级
     */
    public static boolean isNeedUpdate(String result, LocalApp localApp) {
        Document document = Jsoup.parse(result);
//        String packageName = document.select("div.details.preventDefault").select("ul.cf").select("li.special-li").text();
//        Log.d("test", "包名：" + packageName);
        String versionCode = document.select("div.details.preventDefault").select("ul.cf").select("li").get(3).text();

//        Log.d("test", "应用名字：" + localApp.getAppName());
//        Log.d("test", "应用商店版本号：" + versionCode);
//        Log.d("test", "本地版本号：" + localApp.getVersion());


        //商店里面没有这个应用
        if (versionCode == null || versionCode.isEmpty()) {
//            Log.d("test","null没有这个应用false");
            return false;
        }
        //不是最新的版本，需要更新
        if (versionCode.compareTo(localApp.getVersion()) >= 0) {
//            Log.d("test","需要进行更新true");
            return true;
        }

//        Log.d("test","不需要更新false");
        return false;
    }


    /**
     * 获取分类中的精品
     */
    public static List<AppInfo> getKindList(String result) {

        return null;
    }


}
