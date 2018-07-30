package com.guang.jiyu.jiyu.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guang.jiyu.base.Contants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/7/16.
 */

public class UserInfoUtils {
    private static final String PREFERENCE_NAME = "jiyu";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context,String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    public static void saveBoolean(Context context,int keyResId, Boolean value) {
        String key = context.getString(keyResId);
        saveBoolean(context,key, value);
    }


    public static void saveBoolean(Context context,String key, Boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public static Boolean getBoolean(Context context,String key) {
        return getSharedPreferences(context).getBoolean(key, false);
    }


    public static Boolean getBoolean(Context context,String key, boolean def) {
        return getSharedPreferences(context).getBoolean(key, def);
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public static <T> void setDataList(Context context,String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        //Gson gson = new Gson();
        //转换成json数据，再保存
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        //String strJson = gson.toJson(datalist);
        editor.clear();
        try {
            editor.putString(tag, SceneList2String(datalist));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static <T> List<T> getDataList(Context context,String tag) {
        List<T> datalist=new ArrayList<T>();
        String strJson = getSharedPreferences(context).getString(tag, null);
        /*if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();*/
        try {
            datalist = (List<T>) String2SceneList(strJson);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return datalist;

    }

    //将list集合转换成字符串

    public static String SceneList2String(List SceneList) throws IOException {
// 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
// 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
// writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
// 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
// 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }



//将字符串转换成list集合

    @SuppressWarnings("unchecked")
    public static List<String> String2SceneList(String SceneListString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        List SceneList = new ArrayList();
        if(SceneListString != null){
            byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                    Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    mobileBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            SceneList = (List) objectInputStream.readObject();
            objectInputStream.close();
        }
        return SceneList;
    }

    public static Boolean getBoolean(Context context,int keyResId, boolean def) {
        String key = context.getString(keyResId);
        return getSharedPreferences(context).getBoolean(key, def);
    }


    public static int getInt(Context context,String key) {
        return getSharedPreferences(context).getInt(key, 0);
    }


    public static void saveInt(Context context,String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }
    public static boolean isUserLogin(Context context){
        if(UserInfoUtils.getInt(context, Contants.USER_ID) != 0){
            return true;
        }
        return false;
    }

    public static void remove(Context context,String key){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }
}
