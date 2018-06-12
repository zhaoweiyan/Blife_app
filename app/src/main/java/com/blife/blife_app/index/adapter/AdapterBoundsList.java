package com.blife.blife_app.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.index.bean.BeanBonus;
import com.blife.blife_app.utils.util.DateFormatUtils;

import java.util.List;

/**
 * Created by w on 2016/8/29.
 */
public class AdapterBoundsList extends RecyclerView.Adapter {


    private Context context;
    private List<BeanBonus> list;

    public AdapterBoundsList(Context context, List<BeanBonus> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BoundsViewHolde(LayoutInflater.from(context).inflate(R.layout.item_boundslist, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BoundsViewHolde boundsViewHolde = (BoundsViewHolde) holder;
        BeanBonus bounds = list.get(position);
        boundsViewHolde.tv_boundsmoney.setText("￥" + bounds.getPrize_top_money());
        String TIME_MODE = "yyyy-MM-dd\nHH:mm:ss";
        String time = DateFormatUtils.getTimeHStr(bounds.getPub_begin_time() * 1000, TIME_MODE);
        boundsViewHolde.tv_boundstime.setText(time);
        if (bounds.getPub_begin_time() * 1000 > System.currentTimeMillis()) {
            boundsViewHolde.tv_boundsstate.setText("准备中");
        } else if (bounds.getBonus_remain_num() > 0 && bounds.getPub_begin_time() * 1000 < System.currentTimeMillis() && bounds.getPub_end_time() * 1000 < System.currentTimeMillis()) {
            boundsViewHolde.tv_boundsstate.setText("进行中");
        } else {
            boundsViewHolde.tv_boundsstate.setText("已抢光");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class BoundsViewHolde extends RecyclerView.ViewHolder {

        private TextView tv_boundsstate;
        private TextView tv_boundstime;
        private TextView tv_boundsmoney;

        public BoundsViewHolde(View itemView) {
            super(itemView);
            tv_boundsstate = (TextView) itemView.findViewById(R.id.tv_boundsstate);
            tv_boundstime = (TextView) itemView.findViewById(R.id.tv_boundstime);
            tv_boundsmoney = (TextView) itemView.findViewById(R.id.tv_boundsmoney);
        }
    }
}
