package com.blife.blife_app.tools.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blife.blife_app.R;


/**
 * Created by w on 2016/5/30.
 */
public class DialogPopWindows extends PopupWindow implements View.OnClickListener {

    private View rootView;
    private LinearLayout lin_pop_message;
    private TextView tv_pop_title;
    private TextView tv_pop_content;
    private TextView tv_pop_cancel;
    private TextView tv_pop_confirm;

    public final static int TYPE_TITLE = 0;
    public final static int TYPE_MESSAGE = 1;
    public final static int TYPE_CANCEL = 2;
    public final static int TYPE_CONFIRM = 3;

    private View.OnClickListener cancelListener;

    public DialogPopWindows(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.pop_textdialog, null);
        this.setContentView(rootView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(context.getResources().getDrawable(R.color.colortransback));
        this.setOutsideTouchable(true);
        initView();
    }

    private void initView() {
        lin_pop_message = (LinearLayout) rootView.findViewById(R.id.lin_pop_message);
        tv_pop_title = (TextView) rootView.findViewById(R.id.tv_pop_title);
        tv_pop_content = (TextView) rootView.findViewById(R.id.tv_pop_content);
        tv_pop_cancel = (TextView) rootView.findViewById(R.id.tv_textdialogcancel);
        tv_pop_confirm = (TextView) rootView.findViewById(R.id.tv_textdialogconfirm);
        tv_pop_cancel.setOnClickListener(this);
    }

    public void setConfirmClickListener(View.OnClickListener listener) {
        tv_pop_confirm.setOnClickListener(listener);
    }

    public void setCancelClickListener(View.OnClickListener listener) {
        cancelListener = listener;
        tv_pop_cancel.setOnClickListener(cancelListener);
    }

    public void setText(int TextViewType, String text) {
        setText(TextViewType, text, 0, 0, null, 0);
    }

    public void setTextColor(int TextViewType, int color) {
        setText(TextViewType, "null", color, 0, null, 0);
    }

    public void setTextSize(int TextViewType, int size) {
        setText(TextViewType, "null", 0, size, null, 0);
    }

    public void setTextTypeface(int TextViewType, Typeface typeface) {
        setText(TextViewType, "null", 0, 0, typeface, 0);
    }

    public void setTextGravity(int TextViewType, int gravity) {
        setText(TextViewType, "null", 0, 0, null, gravity);
    }

    public void setSingleButton() {
        tv_pop_cancel.setVisibility(View.GONE);
        tv_pop_confirm.setBackgroundResource(R.drawable.selector_popbutton_white);
    }

    public void setText(int TextViewType, String text, int color, int size, Typeface typeface, int gravity) {
        TextView textView = null;
        switch (TextViewType) {
            case TYPE_TITLE:
                textView = tv_pop_title;
                break;
            case TYPE_MESSAGE:
                textView = tv_pop_content;
                break;
            case TYPE_CANCEL:
                textView = tv_pop_cancel;
                break;
            case TYPE_CONFIRM:
                textView = tv_pop_confirm;
                break;
        }
        if (textView != null) {
            if (!TextUtils.isEmpty(text))
                textView.setText(text);
            if (color != 0)
                textView.setTextColor(color);
            if (size != 0)
                textView.setTextSize(size);
            if (typeface != null)
                textView.setTypeface(typeface);
            if (gravity != 0)
                textView.setGravity(gravity);
        }
    }

    public void show(View view) {
        this.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_textdialogcancel:
                if (cancelListener == null) {
                    DialogPopWindows.this.dismiss();
                }
                break;
        }
    }
}
