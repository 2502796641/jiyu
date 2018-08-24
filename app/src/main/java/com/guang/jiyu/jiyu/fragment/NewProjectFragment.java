package com.guang.jiyu.jiyu.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.activity.LoginActivity;
import com.guang.jiyu.jiyu.activity.MessageRecordActivity;
import com.guang.jiyu.jiyu.activity.ProjectIssueRecordActivity;
import com.guang.jiyu.jiyu.activity.ProjectPaySuccessActivity;
import com.guang.jiyu.jiyu.adapter.IconAdapter;
import com.guang.jiyu.jiyu.event.GetPictureEvent;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.utils.ActivityUtils;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.utils.UserInfoUtils;
import com.guang.jiyu.jiyu.widget.AlertDialog;
import com.guang.jiyu.jiyu.widget.MyGridView;
import com.guang.jiyu.jiyu.widget.TitleBar;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by admin on 2018/8/23.
 */

public class NewProjectFragment extends BaseFragment implements OnDateSetListener {
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
    private int[] check_icon = new int[]{R.mipmap.b1_01, R.mipmap.b1_02, R.mipmap.b1_03, R.mipmap.b1_04,
            R.mipmap.b1_05, R.mipmap.b1_06, R.mipmap.b1_07, R.mipmap.b1_08};
    private int[] nor_icon = new int[8];
    private int[] origin_icon = new int[]{R.mipmap.b2_01, R.mipmap.b2_02, R.mipmap.b2_03, R.mipmap.b2_04,
            R.mipmap.b2_05, R.mipmap.b2_06, R.mipmap.b2_07, R.mipmap.b2_08};
    private IconAdapter adapter;
    boolean canLoadImage;
    private TimePickerDialog timePickerDialog;

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
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        initTitle();
        initGridView();
        return rootView;
    }


    private void initTitle() {
        titlebar.setTitle("项目");
        titlebar.setBackgroundColor(getContext().getResources().getColor(R.color.white));//设置默认titleBar颜色
        titlebar.setLeftImageResourceSpeacial(R.mipmap.icon_gift_box);
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (!UserInfoUtils.isUserLogin(getContext())) {
                    ToastUtils.showToast("请先登录");
                    ActivityUtils.startActivity(getContext(), LoginActivity.class);
                    return;
                }*/
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
                System.arraycopy(origin_icon, 0, nor_icon, 0, 8);
                nor_icon[i] = check_icon[i];
                adapter.setArr(nor_icon);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 重置图标
     */
    private void resetIconArr() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_add_picture, R.id.btn_pay, R.id.tv_boardcast_start_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_boardcast_start_time:
                initTimePicket();
                break;
            case R.id.iv_add_picture:
                EventBus.getDefault().post(new GetPictureEvent("getPicture", ivUpload));
                break;
            case R.id.btn_pay:
                EventBus.getDefault().post(new MessageEvent(Contants.EVENT_CHANGE_BLIGHTNESS));
                AlertDialog dialog = new AlertDialog(getActivity(), "确定", "取消", "支付", "确认支付200000？", true);
                dialog.setSampleDialogListener(new AlertDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onConfirm() {
                        ActivityUtils.startActivity(getContext(), ProjectPaySuccessActivity.class);
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

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {
        if (baseEvent instanceof GetPictureEvent) {
            GetPictureEvent getPictureEvent = (GetPictureEvent) baseEvent;
            final RequestOptions options = new RequestOptions();
/*            options.centerCrop()
                    .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                    .error(R.drawable.__picker_ic_broken_image_black_48dp);*/
            Glide.with(this)
                    .load(getPictureEvent.getUri())
                    //.apply(options)
                    .thumbnail(0.1f)
                    .into(ivUpload);
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = getDateToString(millseconds);
        tvBoardcastStartTime.setText(text);
    }
}
