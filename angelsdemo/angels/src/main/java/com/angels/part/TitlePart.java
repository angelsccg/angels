package com.angels.part;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.angels.R;

/**
 * 项目名称：bills
 * 类描述：标题ui获取整合
 * 创建人：angelsc
 * 创建时间：2016/9/22 16:28
 */
public class TitlePart {
    private Activity activity;
    private View view;
    /**返回*/
    private TextView tvBack;
    /**左边01*/
    private TextView tvLeft01;
    /**标题*/
    private TextView tvTitle;
    /**右边01*/
    private TextView tvRight01;
    /**右边02*/
    private TextView tvRight02;

    private Resources res;
    public TitlePart(View view) {
        this.view = view;
        res = view.getResources();
        init();
    }
    public TitlePart(Activity activity) {
        this.activity = activity;
        res = activity.getResources();
        init();
    }

    private void init() {
        initTitle();
    }

    public void initTitle(){
        tvBack = (TextView) view.findViewById(R.id.ac_tv_back);
        tvLeft01 = (TextView) view.findViewById(R.id.ac_tv_left01);
        tvTitle = (TextView) view.findViewById(R.id.ac_tv_title);
        tvRight01 = (TextView) view.findViewById(R.id.ac_tv_right01);
        tvRight02 = (TextView) view.findViewById(R.id.ac_tv_right02);
    }

    public void setOnBack(String text, int resid, View.OnClickListener listener){
        tvBack.setVisibility(View.VISIBLE);
        tvBack.setText(text);
        if(resid!=0){
            tvBack.setCompoundDrawables(res.getDrawable(resid),null,null,null);
        }
        tvBack.setOnClickListener(listener);
    }

    public void setOnLeft01(String text, int resid, View.OnClickListener listener){
        tvLeft01.setVisibility(View.VISIBLE);
        tvLeft01.setText(text);
        if(resid!=0){
            tvLeft01.setCompoundDrawables(res.getDrawable(resid),null,null,null);
        }
        tvLeft01.setOnClickListener(listener);
    }

    public void setOnRight01(String text, int resid, View.OnClickListener listener){
        tvRight01.setVisibility(View.VISIBLE);
        tvRight01.setText(text);
        if(resid!=0){
            tvRight01.setCompoundDrawables(res.getDrawable(resid),null,null,null);
        }
        if(listener!=null){
            tvRight01.setOnClickListener(listener);
        }
    }

    public void setOnRight02(String text, int resid, View.OnClickListener listener){
        tvRight02.setVisibility(View.VISIBLE);
        tvRight02.setText(text);
        if(resid!=0){
            tvRight02.setCompoundDrawables(res.getDrawable(resid),null,null,null);
        }
        if(listener!=null){
            tvRight02.setOnClickListener(listener);
        }
    }
}
