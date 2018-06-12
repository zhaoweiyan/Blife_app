package com.blife.blife_app.index.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.h5.ActivityWebView;
import com.blife.blife_app.index.adapter.Adapter_SuperBonus_PastWinner;
import com.blife.blife_app.index.adapter.InterfacePartWinnerOnItemClick;
import com.blife.blife_app.index.api.API_PastWinnerList;
import com.blife.blife_app.index.api.API_SuperBonusDetail;
import com.blife.blife_app.index.api.API_SuperBonusReward;
import com.blife.blife_app.index.api.API_SuperBonus_Join;
import com.blife.blife_app.index.bean.BeanJoinedSuccess;
import com.blife.blife_app.index.bean.BeanPastWinner;
import com.blife.blife_app.index.bean.BeanSuperBonusDetail;
import com.blife.blife_app.index.bean.BeanSuperBonusReward;
import com.blife.blife_app.index.view.KeyWordEditLayout;
import com.blife.blife_app.index.view.KeyWordsImageView;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.share.ShareUtils;
import com.blife.blife_app.tools.view.MenuPopWindows;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/9/22.
 */
public class ActivitySuperBonusDetail extends BaseActivity implements View.OnClickListener, InterfacePartWinnerOnItemClick {

    private TextView tv_superbonus_time;
    private TextView tv_title, tv_money, tv_mans;
    private TextView tv_info, tv_info_company;
    private Button button_participate, button_winner, button_pastprize, button_share;
    //信息
    private String Event_ID;
    private String keyword;
    //开始时间
    private long end_time;
    //分享
    private ShareUtils shareUtils;

    //keyWords
    private MenuPopWindows dialog;
    private TextView tv_dialog_refresh;
    private TextView tv_dialog_cancel, tv_dialog_confirm;
    private KeyWordsImageView keywordsview;
    private KeyWordEditLayout keyWordEditLayout;

    //往期得主
    private MenuPopWindows pastWinnerPop;
    private RecyclerView recyclerView_pastWinner;
    private List<BeanPastWinner> beanPastWinnerList;
    private Adapter_SuperBonus_PastWinner adapter_superBonus_pastWinner;

    //是否参与标识
    private String IsJoined_Flag;

    //参与成功弹窗
    private MenuPopWindows joinSuccessPop;
    private TextView tv_activity_number;
    private ObjectAnimator animator;

    //广告图片弹窗
    private boolean showed;
    private Handler handler;
    private String dialogImageUrl;
    private MenuPopWindows advImageDialog;
    private ImageView iv_superbonus_advimg;

