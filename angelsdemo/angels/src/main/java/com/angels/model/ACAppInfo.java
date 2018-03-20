package com.angels.model;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

/**
 * 
* @ClassName: AppInfo 
* @Description: 用来存储应用程序信息  
* @author angelsC
* @date 2015-10-15 下午3:09:35 
*
 */
public class ACAppInfo implements Serializable{
    
    private String appLabel;    //应用程序标签  
    private Drawable appIcon;  //应用程序图像  
    private String pkgName = "";    //应用程序所对应的包名  
    private String versionCode = "";    //应用程序所对应的版本号
    private String versionName = "";    //应用程序所对应的版本信息  
      
    public ACAppInfo(){}
      
    public String getAppLabel() {  
        return appLabel;  
    }  
    public void setAppLabel(String appName) {  
        this.appLabel = appName;  
    }  
    public Drawable getAppIcon() {  
        return appIcon;  
    }  
    public void setAppIcon(Drawable appIcon) {  
        this.appIcon = appIcon;  
    }  
    public String getPkgName(){  
        return pkgName ;  
    }  
    public void setPkgName(String pkgName){  
        this.pkgName=pkgName ;  
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }  
    
    
}  