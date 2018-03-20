package com.example.angelsdemo.utils;

/**
 * Created by chencg on 2017/12/14.
 */

public class WristbanDorderCode {
    /**
    *
    * *=========================bong手环UUID=========================
     *
     * */
    /**服务的UUID*/
    public static String serviceUUID = "6e400001-b5a3-f393-e0a9-e50e24dcca1e";
    /**接收数据的特征UUID*/
    public static String receiveCharacteristicUUID = "6e400003-b5a3-f393-e0a9-e50e24dcca1e";
    /**接收数据的特征的BluetoothGattDescriptorUUID  （notify数据上报）*/
    public static String receiveDbUUID = "00002902-0000-1000-8000-00805f9b34fb";
    /**可写入数据的特征UUID*/
    public static String sendCharacteristicUUID = "6e400002-b5a3-f393-e0a9-e50e24dcca1e";

    /***/

    /**
     * ==========================bong手环指令==========================
     */
    /**震动,0xff,0xff,0xff,0x20,0x01**/
    public static byte[] shake = {0x26, (byte) 0xff, (byte) 0xff, (byte) 0xff,0x20,0x01};
    /**读取固件版本号,0x25 0x00 0x00 0x00 0x04**/
    public static byte[] version = {0x25, 0x00, 0x00, 0x00, 0x04};
    /**读取电量**/
    public static byte[] electricQuantity = {0x26 ,0x00,0x00,0x00,0x10};
    /**读取心率**/
    public static byte[] heartRate = {0x26 ,0x00,0x00,0x00,0x52};
    /**开始绑定动画**/
    public static byte[] startAni = {0x29, 0x00,0x00,0x00,0x17};
    /**结束绑定动画 环显示绑定成功图案**/
    public static byte[] endAni = {0x29, 0x00,0x00,0x00,0x18};
    /**步进值数据同步**/
    public static byte[] stepSync = {0x29, 0x00,0x00,0x00,0x19,0x00,0x1B,0x30, (byte) 0xF0,0x00,0x00, (byte) 0xEC, (byte) 0xE1,0x00,0x00,0x08, (byte) 0xF5,0x00,0x00};



    /**
     *
     * *=========================小米手环2 UUID=========================
     *
     * */
//    /**服务的UUID*/
//    public static String serviceUUID = "6e400001-b5a3-f393-e0a9-e50e24dcca1e";
//    /**接收数据的特征UUID*/
//    public static String receiveCharacteristicUUID = "6e400003-b5a3-f393-e0a9-e50e24dcca1e";
//    /**接收数据的特征的BluetoothGattDescriptorUUID  （notify数据上报）*/
//    public static String receiveDbUUID = "00002902-0000-1000-8000-00805f9b34fb";
//    /**可写入数据的特征UUID*/
//    public static String sendCharacteristicUUID = "6e400002-b5a3-f393-e0a9-e50e24dcca1e";
    /**
     * ==========================小米手环2 指令==========================
     */
    /**查找手环（震动一下）**/
    public static byte[] shake_one = {0x03};
    /**查找手环（震动一下）**/
    public static byte[] shake_one2 = {0x05,0x01};
    /****/
    public static byte[] shake_one3 = {0x01,0x00};
}
