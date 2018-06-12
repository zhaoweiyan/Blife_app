package com.blife.blife_app.mine.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.fragment.FragmentMyAdvFinished;
import com.blife.blife_app.adv.advmine.fragment.FragmentMyAdvPerforming;
import com.blife.blife_app.adv.advmine.fragment.FragmentMyAdvPrepare;
import com.blife.blife_app.adv.advmine.fragment.FragmentMyAdvWaiting;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.mine.fragment.FragmentIdentifyCitizen;
import com.blife.blife_app.mine.fragment.FragmentIdentifyCompany;
import com.blife.blife_app.tools.http.Constants;

/**
 * Created by Somebody on 2016/9/13.
 */
public class ActivityPassIdentify extends BaseActivity implements View.OnClickListener {
    private TextView identify_tv_citizen;
    private TextView identify_tv_company;
    private FrameLayout identify_fl;
    private FragmentIdentifyCitizen fragmentIdentifyCitizen;
    private FragmentIdentifyCompany fragmentIdentifyCompany;
    private int citizen_or_company = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify);
        initPre();
        initView();
        initClick();
    }

    private void initPre() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            citizen_or_company = bundle.getInt(Constants.CITIZEN_OR_COMPANY, -1);
        }
    }

    private void initClick() {
        identify_tv_citizen.setOnClickListener(this);
        identify_tv_company.setOnClickListener(this);
    }

    private void initView() {
        initBackTopBar(R.string.identify_real);
        identify_tv_citizen = (TextView) findViewById(R.id.identify_tv_citizen);
        identify_tv_company = (TextView) findViewById(R.id.identify_tv_company);
        identify_fl = (FrameLayout) findViewById(R.id.identify_fl);
        if (citizen_or_company == 1) {
            selectFragment(0);
            identify_tv_citizen.setEnabled(false);
            identify_tv_company.setEnabled(false);
        } else if (citizen_or_company == 2) {
            selectFragment(1);
            identify_tv_citizen.setBackgroundResource(R.drawable.shape_identify_citizen_uncheck);
            identify_tv_citizen.setTextColor(getResources().getColor(R.color.color_reset));
            identify_tv_company.setBackgroundResource(R.drawable.shape_identify_company_check);
            identify_tv_company.setTextColor(getResources().getColor(R.color.colorWhite));
            identify_tv_citizen.setEnabled(false);
            identify_tv_company.setEnabled(false);
        } else {
            identify_tv_citizen.setEnabled(true);
            identify_tv_company.setEnabled(true);
            selectFragment(0);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identify_tv_citizen:
                identify_tv_citizen.setBackgroundResource(R.drawable.shape_identify_citizen_check);
                identify_tv_citizen.setTextColor(getResources().getColor(R.color.colorWhite));
                identify_tv_company.setBackgroundResource(R.drawable.shape_identify_company_uncheck);
                identify_tv_company.setTextColor(getResources().getColor(R.color.color_reset));
                selectFragment(0);
                break;
            case R.id.identify_tv_company:
                identify_tv_citizen.setBackgroundResource(R.drawable.shape_identify_citizen_uncheck);
                identify_tv_citizen.setTextColor(getResources().getColor(R.color.color_reset));
                identify_tv_company.setBackgroundResource(R.drawable.shape_identify_company_check);
                identify_tv_company.setTextColor(getResources().getColor(R.color.colorWhite));
                selectFragment(1);
                break;
        }
    }

    /**
     * 切换Fragment
     */
    private void selectFragment(int position) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        switch (position) {
            case 0:
                if (fragmentIdentifyCitizen == null) {
                    fragmentIdentifyCitizen = new FragmentIdentifyCitizen();
                }
                if (!fragmentIdentifyCitizen.isAdded())
                    fragmentTransaction.replace(R.id.identify_fl, fragmentIdentifyCitizen);
                break;
            case 1:
                if (fragmentIdentifyCompany == null) {
                    fragmentIdentifyCompany = new FragmentIdentifyCompany();
                }
                if (!fragmentIdentifyCompany.isAdded())
                    fragmentTransaction.replace(R.id.identify_fl, fragmentIdentifyCompany);
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

}
