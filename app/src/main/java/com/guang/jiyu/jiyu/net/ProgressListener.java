package com.guang.jiyu.jiyu.net;

/**
 * 进度条监听器
 * Created by admin on 2018/7/10.
 */

public interface  ProgressListener {
    /**
     * 显示进度
     *
     * @param mProgress
     */
    public void onProgress(int mProgress,long contentSize);

    /**
     * 完成状态
     *
     * @param totalSize
     */
    public void onDone(long totalSize);
}
