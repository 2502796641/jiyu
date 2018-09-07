package com.guang.jiyu.jiyu.fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.bumptech.glide.request.RequestOptions;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.activity.LoginActivity;
import com.guang.jiyu.jiyu.activity.MessageRecordActivity;
import com.guang.jiyu.jiyu.activity.ProjectIssueRecordActivity;
import com.guang.jiyu.jiyu.adapter.CurrencyAdapter;
import com.guang.jiyu.jiyu.adapter.IconAdapter;
import com.guang.jiyu.jiyu.event.GetPictureEvent;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.model.CurrencyModel;
import com.guang.jiyu.jiyu.net.OkHttpManage;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.LinkParams;
import com.guang.jiyu.jiyu.utils.LogUtils;
import com.guang.jiyu.jiyu.utils.MyTextUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.AlertDialog;
import com.guang.jiyu.jiyu.widget.MyGridView;
import com.guang.jiyu.jiyu.widget.TitleBar;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.qiniu.android.common.AutoZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2018/8/23.
 */

public class NewProjectFragment extends BaseFragment implements OnDateSetListener/*,View.OnTouchListener*/ {
    @BindView(R.id.grid)
    MyGridView grid;
    Unbinder unbinder;
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.rb_currency_mbtc)
    RadioButton rbCurrencyMbtc;
    @BindView(R.id.rb_currency_gxs)
    RadioButton rbCurrencyGxs;
    @BindView(R.id.rb_currency_dmc)
    RadioButton rbCurrencyDmc;
    @BindView(R.id.rb_currency_cero)
    RadioButton rbCurrencyCero;
    @BindView(R.id.rb_target_register)
    RadioButton rbTargetRegister;
    @BindView(R.id.rb_target_idauthentication)
    RadioButton rbTargetIdauthentication;
    @BindView(R.id.rb_target_token)
    RadioButton rbTargetToken;
    @BindView(R.id.rb_restrict_none)
    RadioButton rbRestrictNone;
    @BindView(R.id.rb_restrict_need_boardcast)
    RadioButton rbRestrictNeedBoardcast;
    @BindView(R.id.et_issue_currency)
    EditText etIssueCurrency;
    @BindView(R.id.et_boardcast_start_date)
    EditText etBoardcastStartDate;
    @BindView(R.id.et_cantake_count)
    EditText etCantakeCount;
    @BindView(R.id.et_cantake_people_count)
    EditText etCantakePeopleCount;
    @BindView(R.id.et_issue_sum)
    EditText etIssueSum;
    @BindView(R.id.tv_all_send)
    TextView tvAllSend;
    @BindView(R.id.tv_sevice_charge)
    TextView tvSeviceCharge;
    @BindView(R.id.tv_actual_pay)
    TextView tvActualPay;
    @BindView(R.id.btn_pay)
    SuperButton btnPay;
    @BindView(R.id.iv_upload)
    ImageView ivUpload;
    @BindView(R.id.iv_add_picture)
    ImageView ivAddPicture;
    @BindView(R.id.tv_boardcast_start_time)
    TextView tvBoardcastStartTime;
    @BindView(R.id.iv_upload2)
    ImageView ivUpload2;
    @BindView(R.id.iv_upload3)
    ImageView ivUpload3;
    /*    @BindView(R.id.scroll_chrid)
        ScrollView scrollChrid;*/
    @BindView(R.id.et_issue_currency_content)
    EditText etIssueCurrencyContent;
    @BindView(R.id.rg_currency)
    RadioGroup rgCurrency;
    @BindView(R.id.rg_target)
    RadioGroup rgTarget;
    @BindView(R.id.rg_restrict)
    RadioGroup rgRestrict;
    @BindView(R.id.scroll_parent)
    ScrollView scrollParent;
    @BindView(R.id.rl_pay)
    RelativeLayout rlPay;
    @BindView(R.id.scroll_img)
    ScrollView scrollImg;
    @BindView(R.id.grid_currency)
    MyGridView gridCurrency;
    private View rootView;
    private int[] check_icon = new int[]{R.mipmap.b1_01, R.mipmap.b1_02, R.mipmap.b1_03, R.mipmap.b1_04,
            R.mipmap.b1_05, R.mipmap.b1_06, R.mipmap.b1_07, R.mipmap.b1_08};
    private int[] nor_icon = new int[8];
    private int[] origin_icon = new int[]{R.mipmap.b2_01, R.mipmap.b2_02, R.mipmap.b2_03, R.mipmap.b2_04,
            R.mipmap.b2_05, R.mipmap.b2_06, R.mipmap.b2_07, R.mipmap.b2_08};
    private IconAdapter adapter;
    private CurrencyAdapter currencyAdapter;
    boolean canLoadImage;
    private TimePickerDialog timePickerDialog;
    private String select_currency, select_target, select_restrict;
    private final String field_currency = "currency";
    private final String field_target = "target";
    private final String field_restrict = "restrict";
    private List<CurrencyModel> currencyList;
    private String currencyValue = "";
    private String serviceCharge = "";
    private String serviceChargePay = "";
    private String actuallPay = "";
    private boolean isCurrencyChoose, isTargetChoose, isRestrictChoose, isIconChoose;
    int selectorPosition;
    private CurrencyModel jycjModel;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message != null){
                switch (message.what){
                    case Contants.GET_DATA_SUCCESS:
                        currencyList = (List<CurrencyModel>) message.obj;
                        initCurrGrid();
                        break;
                }
            }
            return false;
        }
    });

    private void initCurrGrid() {
        currencyAdapter = new CurrencyAdapter(getContext(),currencyList);
        gridCurrency.setAdapter(currencyAdapter);
        gridCurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinearLayout linearLayout=(LinearLayout) gridCurrency.getAdapter().getView(i,view,null);
                TextView textView=(TextView)linearLayout.getChildAt(0);
                currency = textView.getText().toString();
                currencyValue = currencyList.get(i).getCurrencyValue();
                serviceCharge = currencyList.get(i).getServiceCharge();
                isCurrencyChoose = true;
                etCantakeCount.setText("");
                etCantakePeopleCount.setText("");
                etCantakeCount.setFocusable(true);
                etCantakeCount.setFocusableInTouchMode(true);
                etCantakePeopleCount.setFocusable(true);
                etCantakePeopleCount.setFocusableInTouchMode(true);
                currencyAdapter.changeState(i);
//                selectorPosition = i;
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_new_project;
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        scrollParent.smoothScrollTo(0,20);
        initTitle();
        initGridView();
        queryCurrency();
        getQiniukey();
        setTouchListener();
        initCheckedChangeListener();
        initTextChangeListener();
        return rootView;
    }

    private void initTextChangeListener() {
        etCantakeCount.addTextChangedListener(new EditChangedListener(etCantakeCount));
        etCantakePeopleCount.addTextChangedListener(new EditChangedListener(etCantakePeopleCount));
    }

    /**
     * 获取七牛上传图片的key
     */
    private void getQiniukey() {
        JSONObject object = new JSONObject();
        RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/qiniu/key")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(getContext(), Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(getContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("onFailure-----", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("qiniu/key", "----" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    String token = object.getString("data");
                    UserInfoUtils.saveString(getContext(), Contants.QINIU_TOKEN, token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //发送对象:0->注册用户；1->身份认证；2->代币
    //发送限制:0->无限制;1:需要阅读广播
    private void initCheckedChangeListener() {
        etCantakeCount.setFocusable(false);
        etCantakeCount.setFocusableInTouchMode(false);
        etCantakePeopleCount.setFocusable(false);
        etCantakePeopleCount.setFocusableInTouchMode(false);
        etIssueSum.setFocusable(false);
        etCantakePeopleCount.setFocusableInTouchMode(false);

        rgTarget.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isTargetChoose = true;
                if (rbTargetRegister.isChecked()) {
                    sendObject = "0";
                } else if (rbTargetIdauthentication.isChecked()) {
                    sendObject = "1";
                } else if (rbTargetToken.isChecked()) {
                    sendObject = "2";
                }
                //selectRadioBtn(field_target,rgTarget);
            }
        });
