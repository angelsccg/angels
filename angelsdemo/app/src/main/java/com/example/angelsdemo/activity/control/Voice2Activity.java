package com.example.angelsdemo.activity.control;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.angels.widget.VoiceLineView;
import com.example.angelsdemo.utils.AudioRecordDemo;
import com.example.angelsdemo.R;
import com.example.angelsdemo.AudioProcess;
import com.example.angelsdemo.activity.BaseActivity;

public class Voice2Activity extends BaseActivity implements Runnable {

    private MediaRecorder mMediaRecorder;
    private boolean isAlive = true;
    private VoiceLineView voiceLineView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mMediaRecorder == null) return;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
            double db = 0;// 分贝
            //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
            //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
            //同时，也可以配置灵敏度sensibility
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            //只要有一个线程，不断调用这个方法，就可以使波形变化
            //主要，这个方法必须在ui线程中调用
            voiceLineView.setVolume((int) (db));
        }
    };

    private TextView tvHz;
    AudioRecordDemo record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice2);


        tvHz = (TextView) findViewById(R.id.tv_hz);

        record = new AudioRecordDemo();

        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Voice2Activity.this,"开始录音",Toast.LENGTH_SHORT).show();
                String path = Environment.getExternalStorageDirectory() + "/xn/voice/ccg.amr";
//                AudioRecordManager.getInstance().startRecord(path);
                record.start();
            }
        });
        btnExit = (Button) this.findViewById(R.id.btnStop);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Voice2Activity.this,"停止录音",Toast.LENGTH_SHORT).show();
//                AudioRecordManager.getInstance().stopRecord();
                record.stop();
            }
        });
        btnPlay = (Button) this.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Voice2Activity.this,"播放",Toast.LENGTH_SHORT).show();
//                AudioRecordManager.getInstance().stopRecord();
                record.playRecord();
            }
        });
//        initView();

    }

    /*==============================================================*/

    /*==============================================================*/
    @Override
    protected void onDestroy() {
//        JSON.stringify
        isAlive = false;
        if(mMediaRecorder != null){
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        super.onDestroy();

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void run() {
        while (isAlive) {
            handler.sendEmptyMessage(0);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /** Called when the activity is first created. */
    static int frequency = 8000;// 分辨率
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    static final int audioEncodeing = AudioFormat.ENCODING_PCM_16BIT;
    static final int yMax = 50;// Y轴缩小比例最大值
    static final int yMin = 1;// Y轴缩小比例最小值

    int minBufferSize;// 采集数据需要的缓冲区大小
    AudioRecord audioRecord;// 录音
    AudioProcess audioProcess = new AudioProcess();// 处理

    Button btnStart, btnExit,btnPlay; // 开始停止按钮
    SurfaceView sfv; // 绘图所用
    protected boolean isPlay = true;

    private void initView() {
        Context mContext = getApplicationContext();
        // 按键
        btnStart = (Button) this.findViewById(R.id.btnStart);
        // 画笔和画板
        sfv = (SurfaceView) this.findViewById(R.id.SurfaceView01);
        // 初始化显示
        audioProcess.initDraw(yMax / 2, sfv.getHeight(), mContext, frequency);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isPlay ) {
                    isPlay = false;
                    try {
                        // 录音
                        minBufferSize = AudioRecord.getMinBufferSize(
                                frequency, channelConfiguration,
                                audioEncodeing);
                        // minBufferSize = 2 * minBufferSize;
                        audioRecord = new AudioRecord(
                                MediaRecorder.AudioSource.MIC, frequency,
                                channelConfiguration, audioEncodeing,
                                minBufferSize);
                        audioProcess.baseLine = sfv.getHeight() - 100;
                        audioProcess.frequence = frequency;
                        audioProcess.start(audioRecord, minBufferSize, sfv);
                        Toast.makeText(
                                Voice2Activity.this,
                                "当前设备支持您所选择的采样率:"
                                        + String.valueOf(frequency),
                                Toast.LENGTH_SHORT).show();
                        btnStart.setText("停止");
                    } catch (Exception e) {
                        // TODO: handle exception
                        Toast.makeText(
                                Voice2Activity.this,
                                "当前设备不支持你所选择的采样率"
                                        + String.valueOf(frequency)
                                        + ",请重新选择", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    isPlay = true;
                    btnStart.setText("开始1");
                    audioProcess.stop(sfv);
                }
            }
        });
    }
}
