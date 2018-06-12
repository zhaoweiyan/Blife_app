package com.blife.blife_app.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.mine.activity.ActivityAliPay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/9/20.
 */
public class AdapterNotice extends RecyclerView.Adapter {

    private Context context;
    private List<String> list;

    public AdapterNotice(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeHolder(LayoutInflater.from(context).inflate(R.layout.item_notice, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String s = list.get(position);
        NoticeHolder noticeHolder = (NoticeHolder) holder;
        noticeHolder.notice_tv.setText((position+1) + "、" + s);
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0) {
            return list.size();
        } else {
            return 0;
        }
    }

    private class NoticeHolder extends RecyclerView.ViewHolder {
        private TextView notice_tv;

        public NoticeHolder(View inflate) {
            super(inflate);
            notice_tv = (TextView) inflate.findViewById(R.id.notice_tv);
        }
    }
}
