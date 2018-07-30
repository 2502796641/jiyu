package com.guang.jiyu.jiyu.listener;

import android.widget.AbsListView;

import com.guang.jiyu.jiyu.activity.CompletedRankActivity;

/**
 * 滚动时产生的事件
 * Created by admin on 2018/6/25.
 */

public class OnScrollListener implements AbsListView.OnScrollListener {
    private boolean isLastRow;
    public OnScrollListener(boolean isLastRow) {
        this.isLastRow = isLastRow;
    }

    //滚动的时候一直回调，直到停止滚动时才停止回调，单击时回调一次
    //firstVisibleItem:当前嫩看见的第一个列表项ID(从0开始,小半个也算)
    //visibleItemCount：当前能看见的列表项个数(小半个也算)
    //totalItemCount：列表项总共数
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        System.out.println("firstVisibleItem=" + firstVisibleItem);

        System.out.println("visibleItemCount=" + visibleItemCount);
        //判断是否滚动到最后一行
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            System.out.println("已经到最后一行了");
            isLastRow = true;
        }
    }

    //正在滚动时回调，回调2-3次，手指没抛则回调2次，scrollState=2的这次不回调
    //回调顺序如下：
    //第一次：scrollState=SCROLL_STATE_TOUCH_SCROLL(1)正在滚动
    //第二次：scrollState = SCROLL_STATE_FLING(2)手指做了抛的动作（手指离开屏幕前，用力滑了一下）
    //第三次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动

    //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
    //由于用户的操作，屏幕产生惯性滑动时为2
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        System.out.println("789");
        //当滚动到最后一行并且停止滚动时，执行加载
        if (isLastRow && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            //执行加载代码
            isLastRow = false;
        }
    }

}
