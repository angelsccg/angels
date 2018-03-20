package com.example.angelsdemo.activity.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.angels.util.ACLogUtil;
import com.angels.util.ACStringUtil;
import com.angels.util.ACToastUtils;
import com.example.angelsdemo.R;
import com.example.angelsdemo.activity.BaseActivity;
import com.example.angelsdemo.utils.BluetoothUtil;
import com.example.angelsdemo.utils.CRCUtils;
import com.example.angelsdemo.utils.HexStringUtils;
import com.example.angelsdemo.utils.WristbanDorderCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static android.bluetooth.le.ScanSettings.MATCH_MODE_STICKY;
import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY;

public class BluetoothGattActivity extends BaseActivity implements View.OnClickListener {
    private String deviceName = "Bioland-BGM";
    /**
     * 已配对蓝牙设备
     */
    private Set<BluetoothDevice> pairDevices;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothGatt bluetoothGatt;
    private List<BluetoothGattService> mServiceList;
    private BroadcastReceiver mStatusReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED://蓝牙状态值发生改变
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.i("BluetoothGattActivity", "蓝牙-->正在打开蓝牙...");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            startDB();
                            //开始扫描
                            Log.i("BluetoothGattActivity", "蓝牙-->蓝牙设备搜索中...");
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.i("BluetoothGattActivity", "蓝牙-->蓝牙STATE_TURNING_OFF...");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            Log.i("BluetoothGattActivity", "蓝牙-->蓝牙STATE_OFF...");
                            break;
                        default:
                            Log.i("BluetoothGattActivity", "蓝牙-->default01-->" + blueState);
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_FOUND://蓝牙发现设备
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
                    Log.i("BluetoothGattActivity", "蓝牙-->ACTION_FOUND-->" + device.getName() + "--" + device.getType() + "--" + rssi);
                    if ("Thermometer".equals(device.getName().trim())) {
                        connect(device);
                    }
//                    if("MI Band 2".equals(device.getName()) && device.getType() == 2){
//                        connect(device);
//                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED://蓝牙扫描过程开始
                    Log.i("BluetoothGattActivity", "蓝牙-->ACTION_DISCOVERY_STARTED-->蓝牙扫描过程开始");
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED://蓝牙扫描过程结束
                    Log.i("BluetoothGattActivity", "蓝牙-->ACTION_DISCOVERY_FINISHED-->蓝牙扫描过程结束");
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
//                    BluetoothDevice deviceBong = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    if (deviceBong != null && BluetoothGattActivity.this.device != null && deviceBong.getAddress().equals(BluetoothGattActivity.this.device.getAddress())) {
//                        int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);
//                        if (bondState == BluetoothDevice.BOND_BONDED) {
//                            handleDeviceBonded();
//                        }
//                    }
                    Log.i("BlueToothTestActivity", "蓝牙-->ACTION_BOND_STATE_CHANGED");
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_BONDING://正在配对
                            Log.i("BlueToothTestActivity", "蓝牙-->正在配对......");
                            break;
                        case BluetoothDevice.BOND_BONDED://配对结束
                            Log.i("BlueToothTestActivity", "蓝牙-->蓝牙-->完成配对");
                            bluetoothGatt = device.connectGatt(BluetoothGattActivity.this, false, bluetoothGattCallback);
                            break;
                        case BluetoothDevice.BOND_NONE://取消配对/未配对
                            Log.i("BlueToothTestActivity", "蓝牙-->取消配对");
                            break;
                        default:
                            Log.i("BlueToothTestActivity", "蓝牙-->BondState:" + device.getBondState());
                            break;
                    }
                    break;
                default:
                    Log.i("BluetoothGattActivity", "蓝牙-->default02-->" + intent.getAction());
                    break;
            }
        }
    };
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("BluetoothGattActivity", "蓝牙-->searchBD2扫描-->onLeScan-->" + rssi + "--" + device.getName() + "--" + device.getType() + "--" + device.getAddress());
//            if("bong4".equals(device.getName()) && device.getType() == 2){
//                connect(device);
//                bluetoothAdapter.stopLeScan(leScanCallback);
//            }
            if (deviceName.equals(device.getName().trim()) && device.getType() == 2) {
                ACLogUtil.i("蓝牙-->searchBD2扫描-->进来了");
                connect(device);
                bluetoothAdapter.stopLeScan(leScanCallback);
            }
        }
    };
    private BleScanCallback scanCallback;
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private ScanCallback scanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//            super.onScanResult(callbackType, result);
//            Log.i("BluetoothGattActivity","蓝牙-->searchBD2扫描-->onScanResult-->"+callbackType+"--"+result.getDevice().getName()+"--"+result.getDevice().getType() + "--" + result.getDevice().getAddress());
//            if("bong4".equals(result.getDevice().getName()) && result.getDevice().getType() == 2){
//                connect(result.getDevice());
//                mBluetoothLeScanner.stopScan(scanCallback);
//            }
//        }
//
//
//        @Override
//        public void onBatchScanResults(List<ScanResult> results) {
//            super.onBatchScanResults(results);
//            Log.i("BluetoothGattActivity","蓝牙-->searchBD2扫描-->onBatchScanResults-->"+results.size());
//        }
//
//        @Override
//        public void onScanFailed(int errorCode) {
//            super.onScanFailed(errorCode);
//            Log.i("BluetoothGattActivity","蓝牙-->searchBD2扫描-->onScanFailed-->"+errorCode);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_gatt);
        findViewById(R.id.btn01).setOnClickListener(this);
        findViewById(R.id.btn02).setOnClickListener(this);
        findViewById(R.id.btn03).setOnClickListener(this);
        findViewById(R.id.btn04).setOnClickListener(this);
        findViewById(R.id.btn05).setOnClickListener(this);
        findViewById(R.id.btn06).setOnClickListener(this);
        findViewById(R.id.btn07).setOnClickListener(this);
        findViewById(R.id.btn08).setOnClickListener(this);
        findViewById(R.id.btn09).setOnClickListener(this);
        findViewById(R.id.btn10).setOnClickListener(this);
        findViewById(R.id.btn11).setOnClickListener(this);
        findViewById(R.id.btn12).setOnClickListener(this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
//        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        this.registerReceiver(mStatusReceive, intentFilter);

        if (BluetoothUtil.isBluetoothEnabled()) {//已经打开了蓝牙
            startDB();
        } else if (openBlueTooth()) {
            //正在打开蓝牙..
        }
//        connect("88:1B:99:03:2B:F4");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn01:
//                sendMi2();
//                sendMiNew();
                send();

                break;
            case R.id.btn02:
                readBattery();
                break;
            case R.id.btn03:
                readVersion();
                break;
            case R.id.btn04:
                sendShake();
                break;
            case R.id.btn05:
                sendStartAni();
                break;
            case R.id.btn06:
                sendEndAni();
                break;
            case R.id.btn07:
                sendStepSync();
                break;
            case R.id.btn08:
                readHeart();
                break;
            case R.id.btn09:
                sendMiShake();
                break;
            case R.id.btn10:
                readMiHeart();
                break;
            case R.id.btn11:
//                readMiConnect();
                send();
                break;
            case R.id.btn12://温度计
                sendT();
                break;
        }
    }

    /**
     * 发送指令
     */
    public void send() {
        //0x26,0xff,0xff,0xff,0x20,0x01
//        final byte[] content = WristbanDorderCode.boundAnimation;
//        final byte[] content = WristbanDorderCode.shake;
//        final byte[] content = WristbanDorderCode.version;
//        final byte[] content = WristbanDorderCode.electricQuantity;
//        final byte[] content = WristbanDorderCode.heartRate;

//        final byte[] content = WristbanDorderCode.shake_one3;
        final byte[] content = new byte[]{0x15, 0x02, 0x01};
        ACLogUtil.i("蓝牙-->发送数据01：" + ACStringUtil.byteArrayToStr(content));
//        final byte[] byteContent = getCrc16(content);
//        ACLogUtil.i("蓝牙-->发送数据02："+ACStringUtil.byteArrayToStr(byteContent));
//        mWriteThread.startWrite();
        new Thread() {
            @Override
            public void run() {
                super.run();
                if(mServiceList == null){
                    return;
                }
                for (BluetoothGattService service : mServiceList) {
                    //0000fee0-0000-1000-8000-00805f9b34fb
                    //0000fee1-0000-1000-8000-00805f9b34fb

                    /*
                    BluetoothGattDescriptor:
                        00001801-0000-1000-8000-00805f9b34fb
                        00002902-0000-1000-8000-00805f9b34fb
                        00001811     00002901-0000-1000-8000-00805f9b34fb
                        00001811     00002902-0000-1000-8000-00805f9b34fb
                    characteristic:
                        00001532-0000-3512-2118-0009af100700

                        小米手环
                        服务：0000180d-0000-1000-8000-00805f9b34fb
                        心率：00002a37-0000-1000-8000-00805f9b34fb
                    * */
//                    ACLogUtil.i("蓝牙-->service UUID："+ service.getUuid() + "---" + service.getInstanceId() + "--" +service.getType() +"---"+service.describeContents());
                    ACLogUtil.i("蓝牙-->service UUID：" + service.getUuid() + "---" + service.getInstanceId() + "--" + service.getType());

                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
//                        ACLogUtil.i("蓝牙-->characteristic UUID："+ characteristic.getUuid() + "---" + characteristic.getInstanceId() + "--" +characteristic.getProperties() + "---" +characteristic.getWriteType()  +"---"+characteristic.describeContents());
                        ACLogUtil.i("蓝牙-->characteristic UUID：" + characteristic.getUuid() + "---" + characteristic.getInstanceId() + "--" + characteristic.getProperties() + "---" + characteristic.getWriteType());
                        characteristic.setValue(content);
                        //6e400002-b5a3-f393-e0a9-e50e24dcca1e
                        //00002a00-0000-1000-8000-00805f9b34fb
//                        if("00002a00-0000-1000-8000-00805f9b34fb".equals(characteristic.getUuid().toString())){
                        boolean notification = bluetoothGatt.setCharacteristicNotification(characteristic, true);
                        ACLogUtil.i("蓝牙-->通知 notification=" + notification);
//                        }

                        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                        for (BluetoothGattDescriptor dp : descriptors) {
                            dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            // dp.setValue(content);
                            boolean resBd = bluetoothGatt.writeDescriptor(dp);
//                            bluetoothGatt.readDescriptor(dp);
                            ACLogUtil.i("蓝牙-->descriptors resBd: " + resBd + " ,Value=" + ACStringUtil.byteArrayToStr(dp.getValue()) + ",UUID:" + dp.getUuid());
                        }

//                        if("6e400002-b5a3-f393-e0a9-e50e24dcca1e".equals(characteristic.getUuid().toString())){
                        boolean res = bluetoothGatt.writeCharacteristic(characteristic);//开始写入
//                        boolean res = bluetoothGatt.readCharacteristic(characteristic);
                        ACLogUtil.i("蓝牙-->结果写入 res=" + res + ",uuid:" + characteristic.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic.getValue()));
//                        }else{
//                            ACLogUtil.i("蓝牙-->结果不写入 -----,uuid:"+characteristic.getUuid());
//                        }
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        boolean res2 = bluetoothGatt.writeCharacteristic(characteristic);//开始写入
//                        boolean res = bluetoothGatt.readCharacteristic(characteristic);
                        ACLogUtil.i("蓝牙-->结果写入 res2=" + res2 + ",uuid:" + characteristic.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic.getValue()));
                    }
                }
            }
        }.start();
    }

