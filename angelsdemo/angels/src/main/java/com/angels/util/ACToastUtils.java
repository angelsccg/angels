package com.angels.util;

import com.angels.R;
import com.angels.util.ACUntilUtil;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/***
 * 
* @ClassName: ACToastUtils 
* @Description: 提示数据控件Toast的工具类
* @author angelsC
* @date 2015-10-30 下午3:03:07 
*
 *
 */

public class ACToastUtils {

	private static Handler handler = new Handler(Looper.getMainLooper());
	private static Toast toast = null;
	private static Object synObj = new Object();
	@SuppressWarnings("deprecation")
	private static int sysVersion = Integer.parseInt(VERSION.SDK);

	/**
	 * 显示提示消息--默认黑底
	 * 
	 * @param act
	 *            显示的页面显示提示的activity
	 * @param msg
	 *            提示消息
	 */
	public static void showMessage(Context act, final String msg) {
		showMessage(act, msg, Toast.LENGTH_LONG);
	}

	/**
	 * 显示提示消息--默认黑底
	 * 
	 * @param act
	 *            显示的页面显示提示的activity
	 * @param msg
	 *            提示消息
	 */
	public static void showMessage(Context act, final int msg) {
		showMessage(act, act.getResources().getString(msg), Toast.LENGTH_SHORT);
	}

	/**
	 * 
	 * 显示提示消息--黑底
	 * 
	 * @param act
	 *            显示提示的activity
	 * @param msg
	 *            提示消息
	 * @param len
	 *            时间
	 */
	public static void showMessage(final Context act, final String msg,
			final int len) {
		handler.post(new Runnable() {
			public void run() {
				synchronized (synObj) {
					if (toast != null) {
						// 4.0不用cancel
						if (sysVersion <= 14) {
							toast.cancel();
						}
						LayoutInflater inflater = LayoutInflater.from(act);
						View view = inflater.inflate(R.layout.ac_toast, null);
						TextView chapterNameTV = (TextView) view.findViewById(R.id.tipTextView);
						LayoutParams  lp = chapterNameTV.getLayoutParams();
						lp.width = act.getResources().getDisplayMetrics().widthPixels;
						chapterNameTV.setLayoutParams(lp);
						chapterNameTV.setText(msg);
						toast.setDuration(len);
						toast.setView(view);
						toast.setGravity(Gravity.CENTER,
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
					} else {
						toast = Toast.makeText(act, msg, len);
						LayoutInflater inflater = LayoutInflater.from(act);
						View view = inflater.inflate(R.layout.ac_toast, null);
						TextView chapterNameTV = (TextView) view.findViewById(R.id.tipTextView);
						LayoutParams  lp = chapterNameTV.getLayoutParams();
						lp.width = act.getResources().getDisplayMetrics().widthPixels;
						chapterNameTV.setLayoutParams(lp);
						chapterNameTV.setText(msg);
						toast.setDuration(len);
						toast.setView(view);
						toast.setGravity(Gravity.CENTER,
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
					}
					toast.show();
				}
			}
		});

	}
	
	/**
	 * 
	 * 显示提示消息--白底
	 * 
	 * @param act
	 *            显示提示的activity
	 * @param msg
	 *            提示消息
	 * @param len
	 *            时间
	 */
	public static void showWhiteMessage(final Context act, final String msg,
			final int len) {
		handler.post(new Runnable() {
			public void run() {
				synchronized (synObj) {
					if (toast != null) {
						// 4.0不用cancel
						if (sysVersion <= 14) {
							toast.cancel();
						}
						LayoutInflater inflater = LayoutInflater.from(act);
						View view = inflater.inflate(R.layout.ac_toast_white, null);
						TextView chapterNameTV = (TextView) view.findViewById(R.id.tipTextView);
						LayoutParams  lp = chapterNameTV.getLayoutParams();
						lp.width = act.getResources().getDisplayMetrics().widthPixels - ACUntilUtil.dp2px(act, 24);
						chapterNameTV.setLayoutParams(lp);
						chapterNameTV.setText(msg);
						toast.setDuration(len);
						toast.setView(view);
						toast.setGravity(Gravity.CENTER,
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
					} else {
						toast = Toast.makeText(act, msg, len);
						LayoutInflater inflater = LayoutInflater.from(act);
						View view = inflater.inflate(R.layout.ac_toast_white, null);
						TextView chapterNameTV = (TextView) view.findViewById(R.id.tipTextView);
						LayoutParams  lp = chapterNameTV.getLayoutParams();
						lp.width = act.getResources().getDisplayMetrics().widthPixels - ACUntilUtil.dp2px(act, 24);
						chapterNameTV.setLayoutParams(lp);
						chapterNameTV.setText(msg);
						toast.setDuration(len);
						toast.setView(view);
						toast.setGravity(Gravity.CENTER,
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
					}
					toast.show();
				}
			}
		});

	}
}
