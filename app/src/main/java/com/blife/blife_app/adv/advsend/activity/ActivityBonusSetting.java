package com.blife.blife_app.adv.advsend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.api.API_MyadvDetail;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvDetail;
import com.blife.blife_app.adv.advmine.bean.BeanMyadvDetailBonus;
import com.blife.blife_app.adv.advsend.api.API_AdvBonus;
import com.blife.blife_app.adv.advsend.api.API_AdvGetBonus;
import com.blife.blife_app.adv.advsend.api.API_AdvSendTargetGet;
import com.blife.blife_app.adv.advsend.api.API_SubmitAdv;
import com.blife.blife_app.adv.advsend.api.API_UpdateAdvInfo;
import com.blife.blife_app.adv.advsend.bean.BeanAdvInfo;
import com.blife.blife_app.adv.advsend.bean.BeanSendTarget;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.bonus.bean.BeanMessageEvent;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.location.InterfaceLocationCallback;
import com.blife.blife_app.tools.location.LocationUtil;
import com.blife.blife_app.tools.view.MenuPopWindows;
import com.blife.blife_app.tools.view.YHPickerView;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.logcat.LogcatManager;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by w on 2016/8/30.
 */
public class ActivityBonusSetting extends BaseActivity implements View.OnClickListener, TextWatcher {

    private LinearLayout lin_bonus_sendmen, lin_bonus_sendtarget;
    private TextView tv_bonus_starttime, tv_bonus_endtime;
    private EditText et_bonus_sendmennum, et_bonus_firstmen, et_bonus_firstmoney, et_bonus_twomen, et_bonus_twomoney, et_bonus_threemen, et_bonus_threemoney;
    private TextView tv_bonus_randommen, tv_bonus_randommoney;
    private FrameLayout framelayout_bonus_preview;
    private TextView tv_bonus_savedraft, tv_bonus_submit;
    private TextView tv_sendtarget_hint;

    //时间选择
    private TextView currentTimeView;
    private MenuPopWindows timeSelectWindows;
    private YHPickerView pickerView;
    private Button button_selecttime;
    //输入框输入
    private double sendMen, firstMen, firstMoney, twoMen, twoMoney, threeMen, threeMoney;
    private double randomMen;
    //广告ID
    private String ADV_ID, ADV_AGAIN_ID, ADV_FIRST_IMAGE;
    //广告类型
    private int ADV_TYPE;
    //广告开始时间结束时间
    private String DateFormat = "yyyy-MM-dd HH";
    private long startTime = 0, endTime = 0;

    //上传位置个数结果回调
    private int REQUEST_CODE = 101;
    //成功标识
    private boolean isSubmit, BonusAPISuccess, AdvUpdateAPISuccess;

    //当前位置
    private String LocationLat = "", LocationLng = "";

