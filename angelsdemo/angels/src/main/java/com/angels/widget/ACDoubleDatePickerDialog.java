package com.angels.widget;

import java.lang.reflect.Field;

import com.angels.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

/**
 * 
* @ClassName: ACDoubleDatePickerDialog 
* @Description: 双个 时间选择日期（开始日期-结束日期）
* @author angelsC
* @date 2015-9-8 上午11:26:13 
* 
* 
* 注意： 如果想原先是竖屏，弹出对话框的时候是横屏，对话框消失的时候变回竖屏。那么要做以下两个步骤：
* 1，调用该类的setCutScreen方法。
* 2，在配置文件 对应的activity中加android:configChanges="keyboardHidden|orientation|screenSize"
*
 */
public class ACDoubleDatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener {

    
	private static final String START_YEAR = "start_year";
    private static final String END_YEAR = "end_year";
    private static final String START_MONTH = "start_month";
    private static final String END_MONTH = "end_month";
    private static final String START_DAY = "start_day";
    private static final String END_DAY = "end_day";

    private final DatePicker mDatePicker_start;
    private final DatePicker mDatePicker_end;
    private final OnDateSetListener mCallBack;
    
    
    /**开始标题*/
    private TextView tvStartDate;
    /**结束标题*/
    private TextView tvEndDate;
    /**
     * 自定义属性的文本
     * */
    /**确定*/
    private String ok;
    /**取消*/
    private String cancel;
    /**开始日期*/
    private String startDate;
    /**结束日期*/
    private String endDate;
    
    
    private Activity activity;
    private boolean isCutScreen;
    
    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {

        void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                       DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth);
    }
    

    public ACDoubleDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth);
    }

    public ACDoubleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
            int dayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth, true);
    }

    public ACDoubleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
            int dayOfMonth, boolean isDayVisible) {
        super(context, theme);

        mCallBack = callBack;

        Context themeContext = getContext();
        ok = themeContext.getResources().getString(R.string.ac_ok);
        cancel = themeContext.getResources().getString(R.string.ac_cancel);
        setButton(BUTTON_POSITIVE, ok, this);
        setButton(BUTTON_NEGATIVE, cancel, this);
        setIcon(0);
        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.ac_dialog_double_date, null);
        setView(view);
        mDatePicker_start = (DatePicker) view.findViewById(R.id.ac_datePickerStart);
        mDatePicker_end = (DatePicker) view.findViewById(R.id.ac_datePickerEnd);
        mDatePicker_start.init(year, monthOfYear, dayOfMonth, this);
        mDatePicker_end.init(year, monthOfYear, dayOfMonth, this);
        // updateTitle(year, monthOfYear, dayOfMonth);

        tvStartDate = (TextView) view.findViewById(R.id.ac_tvStartDate);
        tvEndDate = (TextView) view.findViewById(R.id.ac_tvEndDate);
        
        // 如果要隐藏当前日期，则使用下面方法。
        if (!isDayVisible) {
            hidDay(mDatePicker_start);
            hidDay(mDatePicker_end);
        }
    }
    /**
     * 隐藏DatePicker中的日期显示
     * 
     * @param mDatePicker
     */
    private void hidDay(DatePicker mDatePicker) {
        Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
        for (Field datePickerField : datePickerfFields) {
            if ("mDaySpinner".equals(datePickerField.getName())) {
                datePickerField.setAccessible(true);
                Object dayPicker = new Object();
                try {
                    dayPicker = datePickerField.get(mDatePicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                // datePicker.getCalendarView().setVisibility(View.GONE);
                ((View) dayPicker).setVisibility(View.GONE);
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        // Log.d(this.getClass().getSimpleName(), String.format("which:%d",
        // which));
        // 如果是“取 消”按钮，则返回，如果是“确 定”按钮，则往下执行
        if (which == BUTTON_POSITIVE)
            tryNotifyDateSet();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        if (view.getId() == R.id.ac_datePickerStart)
            mDatePicker_start.init(year, month, day, this);
        if (view.getId() == R.id.ac_datePickerEnd)
            mDatePicker_end.init(year, month, day, this);
        // updateTitle(year, month, day);
    }

    /**
     * 获得开始日期的DatePicker
     *
     * @return The calendar view.
     */
    public DatePicker getDatePickerStart() {
        return mDatePicker_start;
    }

    /**
     * 获得结束日期的DatePicker
     *
     * @return The calendar view.
     */
    public DatePicker getDatePickerEnd() {
        return mDatePicker_end;
    }

    /**
     * Sets the start date.
     *
     * @param year
     *            The date year.
     * @param monthOfYear
     *            The date month.
     * @param dayOfMonth
     *            The date day of month.
     */
    public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
    }

    /**
     * Sets the end date.
     *
     * @param year
     *            The date year.
     * @param monthOfYear
     *            The date month.
     * @param dayOfMonth
     *            The date day of month.
     */
    public void updateEndDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_end.updateDate(year, monthOfYear, dayOfMonth);
    }

    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mDatePicker_start.clearFocus();
            mDatePicker_end.clearFocus();
            mCallBack.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(),
                    mDatePicker_start.getDayOfMonth(), mDatePicker_end, mDatePicker_end.getYear(),
                    mDatePicker_end.getMonth(), mDatePicker_end.getDayOfMonth());
        }
    }

    @Override
    protected void onStop() {
        // tryNotifyDateSet();
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(START_YEAR, mDatePicker_start.getYear());
        state.putInt(START_MONTH, mDatePicker_start.getMonth());
        state.putInt(START_DAY, mDatePicker_start.getDayOfMonth());
        state.putInt(END_YEAR, mDatePicker_end.getYear());
        state.putInt(END_MONTH, mDatePicker_end.getMonth());
        state.putInt(END_DAY, mDatePicker_end.getDayOfMonth());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int start_year = savedInstanceState.getInt(START_YEAR);
        int start_month = savedInstanceState.getInt(START_MONTH);
        int start_day = savedInstanceState.getInt(START_DAY);
        mDatePicker_start.init(start_year, start_month, start_day, this);

        int end_year = savedInstanceState.getInt(END_YEAR);
        int end_month = savedInstanceState.getInt(END_MONTH);
        int end_day = savedInstanceState.getInt(END_DAY);
        mDatePicker_end.init(end_year, end_month, end_day, this);

    }
    /**
     * 显示
     * */
    @Override
    public void show() {
    	super.show();
    	if(isCutScreen&&activity!=null){
    		//如果是不是横屏改为横屏
    		if(activity.getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
    			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    		}
    	}
    }
    
    @Override
    public void dismiss() {
    	if(isCutScreen&&activity!=null){
    		//如果是不是竖屏改为竖屏
    		if(activity.getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
    			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    		}
    	}
    	super.dismiss();
    }
    

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
		this.getButton(BUTTON_POSITIVE).setText(ok);
	}

	public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
		this.getButton(BUTTON_NEGATIVE).setText(cancel);
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
		tvStartDate.setText(startDate);
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
		tvEndDate.setText(endDate);
	}
	/***
	 * 设置是否 dialog显示的时候横屏，隐藏的时候竖屏
	 */
	public void setCutScreen(Activity activity,boolean isCutScreen){
		this.activity = activity;
		this.isCutScreen = isCutScreen;
	}
}