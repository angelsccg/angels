package com.angels.util;

import java.util.ArrayList;
import java.util.List;

import com.angels.cache.ACConfigHelper;
import com.angels.model.ACAppInfo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 *
 * @ClassName: ACAppInfoUtil
 * @Description: 获取手机信息的工具栏
 * @author angelsC
 * @date 2015-10-13 下午4:15:44
 *
 *
 *       * 功能：系统
 *
 *       1,直接拨打电话 2,打开拨打电话界面 3,判断该报名所对应的应用程序在手机中是否存在
 *       4,判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的 5,打开输入法 6,收起输入法 7,获取设备分辨率（比如800x480）
 *       8,获得设备宽度 9,获取设备密度 10,获取当前版本号显示名 11,获取当前版本号 12,获取mac地址 13,获取imsi
 *       14,获取设备imei
 *       15,获取当前屏幕截图，包含状态栏
 *       16,获取当前屏幕截图，不包含状态栏
 *       17,设置屏幕亮度
 */
public class ACSystemInfoUtil {
	/**直接拨打电话*/
	public synchronized static void callPhone(String phone, Context context) {
		// 此处应该对电话号码进行验证。。
		if (TextUtils.isEmpty(phone)) {
			return;
		}
		String transferUnit = "转";
		phone = phone.replaceAll(" ", "");
		phone = phone.replaceAll(transferUnit, ",,");

		Intent intent = new Intent(Intent.ACTION_CALL,// ACTION_CALL_PRIVILEGED
				Uri.fromParts("tel", phone, null));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		context.startActivity(intent);
	    }
	 /**打开拨打电话界面*/
	    public synchronized static void callPhoneView(String phone, Context context) {
	        // 此处应该对电话号码进行验证。。
	        if (TextUtils.isEmpty(phone)) {
	            return;
	        }
	        String transferUnit = "转";
	        phone = phone.replaceAll(" ", "");
	        phone = phone.replaceAll(transferUnit, ",,");

//	        Intent intent = new Intent(Intent.ACTION_CALL,// ACTION_CALL_PRIVILEGED
//	                Uri.fromParts("tel", phone, null));
//	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	        context.startActivity(intent);

	         Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +phone));
	         context.startActivity(intent);
	    }
	    
	    /**
	     * 判断该报名所对应的应用程序在手机中是否存在
	     * @param packageName
	     */
	    public synchronized static boolean isHaveApp(String packageName, Context context) {
	        
	        ArrayList<ACAppInfo> mlistACAppInfo = queryAppInfo(context);
	        // TODO Auto-generated method stub
	        if(mlistACAppInfo.size() == 0){
	            return false;
	        }
	        
	        for (int i = 0; i < mlistACAppInfo.size(); i++) {
	            if(packageName.equals(mlistACAppInfo.get(i).getPkgName())){
	                return true;
	            }
	        }
	        return false;
	    }
	    /**
	     * 获取手机中所有app的信息（包名、标签 、图像 、 启动应用程序的Intent）
	     * @param context
	     */
	    public synchronized static ArrayList<ACAppInfo> queryAppInfo(Context context) {
	        PackageManager pm = context.getPackageManager(); // 获得PackageManager对象  
	        List<PackageInfo> packs = pm.getInstalledPackages(0);
	        ArrayList<ACAppInfo> mlistACAppInfo = new ArrayList<ACAppInfo>();
	        for (PackageInfo reInfo : packs) {  
	            ACAppInfo ACAppInfo = new ACAppInfo();
	            ACAppInfo.setAppLabel(reInfo.applicationInfo.loadLabel(pm).toString());
	            ACAppInfo.setPkgName(reInfo.packageName);
	            ACAppInfo.setAppIcon(reInfo.applicationInfo.loadIcon(pm));
	            ACAppInfo.setVersionCode(reInfo.versionCode + "");
	            ACAppInfo.setVersionName(reInfo.versionName);
	            mlistACAppInfo.add(ACAppInfo); // 添加至列表中
	        }  
	        
	        return mlistACAppInfo;
	    }  
	    /**
	     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	     * 
	     * @param context
	     * @return true 表示开启
	     */
	    public static final boolean isOpenGPSSettings(final Context context) {
	        LocationManager locationManager = (LocationManager)context
	                .getSystemService(Context.LOCATION_SERVICE);
	        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
	        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	        /**
	         * 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。
	         * 主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
	         */
	        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	        if (gps || network) {
	            return true;
	        }
	        return false;
	    }
	    
	    /**
	     * 打开输入法
	     * 
	     * @param context
	     * @param view
	     */
	    public static void openKeyboard(Context context, View view) {
	        if (null == view) {
	            return;
	        }
	        InputMethodManager imm = (InputMethodManager)context
	                .getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	    }
	    /**
	     * 收起输入法
	     * 
	     * @param context
	     * @param view
	     */
	    public static void hideKeyboard(Context context, View view) {
	        if (null == view) {
	            return;
	        }
	        InputMethodManager imm = (InputMethodManager)context
	                .getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	    }
	    /**
	     * 获取设备分辨率（比如800x480）
	     * 
	     * @param context
	     * @return
	     */
	    public static String getDisplayMetrics(Context context) {
	    	WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	    	 DisplayMetrics dm=new DisplayMetrics();
	    	 manager.getDefaultDisplay().getMetrics(dm);
	    	
	        ACConfigHelper ch = ACConfigHelper.getInstance(context);
	        // 保存至 sharepreferences .
	        ch.saveIntKey(ACConfigHelper.CONFIG_KEY_DEVICE_WIDTH, dm.widthPixels);
	        ch.saveIntKey(ACConfigHelper.CONFIG_KEY_DEVICE_HEIGHT, dm.heightPixels);

	        StringBuilder sb = new StringBuilder();
	        sb.append(dm.heightPixels).append("x").append(dm.widthPixels);
	        return sb.toString();
	    }

	    /**
	     * 获得设备宽度
	     * 
	     * @param ctx
	     * @return
	     */
	    public static int getDeviceWidth(Context ctx) {
	        ACConfigHelper ch = ACConfigHelper.getInstance(ctx);
	        int width = ch.loadIntKey(ACConfigHelper.CONFIG_KEY_DEVICE_WIDTH, 0);
	        if (width == 0) {
	            getDisplayMetrics(ctx);
	            width = ch.loadIntKey(ACConfigHelper.CONFIG_KEY_DEVICE_WIDTH, 0);
	        }
	        return width;
	    }
	    /**
	     * 获得设备高度
	     * 
	     * @param ctx
	     * @return
	     */
	    public static int getDeviceHeight(Context ctx) {
	        ACConfigHelper ch = ACConfigHelper.getInstance(ctx);
	        int width = ch.loadIntKey(ACConfigHelper.CONFIG_KEY_DEVICE_HEIGHT, 0);
	        if (width == 0) {
	            getDisplayMetrics(ctx);
	            width = ch.loadIntKey(ACConfigHelper.CONFIG_KEY_DEVICE_HEIGHT, 0);
	        }
	        return width;
	    }

	    /**
	     * 获取设备密度
	     * 
	     * @param context
	     * @return
	     */
	    public static float getDensity(Context context) {
	        DisplayMetrics dm = new DisplayMetrics();
	        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
	        return dm.density;
	    }
	    /**
	     * 获取当前版本号显示名
	     * 
	     * @param context
	     * @return
	     */
	    public static String getCurrentVersionName(Context context) {
	        try {
	            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
	        } catch (NameNotFoundException e) {
	            e.printStackTrace();
	            return "";
	        }
	    }
	    /**
	     * 获取当前版本号
	     * 
	     * @param context
	     * @return
	     */
	    public static int getCurrentVersion(Context context) {
	        int versionCode = 0;
	        try {
	            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
	        } catch (NameNotFoundException e) {
	            e.printStackTrace();
	        }
	        return versionCode;
	    }
	    /**
	     * 获取mac地址
	     * 
	     * @param context
	     * @return
	     */
	    public static String getMacAddress(Context context) {
	        ACConfigHelper ch = ACConfigHelper.getInstance(context);
	        String mac = ch.loadKey(ACConfigHelper.CONFIG_KEY_MAC);
	        if (TextUtils.isEmpty(mac)) {
	            WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	            WifiInfo info = wifi.getConnectionInfo();
	            mac = info.getMacAddress();
	            if (!TextUtils.isEmpty(mac)) {
	                ch.saveKey(ACConfigHelper.CONFIG_KEY_MAC, mac);
	            }
	        }
	        return mac;
	    }
	    /**
	     * 获取imsi  国际移动用户识别码
	     * 
	     * @param context
	     * @return
	     */
	    public static String getDeviceIMSI(Context context) {
	        ACConfigHelper ch = ACConfigHelper.getInstance(context);
	        String imsi = ch.loadKey(ACConfigHelper.CONFIG_KEY_IMSI);
	        if (TextUtils.isEmpty(imsi)) {
	            TelephonyManager tm = (TelephonyManager)context
	                    .getSystemService(Context.TELEPHONY_SERVICE);
	            imsi = tm.getSubscriberId();
	            if (!TextUtils.isEmpty(imsi)) {
	                ch.saveKey(ACConfigHelper.CONFIG_KEY_IMSI, imsi);
	            }
	        }
	        return imsi;
	    }
	    /**
	     * 获取设备imei  移动设备国际识别码
	     * 
	     * @param context
	     * @return
	     */
	    public static String getDeviceIMEI(Context context) {
	        ACConfigHelper ch = ACConfigHelper.getInstance(context);
	        String imei = ch.loadKey(ACConfigHelper.CONFIG_KEY_IMEI);
	        if (TextUtils.isEmpty(imei)) {
	            TelephonyManager tm = (TelephonyManager)context
	                    .getSystemService(Context.TELEPHONY_SERVICE);
	            imei = tm.getDeviceId();
	            if (!TextUtils.isEmpty(imei)) {
	                ch.saveKey(ACConfigHelper.CONFIG_KEY_IMEI, imei);
	            }
	        }
	        return imei;
	    }

	/**
	 * 获取当前屏幕截图，包含状态栏
	 *
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity)
	{
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getDeviceWidth(activity);
		int height = getDeviceHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;
	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 *
	 * @param activity
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity)
	{
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = getDeviceWidth(activity);
		int height = getDeviceHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return bp;
	}

	/**
	 * 屏幕亮度
	 * @param context
	 * @param brightness
	 */
	private void setLight(Activity context, int brightness) {
		WindowManager.LayoutParams lp = context.getWindow().getAttributes();
		lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
		context.getWindow().setAttributes(lp);
	}
}
