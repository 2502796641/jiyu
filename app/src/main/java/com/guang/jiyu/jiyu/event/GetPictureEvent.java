package com.guang.jiyu.jiyu.event;

import android.net.Uri;
import android.widget.ImageView;

import com.guang.jiyu.base.BaseEvent;

/**
 * Created by admin on 2018/8/24.
 */

public class GetPictureEvent extends BaseEvent{
    private Uri uri;
    private String tag;
    private ImageView iv;

    public GetPictureEvent(Uri uri) {
        this.uri = uri;
    }

    public GetPictureEvent(String tag) {
        this.tag = tag;
    }

    public GetPictureEvent(String tag, ImageView iv) {
        this.tag = tag;
        this.iv = iv;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public ImageView getIv() {
        return iv;
    }

    public void setIv(ImageView iv) {
        this.iv = iv;
    }
}
