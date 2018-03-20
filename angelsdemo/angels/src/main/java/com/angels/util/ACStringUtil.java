package com.angels.util;

import android.text.TextUtils;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.value;

/**
 * 项目名称：bills
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/9/29 10:46
 */
public class ACStringUtil {
    /**
     * double 转换成 String 避免了数字变成科学记数法
     * @param d
     * @return
     */
    public static String doubleToString(double d)
    {
        String i = DecimalFormat.getInstance().format(d);
        String result = i.replaceAll(",", "");
        return result;

    }

    /**
     * 判断一个字符串是否是整型数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if(str == null || TextUtils.isEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**获取url参数*/
    public static Map<String, Object> urlSplit(String data){
        StringBuffer strbuf = new StringBuffer();
        StringBuffer strbuf2 = new StringBuffer();
        Map<String ,Object> map = new HashMap<String,Object>();
        for(int i =0;i<data.length();i++){
            if(data.substring(i,i+1).equals("=")){
                for(int n=i+1;n<data.length();n++){
                    if(data.substring(n,n+1).equals("&")|| n ==data.length()-1){
                        map.put(strbuf.toString(), strbuf2);
                        strbuf =new StringBuffer("");
                        strbuf2 =new StringBuffer("");
                        i=n;
                        break;
                    }
                    strbuf2.append(data.substring(n,n+1));
                }
                continue;
            }
            strbuf.append(data.substring(i,i+1));
        }
        return map;
    }

    /**
     * 替换url中某参数的值
     * @return
     */
    public static String replaceUrlData(String url, String key, String value) {
        if (TextUtils.isEmpty(url) && TextUtils.isEmpty(value)) {
            int index = url.indexOf(key + "=");
            if (index != -1) {
                StringBuilder sb = new StringBuilder();
                sb.append(url.substring(0, index)).append(key + "=").append(value);
                int idx = url.indexOf("&", index);
                if (idx != -1) {
                    sb.append(url.substring(idx));
                }
                url = sb.toString();
            }
        }
        return url;
    }

    public static byte[] strToByteArray(String str) {
        if (str == null) {
            return null;
        }
        byte[] byteArray = str.getBytes();
        return byteArray;
    }
    public static String byteArrayToStr(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0) {
            return null;
        }
        for (int i = 0; i < byteArray.length; i++) {
            int v = byteArray[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv + ",");
        }
        return stringBuilder.toString();
    }
}
