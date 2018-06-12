package com.blife.blife_app.tools.view;

import android.os.Handler;
import android.widget.ImageView;

/**
 * Created by w on 2016/9/18.
 */
public class SceneAnimation {

    private ImageView mImageView;
    private int[] mFrameRess;// 图片
    private int mDuration = 0;
    private Handler handler = new Handler();
    Runnable mRunnable;

    private int mLastFrameNo;
    private int currentIndex = 0;

    //参数包括pDurations数组，执行播放play(1);
    public SceneAnimation(ImageView pImageView, int[] pFrameRess, int pDurations) {
        mImageView = pImageView;
        mFrameRess = pFrameRess;
        mDuration = pDurations;
        mLastFrameNo = pFrameRess.length - 1;
        mImageView.setBackgroundResource(mFrameRess[0]);
        mRunnable = new Runnable() {
            public void run() {
                if (currentIndex == mLastFrameNo) {
                    currentIndex = 0;
                } else {
                    currentIndex = currentIndex + 1;
                }
                mImageView.setBackgroundResource(mFrameRess[currentIndex]);
                play();
            }
        };
    }

    public void play() {
        if (mDuration != 0) {
            handler.postDelayed(mRunnable, mDuration);
        }
    }

}