//    private BluetoothGattService Mi2Service;
//    private BluetoothGattCharacteristic characteristic09;

    /**
     * 发送指令
     */
    public void sendMiNew() {
        //0x26,0xff,0xff,0xff,0x20,0x01
//        final byte[] content = WristbanDorderCode.boundAnimation;
//        final byte[] content = WristbanDorderCode.shake;
//        final byte[] content = WristbanDorderCode.version;
//        final byte[] content = WristbanDorderCode.electricQuantity;
//        final byte[] content = WristbanDorderCode.heartRate;
//        final byte[] content = WristbanDorderCode.shake_one3;
        final byte[] content01 = new byte[]{0x01, 0x08};
        final byte[] content02 = new byte[]{0x03, 0x08, (byte) 0xf3, (byte) 0xc1, 0x41, (byte) 0xdc, 0x51, 0x35, 0x3a, (byte) 0x97, (byte) 0x8b, (byte) 0xd4, 0x0f, 0x0f, 0x2e, (byte) 0xe4, 0x64, (byte) 0x90};
        final byte[] content03 = new byte[]{(byte) 0xe2, 0x07, 0x01, 0x09, 0x09, 0x02, 0x39, 0x02, 0x00, 0x00, 0x20};

        new Thread() {
            @Override
            public void run() {
                super.run();
                BluetoothGattService Service01 = bluetoothGatt.getService(UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("00000003-0000-3512-2118-0009af100700"));
//                BluetoothGattDescriptor dp01 = characteristic01.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//                boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
//                ACLogUtil.i("蓝牙-->通知 notification01=" + notification01);
                //01-1
//                dp01.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                boolean resBd01 = bluetoothGatt.writeDescriptor(dp01);
//                ACLogUtil.i("蓝牙-->descriptors resBd01: "+ resBd01 +" ,Value=" + ACStringUtil.byteArrayToStr(dp01.getValue()) + ",UUID:" + dp01.getUuid());
//                //01-2
//                try {
//                    sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //0x02,0x80,0x07,0x1e,0x7f
                characteristic01.setValue(new byte[]{0x02, (byte) 0x80, 0x0b, 0x00, 0x7f});
                boolean res01 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
                ACLogUtil.i("蓝牙-->结果写入 res=" + res01 + ",uuid:" + characteristic01.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic01.getValue()));

//                try {
//                    sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //02-1
//                characteristic01.setValue(new byte[]{0x02,0x08});
//                boolean res2 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                ACLogUtil.i("蓝牙-->结果写入 res2=" + res2 + ",uuid:"+characteristic01.getUuid() + "--" +ACStringUtil.byteArrayToStr(characteristic01.getValue()));

            }
        }.start();
    }

    /**
     * 0 默认什么都没有
     * 1 第一步
     * 2 第二步
     * 3 第三步
     */
    private int characteristicChangedType = 0;
    private int descriptorType = 0;

    /**
     * 发送指令
     */
    public void sendMi2() {
        //0x26,0xff,0xff,0xff,0x20,0x01
//        final byte[] content = WristbanDorderCode.boundAnimation;
//        final byte[] content = WristbanDorderCode.shake;
//        final byte[] content = WristbanDorderCode.version;
//        final byte[] content = WristbanDorderCode.electricQuantity;
//        final byte[] content = WristbanDorderCode.heartRate;
//        final byte[] content = WristbanDorderCode.shake_one3;
        final byte[] content = new byte[]{0x01, 0x08, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45};
        ACLogUtil.i("蓝牙-->发送数据01：" + ACStringUtil.byteArrayToStr(content));
        new Thread() {
            @Override
            public void run() {
                super.run();
                BluetoothGattService Service01 = bluetoothGatt.getService(UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("00000009-0000-3512-2118-0009af100700"));
                BluetoothGattDescriptor dp01 = characteristic01.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
                ACLogUtil.i("蓝牙-->通知 notification01=" + notification01);
                //01-1
                dp01.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                boolean resBd01 = bluetoothGatt.writeDescriptor(dp01);
                ACLogUtil.i("蓝牙-->descriptors resBd01: " + resBd01 + " ,Value=" + ACStringUtil.byteArrayToStr(dp01.getValue()) + ",UUID:" + dp01.getUuid());
                //01-2
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                characteristic01.setValue(content);
                characteristicChangedType = 1;
                boolean res01 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
                ACLogUtil.i("蓝牙-->结果写入 res=" + res01 + ",uuid:" + characteristic01.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic01.getValue()));
//                try {
//                    sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //02-1
//                characteristic01.setValue(new byte[]{0x02,0x08});
//                boolean res2 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                ACLogUtil.i("蓝牙-->结果写入 res2=" + res2 + ",uuid:"+characteristic01.getUuid() + "--" +ACStringUtil.byteArrayToStr(characteristic01.getValue()));


            }
        }.start();
    }

    /**
     * 第二步
     */
    private void sendMi02() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                BluetoothGattService Service02 = bluetoothGatt.getService(UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic characteristic02 = Service02.getCharacteristic(UUID.fromString("00000009-0000-3512-2118-0009af100700"));
                BluetoothGattDescriptor dp02 = characteristic02.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                boolean notification02 = bluetoothGatt.setCharacteristicNotification(characteristic02, true);
                ACLogUtil.i("蓝牙-->通知 notification02=" + notification02);

                characteristicChangedType = 2;
                characteristic02.setValue(new byte[]{0x02, 0x08});
                boolean res2 = bluetoothGatt.writeCharacteristic(characteristic02);//开始写入
                ACLogUtil.i("蓝牙-->结果写入 res2=" + res2 + ",uuid:" + characteristic02.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic02.getValue()));
            }
        }.start();
    }

    /**
     * 第三步
     */
    private void sendMi03(final byte[] value) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                characteristicChangedType = 3;
                BluetoothGattService Service01 = bluetoothGatt.getService(UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("00000009-0000-3512-2118-0009af100700"));
                boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
                ACLogUtil.i("蓝牙-->通知 notification01=" + notification01);
                characteristic01.setValue(value);
                boolean res1 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
                ACLogUtil.i("蓝牙-->结果写入 res1=" + res1 + ",uuid:" + characteristic01.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic01.getValue()));

            }
        }.start();
    }

    /**
     * 第四步
     */
    private void sendMi04(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
//                characteristicChangedType = 4;

                try {
                    sendMi04_1();
                    sleep(300);
                    sendMi04_1();
                    sleep(300);
                    sendMi04_1();
                    sleep(300);
                    sendMi04_2(new byte[]{0x06, 0x03, 0x00, 0x00});
                    sleep(300);
                    sendMi04_2(new byte[]{0x06, 0x0a, 0x00, 0x00});
                    sleep(300);
                    sendMi04_2(new byte[]{0x06, 0x02, 0x00, 0x01});
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * 第四步 01
     */
    private void sendMi04_1() {
        BluetoothGattService Service01 = bluetoothGatt.getService(UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb"));
        BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("00000003-0000-3512-2118-0009af100700"));
        BluetoothGattDescriptor dp01 = characteristic01.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
        ACLogUtil.i("蓝牙-->通知 notification01=" + notification01);
        dp01.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        boolean resBd01 = bluetoothGatt.writeDescriptor(dp01);
        ACLogUtil.i("蓝牙-->descriptors resBd0123: " + resBd01 + " ,Value=" + ACStringUtil.byteArrayToStr(dp01.getValue()) + ",UUID:" + dp01.getUuid());
    }

    /**
     * 第四步 02
     */
    private void sendMi04_2(final byte[] bytes) {
        characteristicChangedType = 4;
        BluetoothGattService Service01 = bluetoothGatt.getService(UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb"));
        BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("00000003-0000-3512-2118-0009af100700"));
//                BluetoothGattDescriptor dp01 = characteristic01.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
        ACLogUtil.i("蓝牙-->通知 notification01=" + notification01);
//                dp01.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                boolean resBd01 = bluetoothGatt.writeDescriptor(dp01);
//                ACLogUtil.i("蓝牙-->descriptors resBd0123: "+ resBd01 +" ,Value=" + ACStringUtil.byteArrayToStr(dp01.getValue()) + ",UUID:" + dp01.getUuid());
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
        characteristic01.setValue(bytes);
        boolean res1 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x0a,0x00,0x00});
//                boolean res1 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x02,0x00,0x01});
//                boolean res2 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x03,0x00,0x00});
//                boolean res3 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
        ACLogUtil.i("蓝牙-->结果写入 res123=" + res1 + ",uuid:" + characteristic01.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic01.getValue()));
    }


    // 选中发送数据的蓝牙设备，全局变量，否则连接在方法执行完就结束了
