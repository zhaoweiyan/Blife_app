package com.blife.blife_app.bonus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.bonus.adapter.AdapterBonusViewPager;
import com.blife.blife_app.bonus.api.API_Bonus_AlreadyParticipate;
import com.blife.blife_app.bonus.bean.BeanAlreadyParticipate;
import com.blife.blife_app.bonus.bean.BeanMessageEvent;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.utils.encryption.EncryptionUtils;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/8/25.
 */
public class FragmentBonus extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private LinearLayout lin_notParticipate, lin_alreadyParticipate;
    private TextView tv_notParticipate_title, tv_alreadyParticipate_title;
    private TextView tv_notParticipate_num, tv_alreadyParticipate_num;
    private View view_line;
    //切换
    private int currentIndex = 0;
    private int ScreenWidth;
    private TranslateAnimation translateAnimation;

    //Fragment
    private FragmentNotParticipate fragmentNotParticipate;
    private FragmentAlreadyParticipate fragmentAlreadyParticipate;

    //
    private AdapterBonusViewPager adapterBonusViewPager;
    private List<Fragment> fragmentList;
    private ViewPager viewpager_bonus;

    @Override
    public int getLayout() {
        return R.layout.fragment_bonus;
    }

    @Override
    public void init() {
        initTopBar(R.string.bonus_title);
        initView();
        initData();
//        initViewPager();
    }

    private void initView() {
        lin_notParticipate = (LinearLayout) rootView.findViewById(R.id.lin_bonus_not_participate);
        lin_alreadyParticipate = (LinearLayout) rootView.findViewById(R.id.lin_bonus_already_participate);
        tv_notParticipate_title = (TextView) rootView.findViewById(R.id.tv_bonus_not_participate_title);
        tv_alreadyParticipate_title = (TextView) rootView.findViewById(R.id.tv_bonus_already_participate_title);
        tv_notParticipate_num = (TextView) rootView.findViewById(R.id.tv_bonus_notparticipate_num);
        tv_alreadyParticipate_num = (TextView) rootView.findViewById(R.id.tv_bonus_alreadyparticipate_num);
        view_line = rootView.findViewById(R.id.view_bonus_line);
        viewpager_bonus = (ViewPager) rootView.findViewById(R.id.viewpager_bonus);
        lin_notParticipate.setOnClickListener(this);
        lin_alreadyParticipate.setOnClickListener(this);
    }

    private void initData() {
        ScreenWidth = ScreenUtils.getScreenWidth(instance);
        changeFragment(0);
        EventBus.getDefault().register(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_bonus_not_participate:
                changeFragment(0);
                break;
            case R.id.lin_bonus_already_participate:
                changeFragment(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeLine(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changeFragment(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        LogUtils.e("position****" + position);
        switch (position) {
            case 0:
                if (fragmentNotParticipate == null) {
                    fragmentNotParticipate = new FragmentNotParticipate();
                }

                transaction.replace(R.id.frame_bonus_content, fragmentNotParticipate);
                break;
            case 1:
                if (fragmentAlreadyParticipate == null) {
                    fragmentAlreadyParticipate = new FragmentAlreadyParticipate();
                }
                transaction.replace(R.id.frame_bonus_content, fragmentAlreadyParticipate);
                break;
        }
        transaction.commitAllowingStateLoss();
        changeLine(position);
    }

    private void changeLine(int position) {
        if (position == currentIndex) return;
        if (position == 0) {
            currentIndex = 0;
            translateAnimation = new TranslateAnimation(ScreenWidth / 2, 0, 0, 0);
            tv_notParticipate_title.setTextColor(getResources().getColor(R.color.colorRedLoginNormal));
            tv_alreadyParticipate_title.setTextColor(getResources().getColor(R.color.colorLoginAgreement));
            tv_alreadyParticipate_num.setBackgroundResource(R.drawable.shape_round_gray);
            tv_notParticipate_num.setBackgroundResource(R.drawable.shape_nor_participate_red);
        } else {
            currentIndex = 1;
            translateAnimation = new TranslateAnimation(0, ScreenWidth / 2, 0, 0);
            tv_notParticipate_title.setTextColor(getResources().getColor(R.color.colorLoginAgreement));
            tv_alreadyParticipate_title.setTextColor(getResources().getColor(R.color.colorRedLoginNormal));
            tv_alreadyParticipate_num.setBackgroundResource(R.drawable.shape_nor_participate_red);
            tv_notParticipate_num.setBackgroundResource(R.drawable.shape_round_gray);
        }
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);
        view_line.startAnimation(translateAnimation);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void setNotParticipateNum(BeanMessageEvent messageEvent) {
        if (tv_notParticipate_num != null && messageEvent.getType() == 1) {
            tv_notParticipate_num.setText(messageEvent.getMsg());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void setAlreadyParticipateNum(BeanMessageEvent messageEvent) {
        if (tv_alreadyParticipate_num != null && messageEvent.getType() == 2) {
            tv_alreadyParticipate_num.setText(messageEvent.getMsg());
        }
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
