
package com.angels.cache;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 保存本地数据的缓存类 
 */
public class ACConfigHelper {

    private static String PREF_NAME = "AC_Config";

    private static ACConfigHelper mInstance = null;

    private Context mContext;

    protected SharedPreferences mSettings;

    protected SharedPreferences.Editor mEditor;

    //配置项名称
    public static int SZIE_AVATAR = 70;

    /**
     * 设备宽（px）
     */
    public static String CONFIG_KEY_DEVICE_WIDTH = "device_width";
    
    /**
     * 设备高（px）
     */
    public static String CONFIG_KEY_DEVICE_HEIGHT= "device_height";
    
    /**
     * 程序是否是第一次运行
     */
    public static String IS_FIRST_RUN = "is_first_run";
    
    /**
     * 版本号
     */
    public static String VERSION_CODE = "version_code";
    
    public static String CONFIG_KEY_UPGRADE_VERSION = "upgrade_version";
    
    public static String CONFIG_KEY_UPGRADE_MSG = "upgrade_msg";
    
    public static String CONFIG_KEY_UPGRADE_URL = "upgrade_url";
    
    public static String CONFIG_KEY_FORCE_UPGRADE = "force_upgrade";
    
    public static String CONFIG_KEY_UPGRADE_CHECK_TIME = "upgrade_check_time";
    
    public static String CONFIG_KEY_IMSI = "imsi";
    
    public static String CONFIG_KEY_IMEI = "imei";
    
    public static String CONFIG_KEY_MAC = "mac";
    
    public static ACConfigHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ACConfigHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public boolean contains(String key) {
        return mSettings.contains(key);
    }

    public String loadKey(String key) {
        return mSettings.getString(key, "");
    }
    
    public String loadKey(String key, String defValue) {
        return mSettings.getString(key, defValue);
    }
    
    public String getKey(String key) {
        return loadKey(key, "");
    }
    
    public String getKey(String key, String defValue) {
        return loadKey(key, defValue);
    }

    public void saveKey(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void removeKey(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    public void clearkeys() {
        mEditor.clear();
        mEditor.commit();
    }

    public boolean loadBooleanKey(String key, boolean defValue) {
        return mSettings.getBoolean(key, defValue);
    }

    public void saveBooleanKey(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public int loadIntKey(String key, int defValue) {
        return mSettings.getInt(key, defValue);
    }

    public void saveIntKey(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public long loadLongKey(String key, long defValue) {
        return mSettings.getLong(key, defValue);
    }

    public void saveLongKey(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    private ACConfigHelper(Context c) {
        mContext = c;
        mSettings = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();

    }
    
}