    //超级大红包状态
    private int STATE;
    private final int STATE_JOIN = 0x1;
    private final int STATE_CASH = 0x2;
    private TextView tv_superbous_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superbonus_detail);
        initBackTopBar(R.string.index_super_bonus, R.string.superbonus_past_winners);
        initView();
        initData();
        initPastWinner();
        initKeywordsDialog();
        initJoinedSuccessDialog();
        initADVImageDialog();
        if (!TextUtils.isEmpty(Event_ID)) getSuperBonusDetail();
    }

    private void initView() {
        tv_superbonus_time = (TextView) findViewById(R.id.tv_superbonus_time);
        tv_title = (TextView) findViewById(R.id.tv_superbonus_title);
        tv_money = (TextView) findViewById(R.id.tv_superbonus_money);
        tv_mans = (TextView) findViewById(R.id.tv_superbonus_mans);
        tv_info = (TextView) findViewById(R.id.tv_superbonus_info);
        tv_info_company = (TextView) findViewById(R.id.tv_superbonus_info_company);
        button_participate = (Button) findViewById(R.id.button_superbonus_participate);
        button_winner = (Button) findViewById(R.id.button_superbonus_winner);
        button_pastprize = (Button) findViewById(R.id.button_superbonus_pastprize);
        button_share = (Button) findViewById(R.id.button_superbonus_share);

        button_participate.setOnClickListener(this);
        button_winner.setOnClickListener(this);
        button_pastprize.setOnClickListener(this);
        button_share.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Event_ID = bundle.getString(Constants.SUPER_BONUS_EVENT_ID);
            dialogImageUrl = bundle.getString(Constants.SUPER_BONUS_RANDOM_IMAGE);
            L.e("TAG", "随机取数字：" + dialogImageUrl);
        }
        shareUtils = new ShareUtils(instance, baseActivity);
    }

    private void initPastWinner() {
        pastWinnerPop = new MenuPopWindows(instance, R.layout.pop_superbonus_pastwinner);
        View rootView = pastWinnerPop.getRootView();
        recyclerView_pastWinner = (RecyclerView) rootView.findViewById(R.id.recycle_superbonus);
        tv_superbous_history = (TextView) rootView.findViewById(R.id.tv_superbous_history);//暂无往期得主
        recyclerView_pastWinner.setLayoutManager(new LinearLayoutManager(instance, LinearLayoutManager.HORIZONTAL, false));
        beanPastWinnerList = new ArrayList<>();
        adapter_superBonus_pastWinner = new Adapter_SuperBonus_PastWinner(instance, beanPastWinnerList);
        recyclerView_pastWinner.setAdapter(adapter_superBonus_pastWinner);
        adapter_superBonus_pastWinner.setInterfacePartWinnerOnItemClick(this);
    }


    private void initKeywordsDialog() {
        View rootView = LayoutInflater.from(instance).inflate(R.layout.pop_superbonus_keywords, null);
        dialog = new MenuPopWindows(instance, rootView);
        dialog.setViewDismiss(false);
        dialog.setViewFocusable(true);
        tv_dialog_refresh = (TextView) rootView.findViewById(R.id.tv_superbonus_dialog_refresh);
        tv_dialog_cancel = (TextView) rootView.findViewById(R.id.tv_superbonus_dialog_cancel);
        tv_dialog_confirm = (TextView) rootView.findViewById(R.id.tv_superbonus_dialog_confirm);
        tv_dialog_refresh.setOnClickListener(this);
        tv_dialog_cancel.setOnClickListener(this);
        tv_dialog_confirm.setOnClickListener(this);
        keywordsview = (KeyWordsImageView) rootView.findViewById(R.id.keywordsview);
        keyWordEditLayout = (KeyWordEditLayout) rootView.findViewById(R.id.keywordlayout);
    }

    private void initJoinedSuccessDialog() {
        View view = LayoutInflater.from(instance).inflate(R.layout.pop_animation_join_success, null);
        joinSuccessPop = new MenuPopWindows(instance, view);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.lin_rootview);
        tv_activity_number = (TextView) view.findViewById(R.id.tv_activity_number);
        int height = ScreenUtils.getScreenHeight(instance);
        animator = ObjectAnimator.ofFloat(linearLayout, "translationY", -height, -height / 4);
        animator.setDuration(1000);

    }

    @Override
    public void onItem(int position) {
        BeanPastWinner bean = beanPastWinnerList.get(position);
        if (!TextUtils.isEmpty(bean.getReward_page())) {
            openWebView(bean.getReward_page(), bean.getEvent_name());
        }
    }

    private void initADVImageDialog() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        if (advImageDialog != null && advImageDialog.isShowing())
                            advImageDialog.dismiss();
                        break;
                }
            }
        };
        advImageDialog = new MenuPopWindows(instance, R.layout.pop_superbonus_dialog);
        advImageDialog.setViewDismiss(false);
        iv_superbonus_advimg = (ImageView) advImageDialog.getRootView().findViewById(R.id.iv_superbonus_adv_img);
        ImageLoader.getInstance().loadImage(dialogImageUrl, iv_superbonus_advimg, true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (tv_superbonus_time != null && !showed) {
            showed = true;
            advImageDialog.show(tv_superbonus_time);
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    /**
     * 打开网页
     *
     * @param link
     * @param title
     */
    private void openWebView(String link, String title) {
        if (!TextUtils.isEmpty(link)) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.H5_URL_TAG, link);
            bundle.putString(Constants.H5_TITLE_TAG, title);
            startActivity(ActivityWebView.class, bundle);
        } else {
            ToastUtils.showShort(instance, R.string.index_link_empty_open_error);
        }
    }

    @Override
    protected void TopRightClick() {
        super.TopRightClick();
        getSuperBonusHistory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_superbonus_participate:
                dealJoinAndCash();
                break;
            case R.id.button_superbonus_winner:

                break;
            case R.id.button_superbonus_pastprize:
                startActivity(ActivityPastAwards.class);
                break;
            case R.id.button_superbonus_share:
                shareUtils.showSharePopWindow(button_share);
                break;
            case R.id.tv_superbonus_dialog_cancel:
                if (dialog != null)
                    dialog.dismiss();
                keyWordEditLayout.clear();
                break;
            case R.id.tv_superbonus_dialog_confirm:
                join();
                break;
            case R.id.tv_superbonus_dialog_refresh:
                keywordsview.refresh();
                break;
        }
    }

    /**
     * 处理参与&兑奖
     */
    private void dealJoinAndCash() {
        switch (STATE) {
            case STATE_CASH:
                showLoadingDialog();
                API_SuperBonusReward api_superBonusReward = new API_SuperBonusReward(ACCESS_TOKEN, Event_ID);
                dataManager.getServiceData(api_superBonusReward);
                break;
            case STATE_JOIN:
                if (isJoined()) {
                    String id = shardPreferName.getStringData(Constants.SUPER_BONUS_TICKET_ID);
                    tv_activity_number.setText(id);
                    joinSuccessPop.show(button_participate);
                    animator.start();
                } else {
                    keywordsview.refresh();
                    dialog.show(button_participate);
                }
                break;
        }
    }

    private void getSuperBonusDetail() {
        showLoadingDialog();
        API_SuperBonusDetail api_superBonusDetail = new API_SuperBonusDetail(ACCESS_TOKEN, Event_ID);
        dataManager.getServiceData(api_superBonusDetail);
    }

    private void getSuperBonusHistory() {
        API_PastWinnerList api_pastWinnerList = new API_PastWinnerList(ACCESS_TOKEN);
        dataManager.getServiceData(api_pastWinnerList);
    }


    /**
     * 参与超级大红包
     */
    private void join() {
        String key = keyWordEditLayout.getInputText();
        if (TextUtils.isEmpty(key)) {
            if (dialog != null)
                dialog.dismiss();
            keyWordEditLayout.clear();
            showFailedDialog(R.string.superbonus_dialogkeywords_hint, button_participate);
        } else if (TextUtils.isEmpty(Event_ID)) {
            //
        } else if (key.equals(keyword)) {
            if (dialog != null)
                dialog.dismiss();
            API_SuperBonus_Join api_superBonus_join = new API_SuperBonus_Join(ACCESS_TOKEN, Event_ID, key);
            dataManager.getServiceData(api_superBonus_join);
        } else {
            if (dialog != null)
                dialog.dismiss();
            keyWordEditLayout.clear();
            showFailedDialog(R.string.superbonus_dialogkeywords_error, button_participate);
        }
    }

    /**
     * 设置红包信息
     *
     * @param beanSuperBonusDetail
     */
    private void setSuperBonusDetail(BeanSuperBonusDetail beanSuperBonusDetail) {
        BeanSuperBonusDetail bean = beanSuperBonusDetail.getInfo();
        IsJoined_Flag = bean.getEvent_id() + shardPreferName.getStringData(Constants.CACHE_USER_ACCOUNT_KEY);
        tv_superbonus_time.setText(bean.getEnd_time_str());
        tv_title.setText(bean.getEvent_name());
        tv_money.setText("￥" + StringUtils.dealMoney(bean.getBonus_money(), 100));
        tv_mans.setText(getString(R.string.superbonus_participate_mans) + bean.getUser_count() + getString(R.string.superbonus_man));
        tv_info.setText(bean.getDescription());
        tv_info_company.setText(bean.getMerchant_name());
        end_time = bean.getBegin_time();
        keyword = bean.getKeyword();
        keywordsview.setKeyWord(keyword);
        keyWordEditLayout.setKeyWordLength(keyword.length());
        if (bean.getEnd_time() <= beanSuperBonusDetail.getCurrent_time()) {//已结束
            if (bean.getReward_users().size() > 0) {//已开奖
                STATE = STATE_CASH;
                button_participate.setBackgroundResource(R.drawable.shape_yellow);
                button_participate.setText(R.string.superbonus_ending);
                button_participate.setTextColor(getResources().getColor(R.color.color_reset));
                button_participate.setEnabled(true);
            } else {//未开奖
                button_participate.setBackgroundResource(R.drawable.shape_noclick);
                button_participate.setText(R.string.superbonus_opening);
                button_participate.setTextColor(getResources().getColor(R.color.colorWhite));
                button_participate.setEnabled(false);
            }
        } else {//未结束
            STATE = STATE_JOIN;
            if (isJoined()) {
                button_participate.setBackgroundResource(R.drawable.shape_superbonus);
                button_participate.setTextColor(getResources().getColor(R.color.colorWhite));
                button_participate.setText(R.string.superbonus_joined);
                button_participate.setEnabled(true);
            } else {
                button_participate.setBackgroundResource(R.drawable.shape_yellow);
                button_participate.setTextColor(getResources().getColor(R.color.color_reset));
                button_participate.setText(R.string.superbonus_goto_participate);
                button_participate.setEnabled(true);
            }
        }
        shareUtils.setShareTitle(bean.getEvent_name());
        shareUtils.setShareText(shardPreferName.getStringData(Constants.SHARE_INFO_SUPER_BONUS_DESCRIPTION).replace(Constants.SHARE_INFO_DESCRIPTION_REPLACE_MONEY, StringUtils.dealMoney(bean.getBonus_money(), 100)));
        shareUtils.setShareImagePath(bean.getImages().get(0));
        shareUtils.setShareLink(shardPreferName.getStringData(Constants.SHARE_INFO_LINK));
    }

    private boolean isJoined() {
        String values = shardPreferName.getStringData(Constants.SUPER_BONUS_JOINED);
        if (values.equals(IsJoined_Flag))
            return true;
        else
            return false;
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_SuperBonusDetail.TAG)) {
                    L.e("TAG", "BUG调试---取消加载框：" + tag);
                    cancelLoadingDialog();
                    BeanSuperBonusDetail bonusDetail = (BeanSuperBonusDetail) JsonObjUItils.fromJson(json, BeanSuperBonusDetail.class);
                    if (bonusDetail != null)
                        setSuperBonusDetail(bonusDetail);
                }
                if (tag.equals(API_SuperBonus_Join.TAG)) {
                    //新加的票据号：ticket_id
                    //{"code":"00000001","message":"OK","detail":"OK","data":{"result":"success","ticket_id":"880014749434426138211164"}}
                    BeanJoinedSuccess beanJoinedSuccess = (BeanJoinedSuccess) JsonObjUItils.fromJson(json, BeanJoinedSuccess.class);
                    shardPreferName.setStringData(Constants.SUPER_BONUS_TICKET_ID, beanJoinedSuccess.getTicket_id());
                    tv_activity_number.setText(beanJoinedSuccess.getTicket_id());
                    joinSuccessPop.show(button_participate);
                    animator.start();
                    button_participate.setBackgroundResource(R.drawable.shape_superbonus);
                    button_participate.setTextColor(getResources().getColor(R.color.colorWhite));
                    button_participate.setText(R.string.superbonus_joined);
                    shardPreferName.setStringData(Constants.SUPER_BONUS_JOINED, IsJoined_Flag);
                    getSuperBonusDetail();
                }
                if (tag.equals(API_PastWinnerList.TAG)) {
                    BeanPastWinner beanPastWinner = (BeanPastWinner) JsonObjUItils.fromJson(json, BeanPastWinner.class);
                    if (beanPastWinner != null) {
                        if (beanPastWinner.getList().size() <= 0) {
//                            showFailedDialog(getString(R.string.superbonus_past_winners), R.string.superbonus_past_winners_empty, button_participate);
                            tv_superbous_history.setVisibility(View.VISIBLE);
                            recyclerView_pastWinner.setVisibility(View.INVISIBLE);
                            pastWinnerPop.show(button_participate);
                        } else {
                            tv_superbous_history.setVisibility(View.INVISIBLE);
                            recyclerView_pastWinner.setVisibility(View.VISIBLE);
                            beanPastWinnerList.clear();
                            beanPastWinnerList.addAll(beanPastWinner.getList());
                            adapter_superBonus_pastWinner.notifyDataSetChanged();
                            pastWinnerPop.show(button_participate);
                        }
                    }
                }
                if (tag.equals(API_SuperBonusReward.TAG)) {
                    cancelLoadingDialog();
                    BeanSuperBonusReward beanSuperBonusReward = (BeanSuperBonusReward) JsonObjUItils.fromJson(json, BeanSuperBonusReward.class);
                    if (beanSuperBonusReward != null) {
                        Bundle bundle = new Bundle();
                        if (beanSuperBonusReward.getReward_info().isJoined()) {//已参与
                            if (beanSuperBonusReward.getReward_info().getReward_amount() > 0) {//中奖
                                bundle.putInt(Constants.SUPER_BONUS_STATE, Constants.SUPER_BONUS_STATE_WINNING);
                                bundle.putString(Constants.SUPER_BONUS_STATE_WINNING_MONEY, beanSuperBonusReward.getReward_info().getReward_amount() + "");
                            } else {//未中奖
                                bundle.putInt(Constants.SUPER_BONUS_STATE, Constants.SUPER_BONUS_STATE_NO_CASH);
                            }
                        } else {//未参与
                            bundle.putInt(Constants.SUPER_BONUS_STATE, Constants.SUPER_BONUS_STATE_NO_JOIN);
                        }
                        startActivity(ActivitySuperBonusReward.class, bundle);
                    }
                }

            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (tag.equals(API_SuperBonus_Join.TAG)) {
                    cancelLoadingDialog();
                    String code = JsonObjUItils.getERRORJsonCode(message);
                    if (code.equals("21000002")) {//已参与提示
                        button_participate.setBackgroundResource(R.drawable.shape_superbonus);
                        button_participate.setTextColor(getResources().getColor(R.color.colorWhite));
                        button_participate.setText(R.string.superbonus_joined);
                    }
                }
                if (tag.equals(API_SuperBonusReward.TAG)) {
                    cancelLoadingDialog();
                    JsonObjUItils.getJsonCode(message, button_participate, instance, R.string.get_filer);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (advImageDialog != null && advImageDialog.isShowing())
            advImageDialog.dismiss();
    }
}
