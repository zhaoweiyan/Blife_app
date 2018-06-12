package com.blife.blife_app.index.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.index.bean.BeanBonus;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.StringUtils;

import java.util.List;

/**
 * Created by w on 2016/8/29.
 */
public class BoundsLayout extends LinearLayout {

    private Context context;
    private List<BeanBonus> beanBonusList;
    private InterfaceBonusClick bonusClick;

    public static final int STATE_READY = 1;
    public static final int STATE_PROGRESS = 2;
    public static final int STATE_NULL = 3;
    public static final int STATE_END = 4;

    public int MAX_SIZE = 3;

    public BoundsLayout(Context context) {
        super(context);
        init(context);
    }

    public BoundsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoundsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOrientation(HORIZONTAL);
    }

    private void AdapterData(View itemView, int position) {
        TextView tv_state = (TextView) itemView.findViewById(R.id.tv_boundsstate);
        TextView tv_time = (TextView) itemView.findViewById(R.id.tv_boundstime);
        TextView tv_monty = (TextView) itemView.findViewById(R.id.tv_boundsmoney);
        tv_monty.setText("￥" + StringUtils.dealMoney(beanBonusList.get(position).getPrize_top_money(), 100));
        String time = DateFormatUtils.getTimeHStr(beanBonusList.get(position).getPub_begin_time() * 1000, "yyyy-MM-dd\nHH:mm:ss");
        tv_time.setText(time);
        int state = dealState(position);
        tv_state.setText(setState(state));
    }

    public void setData(List<BeanBonus> list) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        if (beanBonusList != null) {
            beanBonusList.clear();
        }
        if (list.size() > MAX_SIZE) {
            beanBonusList = list.subList(0, MAX_SIZE);
        } else {
            beanBonusList = list;
        }
        for (int i = 0; i < list.size(); i++) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_boundslist, null);
            itemView.findViewById(R.id.lin_bonus).setOnClickListener(new onBonusClick(i));
            LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            addView(itemView, layoutParams);
            AdapterData(itemView, i);
        }
        if (getChildCount() < MAX_SIZE) {
            int EmptyViewNum = MAX_SIZE - getChildCount();
            for (int i = 0; i < EmptyViewNum; i++) {
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_boundslist, null);
                itemView.setVisibility(INVISIBLE);
                LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.weight = 1;
                addView(itemView, layoutParams);
            }
        }
    }

    public void setBonusClick(InterfaceBonusClick interfaceBonusClick) {
        this.bonusClick = interfaceBonusClick;
    }

    class onBonusClick implements View.OnClickListener {
        private int pos;

        public onBonusClick(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            if (bonusClick != null) {
                int state = dealState(pos);
                bonusClick.onClick(pos, state);
            }
        }
    }

    private String setState(int state) {
        String stateName;
        switch (state) {
            case STATE_READY:
                stateName = context.getResources().getString(R.string.index_bonus_ready);
                break;
            case STATE_PROGRESS:
                stateName = context.getResources().getString(R.string.index_bonus_progress);
                break;
            case STATE_NULL:
                stateName = context.getResources().getString(R.string.index_bonus_null);
                break;
            case STATE_END:
                stateName = context.getResources().getString(R.string.index_bonus_end);
                break;
            default:
                stateName = context.getResources().getString(R.string.index_bonus_end);
                break;
        }
        return stateName;
    }

    private int dealState(int position) {
        int state;
        BeanBonus bounds = beanBonusList.get(position);
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        if (currentTime < bounds.getPub_begin_time()) {
            state = STATE_READY;
        } else if (bounds.getPub_begin_time() < currentTime && currentTime < bounds.getPub_end_time()) {
            if (bounds.getBonus_remain_num() > 0) {
                state = STATE_PROGRESS;
            } else {
                state = STATE_NULL;
            }
        } else {
            state = STATE_END;
        }
        return state;
    }

}
