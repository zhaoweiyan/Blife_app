package com.blife.blife_app.adv.advmine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.activity.ActivityRefundSchedule;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvRefund;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by Somebody on 2016/9/7.
 */
public class AdapterMyadvRefund extends RecyclerView.Adapter {


    private Context context;
    private ArrayList<BeanMyAdvRefund> list;

    public AdapterMyadvRefund(Context context, ArrayList<BeanMyAdvRefund> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyadvRedundViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myadv_refund, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BeanMyAdvRefund beanMyAdvRefund = list.get(position);

        MyadvRedundViewHolder myadvRedundViewHolder = (MyadvRedundViewHolder) holder;
        if (position == 0) {
            myadvRedundViewHolder.iv_refund_tag.setBackgroundResource(R.mipmap.refund_schduel_red);
        }

        if (beanMyAdvRefund.getCreate_time() != null) {
            myadvRedundViewHolder.tv_refund_time.setText(DateFormatUtils.getTimeHStr(Long.parseLong(beanMyAdvRefund.getCreate_time().trim()) * 1000, "yyyy-MM-dd"));
        } else {
            myadvRedundViewHolder.tv_refund_time.setText("未设置");
        }


        if (beanMyAdvRefund.getRefund_status_str() != null) {
            myadvRedundViewHolder.tv_refund_explain.setText(beanMyAdvRefund.getRefund_status_str());
        } else {
            myadvRedundViewHolder.tv_refund_explain.setText("未设置");
        }
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        } else {
            return 0;
        }
    }

    private class MyadvRedundViewHolder extends RecyclerView.ViewHolder {


        private TextView tv_refund_time, tv_refund_explain;
        private ImageView iv_refund_tag;

        public MyadvRedundViewHolder(View viewitem) {
            super(viewitem);
            iv_refund_tag = (ImageView) viewitem.findViewById(R.id.iv_refund_tag);
            tv_refund_time = (TextView) viewitem.findViewById(R.id.tv_refund_time);
            tv_refund_explain = (TextView) viewitem.findViewById(R.id.tv_refund_explain);
        }
    }
}
