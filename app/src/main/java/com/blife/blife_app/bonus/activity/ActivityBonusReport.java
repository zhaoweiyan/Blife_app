package com.blife.blife_app.bonus.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.bonus.adapter.Adapter_ReportTable;
import com.blife.blife_app.bonus.adapterintreface.InterfaceReportAdapterOnItemClick;
import com.blife.blife_app.bonus.api.API_Bonus_Report;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/9/19.
 */
public class ActivityBonusReport extends BaseActivity implements View.OnClickListener, InterfaceReportAdapterOnItemClick {

    private GridView gridview_table;
    private Button button_submit;
    private List<String> list;
    private Adapter_ReportTable adapter_reportTable;
    private String cause;//当前原因
    private String ADV_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_report);
        initBackTopBar(R.string.bonus_report);
        initView();
        initData();
    }

    private void initView() {
        gridview_table = (GridView) findViewById(R.id.gridview_table);
        button_submit = (Button) findViewById(R.id.button_submit);
        button_submit.setOnClickListener(this);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ADV_ID = bundle.getString(Constants.ADV_ID);
        }
        list = shardPreferName.getStringList(Constants.ACCUSATION_OPTIONS);
        cause = list.get(0);
        adapter_reportTable = new Adapter_ReportTable(instance, list);
        gridview_table.setAdapter(adapter_reportTable);
        adapter_reportTable.setOnItemClick(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit:
                submitData();
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        cause = list.get(position);
    }

    private void submitData() {
        if (TextUtils.isEmpty(ADV_ID))
            return;
        API_Bonus_Report api_bonus_report = new API_Bonus_Report(ACCESS_TOKEN, ADV_ID, cause);
        dataManager.getServiceData(api_bonus_report);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_Bonus_Report.TAG)) {
                    ToastUtils.showShort(instance, R.string.bonus_details_submit_success);
                    finish();
                }
            }

            @Override
            public void onError(Object tag, String message) {

            }
        };
    }
}
