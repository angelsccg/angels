package com.angels.cache;

import com.angels.app.ACApplication;
import com.angels.util.ACSystemInfoUtil;

/**
 * 
* @ClassName: GlobalInfo 
* @Description: 全局缓存信息  （单例模式）
* @author angelsC
* @date 2015-10-13 下午4:04:31 
*
 */
public class ACGlobalInfo {
	/**
	 * 单例
	 */
	private static ACGlobalInfo globalInfo = null;
    public static ACGlobalInfo getInstance(){
        if(globalInfo==null){
            synchronized(ACGlobalInfo.class){
                if(globalInfo==null){
                	globalInfo=new ACGlobalInfo();
                }
            }
        }
        return globalInfo;
    }
    private ACGlobalInfo(){}
    
    /**屏幕高度*/
    private float screenHeight = 0;
    /**屏幕宽度*/
    private float screenWidth = 0;
    
    private ACApplication application;
    
    public void initGlobal(ACApplication application){
    	this.application = application;
    }
    
    /**
     * 获取屏幕高度
     * @return
     */
	public float getScreenHeight() {
		if(screenHeight==0){
    		screenHeight = ACSystemInfoUtil.getDeviceHeight(application);
    	}
		return screenHeight;
	}

	/**
     * 获取屏幕宽度
     * @return
     */
	public float getScreenWidth() {
		if(screenWidth==0){
			screenWidth = ACSystemInfoUtil.getDeviceWidth(application);
    	}
		return screenWidth;
	}
}
