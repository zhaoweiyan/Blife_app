package com.blife.blife_app.bonus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.bonus.bean.BeanRankingList;
import com.blife.blife_app.tools.view.CircleImageView;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.StringUtils;

import java.util.List;

/**
 * Created by w on 2016/9/18.
 */
public class Adapter_RankingList extends RecyclerView.Adapter {

    private final int TOP = 0;
    private final int ITEM = 1;

    private Context context;
    private List<BeanRankingList> list;

    public Adapter_RankingList(Context context, List<BeanRankingList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TOP) {
            return new RankingListTopViewHolder(LayoutInflater.from(context).inflate(R.layout.top_rankinglist, null));
        }
        return new RankingListItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rankinglist, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        BeanRankingList bean = list.get(position);
        if (type == TOP) {
            RankingListTopViewHolder viewHolder = ((RankingListTopViewHolder) holder);
            if (!TextUtils.isEmpty(bean.getUser_headimg())) {
                ImageLoader.getInstance().loadImage(bean.getUser_headimg(), viewHolder.top_user_icon, true);
            }
            viewHolder.tv_top_username.setText(bean.getUser_nickname());
            viewHolder.tv_top_position.setText(context.getResources().getString(R.string.bonus_ranking_No) + bean.getPosition() + context.getResources().getString(R.string.bonus_ranking_position));
            viewHolder.tv_top_money.setText("￥" + StringUtils.dealMoney(bean.getMoney(), 100));
            viewHolder.tv_top_time.setText(DateFormatUtils.getTimeHStr(bean.getCreate_time() * 1000, DateFormatUtils.format_M));
        } else {
            RankingListItemViewHolder viewHolder = (RankingListItemViewHolder) holder;
            if (!TextUtils.isEmpty(bean.getUser_headimg())) {
                ImageLoader.getInstance().loadImage(bean.getUser_headimg(), viewHolder.item_user_icon, true);
            }
            String nickName = bean.getUser_nickname();
            if (nickName.length() == 11 && StringUtils.isNum(nickName)) {
                nickName = StringUtils.hideText(nickName, 3, 4);
            }
            viewHolder.tv_item_username.setText(nickName);
            viewHolder.tv_item_position.setText(bean.getPosition());
            viewHolder.tv_item_position.setBackgroundDrawable(context.getResources().getDrawable(getPositionDraw(position)));
            viewHolder.tv_item_userphone.setText(StringUtils.hideText(bean.getUser_telphone(), 3, 4));
            viewHolder.tv_item_money.setText("￥" + StringUtils.dealMoney(bean.getMoney(), 100));
            viewHolder.tv_item_time.setText(DateFormatUtils.getTimeHStr(bean.getCreate_time() * 1000, DateFormatUtils.format_M));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TOP;
        }
        return ITEM;
    }

    class RankingListTopViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView top_user_icon;
        private TextView tv_top_username, tv_top_position, tv_top_money, tv_top_time;


        public RankingListTopViewHolder(View itemView) {
            super(itemView);
            top_user_icon = (CircleImageView) itemView.findViewById(R.id.top_rankinglist_user_icon);
            tv_top_username = (TextView) itemView.findViewById(R.id.tv_top_rankinglist_user_name);
            tv_top_position = (TextView) itemView.findViewById(R.id.tv_top_rankinglist_user_position);
            tv_top_money = (TextView) itemView.findViewById(R.id.tv_top_rankinglist_money);
            tv_top_time = (TextView) itemView.findViewById(R.id.tv_top_rankinglist_time);
        }
    }

    class RankingListItemViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView item_user_icon;
        private TextView tv_item_position, tv_item_username, tv_item_userphone, tv_item_money, tv_item_time;

        public RankingListItemViewHolder(View itemView) {
            super(itemView);
            item_user_icon = (CircleImageView) itemView.findViewById(R.id.item_rankinglist_user_icon);
            tv_item_position = (TextView) itemView.findViewById(R.id.tv_item_rankinglist_position);
            tv_item_username = (TextView) itemView.findViewById(R.id.tv_item_rankinglist_username);
            tv_item_userphone = (TextView) itemView.findViewById(R.id.tv_item_rankinglist_userphone);
            tv_item_money = (TextView) itemView.findViewById(R.id.tv_item_rankinglist_money);
            tv_item_time = (TextView) itemView.findViewById(R.id.tv_item_rankinglist_time);
        }
    }

    private int getPositionDraw(int position) {
        int drawResource;
        switch (position) {
            case 1:
                drawResource = R.mipmap.bonus_ranklist_one;
                break;
            case 2:
                drawResource = R.mipmap.bonus_ranklist_two;
                break;
            case 3:
                drawResource = R.mipmap.bonus_ranklist_three;
                break;
            default:
                drawResource = R.mipmap.bonus_ranklist_other;
                break;
        }
        return drawResource;
    }

}
