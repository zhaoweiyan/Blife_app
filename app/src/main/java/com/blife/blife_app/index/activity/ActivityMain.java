package com.blife.blife_app.index.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.fragment.FragmentMyAdv;
import com.blife.blife_app.adv.advsend.activity.ActivitySendAdv;
import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.bonus.fragment.FragmentBonus;
import com.blife.blife_app.index.fragment.FragmentHome;
import com.blife.blife_app.mine.fragment.FragmentMine;
import com.blife.blife_app.tools.alarm.AlarmServiceUtil;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.location.UploadLocationService;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/8/23.
 */
public class ActivityMain extends BaseActivity implements View.OnClickListener {
    //视图
    private ImageView iv_home, iv_bonus, iv_adv, iv_mine;
    private TextView tv_home, tv_bonus, tv_adv, tv_mine;
    private LinearLayout lin_home, lin_bonus, lin_adv, lin_mine;
    private ImageView iv_addAdv;
    //Tab切换
    private List<TextView> TabTextViews = new ArrayList<>();
    private List<ImageView> TabImageViews = new ArrayList<>();
    private int[] normalTabIcons = {R.mipmap.unbutton_home, R.mipmap.unbutton_bonus, R.mipmap.unbutton_myad, R.mipmap.unbutton_mine};
    private int[] selectTabIcons = {R.mipmap.button_home, R.mipmap.button_bonus, R.mipmap.button_myad, R.mipmap.button_mine};
    //fragment
    private FragmentHome fragmentHome;
    private FragmentBonus fragmentBonus;
    private FragmentMyAdv fragmentMyAdv;
    private FragmentMine fragmentMine;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initUploadLocation();

    }


    /**
     * 初始化视图
     */
    private void initView() {
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_bonus = (ImageView) findViewById(R.id.iv_bonus);
        iv_adv = (ImageView) findViewById(R.id.iv_adv);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_bonus = (TextView) findViewById(R.id.tv_bonus);
        tv_adv = (TextView) findViewById(R.id.tv_adv);
        tv_mine = (TextView) findViewById(R.id.tv_mine);
        lin_home = (LinearLayout) findViewById(R.id.lin_home);
        lin_bonus = (LinearLayout) findViewById(R.id.lin_bonus);
        lin_adv = (LinearLayout) findViewById(R.id.lin_adv);
        lin_mine = (LinearLayout) findViewById(R.id.lin_mine);
        iv_addAdv = (ImageView) findViewById(R.id.iv_addadv);
        lin_home.setOnClickListener(this);
        lin_bonus.setOnClickListener(this);
        lin_adv.setOnClickListener(this);
        lin_mine.setOnClickListener(this);
        iv_addAdv.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        TabTextViews.add(tv_home);
        TabTextViews.add(tv_bonus);
        TabTextViews.add(tv_adv);
        TabTextViews.add(tv_mine);
        TabImageViews.add(iv_home);
        TabImageViews.add(iv_bonus);
        TabImageViews.add(iv_adv);
        TabImageViews.add(iv_mine);
        if (BlifeApplication.jpushjump == 1) {
            selectTab(1);
        } else {
            selectTab(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_home:
                selectTab(0);
                break;
            case R.id.lin_bonus:
                selectTab(1);
                break;
            case R.id.lin_adv:
                selectTab(2);
                break;
            case R.id.lin_mine:
                selectTab(3);
                break;
            case R.id.iv_addadv:
                startActivity(ActivitySendAdv.class);
                break;
        }
    }

    /**
     * 切换标签
     *
     * @param position
     */
    private void selectTab(int position) {
        for (int i = 0; i < TabTextViews.size(); i++) {
            if (position == i) {
                TabTextViews.get(i).setTextColor(getResources().getColor(R.color.colorRedLoginNormal));
                TabImageViews.get(i).setImageResource(selectTabIcons[i]);
            } else {
                TabTextViews.get(i).setTextColor(getResources().getColor(R.color.colorBlack));
                TabImageViews.get(i).setImageResource(normalTabIcons[i]);
            }
        }
        selectFragment(position);
    }

    /**
     * 切换Fragment
     */
    private void selectFragment(int position) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                if (fragmentHome == null) {
                    fragmentHome = new FragmentHome();
                }
                if (!fragmentHome.isAdded()) {
                    fragmentTransaction.replace(R.id.fragment_content, fragmentHome);
                }
                break;
            case 1:
                if (fragmentBonus == null) {
                    fragmentBonus = new FragmentBonus();
                }
                if (!fragmentBonus.isAdded()) {
                    fragmentTransaction.replace(R.id.fragment_content, fragmentBonus);
                }
                break;
            case 2:
                if (fragmentMyAdv == null) {
                    fragmentMyAdv = new FragmentMyAdv();
                }
                if (!fragmentMyAdv.isAdded()) {
                    fragmentTransaction.replace(R.id.fragment_content, fragmentMyAdv);
                }
                break;
            case 3:
                if (fragmentMine == null) {
                    fragmentMine = new FragmentMine();
                }
                if (!fragmentMine.isAdded()) {
                    fragmentTransaction.replace(R.id.fragment_content, fragmentMine);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void initUploadLocation() {
        AlarmServiceUtil alarmServiceUtil = new AlarmServiceUtil(instance);
        alarmServiceUtil.init(UploadLocationService.class);
        alarmServiceUtil.start(Constants.UPLOAD_LOCATION_TIME);
    }

    /**
     * 两次点击退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.showShort(ActivityMain.this, R.string.one_more_time_finish);
                exitTime = System.currentTimeMillis();
            } else {
                activityTask.finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