    //是否上传发送位置信息
    private boolean isUpdateTarget;
    //定位
    private LocationUtil locationUtil;
    //定位权限
    private boolean LocationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_setting);
        initBackTopBar(R.string.uploadadv_bonus);
        initView();
        initData();
        initLocation();
        activityTask.addFinishActivity(this);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        lin_bonus_sendmen = (LinearLayout) findViewById(R.id.lin_bonus_sendmen);
        lin_bonus_sendtarget = (LinearLayout) findViewById(R.id.lin_bonus_sendtarget);
        tv_bonus_starttime = (TextView) findViewById(R.id.tv_bonus_starttime);
        tv_bonus_endtime = (TextView) findViewById(R.id.tv_bonus_endtime);
        et_bonus_sendmennum = (EditText) findViewById(R.id.et_bonus_sendmennum);
        et_bonus_firstmen = (EditText) findViewById(R.id.et_bonus_firstmen);
        et_bonus_firstmoney = (EditText) findViewById(R.id.et_bonus_firstmoney);
        et_bonus_twomen = (EditText) findViewById(R.id.et_bonus_twomen);
        et_bonus_twomoney = (EditText) findViewById(R.id.et_bonus_twomoney);
        et_bonus_threemen = (EditText) findViewById(R.id.et_bonus_threemen);
        et_bonus_threemoney = (EditText) findViewById(R.id.et_bonus_threemoney);
        tv_bonus_randommen = (TextView) findViewById(R.id.tv_bonus_randommen);
        tv_bonus_randommoney = (TextView) findViewById(R.id.tv_bonus_randommoney);
        framelayout_bonus_preview = (FrameLayout) findViewById(R.id.framelayout_bonus_preview);
        tv_bonus_savedraft = (TextView) findViewById(R.id.tv_bonus_savedraft);
        tv_bonus_submit = (TextView) findViewById(R.id.tv_bonus_submit);
        tv_sendtarget_hint = (TextView) findViewById(R.id.tv_sendtarget_hint);
        //监听输入事件
        et_bonus_sendmennum.addTextChangedListener(this);
        et_bonus_firstmen.addTextChangedListener(this);
        et_bonus_firstmoney.addTextChangedListener(this);
        et_bonus_twomen.addTextChangedListener(this);
        et_bonus_twomoney.addTextChangedListener(this);
        et_bonus_threemen.addTextChangedListener(this);
        et_bonus_threemoney.addTextChangedListener(this);
        //设置监听事件
        lin_bonus_sendmen.setOnClickListener(this);
        lin_bonus_sendtarget.setOnClickListener(this);
        tv_bonus_starttime.setOnClickListener(this);
        tv_bonus_endtime.setOnClickListener(this);
        framelayout_bonus_preview.setOnClickListener(this);
        tv_bonus_savedraft.setOnClickListener(this);
        tv_bonus_submit.setOnClickListener(this);

        //时间选择
        timeSelectWindows = new MenuPopWindows(instance, R.layout.pop_selectsendtime);
        timeSelectWindows.setViewDismiss(false);
        button_selecttime = (Button) timeSelectWindows.getRootView().findViewById(R.id.btn_selecttime);
        button_selecttime.setOnClickListener(this);
        pickerView = (YHPickerView) timeSelectWindows.getRootView().findViewById(R.id.pickview);
        pickerView.viewCurrentAddHour(1);
        pickerView.setOnScrollListener(new YHPickerView.onScrollListener() {
            @Override
            public void onScroll(String year, String month, String day, String hour) {
                currentTimeView.setText(year + "-" + month + "-" + day + "\t" + hour + getString(R.string.hour));
            }
        });

    }

    private void initData() {
        locationUtil = LocationUtil.getInstance(ActivityBonusSetting.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
//            ADV_ID = bundle.getString(Constants.CREATE_ADV_ID);
//            L.e("TAG", "广告ID:" + ADV_ID);
//            ADV_TYPE = bundle.getInt(Constants.ADV_CURRENT_TYPE);
//            if (ADV_TYPE == Constants.ADV_CURRENT_TYPE_DRAFT) {
//                getDraftAdvInfo();
//            }
            ADV_FIRST_IMAGE = bundle.getString(Constants.ADV_CURRENT_FIRST_IMAGE);
            L.e("TAG", "第一张图片：" + ADV_FIRST_IMAGE);
        }
        ADV_ID = shardPreferName.getStringData(Constants.ADV_CURRENT_EDITING_ID);
        L.e("TAG", "广告ID:" + ADV_ID);
        if (!TextUtils.isEmpty(ADV_ID)) {
            ADV_TYPE = shardPreferName.getIntData(Constants.ADV_CURRENT_EDITING_TYPE, 0);
            if (ADV_TYPE == Constants.ADV_CURRENT_TYPE_DRAFT) {
                L.e("TAG", "草稿--获取广告信息:" + ADV_TYPE);
                getDraftAdvInfo();
            }
        }
    }

    /**
     * 获取草稿信息
     */
    private void getDraftAdvInfo() {
        showLoadingDialog();
        API_MyadvDetail api_myadvDetail = new API_MyadvDetail(ACCESS_TOKEN, ADV_ID);
        dataManager.getServiceData(api_myadvDetail);
        API_AdvGetBonus api_advBonus = new API_AdvGetBonus(ACCESS_TOKEN, ADV_ID);
        dataManager.getServiceData(api_advBonus);
        API_AdvSendTargetGet api = new API_AdvSendTargetGet(ACCESS_TOKEN, ADV_ID);
        dataManager.getServiceData(api);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_bonus_sendmen:
                showFailedDialog(R.string.uploadadv_sendmen_hint, tv_bonus_savedraft);
                break;
            case R.id.lin_bonus_sendtarget:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CREATE_ADV_ID, ADV_ID);
                startActivityForResult(ActivitySendTarget.class, REQUEST_CODE, bundle);
                break;
            case R.id.tv_bonus_starttime:
                currentTimeView = tv_bonus_starttime;
                if (startTime != 0) {
                    setPickerViewData(startTime);
                }
                timeSelectWindows.show(currentTimeView);
                break;
            case R.id.tv_bonus_endtime:
                currentTimeView = tv_bonus_endtime;
                if (endTime != 0) {
                    setPickerViewData(endTime);
                } else if (startTime != 0) {
                    setPickerViewData(startTime + 3600);
                }
                timeSelectWindows.show(currentTimeView);
                break;
            case R.id.btn_selecttime:
                setAdvTime();
                break;
            case R.id.framelayout_bonus_preview:
                Bundle bun = new Bundle();
                bun.putString(Constants.ADV_ID, ADV_ID);
                startActivity(ActivityPreView.class, bun);
                break;
            case R.id.tv_bonus_savedraft:
                isSubmit = false;
                PostJsonData();
                break;
            case R.id.tv_bonus_submit:
