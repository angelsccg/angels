package com.angels.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

/**
 * 
 * @ClassName: ACAppUtil
 * @Description: app信息工具类
 * @author angelsC
 * @date 2015-10-15 上午11:47:45
 * 
 * 
 *       2, 获取程序 图标
 *       3,获取程序的版本号
 *       4,获取程序的名字
 *       5, 获取程序的权限
 *       6,获取程序的签名
 * 
 */
public class ACAppUtil {
	
	/*
	 * 获取程序 图标
	 */
	public static Drawable getAppIcon(String packname, Context context)
			throws NameNotFoundException {
		PackageManager pm = context.getPackageManager();
		ApplicationInfo info = pm.getApplicationInfo(packname, 0);
		return info.loadIcon(pm);
	}

	/*
	 * 获取程序的版本号
	 */
	public static String getAppVersion(String packname, Context context)
			throws NameNotFoundException {
		PackageManager pm = context.getPackageManager();
		PackageInfo packinfo = pm.getPackageInfo(packname, 0);
		return packinfo.versionName;
	}

	/*
	 * 获取程序的名字
	 */
	public static String getAppName(String packname, Context context)
			throws NameNotFoundException {
		PackageManager pm = context.getPackageManager();
		ApplicationInfo info = pm.getApplicationInfo(packname, 0);
		return info.loadLabel(pm).toString();
	}

	/*
	 * 获取程序的权限
	 */
	public static String[] getAppPremission(String packname, Context context)
			throws NameNotFoundException {
		PackageManager pm = context.getPackageManager();
		PackageInfo packinfo = pm.getPackageInfo(packname,
				PackageManager.GET_PERMISSIONS);
		// 获取到所有的权限
		return packinfo.requestedPermissions;
	}

	/*
	 * 获取程序的签名
	 */
	public static String getAppSignature(String packname, Context context)
			throws NameNotFoundException {
		PackageManager pm = context.getPackageManager();
		PackageInfo packinfo = pm.getPackageInfo(packname,
				PackageManager.GET_SIGNATURES);
		// 获取到所有的权限
		return packinfo.signatures[0].toCharsString();

	}
}
