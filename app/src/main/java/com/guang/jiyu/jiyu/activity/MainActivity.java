package com.guang.jiyu.jiyu.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.guang.jiyu.R;
import com.guang.jiyu.base.BaseActivity;
import com.guang.jiyu.base.BaseEvent;
import com.guang.jiyu.base.BaseFragment;
import com.guang.jiyu.base.Contants;
import com.guang.jiyu.jiyu.adapter.FragmentAdapter;
import com.guang.jiyu.jiyu.event.GetCandySuccessEvent;
import com.guang.jiyu.jiyu.event.MessageEvent;
import com.guang.jiyu.jiyu.event.ShareInformationEvent;
import com.guang.jiyu.jiyu.event.NewsFlashRefreshEvent;
import com.guang.jiyu.jiyu.fragment.HomeFragment;
import com.guang.jiyu.jiyu.fragment.InformationFragment;
import com.guang.jiyu.jiyu.fragment.MarketFragmant;
import com.guang.jiyu.jiyu.fragment.ProjectFragment;
import com.guang.jiyu.jiyu.fragment.UserFragment;
import com.guang.jiyu.jiyu.model.AirCandyModel;
import com.guang.jiyu.jiyu.model.FastInformationModel;
import com.guang.jiyu.jiyu.utils.ToastUtils;
import com.guang.jiyu.jiyu.widget.GetCandySuccessPopupwindow;
import com.guang.jiyu.jiyu.widget.SharePopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.bbl)
    BottomBarLayout bbl;
    @BindView(R.id.bbl_home)
    BottomBarItem bblHome;
    @BindView(R.id.bbl_information)
    BottomBarItem bblInformation;
    @BindView(R.id.bbl_market)
    BottomBarItem bblMarket;
    @BindView(R.id.bbl_project)
    BottomBarItem bblProject;
    @BindView(R.id.bbl_user)
    BottomBarItem bblUser;


    private List<BaseFragment> mFragmentList = new ArrayList<>();
    private RotateAnimation mRotateAnimation;
    private SharePopupWindow sharePopupWindow;
    private GetCandySuccessPopupwindow getCandySuccessPopupwindow;
    private HomeFragment homeFragment;
    private InformationFragment informationFragment;
    private MarketFragmant marketFragmant;
    private ProjectFragment projectFragment;
    private UserFragment userFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_main);
        initFragment();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化底部导航栏监听事件
     */
    private void initListener() {
        new QBadgeView(this).bindTarget(bblHome).setBadgeNumber(5);
        bbl.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final BottomBarItem bottomBarItem, int previousPosition, final int currentPosition) {
                Log.i("currentPosition","-------" + currentPosition);
                if(currentPosition == 1){
                    EventBus.getDefault().post(new NewsFlashRefreshEvent(2));
                }

                if(currentPosition == 2){
                    //showCenterPopupWindow( );
                    //initGetCandyPopWindow(null);
                }
/*                if (currentPosition == 0) {
                    //如果是第一个，即首
                    if (previousPosition == currentPosition) {
                        //如果是在原来位置上点击,更换首页图标并播放旋转动画
                        bottomBarItem.setIconSelectedResourceId(R.mipmap.tab_loading);//更换成加载图标
                        bottomBarItem.setStatus(true);

                        //播放旋转动画
                        if (mRotateAnimation == null) {
                            mRotateAnimation = new RotateAnimation(0, 360,
                                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                    0.5f);
                            mRotateAnimation.setDuration(800);
                            mRotateAnimation.setRepeatCount(-1);
                        }
                        ImageView bottomImageView = bottomBarItem.getImageView();
                        bottomImageView.setAnimation(mRotateAnimation);
                        bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画
                        return;
                    }
                }*/
                bbl.setCurrentItem(currentPosition);
                //如果点击了其他条目
                /*BottomBarItem bottomItem = bbl.getBottomItem(0);
                bottomItem.setIconSelectedResourceId(R.mipmap.icon_home_select);//更换为原来的图标

                cancelTabLoading(bottomItem);//停止旋转动画*/
            }
        });
    }

    private void initSharePopWindow(FastInformationModel model) {
        sharePopupWindow = new SharePopupWindow(this);
        sharePopupWindow.setOnItemClickListener(new SharePopupWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                switch (v.getId()){
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
        sharePopupWindow.showAtLocation(MainActivity.this.findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
        setWindowAttr(0.6f);

        sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAttr(1f);
            }
        });
    }

    @Subscribe
    public void onEventMainThread(BaseEvent baseEvent) {

        if(baseEvent instanceof MessageEvent){
            MessageEvent messageEvent = (MessageEvent) baseEvent;
            switch (messageEvent.getMessage()){
                case Contants.EVENT_INFORMATION:
                    bbl.setCurrentItem(1);
                    break;
            }
        }
        if(baseEvent instanceof ShareInformationEvent){
            ShareInformationEvent shareInformationEvent = (ShareInformationEvent) baseEvent;
            FastInformationModel model = shareInformationEvent.getInformationModel();
            initSharePopWindow(model);
        }

/*        if(baseEvent instanceof FastInformationEvent){
            FastInformationEvent fastInformationEvent = (FastInformationEvent) baseEvent;
            InformationModel model = fastInformationEvent.getFastInformationModel();
            initPopWindow();
        }*/

    }




    /**
     * 初始化fragment
     */
    private void initFragment() {
         homeFragment = new HomeFragment();
         informationFragment = new InformationFragment();
         marketFragmant = new MarketFragmant();
         projectFragment = new ProjectFragment();
         userFragment = new UserFragment();
        mFragmentList.add(homeFragment);
        mFragmentList.add(informationFragment);
        mFragmentList.add(marketFragmant);
        mFragmentList.add(projectFragment);
        mFragmentList.add(userFragment);
        vpContent.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mFragmentList));
        vpContent.setOffscreenPageLimit(5);//设置缓存页面的个数
        bbl.setViewPager(vpContent);


    }

    public void showCenterPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tvTitle = (TextView)contentView.findViewById(R.id.tv_title);
        TextView tvConfirm = (TextView)contentView.findViewById(R.id.tv_confirm);
        TextView tvCancel = (TextView)contentView.findViewById(R.id.tv_cancel);
        tvTitle.setText("标为已读");
        tvConfirm.setText("置顶公众号");
        tvCancel.setText("取消关注");

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "标为已读", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "置顶公众号", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "取消关注", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(MainActivity.this.getWindow().getDecorView(),Gravity.CENTER,0,0);

    }

    /**
     * 停止首页页签的旋转动画
     */
    private void cancelTabLoading(BottomBarItem bottomItem) {
        Animation animation = bottomItem.getImageView().getAnimation();
        if (animation != null) {
            animation.cancel();
        }
    }


}
