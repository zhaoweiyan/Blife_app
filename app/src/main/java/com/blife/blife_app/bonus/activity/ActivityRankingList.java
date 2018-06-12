package com.blife.blife_app.bonus.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.bonus.adapter.Adapter_RankingList;
import com.blife.blife_app.bonus.api.API_BonusRankingList;
import com.blife.blife_app.bonus.bean.BeanRankingList;
import com.blife.blife_app.mine.api.API_Mine;
import com.blife.blife_app.mine.bean.BeanMine;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/9/18.
 */
public class ActivityRankingList extends BaseActivity {

    private RecyclerView recyclerView_rankingList;
    private String ADV_ID;
    private boolean ADV_END;

    private String BONUS_MONEY;
    private long BONUS_TIME;

    private List<BeanRankingList> list;
    private Adapter_RankingList adapter_rankingList;
    private BeanRankingList beanRankingList;

    //个人信息
    private BeanMine beanMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankinglist);
        initBackTopBar(R.string.bonus_ranking_list);
        recyclerView_rankingList = (RecyclerView) findViewById(R.id.recycle_rankinglist);
        recyclerView_rankingList.setLayoutManager(new LinearLayoutManager(instance, LinearLayoutManager.VERTICAL, false));
        initData();
    }

    private void initData() {
        list = new ArrayList<>();
        adapter_rankingList = new Adapter_RankingList(instance, list);
        recyclerView_rankingList.setAdapter(adapter_rankingList);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ADV_ID = bundle.getString(Constants.ADV_ID);
            ADV_END = bundle.getBoolean(Constants.ADV_TIME_END, false);
            BONUS_MONEY = bundle.getString(Constants.BONUS_GRAB_MONEY);
            BONUS_TIME = bundle.getLong(Constants.BONUS_GRAB_TIME);
        }
        LogUtils.e("advid****" + ADV_ID);
        showLoadingDialog();
        API_Mine api_mine = new API_Mine(ACCESS_TOKEN, shardPreferName.getBooleanData(Constants.IDENTITY_PASS, false));
        dataManager.getServiceData(api_mine);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_BonusRankingList.TAG)) {
                    LogUtils.e("排行榜的列表****"+json);
                    beanRankingList = (BeanRankingList) JsonObjUItils.fromJson(json, BeanRankingList.class);
                    if (beanRankingList != null && beanRankingList.getList().size() > 0) {
                        list.clear();
                        adapter(beanRankingList.getList());
                    }
                    cancelLoadingDialog();
                }
                if (tag.equals(API_Mine.TAG)) {
                    beanMine = (BeanMine) JsonObjUItils.fromJson(json, BeanMine.class);
                    if (!TextUtils.isEmpty(ADV_ID)) {//请求排行榜信息
                        API_BonusRankingList api_bonusRankingList = new API_BonusRankingList(ACCESS_TOKEN, ADV_ID, ADV_END);
                        dataManager.getServiceData(api_bonusRankingList);
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
            }
        };
    }

    private void adapter(List<BeanRankingList> lists) {
        list.clear();
        boolean hasUser = false;
        for (int i = 0; i < lists.size(); i++) {
            BeanRankingList beanRankingList = lists.get(i);
            beanRankingList.setPosition("" + (i + 1));
            if (beanRankingList.getUser_telphone().equals(beanMine.getTelphone())) {
                hasUser = true;
                beanRankingList.setUser_headimg(beanMine.getHeadimg());
                list.add(beanRankingList);
            }
        }
        if (!hasUser) {
            BeanRankingList bean = new BeanRankingList();
            bean.setPosition(beanRankingList.getRank() + "");
            bean.setUser_headimg(beanMine.getHeadimg());
            bean.setMoney(BONUS_MONEY);
            bean.setCreate_time(BONUS_TIME);
            list.add(0, bean);
        }
        list.addAll(lists);
        adapter_rankingList.notifyDataSetChanged();
    }
}
