package com.guang.jiyu.jiyu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.widget.TitleBar;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;

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
                ToastUtils.showToast("完成");
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
                ToastUtils.showToast("2");
                break;
            case R.id.iv_idcard_handon:
                chooseTag = 3;
                ToastUtils.showToast("3");
                break;
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            //selectedPhotos.clear();

            if (photos != null) {
                //selectedPhotos.addAll(photos);
            }
            Uri uri = Uri.fromFile(new File(photos.get(0)));
            if (canLoadImage) {
                final RequestOptions options = new RequestOptions();
                options.centerCrop()
                        .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                        .error(R.drawable.__picker_ic_broken_image_black_48dp);
                if(chooseTag == 1){
                    Glide.with(this)
                            .load(uri)
                            .apply(options)
                            .thumbnail(0.1f)
                            .into(ivIdcardFront);
                }
                if(chooseTag == 2){
                    Glide.with(this)
                            .load(uri)
                            .apply(options)
                            .thumbnail(0.1f)
                            .into(ivIdcardBack);
                }
                if(chooseTag == 3){
                    Glide.with(this)
                            .load(uri)
                            .apply(options)
                            .thumbnail(0.1f)
                            .into(ivIdcardHandon);
                }

            }


            //photoAdapter.notifyDataSetChanged();
        }
    }
}
