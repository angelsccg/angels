package com.example.angelsdemo.utils;

/**
 * Created by chencg on 2017/12/12.
 */


import android.bluetooth.BluetoothAdapter;
import android.util.Log;

/**
 * Bluetooth 管理类
 *
 *
 */
public class BluetoothUtil
{

    /**
     * 当前 Android 设备是否支持 Bluetooth
     *
     * @return true：支持 Bluetooth false：不支持 Bluetooth
     */
    public static boolean isBluetoothSupported()
    {
        return BluetoothAdapter.getDefaultAdapter() != null ? true : false;
    }

    /**
     * 当前 Android 设备的 bluetooth 是否已经开启
     *
     * @return true：Bluetooth 已经开启 false：Bluetooth 未开启
     */
    public static boolean isBluetoothEnabled()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();

        if (bluetoothAdapter != null)
        {
            return bluetoothAdapter.isEnabled();
        }

        return false;
    }

    /**
     * 强制开启当前 Android 设备的 Bluetooth
     *
     * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
     */
    public static boolean turnOnBluetooth()
    {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();

        if (bluetoothAdapter != null)
        {
            Log.i("ACBuletoothUtil","蓝牙正在打开..");
            return bluetoothAdapter.enable();
        }

        return false;
    }
}

