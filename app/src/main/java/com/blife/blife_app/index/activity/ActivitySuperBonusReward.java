package com.blife.blife_app.index.activity;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.util.StringUtils;

/**
 * Created by w on 2016/10/18.
 */
public class ActivitySuperBonusReward extends BaseActivity {

    private TextView tv_reward_title, tv_reward_message;
    private ImageView iv_reward_icon;

    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superbonus_reward);
        initBackTopBar(R.string.superbonus_reward_title);
        initView();
        initTextType();
        initData();
    }

    private void initView() {
        tv_reward_title = (TextView) findViewById(R.id.tv_reward_title);
        tv_reward_message = (TextView) findViewById(R.id.tv_reward_message);
        iv_reward_icon = (ImageView) findViewById(R.id.iv_reward_icon);
    }

    private void initTextType() {
        try {
            typeface = Typeface.createFromAsset(getAssets(), "font/superbonus_reward.ttf");
            tv_reward_title.setTypeface(typeface, Typeface.ITALIC);
            tv_reward_message.setTypeface(typeface, Typeface.ITALIC);
//            tv_reward_title.setTypeface(typeface);
//            tv_reward_message.setTypeface(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int state = bundle.getInt(Constants.SUPER_BONUS_STATE);
            switch (state) {
                case Constants.SUPER_BONUS_STATE_NO_JOIN:
                    viewNoJoin();
                    break;
                case Constants.SUPER_BONUS_STATE_NO_CASH:
                    viewNoCash();
                    break;
                case Constants.SUPER_BONUS_STATE_WINNING:
                    String money = bundle.getString(Constants.SUPER_BONUS_STATE_WINNING_MONEY);
                    String rewardMoney = StringUtils.dealMoney(money, 100);
                    int dex = Integer.valueOf(rewardMoney.substring(rewardMoney.indexOf(".") + 1));
                    if (dex > 0) {
                        viewWinning(rewardMoney);
                    } else {
                        viewWinning(rewardMoney.substring(0, rewardMoney.indexOf(".")));
                    }
                    break;
            }
        }
    }


    private void viewNoJoin() {
        tv_reward_title.setText(R.string.superbonus_no_join_failed_title);
        Drawable drawable = getResources().getDrawable(R.mipmap.superbonus_reward_failed_top);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_reward_title.setCompoundDrawables(drawable, null, null, null);
        tv_reward_message.setText(R.string.superbonus_no_join_failed_message);
        iv_reward_icon.setImageResource(R.mipmap.superbonus_reward_bottom_failed);
    }

    private void viewNoCash() {
        tv_reward_title.setText(R.string.superbonus_joined_failed_title);
        Drawable drawable = getResources().getDrawable(R.mipmap.superbonus_reward_failed_top);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_reward_title.setCompoundDrawables(drawable, null, null, null);
        tv_reward_message.setText(R.string.superbonus_joined_failed_message);
        iv_reward_icon.setImageResource(R.mipmap.superbonus_reward_bottom_failed);
    }

    private void viewWinning(String money) {
        tv_reward_title.setText(R.string.superbonus_joined_winning_title);
        Drawable drawable = getResources().getDrawable(R.mipmap.superbonus_reward_winner_top);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_reward_title.setCompoundDrawables(drawable, null, null, null);
        tv_reward_message.setText(money + getString(R.string.superbonus_money_units));
        iv_reward_icon.setImageResource(R.mipmap.superbonus_reward_bottom_winner);
    }
}
