package com.guang.jiyu.jiyu.utils;

import android.content.Context;

import com.guang.jiyu.jiyu.application.JIYUApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字工具类
 * Created by admin on 2018/6/25.
 */

public class MyTextUtils {

    public static Context context = JIYUApp.getInstance();

    public static String getTxtString(String fileUri) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(fileUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 检查是否是电话号码
     *
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * md5加密密码
     * @param password
     * @return
     */
    public static String md5Password(String password){
        StringBuffer sb = new StringBuffer();
        // 得到一个信息摘要器
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            // 把每一个byte做一个与运算 0xff
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 根据数字返回星期几
     */

    public static String dateForWeek(String num){
        switch (num){
            case "1":
                return "星期一";
                case "2":
                return "星期二";
                case "3":
                return "星期三";
                case "4":
                return "星期四";
                case "5":
                return "星期五";
                case "6":
                return "星期六";
                case "7":
                return "星期七";
        }
        return num;
    }

}
