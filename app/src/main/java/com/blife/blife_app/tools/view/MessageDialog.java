package com.blife.blife_app.tools.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blife.blife_app.R;

/**
 * Created by w on 2016/8/23.
 */
public class MessageDialog extends PopupWindow {

    private View rootView;
    private boolean viewDismiss = false;

    private TextView tv_title, tv_message;

    public MessageDialog(Context context, int layout, String title, String msg) {
        init(context, layout, title, msg);
    }

    public MessageDialog(Context context, int layout) {
        init(context, layout, "", "");
    }
    /**
     * 初始化
     *
     * @param context
     * @param layout
     * @param title
     * @param msg
     */
    private void init(Context context, int layout, String title, String msg) {
        rootView = LayoutInflater.from(context).inflate(layout, null);
        this.setContentView(rootView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colortransback)));
        this.setOutsideTouchable(true);
        setDismissListener();
        tv_title = (TextView) rootView.findViewById(R.id.tv_dialogtitle);
        tv_message = (TextView) rootView.findViewById(R.id.tv_dialogmsg);
        tv_title.setText(title);
        tv_message.setText(msg);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (tv_title != null)
            tv_title.setText(title);
    }

    /**
     * 设置内容
     *
     * @param msg
     */
    public void setMessage(String msg) {
        if (tv_message != null)
            tv_message.setText(msg);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(int title) {
        if (tv_title != null)
            tv_title.setText(title);
    }

    /**
     * 设置内容
     *
     * @param msg
     */
    public void setMessage(int msg) {
        if (tv_message != null)
            tv_message.setText(msg);
    }

    /**
     * 获取标题TextView
     *
     * @return
     */
    public TextView getTv_title() {
        return tv_title;
    }

    /**
     * 获取内容TextView
     *
     * @return
     */
    public TextView getTv_message() {
        return tv_message;
    }

    /**
     * 显示
     *
     * @param view
     */
    public void show(View view) {
        this.showAtLocation(view, Gravity.TOP, 0, 0);
    }

    /**
     * 获取弹窗布局
     *
     * @return
     */
    public View getRootView() {
        return rootView;
    }

    /**
     * 设置点击屏幕是否关闭
     *
     * @param viewDismiss
     */
    public void setViewDismiss(boolean viewDismiss) {
        this.viewDismiss = viewDismiss;
    }

    /**
     * 点击关闭监听
     */
    private void setDismissListener() {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewDismiss) {
                    dismiss();
                }
            }
        });
    }

}
