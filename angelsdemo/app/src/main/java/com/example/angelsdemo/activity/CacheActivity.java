package com.example.angelsdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.angels.manager.DataCleanManager;
import com.angels.web.util.ACWeb;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.control.DialogActivity;
import com.example.angelsdemo.activity.control.GuaguakaActivity;
import com.example.angelsdemo.activity.control.ImageSelectorMainActivity;
import com.example.angelsdemo.activity.control.ImgScroll2Activity;
import com.example.angelsdemo.activity.control.ImgScrollActivity;
import com.example.angelsdemo.activity.control.MenuActivity;
import com.example.angelsdemo.activity.control.RefreshAndLoadMoreActivity;
import com.example.angelsdemo.activity.control.SeldingFinishActivity;
import com.example.angelsdemo.activity.control.StickyNavActivity;
import com.example.angelsdemo.activity.control.StickyNavActivity3;
import com.example.angelsdemo.activity.control.SwipeMenuListviewActivity;
import com.example.angelsdemo.activity.control.SwipeRefreshLayoutRefresh;

/**
 * 项目名称：angels
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/5/8 11:11
 */

public class CacheActivity extends BaseActivity implements View.OnClickListener {
    public static final String[] btnNames= {"缓存大小","清楚缓存"};
    public static final Button[] btns = new Button[btnNames.length];
    private LinearLayout llContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainc);

        llContent = (LinearLayout) findViewById(R.id.llContent);

        for (int i = 0; i < btnNames.length; i++) {
            Button button = new Button(this);
            button.setText(btnNames[i]);
            button.setOnClickListener(this);
            llContent.addView(button);
            btns[i] = button;
            btns[i].setId(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0:
//                DataCleanManager.getCacheSize();
                break;
            case 1:
            {
                DataCleanManager.cleanInternalCache(this);
            }
            break;

            default:
                break;
        }
    }
}