//    private BluetoothDevice selectDevice;

    /**
     * 数据交互的服务
     */
    private BluetoothGattService service;
    /**
     * 只能写入数据的特征
     */
    private BluetoothGattCharacteristic sendCharacteristic;
    /**
     * 能写能收数据的特征
     */
    private BluetoothGattCharacteristic receiveCharacteristic;
    /**
     * 发送数据
     */
    private static final int SEND_TYPE = 0;
    /**
     * 发送数据并接收数据
     */
    private static final int RECEIVE_TYPE = 1;

    /**
     * bong手环 发送指令
     *
     * @param type 0:只发送数据  1：发送数据并接收数据
     */
    private void send(final byte[] order, final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (bluetoothGatt == null) {
                    ACLogUtil.i("蓝牙-->bluetoothGatt为空");
                    return;
                }

                if (service == null) {
                    service = bluetoothGatt.getService(UUID.fromString(WristbanDorderCode.serviceUUID));
                }
                if (service == null) {
                    ACLogUtil.i("蓝牙-->BluetoothGattService为空，UUID：" + WristbanDorderCode.serviceUUID);
                    return;
                }
                if (type == 1) {
                    if (receiveCharacteristic == null) {
                        receiveCharacteristic = service.getCharacteristic(UUID.fromString(WristbanDorderCode.receiveCharacteristicUUID));
                    }
                    if (receiveCharacteristic == null) {
                        ACLogUtil.i("蓝牙-->sendCharacteristic为空，UUID：" + WristbanDorderCode.receiveCharacteristicUUID);
                        return;
                    }
                    BluetoothGattDescriptor dp = receiveCharacteristic.getDescriptor(UUID.fromString(WristbanDorderCode.receiveDbUUID));
                    if (dp == null) {
                        ACLogUtil.i("蓝牙-->BluetoothGattDescriptor，UUID：" + WristbanDorderCode.receiveDbUUID);
                        return;
                    }
                    boolean receiveNotification = bluetoothGatt.setCharacteristicNotification(receiveCharacteristic, true);
                    ACLogUtil.i("蓝牙-->通知 receiveNotification=" + receiveNotification);
                    dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    boolean resBd = bluetoothGatt.writeDescriptor(dp);
                    ACLogUtil.i("蓝牙-->descriptors resBd:" + resBd + ",写入：" + ACStringUtil.byteArrayToStr(dp.getValue()) + ",UUID:" + dp.getUuid());
                }

                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (sendCharacteristic == null) {
                    sendCharacteristic = service.getCharacteristic(UUID.fromString(WristbanDorderCode.sendCharacteristicUUID));
                }
                if (sendCharacteristic == null) {
                    ACLogUtil.i("蓝牙-->sendCharacteristic为空，UUID：" + WristbanDorderCode.sendCharacteristicUUID);
                    return;
                }
                sendCharacteristic.setValue(order);
                boolean notification = bluetoothGatt.setCharacteristicNotification(sendCharacteristic, true);
                ACLogUtil.i("蓝牙-->通知 notification=" + notification);
                boolean res = bluetoothGatt.writeCharacteristic(sendCharacteristic);//开始写入
//              boolean res = bluetoothGatt.readCharacteristic(characteristic);
                ACLogUtil.i("蓝牙-->writeCharacteristic结果 res=" + res);
            }
        }.start();

    }

    /**
     * 小米手环 发送指令
     *
     * @param type 0:只发送数据  1：发送数据并接收数据
     */
    private void sendMi(final byte[] order, final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (bluetoothGatt == null) {
                    ACLogUtil.i("蓝牙-->bluetoothGatt为空");
                    return;
                }

                if (service == null) {
                    service = bluetoothGatt.getService(UUID.fromString(WristbanDorderCode.serviceUUID));
                }
                if (service == null) {
                    ACLogUtil.i("蓝牙-->BluetoothGattService为空，UUID：" + WristbanDorderCode.serviceUUID);
                    return;
                }
                if (type == 1) {
                    if (receiveCharacteristic == null) {
                        receiveCharacteristic = service.getCharacteristic(UUID.fromString(WristbanDorderCode.receiveCharacteristicUUID));
                    }
                    if (receiveCharacteristic == null) {
                        ACLogUtil.i("蓝牙-->sendCharacteristic为空，UUID：" + WristbanDorderCode.receiveCharacteristicUUID);
                        return;
                    }
                    BluetoothGattDescriptor dp = receiveCharacteristic.getDescriptor(UUID.fromString(WristbanDorderCode.receiveDbUUID));
                    if (dp == null) {
                        ACLogUtil.i("蓝牙-->BluetoothGattDescriptor，UUID：" + WristbanDorderCode.receiveDbUUID);
                        return;
                    }
                    boolean receiveNotification = bluetoothGatt.setCharacteristicNotification(receiveCharacteristic, true);
                    ACLogUtil.i("蓝牙-->通知 receiveNotification=" + receiveNotification);
                    dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    boolean resBd = bluetoothGatt.writeDescriptor(dp);
                    ACLogUtil.i("蓝牙-->descriptors resBd:" + resBd + ",写入：" + ACStringUtil.byteArrayToStr(dp.getValue()) + ",UUID:" + dp.getUuid());
                }

                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (sendCharacteristic == null) {
                    sendCharacteristic = service.getCharacteristic(UUID.fromString(WristbanDorderCode.sendCharacteristicUUID));
                }
                if (sendCharacteristic == null) {
                    ACLogUtil.i("蓝牙-->sendCharacteristic为空，UUID：" + WristbanDorderCode.sendCharacteristicUUID);
                    return;
                }
                sendCharacteristic.setValue(order);
                boolean notification = bluetoothGatt.setCharacteristicNotification(sendCharacteristic, true);
                ACLogUtil.i("蓝牙-->通知 notification=" + notification);
                boolean res = bluetoothGatt.writeCharacteristic(sendCharacteristic);//开始写入
//              boolean res = bluetoothGatt.readCharacteristic(characteristic);
                ACLogUtil.i("蓝牙-->writeCharacteristic结果 res=" + res);
            }
        }.start();

    }

    /**
     * 读取电量
     */
    private void readBattery() {
        final byte[] order = WristbanDorderCode.electricQuantity;
        ACLogUtil.i("蓝牙-->发送<读取电量>指令：" + ACStringUtil.byteArrayToStr(order));
        send(order, RECEIVE_TYPE);
    }

    /**
     * 读取版本号
     */
    private void readVersion() {
        final byte[] order = WristbanDorderCode.version;
        ACLogUtil.i("蓝牙-->发送<读取版本号>指令：" + ACStringUtil.byteArrayToStr(order));
        send(order, RECEIVE_TYPE);
    }

    /**
     * 发送震动指令
     */
    private void sendShake() {
        final byte[] order = WristbanDorderCode.shake;
        ACLogUtil.i("蓝牙-->发送<发送震动>指令：" + ACStringUtil.byteArrayToStr(order));
        send(order, SEND_TYPE);
    }

    /**
     * 发送开始动画指令
     */
    private void sendStartAni() {
        final byte[] order = WristbanDorderCode.startAni;
        ACLogUtil.i("蓝牙-->发送<开始动画>指令：" + ACStringUtil.byteArrayToStr(order));
        send(order, SEND_TYPE);
    }

    /**
     * 发送结束动画（打钩）指令
     */
    private void sendEndAni() {
        final byte[] order = WristbanDorderCode.endAni;
        ACLogUtil.i("蓝牙-->发送<结束动画（打钩）>指令：" + ACStringUtil.byteArrayToStr(order));
        send(order, SEND_TYPE);
    }

    /**
     * 发送步数(修改手环上的步数)指令
     */
    private void sendStepSync() {
        final byte[] order = WristbanDorderCode.stepSync;
        ACLogUtil.i("蓝牙-->发送<步数同步>指令：" + ACStringUtil.byteArrayToStr(order));
        send(order, SEND_TYPE);
//        send(order,SEND_TYPE);
    }

    /**
     * 发送接收心率
     */
    private void readHeart() {
        final byte[] order = WristbanDorderCode.heartRate;
        ACLogUtil.i("蓝牙-->发送<发送接收心率>指令：" + ACStringUtil.byteArrayToStr(order));
        send(order, RECEIVE_TYPE);
//        send(order,SEND_TYPE);
    }

    /**
     * 发送小米手环震动
     */
    private void sendMiShake() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                BluetoothGattService Service01 = bluetoothGatt.getService(UUID.fromString("00001811-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("00002a46-0000-1000-8000-00805f9b34fb"));
                BluetoothGattDescriptor dp01 = characteristic01.getDescriptor(UUID.fromString("00002901-0000-1000-8000-00805f9b34fb"));
//                boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
//                ACLogUtil.i("蓝牙-->通知 notification01=" + notification01);
//                dp01.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                boolean resBd01 = bluetoothGatt.writeDescriptor(dp01);
//                ACLogUtil.i("蓝牙-->descriptors resBd0123: "+ resBd01 +" ,Value=" + ACStringUtil.byteArrayToStr(dp01.getValue()) + ",UUID:" + dp01.getUuid());
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                characteristic01.setValue(new byte[]{0x05, 0x01});
                boolean res1 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x0a,0x00,0x00});