/*        rgCurrency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isCurrencyChoose = true;
                etCantakeCount.setText("");
                etCantakePeopleCount.setText("");
                etCantakeCount.setFocusable(true);
                etCantakeCount.setFocusableInTouchMode(true);
                etCantakePeopleCount.setFocusable(true);
                etCantakePeopleCount.setFocusableInTouchMode(true);
                RadioButton rb = (RadioButton) rootView.findViewById(rgCurrency.getCheckedRadioButtonId());
                for (int i = 0; i < currencyList.size(); i++) {
                    if (rb.getText().toString().equals(currencyList.get(i).getCurrency())) {
                        currencyValue = currencyList.get(i).getCurrencyValue();
                        serviceCharge = currencyList.get(i).getServiceCharge();
                    }
                }
                selectRadioBtn(field_currency, rgCurrency);
            }
        });*/
        rgRestrict.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isRestrictChoose = true;
                if (rbRestrictNone.isChecked()) {
                    sendingLimit = "0";
                } else if (rbRestrictNeedBoardcast.isChecked()) {
                    sendingLimit = "1";
                }
                //selectRadioBtn(field_restrict,rgRestrict);
            }
        });
    }

    private void selectRadioBtn(String target, RadioGroup rgTarget) {
        RadioButton rb;
        switch (target) {
            case field_target:
                rb = (RadioButton) rootView.findViewById(rgTarget.getCheckedRadioButtonId());
                select_target = rb.getText().toString();
                break;
            case field_currency:
                rb = (RadioButton) rootView.findViewById(rgTarget.getCheckedRadioButtonId());
                currency = rb.getText().toString();
                break;
            case field_restrict:
                rb = (RadioButton) rootView.findViewById(rgTarget.getCheckedRadioButtonId());
                select_restrict = rb.getText().toString();
                break;
        }
    }

    /**
     * 实际支付
     */
    private void getActualPay() {
        String JYCJ_curr = UserInfoUtils.getString(getContext(), Contants.JYCJ_currencyValue);
        actuallPay = MyTextUtils.doubleFormat((Double.parseDouble(serviceChargePay) + Double.parseDouble(sendTheTotal)) * Double.parseDouble(currencyValue) / Double.parseDouble(JYCJ_curr)) + "";
        tvActualPay.setText("(" + sendTheTotal + "+" + serviceChargePay + ") * " + currencyValue + "/" + JYCJ_curr + "=" + actuallPay);
    }

    /**
     * 查询所有币种的币值
     */
    private void queryCurrency() {
        JSONObject object = new JSONObject();
        RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());
        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/api/project/queryCurrency")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(getContext(), Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(getContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("onFailure-----", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("queryCurrency", "----" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        currencyList = new ArrayList<>();
                        JSONObject data = new JSONObject(object.getString("data"));
                        JSONArray arr = data.getJSONArray("currencyList");
                        JSONObject jycj = data.getJSONObject("currencyMap");
                        jycjModel = new CurrencyModel();
                        jycjModel.currency = jycj.getString("currency");
                        jycjModel.currencyValue = jycj.getString("currencyValue");
                        jycjModel.serviceCharge = jycj.getString("serviceCharge");
                        jycjModel.createTime = jycj.getString("createTime");
                        jycjModel.updateTime = jycj.getString("updateTime");
                        currencyList.add(jycjModel);
                        UserInfoUtils.saveString(getContext(), Contants.JYCJ_currencyValue, jycjModel.currencyValue);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject item = new JSONObject(arr.get(i).toString());
                            CurrencyModel model = new CurrencyModel();
                            model.currency = item.getString("currency");
                            model.currencyValue = item.getString("currencyValue");
                            model.serviceCharge = item.getString("serviceCharge");
                            model.createTime = item.getString("createTime");
                            model.updateTime = item.getString("updateTime");
                            UserInfoUtils.saveString(getContext(), model.currency, model.currencyValue);
                            currencyList.add(model);
                            Message m = new Message();
                            m.what = Contants.GET_DATA_SUCCESS;
                            m.obj = currencyList;
                            handler.sendMessage(m);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTouchListener() {
        scrollParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getActivity().findViewById(R.id.scroll_img).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        scrollImg.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        etIssueCurrencyContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //这句话说的意思告诉父View我自己的事件我自己处理
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    private void initTitle() {
        titlebar.setTitle("项目");
        titlebar.setBackgroundColor(getContext().getResources().getColor(R.color.white));//设置默认titleBar颜色
        titlebar.setLeftImageResourceSpeacial(R.mipmap.icon_gift_box);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityUtils.startActivity(getContext(), ProjectIssueRecordActivity.class);
                //ActivityUtils.startActivity(getContext(), AirDropCandyActivity.class);
            }
        });
        titlebar.addAction(new TitleBar.ImageAction(R.mipmap.icon_msg_remind) {
            @Override
            public void performAction(View view) {
                if (!UserInfoUtils.isUserLogin(getContext())) {
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(getContext(), LoginActivity.class);
                    return;
                }
                ActivityUtils.startActivity(getContext(), MessageRecordActivity.class);

            }
        });
    }

    private void initGridView() {
        System.arraycopy(origin_icon, 0, nor_icon, 0, 8);
        adapter = new IconAdapter(nor_icon, getContext());
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isIconChoose = true;
                icon = i + "";
                System.arraycopy(origin_icon, 0, nor_icon, 0, 8);
                nor_icon[i] = check_icon[i];
                adapter.setArr(nor_icon);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_add_picture, R.id.btn_pay, R.id.tv_boardcast_start_time, R.id.et_cantake_count, R.id.et_cantake_people_count})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_boardcast_start_time:
                initTimePicket();
                break;
            case R.id.et_cantake_count:
                if (!isCurrencyChoose)
                    ToastUtils.showToast("请先选择币种");
                break;
            case R.id.et_cantake_people_count:
                if (!isCurrencyChoose)
                    ToastUtils.showToast("请先选择币种");
                break;
            case R.id.iv_add_picture:
                EventBus.getDefault().post(new GetPictureEvent("getPicture", ivUpload));
                break;
            case R.id.btn_pay:
                if(!UserInfoUtils.isUserLogin(getContext())){
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(getContext(),LoginActivity.class);
                    return;
                }

                if (TextUtils.isEmpty(etIssueCurrency.getText().toString())) {
                    ToastUtils.showToast("请输入发币方");
                    return;
                }

                if (TextUtils.isEmpty(etIssueCurrencyContent.getText().toString())) {
                    ToastUtils.showToast("请输入广播内容");
                    return;
                }
                if (TextUtils.isEmpty(tvBoardcastStartTime.getText().toString())) {
                    ToastUtils.showToast("请输入广播时间");
                    return;
                }
                if (!isIconChoose) {
                    ToastUtils.showToast("请选择图标");
                    return;
                }
                if (!isRestrictChoose) {
                    ToastUtils.showToast("请选择发送限制");
                    return;
                }

                if (!isTargetChoose) {
                    ToastUtils.showToast("请选择发送对象");
                    return;
                }
                if (!isCurrencyChoose) {
                    ToastUtils.showToast("请选择发送币种");
                    return;
                }

                if (TextUtils.isEmpty(etCantakeCount.getText().toString()) || TextUtils.isEmpty(etCantakePeopleCount.getText().toString())) {
                    ToastUtils.showToast("请完善发送量信息");
                    return;
                }

                theIssuingParty = etIssueCurrency.getText().toString();
                if (TextUtils.isEmpty(theIssuingParty)) {
                    ToastUtils.showToast("请输入发币方");
                    return;
                }
                broadcastContent = etIssueCurrencyContent.getText().toString();
                average = etCantakeCount.getText().toString();
                peopleNumber = etCantakePeopleCount.getText().toString();

                if (!MyTextUtils.isNumeric(average) || !MyTextUtils.isNumeric(peopleNumber) /*|| !MyTextUtils.isNumeric(sendTheTotal)*/) {
                    ToastUtils.showToast("请输入合法字符");
                    return;
                } else if (TextUtils.isEmpty(average) || TextUtils.isEmpty(peopleNumber) /*|| TextUtils.isEmpty(sendTheTotal)*/) {
                    ToastUtils.showToast("请完善所需信息");
                    return;
                }


                EventBus.getDefault().post(new MessageEvent(Contants.EVENT_CHANGE_BLIGHTNESS));
                AlertDialog dialog = new AlertDialog(getActivity(), "确定", "取消", "支付", "确认支付" + actuallPay + "?", true);
                dialog.setSampleDialogListener(new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        addProject();
                    }

                    @Override
                    public void onCancel() {
                        EventBus.getDefault().post(new MessageEvent(Contants.EVENT_RECOVER_BLIGHTNESS));
                    }
                });
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        EventBus.getDefault().post(new MessageEvent(Contants.EVENT_RECOVER_BLIGHTNESS));
                    }
                });
                //ActivityUtils.startActivity(getContext(),ProjectPaySuccessActivity.class);
                break;
        }
    }

    /**
     * 发布项目
     */
    private List<File> imgFile;
    private String theIssuingParty;
    private String broadcastContent;
    private String broadcastImages1;
    private String broadcastImages2;
    private String broadcastImages3;
    private String broadcastStartTime;
    private String icon;
    private String currency;
    private String sendingLimit;
    private String sendObject;
    private String average = "";
    private String peopleNumber = "";
    private String sendTheTotal = "";

    private void addProject() {
        sendTheTotal = etIssueSum.getText().toString();
        JSONObject object = new JSONObject();
        try {
            object.put("userId", UserInfoUtils.getInt(getContext(), Contants.USER_ID));
            object.put("theIssuingParty", theIssuingParty);
            object.put("broadcastContent", broadcastContent);
            object.put("broadcastImages1", imgUrls[0]);
            object.put("broadcastImages2", imgUrls[1]);
            object.put("broadcastImages3", imgUrls[2]);
            object.put("broadcastStartTime", broadcastStartTime);
            object.put("icon", icon);
            object.put("currency", currency);
            object.put("sendingLimit", sendingLimit);
            object.put("sendObject", sendObject);
            object.put("average", average);
            object.put("peopleNumber", peopleNumber);
            object.put("sendTheTotal", sendTheTotal);
            object.put("actuallyPay", actuallPay);
            LogUtils.d("object-----", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(Contants.JSON, object.toString());

        final Request request = new Request.Builder()
                .url(LinkParams.REQUEST_URL + "/api/project/addProject")
                .addHeader(Contants.AUTHORIZATION, UserInfoUtils.getString(getContext(), Contants.AUTHORIZATION))
                .post(requestBody)
                .build();
        OkHttpManage.getClient(getContext()).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("onFailure-----", e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtils.d("addProject", "----" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if ("200".equals(object.getString("code"))) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast("发布成功");
                                EventBus.getDefault().post(new MessageEvent(Contants.EVENT_REFRESH_FRAGMENT));
                            }
                        });
                        //handler.sendEmptyMessage(Contants.PROJECT_ADD_SUCCESS);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initTimePicket() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        timePickerDialog = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("请选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.theme_color))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.theme_color))
                .setWheelItemTextSize(14)
                .build();

        timePickerDialog.show(getFragmentManager(), "all");
    }


    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    private List<Uri> photos;
    private String[] imgUrls = new String[3];

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {
        if (baseEvent instanceof GetPictureEvent) {
            GetPictureEvent getPictureEvent = (GetPictureEvent) baseEvent;
            photos = getPictureEvent.getPhotos();
            imgFile = new ArrayList<>();
            for (int i = 0; i < photos.size(); i++) {
                File file = uri2File(photos.get(i));
                imgFile.add(file);
            }

            final RequestOptions options = new RequestOptions();
/*            options.centerCrop()
                    .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                    .error(R.drawable.__picker_ic_broken_image_black_48dp);*/
            if (imgFile.size() == 1) {
                Picasso.get().load(imgFile.get(0)).into(ivUpload);
                ivUpload.setVisibility(View.VISIBLE);
                ivUpload2.setVisibility(View.GONE);
                ivUpload3.setVisibility(View.GONE);
            }

            if (imgFile.size() == 2) {
                Picasso.get().load(imgFile.get(0)).into(ivUpload);
                Picasso.get().load(imgFile.get(1)).into(ivUpload2);
                ivUpload.setVisibility(View.VISIBLE);
                ivUpload2.setVisibility(View.VISIBLE);
                ivUpload3.setVisibility(View.GONE);
            }

            if (imgFile.size() == 3) {
                Picasso.get().load(imgFile.get(0)).into(ivUpload);
                Picasso.get().load(imgFile.get(1)).into(ivUpload2);
                Picasso.get().load(imgFile.get(2)).into(ivUpload3);
                ivUpload.setVisibility(View.VISIBLE);
                ivUpload2.setVisibility(View.VISIBLE);
                ivUpload3.setVisibility(View.VISIBLE);
            }

            Configuration config = new Configuration.Builder()
                    .zone(AutoZone.autoZone)
                    .build();

            UploadManager uploadManager = new UploadManager(config);

            for (int i = 0; i < imgFile.size(); i++) {

                final int finalI = i;
                uploadManager.put(imgFile.get(i), imgFile.get(i).getName(), UserInfoUtils.getString(getContext(), Contants.QINIU_TOKEN), new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            LogUtils.i("qiniu", "Upload Success");
                        } else {
                            LogUtils.i("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        try {
                            imgUrls[finalI] = Contants.QINIU_UPLOAD_URL + response.getString("key");
                            LogUtils.i("qiniu", "imgUrl----" + imgUrls[finalI]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
            }

        }
    }

    /**
     * Uri转换为图片
     *
     * @param uri
     * @return
     */
    private File uri2File(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = getActivity().managedQuery(uri, proj, null,
                null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor
                    .getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        broadcastStartTime = getDateToString(millseconds);
        tvBoardcastStartTime.setText(broadcastStartTime);
    }



/*    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.et_issue_currency_content && canVerticalScroll(etIssueCurrencyContent))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }*/


    class EditChangedListener implements TextWatcher {
        private EditText editText = null;

        public EditChangedListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
/*            if (!isCurrencyChoose) {
                ToastUtils.showToast("请先选择币种");
            }*/

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        // 输入文字后的状态
        @Override
        public void afterTextChanged(Editable s) {
            if (editText == etCantakeCount) {
                average = editText.getText().toString();
            } else if (editText == etCantakePeopleCount) {
                peopleNumber = editText.getText().toString();
            }

            if (MyTextUtils.isNumeric(average)&&MyTextUtils.isNumeric(peopleNumber)&& !TextUtils.isEmpty(average) && !TextUtils.isEmpty(peopleNumber)) {
                rlPay.setVisibility(View.VISIBLE);
                sendTheTotal = (Integer.parseInt(average) * Integer.parseInt(peopleNumber)) + "";
                tvAllSend.setText(average + "*" + peopleNumber + " = " + sendTheTotal);
                etIssueSum.setText(sendTheTotal);
                LogUtils.d("textChange", serviceCharge + "-----------------" + sendTheTotal);
                serviceChargePay = (Double.parseDouble(sendTheTotal) * Double.parseDouble(serviceCharge) / 100) + "";
                tvSeviceCharge.setText(sendTheTotal + "*" + Double.parseDouble(serviceCharge) / 100 + "=" + serviceChargePay);
                getActualPay();
            } else {
                rlPay.setVisibility(View.GONE);
                etIssueSum.setText("");
            }

        }
    }


}
