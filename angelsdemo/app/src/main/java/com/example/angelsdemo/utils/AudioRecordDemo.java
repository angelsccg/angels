package com.example.angelsdemo.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.angelsdemo.BytesTransUtils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by greatpresident on 2014/8/5.
 */
public class AudioRecordDemo {

    private static final String TAG = "AudioRecord";
    static final int SAMPLE_RATE_IN_HZ = 8000;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord mAudioRecord;
    boolean isGetVoiceRun;
    Object mLock;
    int channelConfiguration = AudioFormat.CHANNEL_IN_STEREO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    File file;
    public AudioRecordDemo() {
        mLock = new Object();
     /*============*/
    }

    /*============*/
    double threshold = 1;
    double[] max = new double[3];
    boolean[] getNew = {true,true,true};
    boolean analyzeMode = true;
    int[] maxIndex = {1,4,9};
    int[] topFreq = new int[3];
//    int frequency = 44100;
    int frequency = 8000;
//    int blockSize = 2048;
    int blockSize = BUFFER_SIZE;//640
    ArrayList<Integer> storedMaxFreq = new  ArrayList<Integer>();
//    private boolean append = false;
    /**
     * Loops through FFT results (index 15-250) to find the index with the highest amplitude
     * Uses index to find frequency (Frequency = I*(SamplingRate/SampleSize))
     */
    public void getFrequency(double[] toTransform, int p,int n, int m){
        if (getNew[p] == true){
            max[p] = toTransform[0];
            if (analyzeMode) {
                for(int i = 15; i < 250; i++) { // int i=15; i < toTransform.length
                    if (toTransform[i] > max[p] &&
                            i != maxIndex[n] && i != maxIndex[m] && i != 0) { // && i>15 && i<250
                        max[p] = toTransform[i];
                        maxIndex[p] = i;
                    }
                }
            } else {
                for(int i = 25; i < 300; i++) { // int i=15; i < toTransform.length
                    if (toTransform[i] > max[p] &&
                            i != maxIndex[n] && i != maxIndex[m] && i != 0) { // && i>15 && i<250
                        max[p] = toTransform[i];
                        maxIndex[p] = i;
                    }
                }
            }
            if (analyzeMode){
                //TODO BUFFER_SIZE注意些
                topFreq[p] = (int) Math.round((double)(maxIndex[p]*frequency/blockSize));
                Log.i("BUFFER_SIZE","大小BUFFER_SIZE-->"+BUFFER_SIZE);
            } else {
                topFreq[p] = maxIndex[p]* 12;
            }
        } else {
            max[p] = toTransform[maxIndex[p]];
        }
    }

