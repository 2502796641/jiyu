package com.guang.jiyu.jiyu.utils;

import android.content.Context;
import android.util.Log;

import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.application.JIYUApp;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.qiniu.android.common.AutoZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by admin on 2018/8/31.
 */

public class UploadImgUtils {
    private Context context;
    public UploadImgUtils(Context context) {
        this.context = context;
    }

    private  Configuration config = new Configuration.Builder()
            .zone(AutoZone.autoZone)
            .build();

    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    private  UploadManager uploadManager = new UploadManager(config);
    private   String imgUrl = "";
    public void uploadImg(File file){
        uploadManager.put(file, file.getName(), UserInfoUtils.getString(context, Contants.QINIU_TOKEN), new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    LogUtils.i("qiniu", "Upload Success");
                } else {
                    LogUtils.i("qiniu", "Upload Fail");
                }
                try {
                    imgUrl = Contants.QINIU_UPLOAD_URL + response.getString("key");
                    EventBus.getDefault().post(new MessageEvent(imgUrl));
                    LogUtils.i("qiniu", "imgUrl----" + imgUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

}
