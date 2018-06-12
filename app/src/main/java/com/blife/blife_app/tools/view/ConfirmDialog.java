package com.blife.blife_app.tools.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.interfacer.DialogListener;

/**
 * Created by w on 2016/8/23.
 */
public class ConfirmDialog extends PopupWindow {

    private View rootView;
    private boolean viewDismiss = false;

    private TextView tv_title, tv_message;
    private TextView tv_dialogmsg_cancle;
    private TextView tv_dialogmsg_confirm;
    private DialogListener dialogListener;

    public ConfirmDialog(Context context, int layout, String title) {
        init(context, layout, title);
    }

    public ConfirmDialog(Context context, int layout) {
        init(context, layout, "");
    }

    /**
     * 初始化
     *
     * @param context
     * @param layout
     * @param title
     */
    private void init(Context context, int layout, String title) {
        rootView = LayoutInflater.from(context).inflate(layout, null);
        this.setContentView(rootView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colortransback)));
        this.setOutsideTouchable(true);
        setDismissListener();
        tv_title = (TextView) rootView.findViewById(R.id.tv_dialogtitle);
        tv_title.setText(title);

        tv_dialogmsg_cancle = (TextView) rootView.findViewById(R.id.tv_dialogmsg_cancle);
        tv_dialogmsg_confirm = (TextView) rootView.findViewById(R.id.tv_dialogmsg_confirm);

        tv_dialogmsg_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.dialogCacleListener();
            }
        });

        tv_dialogmsg_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogListener.dialogConfirmListener();
            }
        });
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
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
     * 设置标题
     *
     * @param title
     */
    public void setTitle(int title) {
        if (tv_title != null)
            tv_title.setText(title);
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