    public void updateText(int p, int m, int n, TextView display) {
//        Log.i(TAG, "====###====hz--> 开始"+ max[p] +" --" + threshold  );
        if (max[p] > threshold){ //threshold
            if (storedMaxFreq.size() < 6) { // fills the array
                storedMaxFreq.add(topFreq[p]);
            } else { // if storedMaxFreq is full

                // DEFINITIONS
                int mostCommonSum = 0;
                int amountOfCommonSums = 0;
                boolean changeNote = true;
                int[][] storedValueAmounts = new int[8][2];

                ArrayList<Integer> preferredFrequencies = new ArrayList<Integer>();
                int mostUsable = 0;
                int overallAverage = 0;
                ArrayList<Integer> unpreferredFrequencies = new ArrayList<Integer>();

                // UPDATES ARRAY
                ArrayList<Integer> tempStorage = new ArrayList<Integer>();
                tempStorage.addAll(storedMaxFreq);
                storedMaxFreq.clear();
                storedMaxFreq.add(0, topFreq[p]);
                storedMaxFreq.addAll(1, tempStorage);
                storedMaxFreq.remove(6);

                // FINDS RECURRING FREQUENCIES
                for (int i=0; i<storedMaxFreq.size(); i++) { // loop through the storedMaxFreq
                    for (int x=0; x<storedValueAmounts.length; x++){ // loop to check for empty slots in storedValueAmounts and fill them
                        if (storedValueAmounts[x][0] != 0 && storedMaxFreq.get(i) == storedValueAmounts[x][0]) { // if slot is filled and contents are the same, just increase the count by 1
                            storedValueAmounts[x][1] += 1;
                            break;
                        } else if (storedValueAmounts[x][0] == 0) { // if slot is empty, take it
                            storedValueAmounts[x][0] = storedMaxFreq.get(i);
                            break;
                        }
                    }
                }

                // GETS FREQUENCY TO DISPLAY
                for (int i=0; i<storedMaxFreq.size(); i++) { // gets overall average
                    overallAverage += storedMaxFreq.get(i);
                }
                overallAverage /= storedMaxFreq.size();
                int difference = 100000;
                for (int i=0; i<storedMaxFreq.size(); i++){ // finds mostUsable
                    if (Math.abs(overallAverage-storedMaxFreq.get(i)) < difference) {
                        difference = Math.abs(overallAverage-storedMaxFreq.get(i));
                        mostUsable = storedMaxFreq.get(i);
                    }
                }
                for (int i=0; i<storedValueAmounts.length; i++) { // loop to sum up the most common frequencies
                    if (storedValueAmounts[i][1] > 0) {
                        preferredFrequencies.add(i);
                        mostCommonSum += storedValueAmounts[i][0] * (storedValueAmounts[i][1]+1); // adds weight to the summing
                        amountOfCommonSums += (storedValueAmounts[i][1]+1);
                    } else {
                        unpreferredFrequencies.add(storedValueAmounts[i][0]);
                    }
                }
                for (int i=0; i<(preferredFrequencies.size()); i++) { // loop to remove bad values from the most common frequencies
                    if (storedValueAmounts[preferredFrequencies.get(i)][0] != mostUsable) {
                        if (storedValueAmounts[preferredFrequencies.get(i)][0] < (double)mostUsable*0.79
                                || storedValueAmounts[preferredFrequencies.get(i)][0] > (double)mostUsable*1.21) {
                            mostCommonSum -= storedValueAmounts[preferredFrequencies.get(i)][0] * (storedValueAmounts[preferredFrequencies.get(i)][1]+1);
                            amountOfCommonSums -= (storedValueAmounts[preferredFrequencies.get(i)][1]+1);
                        }
                    }
                }
                for (int i=0; i<unpreferredFrequencies.size(); i++) { // adds back in previously removed values that were, in hindsight, pretty good
                    if (unpreferredFrequencies.get(i) > mostUsable*0.9 && unpreferredFrequencies.get(i) < mostUsable*1.1) {
                        mostCommonSum += unpreferredFrequencies.get(i);
                        amountOfCommonSums++;
                    }
                }
                if (amountOfCommonSums > 0) {
                    mostCommonSum = (int) Math.round((double)(mostCommonSum/amountOfCommonSums));
                } else {
                    mostCommonSum = 0;
                }
                if(display !=null){
                    display.setText(Integer.toString(mostCommonSum)+" Hz ");
                }
                Log.i(TAG, "=============voice===频率：" + mostCommonSum);
            }
        }
    }

    public class RecordAudio extends AsyncTask<Void, double[], Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            Log.i("hz","doInBackground-->值");
            if (analyzeMode){
                // RECORD FROM MICROPHONE
                try {
                    //SET UP AUDIORECORDER
                    int bufferSize = AudioRecord.getMinBufferSize(frequency,
                            channelConfiguration, audioEncoding);
                    mAudioRecord = new AudioRecord(
                            MediaRecorder.AudioSource.MIC, frequency,
                            channelConfiguration, audioEncoding, bufferSize);
//                    mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
//                            SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
//                            AudioFormat.ENCODING_PCM_16BIT, blockSize);
                    short[] buffer = new short[blockSize];
                    double[] toTransform = new double[blockSize];
                    mAudioRecord.startRecording();
                    isGetVoiceRun = true;
                    File filezz = new File(getTempFolderName());
                    if (filezz.exists())
                        filezz.mkdirs();
                    file = new File(getTempFilename());
                    //如果存在，就先删除再创建
                    if (file.exists())
                        file.delete();
                    Log.i(TAG,"file-->删除文件");
                    try {
                        file.createNewFile();
                        Log.i(TAG,"file-->创建文件");
                    } catch (Exception e) {
                        Log.i(TAG,"file-->未能创建" + file.toString());
                        throw new IllegalStateException("file-->未能创建:" + file.toString());
                    }
//                    File filezzzz = new File(root_sd+"doc.txt");
//                    if (filezzzz.exists())
//                        filezzzz.mkdirs();
//
//                    File fileccg = new File(root_sd+"doc.txt");
//                    if (fileccg.exists())
//                        fileccg.delete();
//                    fileccg.createNewFile();
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true));
//                    DataOutputStream dosccg = new DataOutputStream(new FileOutputStream(fileccg, true));
                    // RECORDS AUDIO & PERFORMS FFT
                    while (isGetVoiceRun) {
                        int bufferReadResult = mAudioRecord.read(buffer, 0, blockSize);
                        long v = 0;
                        for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                            toTransform[i] = (double) buffer[i] / 32768.0; // / 32768.0
                            v += buffer[i] * buffer[i];
//                            dos.writeShort(buffer[i]);
//                            dosccg.writeShort(buffer[i]);
                        }

