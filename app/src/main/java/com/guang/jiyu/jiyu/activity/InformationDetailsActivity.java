package com.guang.jiyu.jiyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;

import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.event.FastInformationEvent;
import com.guang.jiyu.jiyu.model.InformationDetailModel;
import com.guang.jiyu.jiyu.model.InformationModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.TitleBarUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.NoScrollWebView;
import com.guang.jiyu.jiyu.widget.SharePopupWindow;
import com.guang.jiyu.jiyu.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/7/13.
 */

public class InformationDetailsActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.webview)
    WebView webview;
/*    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.tv_times)
    TextView tvTimes;
    @BindView(R.id.iv_reading_quentity)
    ImageView ivReadingQuentity;
    @BindView(R.id.tv_reading_count)
    TextView tvReadingCount;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.tv_collect_count)
    TextView tvCollectCount;
    @BindView(R.id.iv_bite)
    ImageView ivBite;
    @BindView(R.id.btn_bite)
    SuperButton btnBite;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;*/

    private InformationModel informationModel;
    private InformationDetailModel informationDetailModel;
    private SharePopupWindow sharePopupWindow;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message != null) {
                switch (message.what) {
                    case Contants.INFODetails_GET_SUCCESS:
                        informationDetailModel = (InformationDetailModel) message.obj;
                        initView(informationDetailModel);
                        break;
                    case Contants.INFODetails_GET_FAILURE:
                        ToastUtils.showToast("数据异常");
                        break;
                    case Contants.INFOConnect_ALREADY_EXICT:
                        ToastUtils.showToast("该资讯已被收藏");
                        break;
                    case Contants.INFOConnect_SUCCESS:
                        ToastUtils.showToast("收藏成功");
                        break;
                }
            }

            return false;
        }
    });

    private void initView(InformationDetailModel informationDetailModel) {
/*        tvTitle.setText(informationDetailModel.getTitle());
        tvSource.setText(informationDetailModel.getInfoSource());
        tvTimes.setText(informationDetailModel.getUpdatedTime());
        tvContent.setText(informationDetailModel.getContent());
        tvReadingCount.setText(informationDetailModel.getClick());
        tvCollectCount.setText(informationDetailModel.getCollection());*/
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_information_details);
        initTitle();
        informationModel = (InformationModel) getIntent().getSerializableExtra("model");
        Log.i("-----", informationModel.toString());
        webview.loadUrl("http://39.104.98.85:8033/#/news?data=" + informationModel.infoId);
        //webview.loadUrl("http://www.baidu.com");
        //webview.loadUrl("https://juejin.im/post/5b50898ef265da0fa21a7f0f");
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }
        });
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //queryInfoDetails();
    }

    public String jsoup(String str) {
        Document doc = (Document) Jsoup.parse(str);
        Elements Img = doc.getElementsByTag("img");  //从一段富文本信息中找到所有图片
        Elements p = doc.getElementsByTag("p"); //从一段富文本信息中找到所有文字
        if (Img.size() != 0) {
            for (Element e_Img : Img) {
                e_Img.attr("width", "100%");//设置图片的宽为100%，高度自适应
                e_Img.attr("height", "auto");
            }
        }
        if (p.size() != 0) {
            for (Element e_p : p) {
                Log.e("xiaoma", e_p.toString());
                e_p.attr("style", "font-size:40px"); //设置字体的大小
            }
        }
        return doc.toString();
    }

    /**
     * 获取文章详情
     */
    private void queryInfoDetails() {
        JSONObject object = new JSONObject();
        try {
            object.put("infoId", informationModel.getInfoId());
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    .url(LinkParams.REQUEST_URL + "/info/queryInfoDetails")
                    .post(requestBody)
                    .build();

            OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("onFailure-----", e.toString());
                    handler.sendEmptyMessage(Contants.INFODetails_GET_FAILURE);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            JSONObject entity = new JSONObject(object.getString("data"));
                            InformationDetailModel model = new InformationDetailModel();
                            model.infoId = entity.getString("infoId");
                            model.title = entity.getString("title");
                            model.subtitle = entity.getString("subtitle");
                            model.content = entity.getString("content");
                            model.author = entity.getString("author");
                            model.infoSource = entity.getString("infoSource");
                            model.click = entity.getString("click");
                            model.updatedTime = entity.getString("updatedTime");
                            model.collection = entity.getString("collection");
                            Message m = new Message();
                            m.what = Contants.INFODetails_GET_SUCCESS;
                            m.obj = model;
                            handler.sendMessage(m);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initTitle() {
        TitleBarUtils.setTitleBarWithImg(this, titlebar, "资讯", R.mipmap.icon_return_with_txt);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titlebar.addAction(new TitleBar.ImageAction(R.mipmap.icon_fast_information_share) {
            @Override
            public void performAction(View view) {
                EventBus.getDefault().post(new FastInformationEvent(informationModel));
            }
        });
    }


    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {

        if (baseEvent instanceof FastInformationEvent) {
            FastInformationEvent fastInformationEvent = (FastInformationEvent) baseEvent;
            InformationModel model = fastInformationEvent.getFastInformationModel();
            initPopWindow();
        }
    }

    private void initPopWindow() {
        sharePopupWindow = new SharePopupWindow(this);
        sharePopupWindow.setOnItemClickListener(new SharePopupWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_weibo:
                        ToastUtils.showToast("微博");
                        break;
                    case R.id.rl_wechat:
                        ToastUtils.showToast("微信");
                        break;
                    case R.id.rl_friends_circle:
                        ToastUtils.showToast("朋友圈");
                        break;
                    case R.id.rl_qq:
                        ToastUtils.showToast("QQ");
                        break;
                }
            }
        });
        sharePopupWindow.showAtLocation(InformationDetailsActivity.this.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
        setWindowAttr(0.6f);

        sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAttr(1f);
            }
        });
    }

/*    @OnClick(R.id.iv_collect)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_collect:
                addCollection();
                break;
        }

    }*/

    /**
     * 收藏资讯
     */
    private void addCollection() {
        if (!UserInfoUtils.isUserLogin(this)) {
            ToastUtils.showToast("请先登录");
            ActivityUtils.startActivity(this, LoginActivity.class);
            return;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("infoId", informationModel.getInfoId());
            object.put("userId", UserInfoUtils.getInt(this, Contants.USER_ID));
            Log.d("infoId-----userId", informationModel.getInfoId() + "----" + UserInfoUtils.getInt(this, Contants.USER_ID));
            RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
            final Request request = new Request.Builder()
                    .url(LinkParams.REQUEST_URL + "/info/addCollection")
                    .post(requestBody)
                    .build();

            OkHttpManage.getClient(this).newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(Contants.INFODetails_GET_FAILURE);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.d("addCollection-----", result);
                    try {
                        JSONObject object = new JSONObject(result);
                        if ("200".equals(object.getString("code"))) {
                            handler.sendEmptyMessage(Contants.INFOConnect_SUCCESS);
                        }

                        if ("500".equals(object.getString("code"))) {
                            handler.sendEmptyMessage(Contants.INFOConnect_ALREADY_EXICT);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
