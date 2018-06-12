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
import com.blife.blife_app.bonus.bean.BeanAlreadyParticipate;
import com.blife.blife_app.tools.view.RoundImageView;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.StringUtils;

import java.util.List;

/**
 * Created by w on 2016/9/13.
 */
public class Adapter_Already_Participate extends RecyclerView.Adapter {

    private Context context;
    private List<BeanAlreadyParticipate> list;
    private InterfaceBonusAdapterOnItemClick onItemClick;

    public Adapter_Already_Participate(Context context, List<BeanAlreadyParticipate> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlreadyParticipateViewHolder(LayoutInflater.from(context).inflate(R.layout.item_already_participate, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BeanAlreadyParticipate bean = list.get(position);
        if (holder instanceof AlreadyParticipateViewHolder) {
            AlreadyParticipateViewHolder viewHolder = ((AlreadyParticipateViewHolder) holder);
            if (bean.getAdv_content() != null && bean.getAdv_content().getImages() != null) {
                String image = bean.getAdv_content().getImages().get(0);
                L.e("TAG", "图片路径：" + image);
                if (!TextUtils.isEmpty(image))
                    ImageLoader.getInstance().loadImage(bean.getAdv_content().getImages().get(0), viewHolder.iv_alreadyparticipate, true);
                else
                    viewHolder.iv_alreadyparticipate.setImageResource(R.mipmap.bonus_empty_icon);
            } else {
                viewHolder.iv_alreadyparticipate.setImageResource(R.mipmap.bonus_empty_icon);
            }
            viewHolder.tv_alreadyparticipate_money.setText("￥" + StringUtils.dealMoney(bean.getMoney() + "", 100));
            viewHolder.tv_alreadyparticipate_title.setText(bean.getAdv_title());
            viewHolder.tv_alreadyparticipate_name.setText(bean.getPub_name());
            String time = DateFormatUtils.getTimeHStr(bean.getCreate_time() * 1000, DateFormatUtils.format_M);
            viewHolder.tv_alreadyparticipate_time.setText(time);
            viewHolder.lin_alreadyparticipate.setOnClickListener(new onItemClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
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

    public void setOnItemClick(InterfaceBonusAdapterOnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    class AlreadyParticipateViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lin_alreadyparticipate;
        private RoundImageView iv_alreadyparticipate;
        private TextView tv_alreadyparticipate_title, tv_alreadyparticipate_name, tv_alreadyparticipate_money, tv_alreadyparticipate_time;

        public AlreadyParticipateViewHolder(View itemView) {
            super(itemView);
            lin_alreadyparticipate = (LinearLayout) itemView.findViewById(R.id.lin_already_participate);
            iv_alreadyparticipate = (RoundImageView) itemView.findViewById(R.id.iv_alreadyparticipate);
            tv_alreadyparticipate_title = (TextView) itemView.findViewById(R.id.tv_alreadyparticipate_title);
            tv_alreadyparticipate_name = (TextView) itemView.findViewById(R.id.tv_alreadyparticipate_name);
            tv_alreadyparticipate_money = (TextView) itemView.findViewById(R.id.tv_alreadyparticipate_money);
            tv_alreadyparticipate_time = (TextView) itemView.findViewById(R.id.tv_alreadyparticipate_time);
        }
    }
}
