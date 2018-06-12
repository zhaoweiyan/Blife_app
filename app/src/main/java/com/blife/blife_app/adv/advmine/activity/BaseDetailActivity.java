package com.blife.blife_app.adv.advmine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.api.API_MyAdvPublish_Address;
import com.blife.blife_app.adv.advmine.api.API_MyadvDetail;
import com.blife.blife_app.adv.advmine.api.API_Myadv_BonusSet;
import com.blife.blife_app.adv.advmine.bean.BeanBonusSetInfo;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvDetail;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvPublishAddress;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvPublishAddressList;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advmine.bean.BeanMyadvDetailBonus;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.DateUtils;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.tools.rollviewpager.TurnImagePager;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/9/6.
 */
public class BaseDetailActivity extends BaseActivity {
    public String adv_id;
    private LinearLayout lin;
    private BeanMyadv beanAdvDetailInfo;
    private TextView tv_pubname, tv_3_range_content, tv_2_range_content, tv_1_range_content, tv_end_time, tv_start_time, tv_link_phone, tv_link_address, tv_link, tv_description1, tv_description2, tv_link_label;
    private RelativeLayout rl_down, rl_2_range_content, rl_3_range_content;
    private ArrayList<String> list = new ArrayList<>();
    private boolean isMoreExplain = false;
    private View v2, v3;
    private TextView tv_bouns_total_num, tv_bonus_total_amount, tv_send_myadv, tv_pub_title, tv_bouns_total_get, tv_bouns_total_acount, tv_bouns_quit_person, tv_bouns_quit_money;
    private LinearLayout lin_get_count;
    private LinearLayout lin_remain_money;
    private TextView tv_prize1_num, tv_prize1_account, tv_prize2_num, tv_prize2_account, tv_prize3_num, tv_prize3_account, tv_prize4_num, tv_prize4_account;
    public String totalAmount;
    private TextView tv_bonus_service_amount;
    private LinearLayout lin_link;
    private String companyLink;
    private boolean isCache = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void init() {
        showLoadingDialog();
        iniPre();
        initView();
        initClick();
        initMyadvDetail();
    }

