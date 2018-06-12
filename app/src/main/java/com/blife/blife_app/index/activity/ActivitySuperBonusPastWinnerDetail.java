package com.blife.blife_app.index.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.index.bean.BeanPastWinner;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.util.StringUtils;

/**
 * Created by w on 2016/9/23.
 */
public class ActivitySuperBonusPastWinnerDetail extends BaseActivity {


    private FrameLayout framelayout_topleft;
    private TextView tv_top_title;
    private ImageView iv_detail_image;
    private TextView tv_pastwinner_activity_name;
    private TextView tv_pastwinner_name;
    private TextView tv_pastwinner_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_winner_detail);
//        initView();
//        initData();
    }

    private void initView() {
        framelayout_topleft = (FrameLayout) findViewById(R.id.framelayout_topleft);
        tv_top_title = (TextView) findViewById(R.id.tv_top_title);
        iv_detail_image = (ImageView) findViewById(R.id.iv_detail_image);
        tv_pastwinner_activity_name = (TextView) findViewById(R.id.tv_pastwinner_activity_name);
        tv_pastwinner_name = (TextView) findViewById(R.id.tv_pastwinner_name);
        tv_pastwinner_money = (TextView) findViewById(R.id.tv_pastwinner_money);
        framelayout_topleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            BeanPastWinner beanPastWinner = (BeanPastWinner) bundle.getSerializable(Constants.SUPER_BONUS_BEAN);
            if (beanPastWinner != null) {
                setPastWinnnerData(beanPastWinner);
            }
        }
    }

    private void setPastWinnnerData(BeanPastWinner bean) {
        if (bean.getImages() != null && bean.getImages().size() > 0)
            ImageLoader.getInstance().loadImage(bean.getImages().get(0), iv_detail_image, true);
        String text = getResources().getString(R.string.superbonus_activity_name) + bean.getEvent_name();
        tv_top_title.setText(text);
        tv_pastwinner_activity_name.setText(text);
        tv_pastwinner_name.setText(getResources().getString(R.string.superbonus_activity_man) + "：" + bean.getReward_users().get(0));
        tv_pastwinner_money.setText(getResources().getString(R.string.superbonus_activity_money) + "：￥" + StringUtils.dealMoney(bean.getBonus_money(), 100));
    }

}
