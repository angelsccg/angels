package com.angels.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/8.
 */
public class ACRecordMap extends HashMap<String, Object> implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -2971982788354456189L;

    public ACRecordMap() {}

    /**
     * 组合参数转map
     * @param params e.g. "a=A&b=B"
     * @param pairSeparator separator to split pair e.g. "&"
     * @param kvSeparator  separator to split key&value e.g. "="
     */
    public ACRecordMap(String params, String pairSeparator, String kvSeparator) {
        String[] arr = params.split(pairSeparator);
        for (int i=0; i<arr.length; i++) {
            String[] arr2 = arr[i].split(kvSeparator);
            if (arr2.length == 2) {
                this.put(arr2[0], arr2[1]);
            }
        }
    }

}
