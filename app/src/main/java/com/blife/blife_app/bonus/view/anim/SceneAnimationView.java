package com.blife.blife_app.bonus.view.anim;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by w on 2016/9/18.
 */
public class SceneAnimationView extends View {

    private int duration = 0;
    private int[] Frames;
    private boolean oneShot;
    private int index;

    public SceneAnimationView(Context context) {
        super(context);
    }

    public SceneAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SceneAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFrames(int[] frames) {
        Frames = frames;
    }

    public void setOneShot(boolean oneShot) {
        this.oneShot = oneShot;
    }

    public void start() {
        if (Frames == null)
            return;
        if (duration == 0)
            return;
        play();
    }

    private void play() {
        if (oneShot) {
            if (index == Frames.length - 1) {

            } else {
                index = index + 1;
            }
        } else {
            if (index == Frames.length - 1) {
                index = 0;
            } else {
                index = index + 1;
            }
        }
//        invalidate();
        postInvalidateDelayed(duration);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), Frames[index]), 0, 0, new Paint());
    }
}
