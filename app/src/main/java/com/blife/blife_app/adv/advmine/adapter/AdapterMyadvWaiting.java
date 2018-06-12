package com.blife.blife_app.adv.advmine.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.activity.ActivityMyadvPrepareDetail;
import com.blife.blife_app.adv.advmine.activity.ActivityMyadvWaitingDetail;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advsend.activity.ActivitySendAdv;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Somebody on 2016/8/29.
 */
public class AdapterMyadvWaiting extends RecyclerView.Adapter {

    private Context context;
    private List<BeanMyadv> list;

    public AdapterMyadvWaiting(Context context, List<BeanMyadv> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyadvViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myadv_prepare, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BeanMyadv beanMyadv = list.get(position);
        LogUtils.e("list***" + list);
        MyadvViewHolder myadvViewHolder = (MyadvViewHolder) holder;
        if (list.get(position).getContent() != null) {
            List<String> images = list.get(position).getContent().getImages();
            if (images != null && images.size() > 0) {
//                ImageLoader.getInstance().loadImage(images.get(0), myadvViewHolder.iv_advdimages, true);
                BitmapUtils bitmapHelp = BitmapHelp.getBitmapUtils(context);
                bitmapHelp.display(myadvViewHolder.iv_advdimages, images.get(0));
            }
        }
        if (!StringUtils.isImpty(list.get(position).getTitle())) {
            myadvViewHolder.tv_title.setText(list.get(position).getTitle());
        }
        if (!StringUtils.isImpty(list.get(position).getPub_name())) {
            myadvViewHolder.tv_pubname.setText(list.get(position).getPub_name());
        }
        if (!StringUtils.isImpty(list.get(position).getCreate_time())) {
            String timeHStr = DateFormatUtils.getTimeHStr(Long.parseLong(list.get(position).getCreate_time()) * 1000, "yyyy-MM-dd HH:mm");
            myadvViewHolder.tv_creattime.setText(timeHStr);
        }
        if (!StringUtils.isImpty(beanMyadv.getBonus_accepted_num())) {
            myadvViewHolder.tv_accepted_number.setText(beanMyadv.getBonus_accepted_num());
        }
        myadvViewHolder.lin_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                //是否缓存
                boolean isCache = true;
                bundle.putBoolean("isCache", isCache);
                bundle.putString(Constants.ADV_ID, beanMyadv.getAdv_id());
                intent.putExtras(bundle);
                intent.setClass(context, ActivityMyadvWaitingDetail.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0)
            return list.size();
        else {
            return 0;
        }
    }

    class MyadvViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_pubname, tv_creattime, tv_accepted_number;
        private ImageView iv_advdimages;
        private LinearLayout lin_list;

        public MyadvViewHolder(View itemView) {
            super(itemView);
            iv_advdimages = (ImageView) itemView.findViewById(R.id.iv_advdimages);
            lin_list = (LinearLayout) itemView.findViewById(R.id.lin_list);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_pubname = (TextView) itemView.findViewById(R.id.tv_pubname);
            tv_creattime = (TextView) itemView.findViewById(R.id.tv_creattime);
            tv_accepted_number = (TextView) itemView.findViewById(R.id.tv_accepted_number);
        }
    }
}
