package com.angels.jni;
/**
 * 
* @ClassName: Uninstall 
* @Description: 卸载弹出网页  （保存的包名路径不能变）
* @author angelsC
* @date 2015-11-20 上午11:21:09 
*
 */
public class ACUninstall {
	
	public native String init(String path,String url);
    
	static {
        System.loadLibrary("uninstall-jni");
    }
}
