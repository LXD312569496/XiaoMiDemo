package com.example.asus.xiaomidemo.xiaomi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/10/23.
 */

public class AppInfo implements Parcelable {
    String appName;//名字
    String icon;//图片
    String detailUrl;//详情页面
    String downLoadUrl;//下载地址

    String company;//公司
    double star;//星星的等级
    String size;//软件的大小
    String version;//软件的版本号
    String packageName;//软件的包名
    String updateTime;//软件的更新时间

    List<String> imgs;//软件的几张缩略图
    String description;//软件的功能介绍
    String updateLog;//软件的更新日志

    String commentNum;//评分的人数
    String category;//软件的类别
    List<String> permissionList;//所需要的权限

    ArrayList<AppInfo> sameAppList;//同个开发者的应用

    boolean isInstall = false;//是否已安装
    boolean isUpdate = false;//是否需要升级


    public AppInfo() {
    }

    protected AppInfo(Parcel in) {
        appName = in.readString();
        icon = in.readString();
        detailUrl = in.readString();
        downLoadUrl = in.readString();
        company = in.readString();
        star = in.readDouble();
        size = in.readString();
        version = in.readString();
        packageName = in.readString();
        updateTime = in.readString();
        imgs = in.createStringArrayList();
        description = in.readString();
        updateLog = in.readString();
        commentNum = in.readString();
        category = in.readString();
        permissionList = in.createStringArrayList();
        sameAppList = in.createTypedArrayList(AppInfo.CREATOR);
        isInstall = in.readByte() != 0;
        isUpdate = in.readByte() != 0;
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    public ArrayList<AppInfo> getSameAppList() {
        return sameAppList;
    }

    public void setSameAppList(ArrayList<AppInfo> sameAppList) {
        this.sameAppList = sameAppList;
    }




    public boolean isInstall() {
        return isInstall;
    }

    public void setInstall(boolean install) {
        isInstall = install;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getStar() {
        return star;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(appName);
        dest.writeString(icon);
        dest.writeString(detailUrl);
        dest.writeString(downLoadUrl);
        dest.writeString(company);
        dest.writeDouble(star);
        dest.writeString(size);
        dest.writeString(version);
        dest.writeString(packageName);
        dest.writeString(updateTime);
        dest.writeStringList(imgs);
        dest.writeString(description);
        dest.writeString(updateLog);
        dest.writeString(commentNum);
        dest.writeString(category);
        dest.writeStringList(permissionList);
        dest.writeTypedList(sameAppList);
        dest.writeByte((byte) (isInstall ? 1 : 0));
        dest.writeByte((byte) (isUpdate ? 1 : 0));
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", ic_app='" + icon + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", downLoadUrl='" + downLoadUrl + '\'' +
                ", company='" + company + '\'' +
                ", star=" + star +
                ", size='" + size + '\'' +
                ", version='" + version + '\'' +
                ", packageName='" + packageName + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", imgs=" + imgs +
                ", description='" + description + '\'' +
                ", updateLog='" + updateLog + '\'' +
                ", commentNum='" + commentNum + '\'' +
                ", category='" + category + '\'' +
                ", permissionList=" + permissionList +
                ", isInstall=" + isInstall +
                ", isUpdate=" + isUpdate +
                '}';
    }
}
