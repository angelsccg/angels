package com.angels.widget;

import com.angels.R;
import com.angels.util.ACUntilUtil;
import com.angels.widget.ACScrollView.ScrollViewListener;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * 
 * @ClassName: SizeTextView
 * @Description: TextView的文字大小 随着与参照控件的距离 变化而改变
 * @author angelsC
 * @date 2015-9-10 下午5:34:03
 * 
 *       xmlns:angels="http://schemas.android.com/apk/res/包名" 自定义属性：acMinSize
 *       （最小字体大小）
 * 
 */
public class ACSizeTextView extends TextView {

	/** 参照控件 */
//	private View referView;
	/** 与参照控件的距离 */
//	private float distance;

	/** 默认字体大小 */
	private float defaultSize;
	/** 最小字体大小 */
	private float minSize;
	
	private SizeTextViewListener sizeTextViewListener;

	public ACSizeTextView(Context context) {
		super(context);
		init(context, null, 0, 0);
	}

	public ACSizeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0, 0);
	}

	public ACSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr, 0);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ACSizeTextView(Context context, AttributeSet attrs,
						  int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs, defStyleAttr, defStyleRes);
	}

	private void init(Context context, AttributeSet attrs, int defStyle,
			int defStyleRes) {
		defaultSize = getTextSize();

		if (attrs != null) {
			// TypedArray是存放资源的array,1.通过上下文得到这个数组,attrs是构造函数传进来的,对应attrs.xml
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.ACSizeTextView);
			minSize = a.getDimension(R.styleable.ACSizeTextView_acMinSize, 0);
			Log.i("ACSizeTextView", "init-->minSize:" + minSize);
		}
	}

	/**
	 * 文字变化
	 */
	public void changeSize(float distance,float y) {
//		distance = this.getY() - referView.getY() - referView.getHeight();
		int alpha = 255;
		boolean isShow = false;
		if (y <= distance && y >= 0) {
			this.setVisibility(VISIBLE);
			float changeSize = defaultSize - (defaultSize - minSize) / distance * y;
			this.setTextSize(ACUntilUtil.px2dp(getContext(), changeSize));// 字体大小变化
			alpha = (int) (255 / distance * y);
		} else if (y > distance) {
			this.setVisibility(INVISIBLE);
			isShow = true;
		}else{
			alpha = 0;
		}
		if(sizeTextViewListener!=null)
			sizeTextViewListener.changed(this,alpha,isShow);

	}

	public float getDefaultSize() {
		return defaultSize;
	}

	public void setDefaultSize(float defaultSize) {
		this.defaultSize = defaultSize;
	}

	public float getMinSize() {
		return minSize;
	}

	public void setMinSize(float minSize) {
		this.minSize = minSize;
	}
	
	public void setSizeTextViewListener(SizeTextViewListener sizeTextViewListener) {
		this.sizeTextViewListener = sizeTextViewListener;
	}
	
	public interface SizeTextViewListener {
		void changed(ACSizeTextView textView, int alpha, boolean isShow);
	}

}
