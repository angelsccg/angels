package com.angels.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @ClassName: ACMath
 * @Description: 数学算法
 * @author angelsC
 * @date 2016-3-25 下午4:15:44
 *
 *
 *       * 功能：系统
 *
 *       1,判断是否是数字
 *       2,判断是否为整数
 *       3，判断是否为浮点数，包括double和float
 *       4，是否为空白,包括null和""
 *       5，判断是否是指定长度的字符串
 */
public class ACMath {
    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str)
    {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches())
        {
            return false;
        }
        return true;
    }

    /**
     * 判断是否为整数
     *
     * @param str
     *            传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str)
    {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为浮点数，包括double和float
     *
     * @param str
     *            传入的字符串
     * @return 是浮点数返回true,否则返回false
     */
    public static boolean isDouble(String str)
    {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 是否为空白,包括null和""
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str)
    {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断是否是指定长度的字符串
     *
     * @param text
     *            字符串
     * @param lenght
     *            自定的长度
     * @return
     */
    public static boolean isLenghtStrLentht(String text, int lenght)
    {
        if (text.length() <= lenght)
            return true;
        else
            return false;
    }
}
