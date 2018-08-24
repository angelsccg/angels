package com.example.angelsdemo.activity.voice;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;

import java.util.Locale;

/**
 * Created by chencg on 2018/4/8.
 */

public class WordToVoiceActivity extends BaseActivity {
    private TextToSpeech tts;
    private Button btnSpeak;
    private EditText etSpeak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_word_to_voice);

        etSpeak =  (EditText) findViewById(R.id.et_speak);
        btnSpeak = (Button) findViewById(R.id.btn_speak);
        btnSpeak.setOnClickListener(new MyOnClickListener());
        tts = new TextToSpeech(this, new MyOnInitialListener());
    }
    class MyOnInitialListener implements TextToSpeech.OnInitListener {

        @Override
        public void onInit(int status) {
            // tts.setEngineByPackageName("com.iflytek.vflynote");
//            tts.setLanguage(Locale.CHINESE);
            tts.setLanguage(Locale.CHINA);
        }

    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_speak:
                    String speak = etSpeak.getText().toString();
                    if(TextUtils.isEmpty(speak)){
                        speak = "你想说什么";
                    }
                    tts.speak(speak, TextToSpeech.QUEUE_FLUSH, null);
                    break;

                default:
                    break;
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tts != null) { // 关闭TTS引擎
            tts.shutdown();
        }
    }

}