    private void iniPre() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("adv_id")) {
            adv_id = bundle.getString("adv_id");
        } else {
            adv_id = "-1";
        }

        if (bundle != null && bundle.containsKey("isCache")) {
            isCache = bundle.getBoolean("isCache", false);
        }
        LogUtils.e("adv_id****" + adv_id);
    }

    private void initClick() {
        rl_down.setOnClickListener(new TopClick());
        lin_link.setOnClickListener(new TopClick());
    }

    /**
     * TopBar监听
     */
    class TopClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rl_down:
                    if (!isMoreExplain) {
                        tv_description1.setVisibility(View.VISIBLE);
                        tv_description2.setVisibility(View.GONE);
                    } else {
                        tv_description1.setVisibility(View.GONE);
                        tv_description2.setVisibility(View.VISIBLE);
                    }
                    isMoreExplain = !isMoreExplain;
                    break;
                case R.id.lin_link:
                    if (companyLink != null) {
                        openBrowser(companyLink);
                    }
                    break;
            }
        }
    }

    /**
     * 打开浏览器
     *
     * @param url
     */
    private void openBrowser(String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {

        //轮播图  块
        lin = (LinearLayout) findViewById(R.id.lin);
        int screenWidth = ScreenUtils.getScreenWidth(this);
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lin.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = screenWidth * 4 / 3;// 控件的高
        LogUtils.e("linearParams.height====" + linearParams.height + "**screenWidth====" + screenWidth);
        lin.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>

        //广告详情  块
        rl_down = (RelativeLayout) findViewById(R.id.rl_down);
        tv_pubname = (TextView) findViewById(R.id.tv_pubname);
        tv_pub_title = (TextView) findViewById(R.id.tv_pub_title);
        tv_description1 = (TextView) findViewById(R.id.tv_description1);
        tv_description2 = (TextView) findViewById(R.id.tv_description2);

        //商家信息 块
        lin_link = (LinearLayout) findViewById(R.id.lin_link);
        tv_link_label = (TextView) findViewById(R.id.tv_link_label);
        tv_link = (TextView) findViewById(R.id.tv_link);
        tv_link_address = (TextView) findViewById(R.id.tv_link_address);
        tv_link_phone = (TextView) findViewById(R.id.tv_link_phone);

        //投放设置 块
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        tv_1_range_content = (TextView) findViewById(R.id.tv_1_range_content);
        v2 = (View) findViewById(R.id.v2);
        tv_2_range_content = (TextView) findViewById(R.id.tv_2_range_content);
        rl_2_range_content = (RelativeLayout) findViewById(R.id.rl_2_range_content);
        v3 = (View) findViewById(R.id.v3);
        tv_3_range_content = (TextView) findViewById(R.id.tv_3_range_content);
        rl_3_range_content = (RelativeLayout) findViewById(R.id.rl_3_range_content);

        //红包设置
        tv_prize1_num = (TextView) findViewById(R.id.tv_prize1_num);
        tv_prize1_account = (TextView) findViewById(R.id.tv_prize1_account);
        tv_prize2_num = (TextView) findViewById(R.id.tv_prize2_num);
        tv_prize2_account = (TextView) findViewById(R.id.tv_prize2_account);
        tv_prize3_num = (TextView) findViewById(R.id.tv_prize3_num);
        tv_prize3_account = (TextView) findViewById(R.id.tv_prize3_account);
        tv_prize4_num = (TextView) findViewById(R.id.tv_prize4_num);
        tv_prize4_account = (TextView) findViewById(R.id.tv_prize4_account);

        tv_bouns_total_num = (TextView) findViewById(R.id.tv_bouns_total_num);
        tv_bonus_total_amount = (TextView) findViewById(R.id.tv_bonus_total_amount);
        tv_bonus_service_amount = (TextView) findViewById(R.id.tv_bonus_service_amount);
    }

    private void initMyadvDetail() {
        //广告详情
        API_MyadvDetail api_myadvDetail = new API_MyadvDetail(ACCESS_TOKEN, adv_id, isCache);
        if (isCache == false) {
            dataManager.clearCache(api_myadvDetail);
        }
        dataManager.getServiceData(api_myadvDetail);
        //投放范围
        API_MyAdvPublish_Address api_myAdvPublish_address = new API_MyAdvPublish_Address(ACCESS_TOKEN, adv_id);
        dataManager.getServiceData(api_myAdvPublish_address);
        //红包配置
        API_Myadv_BonusSet api_myadv_bonusSet = new API_Myadv_BonusSet(ACCESS_TOKEN, adv_id);
        dataManager.getServiceData(api_myadv_bonusSet);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {

                if (tag.equals(API_MyAdvPublish_Address.TAG)) {
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(BaseDetailActivity.this);
                        return;
                    }
                    LogUtils.e("投放范围****" + json);
                    BeanMyAdvPublishAddressList beanMyAdvPublishAddress = (BeanMyAdvPublishAddressList) JsonObjUItils.fromJson(json, BeanMyAdvPublishAddressList.class);
                    ArrayList<BeanMyAdvPublishAddress> beanMyAdvPublishAddressList = beanMyAdvPublishAddress.getList();
                    if (beanMyAdvPublishAddressList != null && beanMyAdvPublishAddressList.size() > 0) {
                        v2.setVisibility(View.GONE);
                        v3.setVisibility(View.GONE);
                        rl_2_range_content.setVisibility(View.GONE);
                        rl_3_range_content.setVisibility(View.GONE);
                        int[] color = new int[]{getResources().getColor(R.color.color_reset), getResources().getColor(R.color.color_fea000)};
                        if (beanMyAdvPublishAddressList.size() == 1) {
                            if (beanMyAdvPublishAddressList.get(0).getPub_address() != null && beanMyAdvPublishAddressList.get(0).getPub_range() != null) {
                                String[] str = {beanMyAdvPublishAddressList.get(0).getPub_address(), "<" + beanMyAdvPublishAddressList.get(0).getPub_range().trim() + "米"};
                                SpannableStringBuilder spannableStringBuilder = StringUtils.setDiffColorText(str, color);
                                tv_1_range_content.setText(spannableStringBuilder);
                            }

                        } else if (beanMyAdvPublishAddressList.size() == 2) {
                            v2.setVisibility(View.VISIBLE);
                            v3.setVisibility(View.GONE);
                            rl_2_range_content.setVisibility(View.VISIBLE);
                            rl_3_range_content.setVisibility(View.GONE);
                            if (beanMyAdvPublishAddressList.get(0).getPub_address() != null && beanMyAdvPublishAddressList.get(0).getPub_range() != null) {
                                String[] str = {beanMyAdvPublishAddressList.get(0).getPub_address(), "<" + beanMyAdvPublishAddressList.get(0).getPub_range().trim() + "米"};
                                SpannableStringBuilder spannableStringBuilder = StringUtils.setDiffColorText(str, color);
                                tv_1_range_content.setText(spannableStringBuilder);
                            }
                            if (beanMyAdvPublishAddressList.get(1).getPub_address() != null && beanMyAdvPublishAddressList.get(1).getPub_range() != null) {
                                String[] str = {beanMyAdvPublishAddressList.get(1).getPub_address(), "<" + beanMyAdvPublishAddressList.get(1).getPub_range().trim() + "米"};
                                SpannableStringBuilder spannableStringBuilder = StringUtils.setDiffColorText(str, color);
                                tv_2_range_content.setText(spannableStringBuilder);
                            }

                        } else if (beanMyAdvPublishAddressList.size() == 3) {
                            v2.setVisibility(View.VISIBLE);
                            v3.setVisibility(View.VISIBLE);
                            rl_2_range_content.setVisibility(View.VISIBLE);
                            rl_3_range_content.setVisibility(View.VISIBLE);

                            if (beanMyAdvPublishAddressList.get(0).getPub_address() != null && beanMyAdvPublishAddressList.get(0).getPub_range() != null) {
                                String[] str = {beanMyAdvPublishAddressList.get(0).getPub_address(), "<" + beanMyAdvPublishAddressList.get(0).getPub_range().trim() + "米"};
                                SpannableStringBuilder spannableStringBuilder = StringUtils.setDiffColorText(str, color);
                                tv_1_range_content.setText(spannableStringBuilder);
                            }
                            if (beanMyAdvPublishAddressList.get(1).getPub_address() != null && beanMyAdvPublishAddressList.get(1).getPub_range() != null) {
                                String[] str = {beanMyAdvPublishAddressList.get(1).getPub_address(), "<" + beanMyAdvPublishAddressList.get(1).getPub_range().trim() + "米"};
                                SpannableStringBuilder spannableStringBuilder = StringUtils.setDiffColorText(str, color);
                                tv_2_range_content.setText(spannableStringBuilder);
                            }
                            if (beanMyAdvPublishAddressList.get(2).getPub_address() != null && beanMyAdvPublishAddressList.get(2).getPub_range() != null) {
                                String[] str = {beanMyAdvPublishAddressList.get(1).getPub_address(), "<" + beanMyAdvPublishAddressList.get(2).getPub_range().trim() + "米"};
                                SpannableStringBuilder spannableStringBuilder = StringUtils.setDiffColorText(str, color);
                                tv_3_range_content.setText(spannableStringBuilder);
                            }
                        }
                    }
                }
                if (tag.equals(API_Myadv_BonusSet.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("红包设置显示****" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(BaseDetailActivity.this);
                        return;
                    }
                    BeanMyadvDetailBonus beanMyadvDetailBonus = (BeanMyadvDetailBonus) JsonObjUItils.fromJson(json, BeanMyadvDetailBonus.class);
                    BeanBonusSetInfo info = beanMyadvDetailBonus.getInfo();
                    if (info.getPrize_top_num() != null) {
                        tv_prize1_num.setText(info.getPrize_top_num().trim());
                    }
                    if (info.getPrize_top_per_money() != null) {
                        tv_prize1_account.setText(NumberUtils.getTwoPoint(info.getPrize_top_per_money().trim()));
                    }
                    if (info.getPrize_second_num() != null) {
                        tv_prize2_num.setText(info.getPrize_second_num().trim());
                    }
                    if (info.getPrize_second_per_money() != null) {
                        tv_prize2_account.setText(NumberUtils.getTwoPoint(info.getPrize_second_per_money().trim()));
                    }
                    if (info.getPrize_third_num() != null) {
                        tv_prize3_num.setText(info.getPrize_third_num().trim());
                    }
                    if (info.getPrize_third_per_money() != null) {
                        tv_prize3_account.setText(NumberUtils.getTwoPoint(info.getPrize_third_per_money().trim()));
                    }
                    if (info.getPrize_consolation_num() != null) {
                        tv_prize4_num.setText(info.getPrize_consolation_num().trim());
                    }
                    if (info.getPrize_consolation_per_money() != null) {
                        tv_prize4_account.setText("随机生成");
                    }
                    if (info.getPrize_total_money() != null) {
                        tv_bonus_total_amount.setText("￥" + NumberUtils.getTwoPoint(info.getPrize_total_money()));
                        getAcceptAmount(info.getPrize_total_money());
                    }

                    if (info.getPrize_service_money() != null) {
                        tv_bonus_service_amount.setText("￥" + NumberUtils.getTwoPoint(info.getPrize_service_money()));
                    }
                }
                if (tag.equals(API_MyadvDetail.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("我的广告详情**" + json);
                    if (list != null && list.size() > 0)
                        list.clear();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(BaseDetailActivity.this);
                        return;
                    }
                    //轮播图
                    BeanMyAdvDetail beanMyAdvDetail = (BeanMyAdvDetail) JsonObjUItils.fromJson(json, BeanMyAdvDetail.class);
                    beanAdvDetailInfo = beanMyAdvDetail.getInfo();
                    initGetData(beanAdvDetailInfo);
                    if (beanAdvDetailInfo.getContent() != null) {
                        List<String> turnImages = beanAdvDetailInfo.getContent().getImages();
                        if (turnImages != null && turnImages.size() > 0) {
                            for (int i = 0; i < turnImages.size(); i++) {
                                list.add(turnImages.get(i));
                            }
                            TurnImagePager turnImagePager = new TurnImagePager(BaseDetailActivity.this, list);
                            lin.removeAllViews();
                            lin.addView(turnImagePager.initView());
                            turnImagePager.initData();
                        }

                        if (!TextUtils.isEmpty(beanAdvDetailInfo.getContent().getLink_label().trim())) {
                            tv_link_label.setText(beanAdvDetailInfo.getContent().getLink_label());
                            tv_link_label.setVisibility(View.VISIBLE);
                        } else {
                            tv_link_label.setVisibility(View.GONE);
                        }

                        if (beanAdvDetailInfo.getContent().getLink() != null) {
                            tv_link.setText(beanAdvDetailInfo.getContent().getLink());
                            companyLink = beanAdvDetailInfo.getContent().getLink();
                        }

                        if (beanAdvDetailInfo.getContent().getContact_phone() != null) {
                            tv_link_phone.setText(beanAdvDetailInfo.getContent().getContact_phone());
                        }
                    }
                    if (beanAdvDetailInfo.getContact_address() != null) {
                        tv_link_address.setText(beanAdvDetailInfo.getContact_address());
                    }

                    if (beanAdvDetailInfo.getDescription() != null) {
                        tv_description1.setText(beanAdvDetailInfo.getDescription().trim());
                        tv_description2.setText(beanAdvDetailInfo.getDescription().trim());
                    }

                    if (beanAdvDetailInfo.getTitle() != null) {
                        tv_pub_title.setText(beanAdvDetailInfo.getTitle().trim());
                    }

                    if (beanAdvDetailInfo.getPub_name() != null) {
                        tv_pubname.setText(beanAdvDetailInfo.getPub_name());
                    }

                    if (beanAdvDetailInfo.getBonus_total_num() != null) {
                        tv_bouns_total_num.setText(beanAdvDetailInfo.getBonus_total_num() + "人");
                    }
                    if (beanAdvDetailInfo.getBonus_total_amount() != null) {
                        getAmount(beanAdvDetailInfo.getBonus_total_amount().trim());
                        totalAmount = beanAdvDetailInfo.getBonus_total_amount();
                    }

                    if (beanAdvDetailInfo.getPub_begin_time() != null) {
                        tv_start_time.setText(DateUtils.getTime(beanAdvDetailInfo.getPub_begin_time()));
                    }
                    if (beanAdvDetailInfo.getPub_end_time() != null) {
                        tv_end_time.setText(DateUtils.getTime(beanAdvDetailInfo.getPub_end_time()));
                    }
                    if (beanAdvDetailInfo.getPub_begin_time() != null && beanAdvDetailInfo.getPub_end_time() != null) {
                        getTime(beanAdvDetailInfo.getPub_begin_time(), beanAdvDetailInfo.getPub_end_time());
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
//                ToastUtils.showShort(BaseDetailActivity.this, JsonObjUItils.getERRORJsonDetail(message));
                cancelLoadingDialog();
            }
        };
    }

    protected void initGetData(BeanMyadv beanMyadv) {

    }

    protected void getAmount(String trim) {

    }

    protected void getTime(String startTime, String endTime) {

    }

    protected void getAcceptAmount(String accept) {

    }
}
