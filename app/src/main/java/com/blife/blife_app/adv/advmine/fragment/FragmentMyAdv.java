package com.blife.blife_app.adv.advmine.fragment;


import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.bonus.bean.BeanMessageEvent;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.utils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/8/25.
 */
public class FragmentMyAdv extends BaseFragment implements View.OnTouchListener {
    private TextView tv_prepare;
    private TextView tv_waiting, tv_performing, tv_isfinish;
    private List<TextView> TabTextViews = new ArrayList<>();
    private FragmentMyAdvPrepare fragmentMyAdvPrepare;
    private FragmentMyAdvWaiting fragmentMyAdvWaiting;
    private FragmentMyAdvPerforming fragmentMyAdvPerforming;
    private FragmentMyAdvFinished fragmentMyAdvFinished;
    private FrameLayout fragment_adv;
    private int lastX;
    private View tabline1;
    private int screenWidth;
    private TranslateAnimation translateAnimation;
    private int currentIndex = 0;

    //Tab切换
    @Override
    public int getLayout() {
        return R.layout.fragment_myadv;
    }

    @Override
    public void init() {
        initTopBar(R.string.myadv_title);
        initView();
        initClick();
        initData();
    }

    private void initClick() {
        tv_prepare.setOnTouchListener(this);
        tv_waiting.setOnTouchListener(this);
        tv_performing.setOnTouchListener(this);
        tv_isfinish.setOnTouchListener(this);
    }

    private void initData() {
        lastX = 0;
        TabTextViews.clear();
        TabTextViews.add(tv_prepare);
        TabTextViews.add(tv_waiting);
        TabTextViews.add(tv_performing);
        TabTextViews.add(tv_isfinish);
        selectTab(0);
        EventBus.getDefault().register(this);
    }

    private void initView() {
        tv_prepare = (TextView) rootView.findViewById(R.id.tv_prepare);
        tv_waiting = (TextView) rootView.findViewById(R.id.tv_waiting);
        tv_performing = (TextView) rootView.findViewById(R.id.tv_performing);
        tv_isfinish = (TextView) rootView.findViewById(R.id.tv_isfinish);
        tabline1 = (View) rootView.findViewById(R.id.tabline1);
        screenWidth = ScreenUtils.getScreenWidth(getActivity());
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) tabline1.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.width = screenWidth / 4;// 控件的宽强制设成5份
        linearParams.leftMargin = 0;
        tabline1.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>

        fragment_adv = (FrameLayout) rootView.findViewById(R.id.fragment_adv);

    }

    /**
     * 切换标签
     *
     * @param position
     */
    private void selectTab(int position) {
        for (int i = 0; i < TabTextViews.size(); i++) {
            if (position == i) {
                TabTextViews.get(i).setTextColor(getResources().getColor(R.color.color_common));
                translateAnimation = new TranslateAnimation((screenWidth / 4) * currentIndex, (screenWidth / 4) * i, 0, 0);
                currentIndex = i;
            } else {
                TabTextViews.get(i).setTextColor(getResources().getColor(R.color.color_user_agreement));
//                translateAnimation = new TranslateAnimation(0, screenWidth / 4, 0, 0);
            }
        }
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        tabline1.startAnimation(translateAnimation);
        selectFragment(position);
    }

    /**
     * 切换Fragment
     */
    private void selectFragment(int position) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        switch (position) {
            case 0:
                if (fragmentMyAdvPrepare == null) {
                    fragmentMyAdvPrepare = new FragmentMyAdvPrepare();
                }
                if (!fragmentMyAdvPrepare.isAdded())
                    fragmentTransaction.replace(R.id.fragment_adv, fragmentMyAdvPrepare);
                break;
            case 1:
                if (fragmentMyAdvWaiting == null) {
                    fragmentMyAdvWaiting = new FragmentMyAdvWaiting();
                }
                if (!fragmentMyAdvWaiting.isAdded())
                    fragmentTransaction.replace(R.id.fragment_adv, fragmentMyAdvWaiting);
                break;
            case 2:
                if (fragmentMyAdvPerforming == null) {
                    fragmentMyAdvPerforming = new FragmentMyAdvPerforming();
                }
                if (!fragmentMyAdvPerforming.isAdded())
                    fragmentTransaction.replace(R.id.fragment_adv, fragmentMyAdvPerforming);
                break;
            case 3:
                if (fragmentMyAdvFinished == null) {
                    fragmentMyAdvFinished = new FragmentMyAdvFinished();
                }
                if (!fragmentMyAdvFinished.isAdded()) {
                    fragmentTransaction.replace(R.id.fragment_adv, fragmentMyAdvFinished);
                }
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void translateLine(MotionEvent event) {
//        getX是获取以widget左上角为坐标原点计算的Ｘ轴坐标直．
//        getRawX 获取的是以屏幕左上角为坐标原点计算的Ｘ轴坐标直．
        float x = event.getX();
        float rx = event.getRawX();
        final float nx = rx - x;
        TranslateAnimation trans = null;
        if (nx > lastX) {
            trans = new TranslateAnimation(0, nx - lastX, 0, 0);
        } else if (nx < lastX) {
            trans = new TranslateAnimation(0, (lastX - nx) * -1, 0, 0);
        } else {
            return;
        }
        trans.setDuration(300);

        trans.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) tabline1
                        .getLayoutParams();
                params.leftMargin = (int) nx;
                tabline1.setLayoutParams(params);
            }
        });
        trans.setFillEnabled(true);
        tabline1.startAnimation(trans);
        lastX = (int) nx;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_prepare:
                selectTab(0);
                break;
            case R.id.tv_waiting:
                selectTab(1);
                break;
            case R.id.tv_performing:
                selectTab(2);
                break;
            case R.id.tv_isfinish:
                selectTab(3);
                break;
        }
//        translateLine(event);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void paySuccessChangeTab(BeanMessageEvent messageEvent) {
        if (messageEvent.getType() == 0x100) {
            selectTab(1);
        } else if (messageEvent.getType() == 0x200) {
            fragmentMyAdvPrepare.getApiData();
        } else {
            selectTab(0);
        }
    }

    @Override
    public void onDestroy() {
        LogUtils.e("当前****" + "ondestroy  fragmentMyadv");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
