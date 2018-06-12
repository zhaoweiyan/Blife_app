package com.blife.blife_app.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.index.bean.BeanPastWinner;
import com.blife.blife_app.tools.view.RoundImageView;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;

import java.util.List;

/**
 * Created by w on 2016/9/26.
 */
public class Adapter_PastAwards extends RecyclerView.Adapter {

    private Context context;
    private List<BeanPastWinner> list;
    private InterfacePastAwardsItemClick itemClick;

    public Adapter_PastAwards(Context context, List<BeanPastWinner> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PastAwardsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_past_awards, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PastAwardsViewHolder viewHolder = ((PastAwardsViewHolder) holder);
        BeanPastWinner bean = list.get(position);
        if (bean.getImages().size() > 0) {
            ImageLoader.getInstance().loadImage(bean.getImages().get(0), viewHolder.roundImageView, true);
        }
        viewHolder.tv_title.setText(bean.getEvent_name());
        viewHolder.tv_name.setText(bean.getMerchant_name());
        viewHolder.tv_time.setText(context.getResources().getString(R.string.superbonus_activity_time) +
                bean.getBegin_time_str() + "~" + bean.getEnd_time_str());
        viewHolder.lin_past_awards.setOnClickListener(new OnClick(position));
    }

    class OnClick implements View.OnClickListener {
        int position;

        public OnClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (itemClick != null)
                itemClick.onItem(position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClick(InterfacePastAwardsItemClick itemClick) {
        this.itemClick = itemClick;
    }

    class PastAwardsViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lin_past_awards;
        private RoundImageView roundImageView;
        private TextView tv_title, tv_name, tv_time;

        public PastAwardsViewHolder(View itemView) {
            super(itemView);
            lin_past_awards = (LinearLayout) itemView.findViewById(R.id.lin_past_awards);
            roundImageView = (RoundImageView) itemView.findViewById(R.id.roundimage_item_pastawards);
            tv_title = (TextView) itemView.findViewById(R.id.tv_item_pastawards_title);
            tv_name = (TextView) itemView.findViewById(R.id.tv_item_pastawards_companyname);
            tv_time = (TextView) itemView.findViewById(R.id.tv_item_pastawards_time);
        }
    }
}
