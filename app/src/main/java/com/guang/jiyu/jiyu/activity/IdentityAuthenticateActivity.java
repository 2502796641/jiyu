package com.guang.jiyu.jiyu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.event.GetPictureEvent;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UploadImgUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 身份认证activity
 * Created by admin on 2018/6/21.
 */

public class IdentityAuthenticateActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_idcard_code)
    EditText etIdcardCode;
    @BindView(R.id.iv_idcard_front)
    ImageView ivIdcardFront;
    @BindView(R.id.iv_idcard_back)
    ImageView ivIdcardBack;
    @BindView(R.id.iv_idcard_handon)
    ImageView ivIdcardHandon;


    List<String> photos = null;
    boolean canLoadImage;
    int chooseTag = 0;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_identity_authenticate);
        initTitle();
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "身份认证", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebar.addAction(new TitleBar.TextAction("完成") {
            @Override
            public void performAction(View view) {
                IdentityAuthenticateCommit();
                ToastUtils.showToast("完成");
            }
        });
    }

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {
        if (baseEvent instanceof MessageEvent) {
            MessageEvent event = (MessageEvent) baseEvent;
            imgUrl = event.getMessage();
            if(chooseTag == 1){
                Picasso.get().load(imgFile).into(ivIdcardFront);
                FrontUrl = imgUrl;

            }
            if(chooseTag == 2){
                Picasso.get().load(imgFile).into(ivIdcardBack);
                BackUrl = imgUrl;

            }
            if(chooseTag == 3){
                Picasso.get().load(imgFile).into(ivIdcardHandon);
                HandonUrl = imgUrl;
            }
        }
    }


    private void IdentityAuthenticateCommit() {
        userName = etName.getText().toString();
        idNumber = etIdcardCode.getText().toString();
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(idNumber)||TextUtils.isEmpty(FrontUrl)||TextUtils.isEmpty(BackUrl)||TextUtils.isEmpty(HandonUrl)){
            ToastUtils.showToast("请完善所需资料");
            return;
        }
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("userId", UserInfoUtils.getInt(this,Contants.USER_ID) + "");
        mbody.addFormDataPart("userName", userName);
        mbody.addFormDataPart("idNumber", idNumber);
        mbody.addFormDataPart("idcardFrontPhoto", FrontUrl);
        mbody.addFormDataPart("idcardBackPhoto", BackUrl);
        mbody.addFormDataPart("selfPhoto", HandonUrl);
        RequestBody requestBody = mbody.build();
        LogUtils.d("/idcard/cert-----", requestBody.toString());
        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/idcard/cert")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(this, Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("register-----", e.toString());
                mHandler.sendEmptyMessage(Contants.WALLET_GET_FAILURE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("/idcard/cert-----", result);
                try {
                    JSONObject object = new JSONObject(result);
                    ToastUtils.showToast(object.getString("message"));
                    if ("200".equals(object.getString("code"))) {


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @OnClick({R.id.iv_idcard_front, R.id.iv_idcard_back, R.id.iv_idcard_handon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_idcard_front:
                chooseTag = 1;
                canLoadImage = AndroidLifecycleUtils.canLoadImage(ivIdcardFront.getContext());
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .start(IdentityAuthenticateActivity.this);
                break;
            case R.id.iv_idcard_back:
                chooseTag = 2;
                canLoadImage = AndroidLifecycleUtils.canLoadImage(ivIdcardFront.getContext());
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .start(IdentityAuthenticateActivity.this);
                break;
            case R.id.iv_idcard_handon:
                chooseTag = 3;
                canLoadImage = AndroidLifecycleUtils.canLoadImage(ivIdcardFront.getContext());
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .start(IdentityAuthenticateActivity.this);
                break;
        }
    }
    private String FrontUrl,BackUrl,HandonUrl,userName,idNumber,imgUrl;
    private File imgFile;
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            //selectedPhotos.clear();

            imgFile = new File(photos.get(0));
            UploadImgUtils uploadImgUtils = new UploadImgUtils(this);
            uploadImgUtils.uploadImg(new File(photos.get(0)));
            LogUtils.d("imgUrl-----", "----" + imgUrl);
            if (canLoadImage) {
                final RequestOptions options = new RequestOptions();
                options.centerCrop()
                        .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                        .error(R.drawable.__picker_ic_broken_image_black_48dp);
/*                if(chooseTag == 1){
                    Picasso.get().load(imgFile).into(ivIdcardFront);
                    FrontUrl = imgUrl;

                }
                if(chooseTag == 2){
                    Picasso.get().load(imgFile).into(ivIdcardBack);
                    BackUrl = imgUrl;

                }
                if(chooseTag == 3){
                    Picasso.get().load(imgFile).into(ivIdcardHandon);
                    HandonUrl = imgUrl;
                }*/

            }


            //photoAdapter.notifyDataSetChanged();
        }
    }
}
