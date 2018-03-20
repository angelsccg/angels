package com.angels.util;

import java.util.ArrayList;

import android.graphics.Paint;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @ClassName: ACViewTextCondition
 * @Description: 控件的一些文本条件
 * @author angelsC
 * @date 2015-11-2 下午2:14:03
 * 
 *       1，设置EditText位数控制(只支持数字输入，只支持小数点前decimal位，小数点后integer位。非法输入不写入。)
 *       2, 计算出该TextView中文字的长度(像素)
 */
public class ACViewTextCondition {

	/** 输入框小数的位数 */
	// private static final int DECIMAL_DIGITS = 2;
	/** 输入框整数的位数 */
	// private static final int INTEGER_DIGITS = 3;
	/**
	 * 设置位数控制(只支持数字输入，只支持小数点前decimal位，小数点后integer位。非法输入不写入。)
	 * 
	 * @param editText
	 * @param integer
	 *            小数点后integer位
	 * @param decimal
	 *            小数点前decimal位
	 */
	public static void digit(EditText editText, final int integer,
			final int decimal) {
		/** * */
		InputFilter lengthfilter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				Log.i("test", "filter:" + source + "-" + start + "-" + end
						+ "-" + dest + "-" + dstart + "-" + dend);
				// 删除等特殊字符，，直接返回
				if ("".equals(source.toString())) {
					return source;
				}
				// 复制进来1111.111这类不符合的时候
				if (source.toString().length() > 1) {
					String[] s = source.toString().split("\\.");
					if (s.length > 1) {
						if (s[0].length() > integer || s[1].length() > decimal) {
							return source.subSequence(start,
									end - source.length());
						}
					} else if (s.length == 1) {
						if (s[0].length() > integer) {
							return source.subSequence(start,
									end - source.length());
						}
					}
				}

				String dValue = dest.toString();
				String[] splitArray = dValue.split("\\.");
				if (splitArray != null) {
					Log.i("test", "splitArray:" + splitArray.length);
				}
				if (splitArray.length > 1) {
					int index = dValue.indexOf(".");
					if (index < dstart) {
						// 小数
						String dotValue = splitArray[1];
						int diff = dotValue.length() + 1 - decimal;
						if (diff > 0) {
							return source.subSequence(start, end - diff);
						}
					} else {
						// 整数
						String intValue = splitArray[0];
						int diff2 = intValue.length() + 1 - integer;
						if (diff2 > 0) {
							return source.subSequence(start, end - diff2);
						}
					}
				} else if (splitArray.length == 1) {
					if (".".equals(source.toString())) {
						return source;
					}
					int index = dValue.indexOf(".");
					Log.i("test", "index:" + dValue + "-" + index);
					if (index != -1) {
						return source;
					}
					int diff2 = dValue.length() + 1 - integer;
					if (diff2 > 0) {
						return source.subSequence(start, end - diff2);
					}
				}

				return source;
			}
		};
		editText.setFilters(new InputFilter[] { lengthfilter });
	}

	/**
	 *  计算出该TextView中文字的长度(像素)
	 * @param textView
	 * @param text
	 * @return
	 */
	public static float getTextViewLength(TextView textView, String text) {
		TextPaint paint = textView.getPaint();
		// 得到使用该paint写上text的时候,像素为多少
		float textLength = paint.measureText(text);
		return textLength;
	}
}
