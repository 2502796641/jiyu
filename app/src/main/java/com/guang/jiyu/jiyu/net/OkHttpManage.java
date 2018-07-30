package com.guang.jiyu.jiyu.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.application.JIYUApp;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by admin on 2018/7/10.
 */

public class OkHttpManage {
    private static OkHttpManage mInstance;
    private OkHttpClient mClient;
    private Handler mHnadler;
    private Gson mGson;

    /**
     * 单例
     *
     * @return
     */
    public static synchronized OkHttpManage getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpManage();
        }
        return mInstance;
    }

    public static OkHttpClient getClientWithCache(Context context){
        File sdcache = context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        //设置超时时间及缓存，下边都应该这样设置的。
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));

        return builder.build();
    }

    public static OkHttpClient getClient(final Context context){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30000, TimeUnit.SECONDS)
                .writeTimeout(30000, TimeUnit.SECONDS)
                .readTimeout(30000, TimeUnit.SECONDS)
                /*.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                        .newBuilder()
                        .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(context,Contants.AUTHORIZATION))
                        .build();
                         return chain.proceed(request);                     }
                })*/;


        return builder.build();
    }


    /**
     * 构造函数
     */
    private OkHttpManage() {
        initOkHttp();
        mHnadler = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    /**
     * 初始化OkHttpClient
     */
    private void initOkHttp() {
        mClient = new OkHttpClient().newBuilder()
                .readTimeout(30000, TimeUnit.SECONDS)
                .connectTimeout(30000, TimeUnit.SECONDS)
                .writeTimeout(30000, TimeUnit.SECONDS)
                .build();
    }



    /**
     * 请求
     *
     * @param client
     * @param callBack
     */
    public void request(BaseOkHttpClient client, final BaseCallBack callBack) {
        if (callBack == null) {
            throw new NullPointerException(" callback is null");
        }
        mClient.newCall(client.buildRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendonFailureMessage(callBack, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    if (callBack.mType == null || callBack.mType == String.class) {
                        sendonSuccessMessage(callBack, result);
                    } else {
                        sendonSuccessMessage(callBack, mGson.fromJson(result, callBack.mType));
                    }
                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    sendonErrorMessage(callBack, response.code());
                }
            }
        });
    }

    /**
     * 成功信息
     *
     * @param callBack
     * @param result
     */
    private void sendonSuccessMessage(final BaseCallBack callBack, final Object result) {
        mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(result);
            }
        });
    }

    /**
     * 失败信息
     *
     * @param callBack
     * @param call
     * @param e
     */
    private void sendonFailureMessage(final BaseCallBack callBack, final Call call, final IOException e) {
        mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onFailure(call, e);
            }
        });
    }

    /**
     * 错误信息
     *
     * @param callBack
     * @param code
     */
    private void sendonErrorMessage(final BaseCallBack callBack, final int code) {
        mHnadler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(code);
            }
        });
    }
}
