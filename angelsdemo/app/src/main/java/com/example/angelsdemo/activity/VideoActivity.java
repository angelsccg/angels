package com.example.angelsdemo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.angels.web.util.ACWeb;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.control.DialogActivity;
import com.example.angelsdemo.activity.control.GuaguakaActivity;
import com.example.angelsdemo.activity.control.ImgScroll2Activity;
import com.example.angelsdemo.activity.control.ImgScrollActivity;
import com.example.angelsdemo.activity.control.MenuActivity;
import com.example.angelsdemo.activity.control.RefreshAndLoadMoreActivity;
import com.example.angelsdemo.activity.control.SeldingFinishActivity;
import com.example.angelsdemo.activity.control.SwipeMenuListviewActivity;
import com.example.angelsdemo.activity.control.SwipeRefreshLayoutRefresh;
import com.example.angelsdemo.activity.voice.WordToVoiceActivity;

import java.io.File;

/**
 * 项目名称：angels3
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/1/13 16:28
 */

public class VideoActivity extends BaseActivity implements View.OnClickListener{
    public static final String[] btnNames= {"系统自带的播放器 ","文字转语音","打开设置 默认语音设置"};
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
//                File audioFile = new File(Environment.getExternalStorageDirectory().getPath()+"/mp4/123.mp4");
//                Uri uri = Uri.fromFile(audioFile);

//                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/mp4/123.mp4");

                Uri uri =  Uri.parse(Environment.getExternalStorageDirectory() + "/Pictures/mp4/123.mp4");
                //调用系统自带的播放器
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Log.v("URI:::::::::", uri.toString());
                intent.setDataAndType(uri, "video/mp4");
                startActivity(intent);
                break;
            case 1:
            {
//                Intent it = new Intent(Intent.ACTION_VIEW);
//                it.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/a.MP3"), "audio/MP3");
//                startActivity(it);
                Intent intent1 = new Intent(this, WordToVoiceActivity.class);
                startActivity(intent1);
            }
            break;
            case 2:
            {
                startActivity(new Intent("com.android.settings.TTS_SETTINGS"));
            }
            break;
            default:
                break;
        }
    }
}
