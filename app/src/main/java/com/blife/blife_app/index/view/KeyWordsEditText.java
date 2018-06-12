package com.blife.blife_app.index.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.utils.logcat.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by w on 2016/9/28.
 */
public class KeyWordsEditText extends LinearLayout {

    private Context context;
    private final int MARGIN = 5;
    private int childSpec;
    private boolean isMeasureFinish = false;
    //输入框集合
    private List<TextView> views;


    public KeyWordsEditText(Context context) {
        super(context);
        init(context);
    }

    public KeyWordsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyWordsEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isMeasureFinish) return;
        int width = getMeasuredWidth();
        L.e("TAG", "onMeasure视图宽度：" + width);
        if (width <= 0) return;
        int childCount = getChildCount();
        if (childCount <= 0) return;
        int dealWidth = 0;
        if (childCount >= 5) {
            dealWidth = width - MARGIN * 2 * childCount;
            childSpec = dealWidth / childCount;
        } else {
            dealWidth = width - MARGIN * 2 * 5;
            childSpec = dealWidth / 5;
        }
//        int dealWidth = width - MARGIN * 2 * childCount;
//        L.e("TAG", "dealWidth:" + dealWidth);
////        int childCountWidth = dealWidth > childWidth * childCount ? dealWidth : childWidth * childCount;
//        int childCountWidth = dealWidth;
//        L.e("TAG", "childCountWidth:" + childCountWidth);
////        childSpec = childCountWidth / childCount < childCountWidth / 5 ? childCountWidth / childCount : childCountWidth / 5;
//        childSpec = childCountWidth / childCount;
//        L.e("TAG", "childSpec:" + childSpec);
//        if (childSpec > childCountWidth / 5) {
//            childSpec = childCountWidth / 5;
//        }
        L.e("TAG", "childSpec:" + childSpec);
        if (childSpec > 0) {
            for (int i = 0; i < childCount; i++) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(i).getLayoutParams();
                layoutParams.width = childSpec;
                layoutParams.height = childSpec;
                layoutParams.leftMargin = MARGIN;
                layoutParams.rightMargin = MARGIN;
            }
            getLayoutParams().width = childSpec * childCount + MARGIN * childCount * 2;
            isMeasureFinish = true;
        }
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        this.context = context;
        views = new ArrayList<>();

    }

    public void setKeywordsNum(int num) {
        for (int i = 0; i < num; i++) {
            createEditText();
        }
    }

    public void setText(String text) {
        int length = text.length();
        for (int i = 0; i < views.size(); i++) {
            if (i < length) {
                views.get(i).setText(text.substring(i, i + 1));
            } else {
                views.get(i).setText("");
            }
        }
    }

    private void createEditText() {
        View view = LayoutInflater.from(context).inflate(R.layout.item_keywords_view, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_keyword);
        views.add(textView);
        addView(view);
    }
}
