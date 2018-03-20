
package com.angels.exception;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 异常类封装
 *
 */
public class ACException extends Exception {
    private static final long serialVersionUID = -8618791505361792493L;
    
    public static final String JSON_ERROR_CODE = "errorCode";
    public static final String JSON_ERROR_MESSAGE = "errorMessge";
    public static final String JSON_DATA = "data";
    
    public final static String TIMEOUT_ERROR = "网络请求超时,请重试";
    public final static String UNKNOW_ERROR = "网络请求异常,请重试";
    
    /**
     * 错误码
     */
    private int mErrorCode = 0;
    
    public ACException(Exception cause) {
        super(cause instanceof JSONException ? "数据解析错误" : cause.getMessage());
        if (cause instanceof ACException) {
            mErrorCode = ((ACException)cause).getErrorCode();
        }
    }

    public ACException(String message) {
        super(message);
    }

    public ACException(JSONObject json) {
        super(json.optString("errorMessge"));
        mErrorCode = json.optInt("errorCode");
    }

    public ACException(int code, String message) {
        super(message);
        mErrorCode = code;
    }

    /**
     * 返回异常代码
     * 
     * @return
     */
    public int getErrorCode() {
        return mErrorCode;
    }

    /**
     * 返回异常的简明信息
     * 
     * @return
     */
    public String getSimpleMsg() {
        return getMessage();
    }
}
