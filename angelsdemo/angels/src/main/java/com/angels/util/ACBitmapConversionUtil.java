package com.angels.util;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Base64;
 
public class ACBitmapConversionUtil {
	
	public static final String ENCODE = "utf-8";
	
	/**
	 * 将byte[]数组转换为图片Bitmap
	 */
	public static Bitmap bytesToBitmap(byte[] b) {

		if (b != null && b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 将图片Bitmap转换成byte[]数组
	 */
	public static byte[] bitmapToBytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, baos);
		return baos.toByteArray();

	}

	/**
	 * 将byte[]数组转换为string
	 */
	public static String bytesToString(byte[] b) {
		String str = "";
		try {
			str = new String(b, ENCODE);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 将string转换为byte[]数组
	 */
	public static byte[] StringTobytes(String str) {
		byte[] bytes = null;
		try {
			bytes = str.getBytes(ENCODE);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap(

		drawable.getIntrinsicWidth(),

		drawable.getIntrinsicHeight(),

		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

		: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		// canvas.setBitmap(bitmap);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());

		drawable.draw(canvas);

		return bitmap;

	}
	/**  
     * 图片转成string  
     *   
     * @param bitmap  
     * @return  
     */  
    public static String convertIconToString(Bitmap bitmap)  
    {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream  
        bitmap.compress(CompressFormat.PNG, 100, baos);  
        byte[] appicon = baos.toByteArray();// 转为byte数组  
        return Base64.encodeToString(appicon, Base64.DEFAULT);  
  
    } 
    /**  
     * string转成bitmap  
     *   
     * @param st  
     */  
    public static Bitmap convertStringToIcon(String st)  
    {  
        // OutputStream out;  
        Bitmap bitmap = null;  
        try  
        {  
            // out = new FileOutputStream("/sdcard/aa.jpg");  
            byte[] bitmapArray;  
            bitmapArray = Base64.decode(st, Base64.DEFAULT);  
            bitmap =  
                    BitmapFactory.decodeByteArray(bitmapArray, 0,  
                            bitmapArray.length);  
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);  
            return bitmap;  
        }  
        catch (Exception e)  
        {  
            return null;  
        }  
    }  
}
