package com.example.angelsdemo.activity.tool;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angels.util.ACViewTextCondition;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

public class TouchActivity extends BaseActivity implements View.OnTouchListener {
	private TextView tvTouchShowStart;
	private TextView tvTouchShowIng;
	private TextView tvTouchShowEnd;
	private LinearLayout llTouch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch);
		init();
	}

	private void init() {
		tvTouchShowStart = (TextView) findViewById(R.id.touch_show_start);
		tvTouchShowIng = (TextView) findViewById(R.id.touch_show_ing);
		tvTouchShowEnd = (TextView) findViewById(R.id.touch_show_end);
		llTouch = (LinearLayout) findViewById(R.id.ll_touch);
		llTouch.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			/**
			 * 点击的开始位置
			 */
			case MotionEvent.ACTION_DOWN:
				tvTouchShowStart.setText("起始位置：(" + event.getX() + "," + event.getY() + ")");
				break;
			/**
			 * 触屏实时位置
			 */
			case MotionEvent.ACTION_MOVE:
				tvTouchShowIng.setText("实时位置：(" + event.getX() + "," + event.getY()+ ")");
				break;
			/**
			 * 离开屏幕的位置
			 */
			case MotionEvent.ACTION_UP:
				tvTouchShowEnd.setText("结束位置：(" + event.getX() + "," + event.getY()+ ")");
				break;
			default:
				break;
		}
		/**
		 *  注意返回值
		 *  true：view继续响应Touch操作；
		 *  false：view不再响应Touch操作，故此处若为false，只能显示起始位置，不能显示实时位置和结束位置
		 */
		return true;
	}


}
