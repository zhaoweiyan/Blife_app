package com.blife.blife_app.bonus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.blife.blife_app.R;
import com.blife.blife_app.bonus.adapterintreface.InterfaceReportAdapterOnItemClick;

import java.util.List;

/**
 * Created by w on 2016/9/19.
 */
public class Adapter_ReportTable extends BaseAdapter {

    private Context context;
    private List<String> list;
    private int currentIndex = 0;
    private InterfaceReportAdapterOnItemClick onItemClick;

    public Adapter_ReportTable(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReportViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ReportViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_report_table, null);
            viewHolder.button_item_report = (Button) convertView.findViewById(R.id.button_item_report);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ReportViewHolder) convertView.getTag();
        }
        viewHolder.button_item_report.setText(list.get(position));
        viewHolder.button_item_report.setOnClickListener(new onItemClick(position));
        if (position == currentIndex) {
            viewHolder.button_item_report.setEnabled(false);
        } else {
            viewHolder.button_item_report.setEnabled(true);
        }
        return convertView;
    }

    class onItemClick implements View.OnClickListener {

        int pos;

        public onItemClick(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            singleClick(pos);
            if (onItemClick != null) {
                onItemClick.onItemClick(pos);
            }
        }
    }

    public void setOnItemClick(InterfaceReportAdapterOnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    private void singleClick(int position) {
        if (position != currentIndex) {
            currentIndex = position;
            this.notifyDataSetChanged();
        }
    }


    class ReportViewHolder {
        Button button_item_report;
    }
}