//                boolean res1 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x02,0x00,0x01});
//                boolean res2 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x03,0x00,0x00});
//                boolean res3 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
                ACLogUtil.i("蓝牙-->结果写入 res1=" + res1 + ",uuid:" + characteristic01.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic01.getValue()));
            }
        }.start();
    }
    /**
     * 温度计接收
     */
    private void sendT() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                BluetoothGattService Service01 = bluetoothGatt.getService(UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));
//                BluetoothGattCharacteristic characteristic02 = Service01.getCharacteristic(UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb"));
                BluetoothGattDescriptor dp01 = characteristic01.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
                ACLogUtil.i("蓝牙-->通知 notification01=" + notification01);
                dp01.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                boolean resBd01 = bluetoothGatt.writeDescriptor(dp01);
                ACLogUtil.i("蓝牙-->descriptors resBd01: "+ resBd01 +" ,Value=" + ACStringUtil.byteArrayToStr(dp01.getValue()) + ",UUID:" + dp01.getUuid());
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                characteristic01.setValue(new byte[]{0x05, 0x01});
//                boolean res1 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x0a,0x00,0x00});
//                boolean res1 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x02,0x00,0x01});
//                boolean res2 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x03,0x00,0x00});
//                boolean res3 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                ACLogUtil.i("蓝牙-->结果写入 res1=" + res1 + ",uuid:" + characteristic01.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic01.getValue()));
            }
        }.start();
    }

    /**
     * 心率
     */
    private void readMiHeart() {
//        send();
        new Thread() {
            @Override
            public void run() {
                super.run();
                BluetoothGattService Service01 = bluetoothGatt.getService(UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic characteristic02 = Service01.getCharacteristic(UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb"));
                BluetoothGattDescriptor dp01 = characteristic01.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
                boolean notification02 = bluetoothGatt.setCharacteristicNotification(characteristic02, true);
                ACLogUtil.i("蓝牙-->通知 notification01=" + notification01 + "---" + notification02);
                dp01.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                boolean resBd01 = bluetoothGatt.writeDescriptor(dp01);
                ACLogUtil.i("蓝牙-->descriptors resBd0123: " + resBd01 + " ,Value=" + ACStringUtil.byteArrayToStr(dp01.getValue()) + ",UUID:" + dp01.getUuid());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean notification03 = bluetoothGatt.setCharacteristicNotification(characteristic02, true);
                ACLogUtil.i("蓝牙-->通知 notification02=" + notification03);
                characteristic02.setValue(new byte[]{0x15, 0x02, 0x01});
                boolean res1 = bluetoothGatt.writeCharacteristic(characteristic02);//开始写入
                ACLogUtil.i("蓝牙-->结果写入 res1=" + res1 + ",uuid:" + characteristic02.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic02.getValue()));
            }
        }.start();
    }

    /**
     * 连接
     */
    private void readMiConnect() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                BluetoothGattService Service01 = bluetoothGatt.getService(UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb"));
//                BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb"));
                BluetoothGattCharacteristic characteristic01 = Service01.getCharacteristic(UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb"));
                BluetoothGattDescriptor dp01 = characteristic01.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                boolean notification01 = bluetoothGatt.setCharacteristicNotification(characteristic01, true);
                ACLogUtil.i("蓝牙-->通知 notification01=" + notification01);
//                dp01.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                boolean resBd01 = bluetoothGatt.writeDescriptor(dp01);
//                ACLogUtil.i("蓝牙-->descriptors resBd0123: "+ resBd01 +" ,Value=" + ACStringUtil.byteArrayToStr(dp01.getValue()) + ",UUID:" + dp01.getUuid());
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                characteristic01.setValue(new byte[]{0x14, 0x00});
                boolean res1 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x0a,0x00,0x00});
//                boolean res1 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x02,0x00,0x01});
//                boolean res2 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
//                characteristic01.setValue(new byte[]{0x06,0x03,0x00,0x00});
//                boolean res3 = bluetoothGatt.writeCharacteristic(characteristic01);//开始写入
                ACLogUtil.i("蓝牙-->结果写入 res1=" + res1 + ",uuid:" + characteristic01.getUuid() + "--" + ACStringUtil.byteArrayToStr(characteristic01.getValue()));
            }
        }.start();
    }


    /**
     * 写数据
     *
     * @param buffer 指令
     * @return boolean
     */
    private boolean writeCharcteristic(byte[] buffer) {
        //控制uuid
        UUID uuid = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb");
//        if (mGattHashMap == null || !mGattHashMap.containsKey(uuid)) return false;
        //根据uuid从缓存中拿取蓝牙服务特征
//        BluetoothGattCharacteristic mCharacteristic = mGattHashMap.get(uuid);
        BluetoothGattCharacteristic mCharacteristic = mServiceList.get(2).getCharacteristics().get(0);
        if (mCharacteristic == null) return false;
        int charaProp = mCharacteristic.getProperties();
        return writeCharacteristic(buffer, charaProp, mCharacteristic);
    }

    /**
     * 向设备写数据,根据Properties性质使用不同的写入方式
     *
     * @param buffer         写入的数据
     * @param charaProp      BluetoothGattCharacteristic属性
     * @param characteristic BluetoothGattCharacteristic对象
     * @return boolean
     */
    private boolean writeCharacteristic(byte[] buffer, int charaProp, BluetoothGattCharacteristic characteristic) {
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {// PROPERTY_WRITE 默认类型，需要外围设备的确认,才能继续发送写
            // 可写，二进制1000
            characteristic.setValue(buffer);
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);

//            return mBluetoothLeService.writeCharecteristic(characteristic);
            return writeCharecteristic(characteristic);
        } else if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {// PROPERTY_WRITE_NO_RESPONSE 设置该类型不需要外围设备的回应，可以继续写数据。加快传输速率
            // 只可写，二进制0100
            characteristic.setValue(buffer);
            characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            return writeCharecteristic(characteristic);
        }
        return false;
    }

    /**
     * 添加校验码
     *
     * @param content byte[]
     * @return 具备校验码的byte[]
     */
    private byte[] getCrc16(byte[] content) {
        int crc = CRCUtils.crc16_ccitt(content, content.length);//crc校验码
        byte crcL = (byte) (crc & 0xff);
        byte crcH = (byte) ((crc >> 8) & 0xff);
        byte[] bytes = new byte[content.length + 2];
        System.arraycopy(content, 0, bytes, 0, content.length);
        bytes[content.length] = crcL;
        bytes[content.length + 1] = crcH;
        return bytes;
    }


    private void startDB() {
        //获取已配对蓝牙设备
        getPairBD();
        //搜索
//        searchBD();
        searchBD2();
    }

    /**
     * 打开蓝牙
     */
    private boolean openBlueTooth() {

        /*1判断是否支持蓝牙*/
//        if(BluetoothUtil.isBluetoothSupported()){
//            ACToastUtils.showMessage(this,"该手机不支持蓝牙");
//            return false;
//        }
         /*2判断是否已经打开了蓝牙,没打开的话就打开*/
        if (!BluetoothUtil.isBluetoothEnabled()) {
            boolean isOpen = BluetoothUtil.turnOnBluetooth();
            if (!isOpen) {
                ACToastUtils.showMessage(this, "打开蓝牙失败，请手动打开蓝牙");
            }
            return isOpen;
        }
        return true;
    }

    /**
     * 3.获取已配对的蓝牙设备
     *
     * @return
     */
    private void getPairBD() {
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        pairDevices = bluetoothAdapter.getBondedDevices();
        for (int i = 0; i < pairDevices.size(); i++) {
            BluetoothDevice device = pairDevices.iterator().next();
            ACLogUtil.i("蓝牙-->已配对的蓝牙设备-->" + device.getName());
        }
    }

    /***
     * 4.搜索周围的蓝牙设备
     */
    private void searchBD() {

        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        bluetoothAdapter.startDiscovery();

    }

    /***
     * 4.搜索周围的蓝牙设备
     */
    private void searchBD2() {
        ACLogUtil.i("蓝牙-->searchBD2扫描-->版本判断" + Build.VERSION.SDK_INT + "--" + Build.VERSION_CODES.LOLLIPOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //2. Android 5.0以上，扫描的结果在mScanCallback中进行处理
            scanCallback = new BleScanCallback();
            mBluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
//            List<ScanFilter> filters = null;
//            List<ScanFilter> filters = new ArrayList<>();
//            ScanFilter filter = new ScanFilter.Builder().setDeviceName(deviceName).build();
//            ScanFilter filter2 = new ScanFilter.Builder().setDeviceName("bong4").build();
//            filters.add(filter);
//            filters.add(filter2);
//            mBluetoothLeScanner.startScan(filters, getScanSettings(), scanCallback);
            mBluetoothLeScanner.startScan(scanCallback);
            //搜索的时候可添加个过滤器
//            mBluetoothLeScanner.startScan(getScanFilters(), getScanSettings(), getScanCallback());
            ACLogUtil.i("蓝牙-->searchBD2扫描-->开始搜索");
        } else {
            ACLogUtil.i("蓝牙-->Android 4.3以上，Android 5.0以下");
            //1. Android 4.3以上，Android 5.0以下
            bluetoothAdapter.startLeScan(leScanCallback);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private ScanSettings getScanSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new ScanSettings.Builder()
                    .setScanMode(SCAN_MODE_LOW_LATENCY)
                    .setMatchMode(MATCH_MODE_STICKY)
                    .build();
        } else {
            return new ScanSettings.Builder()
                    .setScanMode(SCAN_MODE_LOW_LATENCY)
                    .build();
        }
    }

    private BluetoothDevice device;