//                if (LocationPermission) {
//                    isSubmit = true;
//                    PostJsonData();
//                } else {
//                    initLocation();
//                }
                isSubmit = true;
                initLocation();
                break;
        }
    }

    private void setPickerViewData(long data) {
        if (data <= 0)
            return;
        String year = DateFormatUtils.getTimeHStr(data * 1000, "yyyy");
        String month = DateFormatUtils.getTimeHStr(data * 1000, "MM");
        String day = DateFormatUtils.getTimeHStr(data * 1000, "dd");
        String hour = DateFormatUtils.getTimeHStr(data * 1000, "HH");
        if (pickerView != null)
            pickerView.setData(year, month, day, hour);
    }

    /**
     * 设置广告开始时间结束时间
     */
    private void setAdvTime() {
        currentTimeView.setText(pickerView.getCurrentYear() + "-" + pickerView.getCurrentMonth() + "-" + pickerView.getCurrentDay() + "\t" + pickerView.getCurrentHour() + getString(R.string.hour));
        timeSelectWindows.dismiss();
        String time = pickerView.getCurrentYear() + "-" + pickerView.getCurrentMonth() + "-" + pickerView.getCurrentDay() + " " + pickerView.getCurrentHour();
        if (currentTimeView.getId() == tv_bonus_starttime.getId()) {
            startTime = DateFormatUtils.getlongHTime(time, DateFormat) / 1000;
        } else {
            endTime = DateFormatUtils.getlongHTime(time, DateFormat) / 1000;
        }

    }

    /**
     * 红包数据提交
     */
    private void PostJsonData() {
        if (isSubmit) {
            if (checkTime() && checkMenNum() && checkMoney()) {
                showLoadingDialog();
                updateAdvSendTime();
                postAdvBonus();
            }
        } else {
            boolean timeTrue, moneyTrue = false;
            if (startTime <= 0 && endTime <= 0) {//没有填写投放时间信息
                AdvUpdateAPISuccess = true;
                timeTrue = true;
            } else {//填写投放信息
                if (checkTime()) {//检查信息完整性
                    timeTrue = true;
                } else {
                    timeTrue = false;
                }
            }
            if (timeTrue) {
                if (sendMen <= 0 && firstMen <= 0 && twoMen <= 0 && threeMen <= 0
                        && firstMoney <= 0 && twoMoney <= 0 && threeMoney <= 0) {//没有填写红包信息
                    BonusAPISuccess = true;
                    moneyTrue = true;
                } else {//填写红包信息
                    if (checkMenNum() && checkMoney()) {//检查信息完整性
                        moneyTrue = true;
                    } else {
                        moneyTrue = false;
                    }
                }
            }
            if (timeTrue && moneyTrue) {//时间与红包信息全部为空或者全部正确
                if (AdvUpdateAPISuccess && BonusAPISuccess) {//时间与红包信息全部为空
//                    ToastUtils.showShort(instance, R.string.uploadadv_bonus_save_success);
                    RefreshData(0x300);
                    activityTask.FinishAllAddActivity();
                } else if (!AdvUpdateAPISuccess && !BonusAPISuccess) {//时间与红包信息全部正确
                    showLoadingDialog();
                    postAdvBonus();
                    updateAdvSendTime();
                } else if (!AdvUpdateAPISuccess) {//时间信息不为空
                    showLoadingDialog();
                    updateAdvSendTime();
                } else {//红包信息不为空
                    showLoadingDialog();
                    postAdvBonus();
                }
            }
        }
    }

    /**
     * 校验时间
     */
    private boolean checkTime() {
        if (!isUpdateTarget) {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendtarget_empty, tv_bonus_savedraft);
            return false;
        }
        if (startTime <= 0) {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendstarttime_empty, tv_bonus_savedraft);
            return false;
        }
        if (endTime <= 0) {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendendtime_empty, tv_bonus_savedraft);
            return false;
        }
        long currentTime = System.currentTimeMillis() / 1000;
        if (startTime <= currentTime) {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendtime_start_error, tv_bonus_savedraft);
            return false;
        }
        if (startTime >= endTime) {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendtime_end_equals_start, tv_bonus_savedraft);
            return false;
        }
        return true;
    }

    /**
     * 校验金额
     *
     * @return
     */
    private boolean checkMoney() {
        if (firstMoney <= 0) {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendmen_error, tv_bonus_savedraft);
            return false;
        }
        if (firstMoney < 1) {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendmoney_small, tv_bonus_savedraft);
            return false;
        }
        if (twoMen != 0) {
            if (twoMoney != 0) {
                if (twoMoney < 1) {
                    showFailedDialog(R.string.toast_uploadadv_bonus_sendmoney_small, tv_bonus_savedraft);
                    return false;
                }
                if (twoMoney >= firstMoney) {
                    showFailedDialog(R.string.toast_uploadadv_bonus_twomoney_error, tv_bonus_savedraft);
                    return false;
                }
            } else {
                showFailedDialog(R.string.toast_uploadadv_bonus_sendmen_error, tv_bonus_savedraft);
                return false;
            }
        } else {
            if (twoMoney != 0) {
                if (twoMoney < 1) {
                    showFailedDialog(R.string.toast_uploadadv_bonus_sendmoney_small, tv_bonus_savedraft);
                    return false;
                }
                showFailedDialog(R.string.toast_uploadadv_bonus_sendmen_error, tv_bonus_savedraft);
                return false;
            }
        }
        if (threeMen != 0) {
            if (threeMoney != 0) {
                if (threeMoney < 1) {
                    showFailedDialog(R.string.toast_uploadadv_bonus_sendmoney_small, tv_bonus_savedraft);
                    return false;
                }
                if (threeMoney >= twoMoney) {
                    showFailedDialog(R.string.toast_uploadadv_bonus_threemoney_error, tv_bonus_savedraft);
                    return false;
                }
            } else {
                showFailedDialog(R.string.toast_uploadadv_bonus_sendmen_error, tv_bonus_savedraft);
                return false;
            }
        } else {
            if (threeMoney != 0) {
                if (threeMoney < 1) {
                    showFailedDialog(R.string.toast_uploadadv_bonus_sendmoney_small, tv_bonus_savedraft);
                    return false;
                }
                showFailedDialog(R.string.toast_uploadadv_bonus_sendmen_error, tv_bonus_savedraft);
                return false;
            }
        }
        return true;
    }

    /**
     * 校验人数
     *
     * @return
     */
    private boolean checkMenNum() {
        if (sendMen <= 0) {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendmen_empty, tv_bonus_savedraft);
            return false;
        }
        if (firstMen <= 0) {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendmen_error, tv_bonus_savedraft);
            return false;
        }
        double num = firstMen + twoMen + threeMen + randomMen;
        if (sendMen == num) {
            return true;
        } else {
            showFailedDialog(R.string.toast_uploadadv_bonus_sendmen_error, tv_bonus_savedraft);
            return false;
        }
    }

    /**
     * 上传广告红包
     */
    private void postAdvBonus() {
        BonusAPISuccess = false;
        API_AdvBonus api_advBonus = new API_AdvBonus(ACCESS_TOKEN, createBonusJsonData());
        dataManager.getServiceData(api_advBonus);
    }

    /**
     * 更新广告发送时间
     */
    private void updateAdvSendTime() {
        AdvUpdateAPISuccess = false;
        API_UpdateAdvInfo api_updateAdvInfo = new API_UpdateAdvInfo(ACCESS_TOKEN, createUpdateAdvTimeJson());
        dataManager.getServiceData(api_updateAdvInfo);
    }

    private void initLocation() {
        locationUtil.startLocation(new InterfaceLocationCallback() {
            @Override
            public void onLocationSuccess(BDLocation bdLocation) {
                LocationPermission = true;
                LocationLat = "" + bdLocation.getLatitude();
                LocationLng = "" + bdLocation.getLongitude();
                if (isSubmit)
                    PostJsonData();
            }

            @Override
            public void onLocationError() {
                LocationPermission = false;
            }

            @Override
            public void onCancelShowRationale() {
                LocationPermission = false;
            }

            @Override
            public void onDeniedDialogPositive() {
                LocationPermission = false;
            }

            @Override
            public void onDeniedDialogNegative() {
                LocationPermission = false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 提交信息
     */
    private void submitData() {
        if (BonusAPISuccess && AdvUpdateAPISuccess) {
            if (isSubmit) {
                L.e("TAG", "申请提交参数：" + ACCESS_TOKEN + "--ADV_ID" + ADV_ID + "--LAT" + LocationLat + "--LNG" + LocationLng);
                API_SubmitAdv api_submitAdv = new API_SubmitAdv(ACCESS_TOKEN, ADV_ID, LocationLat, LocationLng);
                dataManager.getServiceData(api_submitAdv);
            } else {
                cancelLoadingDialog();
                //清空编辑中的广告ID与类型
                shardPreferName.setStringData(Constants.ADV_CURRENT_EDITING_ID, "");
                shardPreferName.setIntData(Constants.ADV_CURRENT_EDITING_TYPE, 0);
                RefreshData(0x300);
                activityTask.FinishAllAddActivity();
            }
        }
    }

    private void RefreshData(int messageEventType) {
        BeanMessageEvent messageEvent = new BeanMessageEvent(messageEventType);
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_AdvBonus.TAG)) {
                    L.e("TAGXX", "红包设置成功" + json);
                    BonusAPISuccess = true;
                    submitData();
                }
                if (tag.equals(API_UpdateAdvInfo.TAG)) {
                    L.e("TAGXX", "更新广告时间成功" + json);
                    AdvUpdateAPISuccess = true;
                    submitData();
                }
                if (tag.equals(API_SubmitAdv.TAG)) {
                    cancelLoadingDialog();
                    L.e("TAGXX", "提交信息成功" + json);
                    BeanAdvInfo beanAdvInfo = (BeanAdvInfo) JsonObjUItils.fromJson(json, BeanAdvInfo.class);
                    shardPreferName.setStringData(Constants.PAYMENT_ORDER, beanAdvInfo.getInfo().getBonus_payment_order_id());
                    //清空编辑中的广告ID与类型
                    shardPreferName.setStringData(Constants.ADV_CURRENT_EDITING_ID, "");
                    shardPreferName.setIntData(Constants.ADV_CURRENT_EDITING_TYPE, 0);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.CREATE_ADV_ID, ADV_ID);
                    bundle.putString(Constants.ADV_CURRENT_TITLE, beanAdvInfo.getInfo().getTitle());
                    bundle.putString(Constants.ADV_CURRENT_FIRST_IMAGE, ADV_FIRST_IMAGE);
                    bundle.putLong(Constants.CREATE_ADV_TOTAL_AMOUNT, beanAdvInfo.getInfo().getBonus_total_amount());
                    bundle.putLong(Constants.ADV_CURRENT_START_TIME, beanAdvInfo.getInfo().getPub_begin_time());
                    bundle.putLong(Constants.ADV_CURRENT_END_TIME, beanAdvInfo.getInfo().getPub_end_time());
                    startActivity(ActivityPayment.class, bundle);
                }
                if (tag.equals(API_MyadvDetail.TAG)) {
                    cancelLoadingDialog();
                    L.e("TAG", "广告草稿JSON：" + json);
                    BeanMyAdvDetail beanMyAdvDetail = (BeanMyAdvDetail) JsonObjUItils.fromJson(json, BeanMyAdvDetail.class);
                    setDraftAdvInfo(beanMyAdvDetail);
                }
                if (tag.equals(API_AdvGetBonus.TAG)) {
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json))
                        return;
                    L.e("TAG", "调试BUG---红包配置信息JSON：" + json);
                    LogcatManager.e(instance, "Test_Bonus_API_Data", json);
                    BeanMyadvDetailBonus beanMyadvDetailBonus = (BeanMyadvDetailBonus) JsonObjUItils.fromJson(json, BeanMyadvDetailBonus.class);
                    setBonusInfo(beanMyadvDetailBonus);
                }
                if (tag.equals(API_AdvSendTargetGet.TAG)) {
                    cancelLoadingDialog();
                    L.e("TAG", "广告投放信息JSON：" + json);
                    BeanSendTarget beanSendTarget = (BeanSendTarget) JsonObjUItils.fromJson(json, BeanSendTarget.class);
                    int num = beanSendTarget.getList().size();
                    if (num > 0) {
                        isUpdateTarget = true;
                        tv_sendtarget_hint.setText(String.format(getString(R.string.uploadadv_sendtarget_hint_num), num));
                    } else {
                        isUpdateTarget = false;
                        tv_sendtarget_hint.setHint(getString(R.string.uploadadv_sendtarget_hint));
                    }
                }

            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (tag.equals(API_AdvBonus.TAG)) {
                    L.e("TAGXX", "红包设置失败" + message);
                    BonusAPISuccess = false;
                    showFailedDialog(R.string.toast_network_Available, tv_bonus_submit);
                }
                if (tag.equals(API_UpdateAdvInfo.TAG)) {
                    L.e("TAGXX", "更新广告时间失败" + message);
                    AdvUpdateAPISuccess = false;
                    showFailedDialog(R.string.toast_network_Available, tv_bonus_submit);
                }
                if (tag.equals(API_SubmitAdv.TAG)) {
                    L.e("TAGXX", "提交信息失败" + message);//{"code":"60000035","message":"USER_VERIFY_STATUS_NOT_ALLOWED","detail":"用户认证状态尚未通过"}
                    String code = JsonObjUItils.getERRORJsonCode(message);
                    if (code.equals("60000035")) {
                        showFailedDialog(getString(R.string.identify_mine_title), R.string.identify_mine, tv_bonus_submit);
                    } else {
                        showFailedDialog(R.string.toast_network_Available, tv_bonus_submit);
                    }
                }
            }
        };
    }

    /**
     * 设置红包信息
     *
     * @param beanMyadvDetailBonus
     */
    private void setBonusInfo(BeanMyadvDetailBonus beanMyadvDetailBonus) {
        if (beanMyadvDetailBonus == null)
            return;
        setMenEdit(et_bonus_sendmennum, beanMyadvDetailBonus.getInfo().getPrize_total_num());
        setMenEdit(et_bonus_firstmen, beanMyadvDetailBonus.getInfo().getPrize_top_num());
        setMenEdit(et_bonus_twomen, beanMyadvDetailBonus.getInfo().getPrize_second_num());
        setMenEdit(et_bonus_threemen, beanMyadvDetailBonus.getInfo().getPrize_third_num());
        setMenEdit(tv_bonus_randommen, beanMyadvDetailBonus.getInfo().getPrize_consolation_num());

        setBonusEdit(et_bonus_firstmoney, beanMyadvDetailBonus.getInfo().getPrize_top_per_money());
        setBonusEdit(et_bonus_twomoney, beanMyadvDetailBonus.getInfo().getPrize_second_per_money());
        setBonusEdit(et_bonus_threemoney, beanMyadvDetailBonus.getInfo().getPrize_third_per_money());
    }

    /**
     * 设置红包人数
     *
     * @param view
     * @param men
     */
    private void setMenEdit(TextView view, String men) {
        if (men.equals("0")) {
            view.setText("");
        } else {
            view.setText(men);
        }
    }

    /**
     * 设置红包金额
     *
     * @param editText
     * @param money
     */
    private void setBonusEdit(EditText editText, String money) {
        if (money.equals("0")) {
            editText.setText("");
        } else {
            editText.setText(StringUtils.dealMoney(money, 100));
        }
    }

    /**
     * 设置广告发布时间信息
     *
     * @param beanMyAdvDetail
     */
    private void setDraftAdvInfo(BeanMyAdvDetail beanMyAdvDetail) {
//        ADV_FIRST_IMAGE = beanMyAdvDetail.getInfo().getContent().getImages().get(0);
        startTime = Long.valueOf(beanMyAdvDetail.getInfo().getPub_begin_time());
        if (startTime > 0)
            tv_bonus_starttime.setText(DateFormatUtils.getTimeHStr(startTime * 1000, DateFormat) + getString(R.string.hour));
        endTime = Long.valueOf(beanMyAdvDetail.getInfo().getPub_end_time());
        if (endTime > 0)
            tv_bonus_endtime.setText(DateFormatUtils.getTimeHStr(endTime * 1000, DateFormat) + getString(R.string.hour));
    }

    /**
     * 创建红包信息Json
     *
     * @return
     */
    //{"adv_id": "400014700292788302171920", "prize_top_num": 10, "prize_top_per_money": 10000, "prize_second_num": 20,
    // "prize_second_per_money": 5000, "prize_third_num": 100, "prize_third_per_money": 1000, "prize_total_num": 1000}
    //{"adv_id": "400014700292788302171920", "prize_top_num": 10, "prize_top_per_money": 10000, "prize_second_num": 20,
    // "prize_second_per_money": 5000, "prize_third_num": 100, "prize_third_per_money": 1000, "prize_total_num": 1000}
    private String createBonusJsonData() {
        String json;
        JSONObject object = new JSONObject();
        try {
            object.put("adv_id", ADV_ID);
            object.put("prize_top_num", firstMen);
//            object.put("prize_top_per_money", firstMoney * 100);
            object.put("prize_top_per_money", getBranch(firstMoney));
            object.put("prize_second_num", twoMen);
//            object.put("prize_second_per_money", twoMoney * 100);
            object.put("prize_second_per_money", getBranch(twoMoney));
            object.put("prize_third_num", threeMen);
//            object.put("prize_third_per_money", threeMoney * 100);
            object.put("prize_third_per_money", getBranch(threeMoney));
            object.put("prize_total_num", sendMen);
            json = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            json = "";
        }
        L.e("TAG", "红包提交JSON:" + json);
        return json;
    }

    private int getBranch(double money) {
        BigDecimal bigDecimal = BigDecimal.valueOf(money).multiply(new BigDecimal(100));
        return bigDecimal.intValue();
    }

    /**
     * 更新广告发布时间
     *
     * @return
     */
    private String createUpdateAdvTimeJson() {
        String json;
        JSONObject object = new JSONObject();
        try {
            object.put("adv_id", ADV_ID);
            object.put("begin_time", startTime);
            object.put("end_time", endTime);
            json = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            json = "";
        }
        L.e("TAG", "更新广告发布时间提交JSON:" + json);
        return json;
    }


    /**
     * 获取输入的值
     */
    private void getEditInput() {
        sendMen = getEditText(et_bonus_sendmennum);
        firstMen = getEditText(et_bonus_firstmen);
        firstMoney = getEditText(et_bonus_firstmoney);
        twoMen = getEditText(et_bonus_twomen);
        twoMoney = getEditText(et_bonus_twomoney);
        threeMen = getEditText(et_bonus_threemen);
        threeMoney = getEditText(et_bonus_threemoney);
        L.e("TAG", "一等金额：" + firstMoney);
        L.e("TAG", "二等金额：" + twoMoney);
        L.e("TAG", "三等金额：" + threeMoney);
        L.e("TAG", "一等人数：" + firstMen);
        L.e("TAG", "二等人数：" + twoMen);
        L.e("TAG", "三等人数：" + threeMen);
        if (threeMen > 0 && threeMoney > 0) {
            randomMen = sendMen - firstMen - twoMen - threeMen;
            if (randomMen <= 0) {
                randomMen = 0;
            }
        } else {
            randomMen = 0;
        }
        tv_bonus_randommen.setText("" + ((int) randomMen));
    }

    /**
     * 转化输入的值
     *
     * @param editText
     * @return
     */
    private double getEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            try {
                String money = "";
                int index = text.indexOf(".");
                if (index != -1) {//有小数点
                    money = text.substring(0, text.length() > index + 3 ? index + 3 : text.length());
                } else {//没有小数点
                    money = text;
                }
                BigDecimal bigDecimal = new BigDecimal(money);
                return bigDecimal.doubleValue();
            } catch (Exception e) {
                e.printStackTrace();
                L.e("TAG", "错误：********");
                return 0;
            }
        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                int num = data.getIntExtra(Constants.SEND_TARGET_NUM, 0);
                L.e("TAG", "投放位置个数：" + num);
                if (num > 0) {
                    isUpdateTarget = true;
                    tv_sendtarget_hint.setText(String.format(getString(R.string.uploadadv_sendtarget_hint_num), num));
                    tv_sendtarget_hint.setHint("");
                } else {
                    isUpdateTarget = false;
                    tv_sendtarget_hint.setText("");
                    tv_sendtarget_hint.setHint(getString(R.string.uploadadv_sendtarget_hint));
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        getEditInput();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeSelectWindows != null)
            timeSelectWindows.dismiss();
        shardPreferName.setStringData(Constants.ADV_CURRENT_EDITING_ID, "");
        shardPreferName.setIntData(Constants.ADV_CURRENT_EDITING_TYPE, 0);
    }
}