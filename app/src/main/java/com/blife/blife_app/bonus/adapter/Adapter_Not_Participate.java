package com.blife.blife_app.bonus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.bonus.adapterintreface.InterfaceBonusAdapterOnItemClick;
import com.blife.blife_app.bonus.bean.BeanNotParticipate;
import com.blife.blife_app.tools.view.RoundImageView;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.StringUtils;

import java.util.List;

/**
 * Created by w on 2016/9/13.
 */
public class Adapter_Not_Participate extends RecyclerView.Adapter {

    private Context context;
    private List<BeanNotParticipate> list;
    private InterfaceBonusAdapterOnItemClick onItemClick;

    public Adapter_Not_Participate(Context context, List<BeanNotParticipate> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotParticipateViewHolder(LayoutInflater.from(context).inflate(R.layout.item_not_participate, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BeanNotParticipate bean = list.get(position);
        if (holder instanceof NotParticipateViewHolder) {
            NotParticipateViewHolder viewHolder = ((NotParticipateViewHolder) holder);
            viewHolder.tv_notparticipate_money.setText("￥" + StringUtils.dealMoney(bean.getPrize_top_money(), 100));
            viewHolder.tv_notparticipate_starttime.setText(DateFormatUtils.getTimeHStr(bean.getPub_begin_time() * 1000,
                    DateFormatUtils.format_M));
            viewHolder.tv_notparticipate_endtime.setText(DateFormatUtils.getTimeHStr(bean.getPub_end_time() * 1000,
                    DateFormatUtils.format_M));
            if (bean.getContent() != null && bean.getContent().getImages() != null) {
                String image = bean.getContent().getImages().get(0);
                L.e("TAG", "图片路径：" + image);
                if (!TextUtils.isEmpty(image)) {
                    ImageLoader.getInstance().loadImage(image, viewHolder.iv_notparticipate, true);
                } else {
                    viewHolder.iv_notparticipate.setImageResource(R.mipmap.bonus_empty_icon);
                }
            }else{
                viewHolder.iv_notparticipate.setImageResource(R.mipmap.bonus_empty_icon);
            }
            viewHolder.lin_notparticipate.setOnClickListener(new onItemClick(position));
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClick(InterfaceBonusAdapterOnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    class onItemClick implements View.OnClickListener {
        private int position;

        public onItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onItemClick != null) {
                onItemClick.onClick(position);
            }
        }
    }

    class NotParticipateViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lin_notparticipate;
        private RoundImageView iv_notparticipate;
        private TextView tv_notparticipate_money, tv_notparticipate_starttime, tv_notparticipate_endtime;

        public NotParticipateViewHolder(View itemView) {
            super(itemView);
            lin_notparticipate = (LinearLayout) itemView.findViewById(R.id.lin_not_participate);
            iv_notparticipate = (RoundImageView) itemView.findViewById(R.id.iv_notparticipate);
            tv_notparticipate_money = (TextView) itemView.findViewById(R.id.tv_notparticipate_money);
            tv_notparticipate_starttime = (TextView) itemView.findViewById(R.id.tv_notparticipate_starttime);
            tv_notparticipate_endtime = (TextView) itemView.findViewById(R.id.tv_notparticipate_endtime);
        }
    }
}