//    private String address;

    /***
     * 6.蓝牙设备的连接
     */
    private void connect(String address) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        connect(device);
    }

    private void connect(BluetoothDevice device) {
        this.device = device;
//        boolean isCreateBond = device.createBond();
//        ACLogUtil.i("蓝牙-->gatt-->onConnectionStateChange-->isCreateBond:"+isCreateBond);
        //如果想要取消已经配对的设备，只需要将creatBond改为removeBond
//        Method method = null;
//        try {
//            method =BluetoothDevice.class.getMethod("createBond");
//            Log.e(getPackageName(), "蓝牙-->开始配对");
//            method.invoke(device);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

//        this.address = device.getAddress();
//        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//        if(isCreateBond){
//
//        }else{
//            bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback);
//        }
        ACLogUtil.i("蓝牙-->开始连接");
        bluetoothAdapter.enable();
        bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback);
//        ACLogUtil.i("蓝牙-->连接-->"+bluetoothGatt.connect());

    }

    public void close() {
        if (bluetoothGatt == null) {
            return;
        }
        device = null;
        bluetoothGatt.disconnect();
        bluetoothGatt.close();
        bluetoothGatt = null;
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            //status 表示相应的连接或断开操作是否完成，而不是指连接状态
            ACLogUtil.i("蓝牙-->gatt-->onConnectionStateChange-->" + status + "--" + newState);
            if (BluetoothGatt.GATT_SUCCESS == status) {
                ACLogUtil.i("蓝牙-->onConnectionStateChange:status ---> success");
            }
            if (newState == BluetoothProfile.STATE_CONNECTED) {//连接着
                ACLogUtil.i("蓝牙-->onConnectionStateChange:newState ---> 连接着");
                if (mServiceList == null) {
                    gatt.discoverServices();
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//断开
                ACLogUtil.i("蓝牙-->onConnectionStateChange:newState ---> 断开");
                close(); // 防止出现status 133
//                gatt.connect();
//                connect(address);
//                ACLogUtil.i("蓝牙-->onConnectionStateChange:newState ---> 重新开始连接");
            } else{
                close();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i("BluetoothGattActivity", "onServicesDiscovered status" + status);
            mServiceList = gatt.getServices();
            if (mServiceList != null) {
                ACLogUtil.i("蓝牙-->Services num:" + mServiceList.size());
            }
            for (BluetoothGattService service : mServiceList) {
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                ACLogUtil.i("蓝牙-->扫描到Service：" + service.getUuid());
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    ACLogUtil.i("蓝牙-->characteristic: " + characteristic.getUuid());

                    List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                    for (BluetoothGattDescriptor descriptor : descriptors) {
                        ACLogUtil.i("蓝牙-->BluetoothGattDescriptor: " + descriptor.getUuid() + "--" + descriptor.getCharacteristic().getUuid());
                    }
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            byte[] data = characteristic.getValue();
            ACLogUtil.i("蓝牙-->读取结果onCharacteristicRead UUID : " + characteristic.getUuid() + "----" + ACStringUtil.byteArrayToStr(data));
            //读取到值，在这里读数据
            if (status == BluetoothGatt.GATT_SUCCESS) {
                int flag = characteristic.getProperties();
                int format = -1;
                if ((flag & 0x01) != 0) {
                    format = BluetoothGattCharacteristic.FORMAT_UINT16;
                    ACLogUtil.i("蓝牙-->Heart rate format UINT16.");
                } else {
                    format = BluetoothGattCharacteristic.FORMAT_UINT8;
                    ACLogUtil.i("蓝牙-->Heart rate format UINT8.");
                }
                final int value = characteristic.getIntValue(format, 1);
                final int value2 = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1);
                final int value3 = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1);
                ACLogUtil.i("蓝牙-->读取到的值是：" + value + "--" + value2 + "--" + value3);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {

            }
            byte[] data = characteristic.getValue();
            ACLogUtil.i("蓝牙-->onCharacteristicWrite写入结果 uuid:" + characteristic.getUuid() + "---" + status + "---" + ACStringUtil.byteArrayToStr(data));
            //读取到值，在这里读数据
            if (status == BluetoothGatt.GATT_SUCCESS) {
                int flag = characteristic.getProperties();
                int format = -1;
                if ((flag & 0x01) != 0) {
                    format = BluetoothGattCharacteristic.FORMAT_UINT16;
                    ACLogUtil.i("蓝牙-->Heart rate format UINT16.");
                } else {
                    format = BluetoothGattCharacteristic.FORMAT_UINT8;
                    ACLogUtil.i("蓝牙-->Heart rate format UINT8.");
                }
                final int value = characteristic.getIntValue(format, 1);
                final int value2 = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1);
                ACLogUtil.i("蓝牙-->读取到的值是：" + value + "--" + value2);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] data = characteristic.getValue();
            ACLogUtil.i("蓝牙-->onCharacteristicChanged 变化了 uuid:" + characteristic.getUuid() + "----" + ACStringUtil.byteArrayToStr(data));
            try {
                if (data[0] == 0x10 && data[1] == 0x01 && data[2] == 0x01) {
                    ACLogUtil.i("蓝牙-->01-->第一步完成");
                    sendMi02();
                } else if (data[0] == 0x10 && data[1] == 0x02 && data[2] == 0x01) {
                    ACLogUtil.i("蓝牙-->01-->第二步完成");
                    //加密
                    byte[] eValue = handleAESAuth(data, getSecretKey());
                    byte[] responseValue = addAll(new byte[]{0x03, 0x08}, eValue);
                    Log.i("认证", "蓝牙-->01-->第三步前 数据处理-->" + bcd2Str2(responseValue));
                    sendMi03(responseValue);
                } else if (data[0] == 0x10 && data[1] == 0x03 && data[2] == 0x01) {
                    ACLogUtil.i("蓝牙-->01-->第三步完成");
                }
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

//            if(characteristicChangedType == 1){
//                characteristicChangedType = 0;
//                //第二步
//                sendMi02();
//            }else if(characteristicChangedType == 2){
//                characteristicChangedType = 0;
//                ACLogUtil.i("蓝牙-->第二步可以============================" );
//                //开始第三步
//                sendMi03();
//            }else if(characteristicChangedType == 3){
//                characteristicChangedType = 0;
//                ACLogUtil.i("蓝牙-->第三步可以============================" );
//                sendMi04(41);
//                sendMi04(42);
//                sendMi04(43);
//            }else if(characteristicChangedType == 4){
//                characteristicChangedType = 0;
//                ACLogUtil.i("蓝牙-->第四步可以============================" );
//            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            byte[] data = descriptor.getValue();
            ACLogUtil.i("蓝牙-->onDescriptorRead 读取 uuid:" + ACStringUtil.byteArrayToStr(data) + "----" + status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            byte[] data = descriptor.getValue();
            ACLogUtil.i("蓝牙-->onDescriptorWrite 写入:" + ACStringUtil.byteArrayToStr(data) + "----" + status + "---descriptorType:" + descriptorType);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            ACLogUtil.i("蓝牙-->onReliableWriteCompleted" + "----" + status);

        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            ACLogUtil.i("蓝牙-->onReadRemoteRssi" + "----" + status + "---" + rssi);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            ACLogUtil.i("蓝牙-->onMtuChanged" + "----" + status + "---" + mtu);
        }
    };


    /**
     * 读取蓝牙数据
     *
     * @param characteristic 蓝牙特征值
     * @return true or false
     */
    public boolean readCharacteristic(BluetoothGattCharacteristic characteristic) {
        return !(bluetoothAdapter == null || bluetoothGatt == null)
                && bluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * 往设备中写入数据
     *
     * @param characteristic 蓝牙特征值
     * @return true or false
     */
    public boolean writeCharecteristic(BluetoothGattCharacteristic characteristic) {
        ACLogUtil.i("蓝牙-->开始写入 uuid:" + characteristic.getUuid());
        return !(bluetoothAdapter == null || bluetoothGatt == null)
                && bluetoothGatt.writeCharacteristic(characteristic);
    }
    boolean isSearch;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class BleScanCallback extends ScanCallback {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            ACLogUtil.i("蓝牙-->searchBD2扫描-->onScanResult-->" + callbackType + "--" + result.getDevice().getName() + "--" + result.getDevice().getType() + "--" + result.getDevice().getAddress() + "---Rssi：" + result.getRssi());
//            if("bong4".equals(result.getDevice().getName()) && result.getDevice().getType() == 2){
//                connect(result.getDevice());
//                mBluetoothLeScanner.stopScan(scanCallback);
//            }
//            if("Bluetooth BP".equals(result.getDevice().getName()) && result.getDevice().getType() == 2){
            if (deviceName.equals(result.getDevice().getName()) && !isSearch) {
                isSearch =true;
                connect(result.getDevice());
                mBluetoothLeScanner.stopScan(scanCallback);
            }
        }


        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            ACLogUtil.i("蓝牙-->searchBD2扫描-->onBatchScanResults-->" + results.size());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            ACLogUtil.i("蓝牙-->searchBD2扫描-->onScanFailed-->" + errorCode);
        }
    }


    private byte[] handleAESAuth(byte[] value, byte[] secretKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        byte[] mValue = Arrays.copyOfRange(value, 3, 19);
        Cipher ecipher = Cipher.getInstance("AES/ECB/NoPadding");
        SecretKeySpec newKey = new SecretKeySpec(secretKey, "AES");
        ecipher.init(Cipher.ENCRYPT_MODE, newKey);
        byte[] enc = ecipher.doFinal(mValue);
        return enc;
    }

    private byte[] getSecretKey() {
        return new byte[]{0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45};
    }

    public static byte[] addAll(byte[] array1, byte... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        } else {
            byte[] joinedArray = new byte[array1.length + array2.length];
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
            return joinedArray;
        }
    }

    public static byte[] clone(byte[] array) {
        return array == null ? null : (byte[]) array.clone();
    }

    public static String bcd2Str2(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append("0x" + 0);
                stringBuilder.append(hv + ",");
            } else {
                stringBuilder.append("0x" + hv + ",");
            }

        }
        return stringBuilder.toString();
    }

}
/*====================通过address获取设备Device=============================*/
// 如果选择设备为空则代表还没有选择设备
//if (selectDevice == null) {
//        //通过地址获取到该设备
//        selectDevice = bluetoothAdapter.getRemoteDevice(address);
//        }