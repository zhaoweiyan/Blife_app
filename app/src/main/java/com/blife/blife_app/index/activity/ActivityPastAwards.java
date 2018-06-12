package com.blife.blife_app.index.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.h5.ActivityWebView;
import com.blife.blife_app.index.adapter.Adapter_PastAwards;
import com.blife.blife_app.index.adapter.InterfacePastAwardsItemClick;
import com.blife.blife_app.index.api.API_PastWinnerList;
import com.blife.blife_app.index.bean.BeanPastWinner;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/9/26.
 */
public class ActivityPastAwards extends BaseActivity implements InterfacePastAwardsItemClick {

    private RecyclerView recyclerView;
    private Adapter_PastAwards adapter_pastAwards;
    private List<BeanPastWinner> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_awards);
        initBackTopBar(R.string.superbonus_past_prize);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_pastawardslist);
        recyclerView.setLayoutManager(new LinearLayoutManager(instance, LinearLayoutManager.VERTICAL, false));

        list = new ArrayList<>();
        adapter_pastAwards = new Adapter_PastAwards(instance, list);
        recyclerView.setAdapter(adapter_pastAwards);
        API_PastWinnerList api_pastWinnerList = new API_PastWinnerList(ACCESS_TOKEN);
        dataManager.getServiceData(api_pastWinnerList);
        adapter_pastAwards.setItemClick(this);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_PastWinnerList.TAG)) {
                    BeanPastWinner beanPastWinner = (BeanPastWinner) JsonObjUItils.fromJson(json, BeanPastWinner.class);
                    if (beanPastWinner != null) {
                        list.clear();
                        list.addAll(beanPastWinner.getList());
                        adapter_pastAwards.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {

            }
        };
    }

    @Override
    public void onItem(int position) {
        BeanPastWinner bean = list.get(position);
        String link = bean.getReward_page();
        if (!TextUtils.isEmpty(link)) {
            openWebView(link, bean.getEvent_name());
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

}
