package com.blife.blife_app.adv.advmine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.bean.BeanGetCount;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.view.CircleImageView;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.NumberUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Somebody on 2016/9/8.
 */
public class AdapterMyadvGetCount extends RecyclerView.Adapter {


    private Context context;
    private ArrayList<BeanGetCount> list;

    public AdapterMyadvGetCount(Context context, ArrayList<BeanGetCount> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GetCountHolder(LayoutInflater.from(context).inflate(R.layout.item_getcount, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetCountHolder getCountHolder = (GetCountHolder) holder;
        BeanGetCount beanGetCount = list.get(position);
        if (beanGetCount.getUser_nickname() != null) {
            getCountHolder.tv_user_name.setText(beanGetCount.getUser_nickname().trim());
        } else {
            getCountHolder.tv_user_name.setText("未设置");
        }
        if (beanGetCount.getUser_telphone() != null) {
            getCountHolder.tv_user_phone.setText(beanGetCount.getUser_telphone().trim());
        } else {
            getCountHolder.tv_user_phone.setText("未设置");
        }
        if (beanGetCount.getMoney() != null) {
            getCountHolder.tv_user_bonus.setText(NumberUtils.getTwoPoint(beanGetCount.getMoney().trim()) + "元");
        } else {
            getCountHolder.tv_user_bonus.setText("未设置");
        }
        getCountHolder.tv_user_time.setText(DateFormatUtils.getTimeHStr(beanGetCount.getCreate_time() * 1000, DateFormatUtils.format));
        if (beanGetCount.getUser_headimg() != null) {
            LogUtils.e("roundimage");
            BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(context);
//            bitmapUtils.display(getCountHolder.iv_user_header, "http://adv-img.cdn.blife-tech.com/x3/qn/17c034332fdde34c089c3e9131feb6db.png");
            bitmapUtils.display(getCountHolder.iv_user_header, beanGetCount.getUser_headimg());
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

    private class GetCountHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_user_header;
        TextView tv_user_name, tv_user_phone, tv_user_bonus, tv_user_time;

        public GetCountHolder(View inflate) {
            super(inflate);
            iv_user_header = (CircleImageView) inflate.findViewById(R.id.iv_user_header);
            tv_user_name = (TextView) inflate.findViewById(R.id.tv_user_name);
            tv_user_phone = (TextView) inflate.findViewById(R.id.tv_user_phone);
            tv_user_bonus = (TextView) inflate.findViewById(R.id.tv_user_bonus);
            tv_user_time = (TextView) inflate.findViewById(R.id.tv_user_time);
        }
    }
}
