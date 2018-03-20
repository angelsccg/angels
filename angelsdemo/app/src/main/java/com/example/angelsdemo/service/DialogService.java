package com.example.angelsdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.angels.util.ACLog;
import com.example.angelsdemo.activity.ControlActivity;
import com.example.angelsdemo.activity.dialog.DialogActivity;

/**
 * 项目名称：angels
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/5/3 13:55
 */

public class DialogService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        ACLog.i("DialogService-->onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ACLog.i("DialogService-->onStartCommand"+flags+"--"+startId);

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent1 = new Intent(getApplicationContext(), DialogActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                super.run();
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        ACLog.i("DialogService-->onDestroy");
        super.onDestroy();
    }

}