                        // 平方和除以数据总长度，得到音量大小。
                        double mean = v / (double) bufferReadResult;
                        double volume = 10 * Math.log10(mean);
                        Log.i(TAG, "=============voice===分贝：" + volume);
                        calc1(buffer,0,bufferReadResult);
                        byte[] bufferByte = BytesTransUtils.getInstance().Shorts2Bytes(buffer);
                         dos.write(bufferByte);
                        publishProgress(toTransform);
                    }
                    dos.close();
//                    dosccg.close();
                    mAudioRecord.stop();
                    mAudioRecord.release();
                } catch (Throwable t) {
                    t.printStackTrace();
                    Log.e("AudioRecord", "Recording Failed");
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(double[]... toTransform) {
            // CALLS TEXT UPDATE
            updateText(0, 1, 2, null);
            //updateText(1, 0, 2, frequencyDisplay2);
            //updateText(2, 0, 1, frequencyDisplay3);


            // DRAWS VISUALIZATION
        }
    }

    /**
     * 除杂音
     */
    void calc1(short[] lin,int off,int len) {
        int i,j;

        for (i = 0; i < len; i++) {
            j = lin[i+off];
            lin[i+off] = (short)(j>>2);
        }
    }
    String root_sd = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xn/voice2/";
    String fileName = "voiceccg";
    public String newFileName() {

        String mFileName = root_sd + fileName + ".wav";
        return mFileName;
    }

    private String getTempFilename() {
        return (getTempFolderName() + ".raw");
    }
    private String getTempFolderName() {
        return (root_sd + fileName);
    }
    private void deleteTempFile() {
        File file = new File(getTempFilename());
        file.delete();
    }


    /*===============*/
    RecordAudio recordTask;
    public  void start(){
        if (isGetVoiceRun) {
            Log.e(TAG, "还在录着呢");
            return;
        }
        recordTask = new RecordAudio();
        recordTask.execute();
//        getNoiseLevel();
    }
    public void onSaveInstanceState() {
        if (isGetVoiceRun){
            recordTask.cancel(true);
        }
        isGetVoiceRun = false;
    }

    public void onPause(){
        if (isGetVoiceRun){
            recordTask.cancel(true);
        }
        if(mAudioRecord != null){
            mAudioRecord.release();
            mAudioRecord = null;
        }
        isGetVoiceRun = false;
    }
    public void onDestory(){
        if(mAudioRecord != null){
            mAudioRecord.release();
            mAudioRecord = null;
        }
        isGetVoiceRun = false;
    }
    /**停止播放*/
    public void stop(){
        isGetVoiceRun = false;
//        append = false;
        copyWaveFile(getTempFilename(), newFileName());
        deleteTempFile();
    }

    private void copyWaveFile(String inFilename, String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = frequency;
        int channels = 2;
        long byteRate = 16 * frequency * channels / 8;
        byte data[] = new byte[blockSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while (in.read(data) != -1) {
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen,
                                     long totalDataLen, long longSampleRate, int channels, long byteRate)
            throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }

    //播放文件
    public void playRecord() {
        if(file == null){
            return;
        }
        //读取文件
        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];
        try {
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            int i = 0;
            while (dis.available() > 0) {
                music[i] = dis.readShort();
                i++;
            }
            dis.close();
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    16000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    musicLength * 2,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();
            audioTrack.write(music, 0, musicLength);
            audioTrack.stop();
        } catch (Throwable t) {
            Log.e(TAG, "播放失败");
        }
    }
}
