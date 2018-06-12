package com.blife.blife_app.adv.advsend.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advsend.bean.BeanSendTarget;
import com.blife.blife_app.utils.logcat.L;

import java.util.List;

/**
 * Created by w on 2016/8/31.
 */
public class VerticalListLayout extends LinearLayout {

    private Context context;
    private InterfaceOnItemDelete interfaceOnItemDelete;

    public VerticalListLayout(Context context) {
        super(context);
        init(context);
    }

    public VerticalListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticalListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOrientation(VERTICAL);
    }


    public void setData(List<BeanSendTarget> list) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        for (int i = 0; i < list.size(); i++) {
            L.e("TAG", "列表长度：" + list.size());
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_verticallist, null);
            addView(itemView);
            adapterData(itemView, list.get(i), i);
        }
    }

    private void adapterData(View view, BeanSendTarget bean, int position) {
        TextView tv_address = (TextView) view.findViewById(R.id.tv_address);
        TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        String distance = "";
//        if (bean.getPub_range() < 1000) {
//            distance = "\t<1000" + context.getString(R.string.uploadadv_m);
//        } else {
        distance = "\t" + (int) bean.getPub_range() + context.getString(R.string.uploadadv_m);
//        }
        SpannableStringBuilder builder = setDiffColorText(new String[]{bean.getPub_address(), distance},
                new int[]{context.getResources().getColor(R.color.color_reset), context.getResources().getColor(R.color.color_fea000)});
        tv_address.setText(builder);
//        tv_distance.setText("<" + ((int) bean.getPub_range()));
        tv_delete.setOnClickListener(new onItemClick(position));

    }

    public void setInterfaceOnItemDelete(InterfaceOnItemDelete interfaceOnItemDelete) {
        this.interfaceOnItemDelete = interfaceOnItemDelete;
    }

    private SpannableStringBuilder setDiffColorText(String[] texts, int[] colors) {
        String text = "";
        for (String s : texts) {
            text = text + s;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        int current = 0;
        for (int i = 0; i < texts.length; i++) {
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(colors[i]);
            builder.setSpan(foregroundColorSpan, current, current + texts[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            current = current + texts[i].length();
        }
        return builder;
    }

    class onItemClick implements View.OnClickListener {
        private int pos;

        public onItemClick(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            if (interfaceOnItemDelete != null) {
                interfaceOnItemDelete.onDelete(pos);
            }
        }
    }

}
