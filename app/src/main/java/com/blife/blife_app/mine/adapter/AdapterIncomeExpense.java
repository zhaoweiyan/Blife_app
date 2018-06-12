package com.blife.blife_app.mine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.mine.activity.ActivityIncomeExpense;
import com.blife.blife_app.mine.bean.BeanIncomeExpense;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.NumberUtils;

import java.util.ArrayList;

/**
 * Created by Somebody on 2016/9/19.
 */
public class AdapterIncomeExpense extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<BeanIncomeExpense> list;

    public AdapterIncomeExpense(Context context, ArrayList<BeanIncomeExpense> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IncomeExpenseHolder(LayoutInflater.from(context).inflate(R.layout.item_income_expense, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BeanIncomeExpense beanIncomeExpense = list.get(position);
        IncomeExpenseHolder incomeExpenseHolder = (IncomeExpenseHolder) holder;
        if (beanIncomeExpense.getSource_str() != null) {
            incomeExpenseHolder.incomexpense_tv_title.setText(beanIncomeExpense.getSource_str());
        }
        incomeExpenseHolder.incomexpense_tv_creattime.setText(DateFormatUtils.getTimeHStr(beanIncomeExpense.getCreate_time() * 1000, DateFormatUtils.format));
        if (beanIncomeExpense.getType() != null && beanIncomeExpense.getAmount() != null) {
            if (beanIncomeExpense.getType().trim().equals("income")) {
                incomeExpenseHolder.incomexpense_tv_amount.setText("+" + NumberUtils.getTwoPoint(beanIncomeExpense.getAmount()));
                incomeExpenseHolder.incomexpense_tv_amount.setTextColor(context.getResources().getColor(R.color.color_fea000));
            } else if (beanIncomeExpense.getType().trim().equals("expense")) {
                incomeExpenseHolder.incomexpense_tv_amount.setText("-" + NumberUtils.getTwoPoint(beanIncomeExpense.getAmount()));
                incomeExpenseHolder.incomexpense_tv_amount.setTextColor(context.getResources().getColor(R.color.colorexpense));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    private class IncomeExpenseHolder extends RecyclerView.ViewHolder {

        private TextView incomexpense_tv_title, incomexpense_tv_creattime, incomexpense_tv_amount;

        public IncomeExpenseHolder(View inflate) {
            super(inflate);
            incomexpense_tv_title = (TextView) inflate.findViewById(R.id.incomexpense_tv_title);
            incomexpense_tv_creattime = (TextView) inflate.findViewById(R.id.incomexpense_tv_creattime);
            incomexpense_tv_amount = (TextView) inflate.findViewById(R.id.incomexpense_tv_amount);
        }
    }
}
