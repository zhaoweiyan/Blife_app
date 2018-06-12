package com.blife.blife_app.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.index.bean.BeanPastWinner;
import com.blife.blife_app.tools.view.RoundImageView;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.util.StringUtils;

import java.util.List;

/**
 * Created by w on 2016/9/23.
 */
public class Adapter_SuperBonus_PastWinner extends RecyclerView.Adapter {

    private Context context;
    private List<BeanPastWinner> list;
    private InterfacePartWinnerOnItemClick interfacePartWinnerOnItemClick;

    public Adapter_SuperBonus_PastWinner(Context context, List<BeanPastWinner> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PastViewHolder(LayoutInflater.from(context).inflate(R.layout.item_superbonus_pastwinner, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PastViewHolder viewHolder = ((PastViewHolder) holder);
        BeanPastWinner bean = list.get(position);
        viewHolder.tv_activity_name.setText(context.getResources().getString(R.string.superbonus_activity_name) + bean.getEvent_name());
        viewHolder.tv_activity_money.setText(context.getResources().getString(R.string.superbonus_activity_money) + "￥" + StringUtils.dealMoney(bean.getBonus_money(), 100));
        if (bean.getImages() != null && bean.getImages().size() > 0) {
            ImageLoader.getInstance().loadImage(bean.getImages().get(0), viewHolder.roundImageView, true);
        }
        viewHolder.framelayout_past_winner.setOnClickListener(new OnItemClick(position));
    }

    class OnItemClick implements View.OnClickListener {

        int position;

        public OnItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (interfacePartWinnerOnItemClick != null) {
                interfacePartWinnerOnItemClick.onItem(position);
            }
        }
    }

    public void setInterfacePartWinnerOnItemClick(InterfacePartWinnerOnItemClick interfacePartWinnerOnItemClick) {
        this.interfacePartWinnerOnItemClick = interfacePartWinnerOnItemClick;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PastViewHolder extends RecyclerView.ViewHolder {
        private RoundImageView roundImageView;
        private TextView tv_activity_name, tv_activity_money;
        private FrameLayout framelayout_past_winner;

        public PastViewHolder(View itemView) {
            super(itemView);
            roundImageView = (RoundImageView) itemView.findViewById(R.id.roundiv_past_image);
            tv_activity_name = (TextView) itemView.findViewById(R.id.tv_item_past_name);
            tv_activity_money = (TextView) itemView.findViewById(R.id.tv_item_past_money);
            framelayout_past_winner = (FrameLayout) itemView.findViewById(R.id.framelayout_past_winner);
        }
    }

}
