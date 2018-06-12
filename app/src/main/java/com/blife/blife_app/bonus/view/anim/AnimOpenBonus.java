package com.blife.blife_app.bonus.view.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.tools.view.MenuPopWindows;

/**
 * Created by w on 2016/9/12.
 */
public class AnimOpenBonus {

    private String money = "";
    private String name = "";
    private Context context;
    private AnimatorSet animatorSet;
    private AnimationDrawable startAnim;
    private Handler handler;
    private long duration = 0;
    //布局
    private View rootView;
    private LinearLayout lin_view;
    private ImageView iv_anim_back, iv_anim_light, iv_anim_flower;
    private TextView tv_money, tv_name, tv_name_bottom;
    private MenuPopWindows popupWindow;

    //动画
    private int globAnimDuration = 600, flowerAnimDuration = 500;
    private ObjectAnimator globAnimation, flowerAnimation;


    public AnimOpenBonus(Context context) {
        this.context = context;
        initLayout();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 100) {
                    iv_anim_back.setVisibility(View.INVISIBLE);
                    iv_anim_flower.setVisibility(View.VISIBLE);
                    flowerAnimation.start();
                } else if (msg.what == 200) {
                    tv_money.setText("￥" + money);
                    tv_name.setText(name);
//                    tv_name_bottom.setText(name);
                    iv_anim_light.setVisibility(View.VISIBLE);
                    lin_view.setVisibility(View.VISIBLE);
                    if (animatorSet != null && !animatorSet.isRunning()) {
                        animatorSet.start();
                    }
                }
            }
        };
    }

    /**
     * 显示
     *
     * @param money
     * @param name
     */
    public void show(View view, String money, String name) {
        this.money = money;
        this.name = name;
        popupWindow.show(view);
        if (globAnimation != null && !globAnimation.isRunning()) {
            iv_anim_back.setVisibility(View.VISIBLE);
            globAnimation.start();
            handler.sendEmptyMessageDelayed(100, globAnimDuration - 100);
            handler.sendEmptyMessageDelayed(200, globAnimDuration - 100 + flowerAnimDuration);
        }
    }

    /**
     * 取消
     */
    private void cancel() {
        if (startAnim != null && startAnim.isRunning()) {
            startAnim.stop();
        }
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        iv_anim_back.setVisibility(View.INVISIBLE);
        iv_anim_flower.setVisibility(View.INVISIBLE);
        iv_anim_light.setVisibility(View.INVISIBLE);
        lin_view.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始化界面
     */
    private void initLayout() {
        rootView = LayoutInflater.from(context).inflate(R.layout.anim_open_bonus, null);
        lin_view = (LinearLayout) rootView.findViewById(R.id.lin_view);
        iv_anim_back = (ImageView) rootView.findViewById(R.id.iv_anim_back);
        iv_anim_flower = (ImageView) rootView.findViewById(R.id.iv_anim_flower);
        iv_anim_light = (ImageView) rootView.findViewById(R.id.iv_anim_light);
        tv_money = (TextView) rootView.findViewById(R.id.tv_anim_money);
        tv_name = (TextView) rootView.findViewById(R.id.tv_anim_name);
        tv_name_bottom = (TextView) rootView.findViewById(R.id.tv_anim_name_bottom);
        popupWindow = new MenuPopWindows(context, rootView);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                cancel();
            }
        });
        initStartAnimationDrawable();
        iv_anim_back.setVisibility(View.INVISIBLE);
        iv_anim_flower.setVisibility(View.INVISIBLE);
        iv_anim_light.setVisibility(View.INVISIBLE);
        lin_view.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始化动画
     */
    private void initStartAnimationDrawable() {
//        startAnim = (AnimationDrawable) context.getResources().getDrawable(R.drawable.open_bonus_start);
//        iv_anim_back.setBackgroundDrawable(startAnim);
//        for (int i = 0; i < startAnim.getNumberOfFrames(); i++) {
//            duration = duration + startAnim.getDuration(i);
//        }
//        animation = new SceneAnimation(iv_anim_back, BonusAnimConstant.OpenBonusAnim, 10);
        initStartAnim();
        initLightAnim();
    }

    /**
     * 初始化光动画
     */
    private void initLightAnim() {
        animatorSet = new AnimatorSet();
        ObjectAnimator rotation = ObjectAnimator.ofFloat(iv_anim_light, "rotation", 0f, 359.9f);
        rotation.setRepeatMode(ValueAnimator.RESTART);
        rotation.setRepeatCount(ValueAnimator.INFINITE);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setDuration(6000);

        PropertyValuesHolder zoomScaleX = PropertyValuesHolder.ofFloat("scaleX", 1.3f, 0.9f, 1.05f, 0.95f, 1.0f);
        PropertyValuesHolder zoomScaleY = PropertyValuesHolder.ofFloat("scaleY", 1.3f, 0.9f, 1.05f, 0.95f, 1.0f);
        ObjectAnimator zoomScale = ObjectAnimator.ofPropertyValuesHolder(iv_anim_light, zoomScaleX, zoomScaleY);
        zoomScale.setDuration(800);
        animatorSet.play(rotation).with(zoomScale);
    }

    /**
     * 初始化金币动画
     */
    private void initStartAnim() {
        float h = ScreenUtils.getScreenHeight(context);
        PropertyValuesHolder zoomScaleX = PropertyValuesHolder.ofFloat("scaleX", 0, 0.3f, 0.6f, 0.8f, 1.0f, 0.5f, 1.0f, 0.5f);
        PropertyValuesHolder TransAnimY = PropertyValuesHolder.ofFloat("y", 0, 0.1f * h, 0.3f * h, 0.5f * h, 0.7f * h, 0.8f * h, 0.7f * h, 0.5f * h);
        globAnimation = ObjectAnimator.ofPropertyValuesHolder(iv_anim_back, zoomScaleX, TransAnimY);
        globAnimation.setDuration(globAnimDuration);

        PropertyValuesHolder ScaleX = PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1.5f, 2.0f, 6.0f);
        PropertyValuesHolder ScaleY = PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1.0f, 1.5f, 4.0f);
        PropertyValuesHolder TransY = PropertyValuesHolder.ofFloat("y", 0.5f * h, 0.3f * h);
        flowerAnimation = ObjectAnimator.ofPropertyValuesHolder(iv_anim_flower, ScaleX, ScaleY, TransY);
        flowerAnimation.setDuration(flowerAnimDuration);
    }

}
