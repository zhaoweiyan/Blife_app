package com.blife.blife_app.tools.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.blife.blife_app.R;

/**
 * Created by w on 2016/5/25.
 */
public class MenuPopWindows extends PopupWindow {

    private View rootView;
    private boolean viewDismiss = true;

    public MenuPopWindows(Context context, int layout) {
        rootView = LayoutInflater.from(context).inflate(layout, null);
        this.setContentView(rootView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colortransback)));
        this.setOutsideTouchable(true);
        setDismissListener();
    }

    public MenuPopWindows(Context context, View view) {
        rootView = view;
        this.setContentView(rootView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colortransback)));
        this.setOutsideTouchable(true);
        setDismissListener();
    }

    public void show(View view) {
        this.showAtLocation(view, Gravity.TOP, 0, 0);
    }

    public View getRootView() {
        return rootView;
    }

    public void setViewDismiss(boolean viewDismiss) {
        this.viewDismiss = viewDismiss;
    }

    public void setViewFocusable(boolean flag) {
        this.setFocusable(flag);
    }

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
