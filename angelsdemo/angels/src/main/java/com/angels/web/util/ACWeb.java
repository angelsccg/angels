package com.angels.web.util;

import com.angels.web.ui.ACWebActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ACWeb {
	/**
	 *
	 * @param activity
	 * @param url 链接地址
	 * @param map 数据
	 * @param requestCode 返回的code
	 */
	public static void showWeb(Activity activity, String url,HashMap<String,String> map,int requestCode) {
		Intent intent = new Intent(activity, ACWebActivity.class);
		Uri uri = Uri.parse(url);
		intent.setData(uri);
		intent.putExtra("url", url);

		if(map!=null){
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()){
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				String val = (String)entry.getValue();
				intent.putExtra(key, val);
			}
		}
		if(requestCode==0){
			activity.startActivity(intent);
		}else{
			activity.startActivityForResult(intent, requestCode);
		}
//		context.startActivity(intent);

	}
	public static void showWeb(Activity activity, String url) {
		showWeb(activity,url,null,0);
	}

}
