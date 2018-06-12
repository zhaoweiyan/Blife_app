package com.blife.blife_app.tools.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.interfacer.DialogListener;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.ScreenUtils;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.PixelUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by w on 2016/8/23.
 */
public class JPushReceiverDialog extends PopupWindow {

    private View rootView;
    private boolean viewDismiss = false;

    private DialogListener dialogListener;
    private ImageView dialog_jpush_close;
    private ImageView dialog_jpush_img;
    private int screenWidth;

    public JPushReceiverDialog(Context context, int layout, String title) {
        init(context, layout, title);
    }

    public JPushReceiverDialog(Context context, int layout) {
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
        dialog_jpush_img = (ImageView) rootView.findViewById(R.id.dialog_jpush_img);
        dialog_jpush_close = (ImageView) rootView.findViewById(R.id.dialog_jpush_close);
        screenWidth = ScreenUtils.getScreenWidth(context);


        //动态设置自适应屏幕并且4：3比例的弹框
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) dialog_jpush_img.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = (screenWidth - 75) * 4 / 3;// 控件的高
        linearParams.width = (screenWidth - 75);// 控件的宽
        LogUtils.e("screenWidth****" + linearParams.width);
        LogUtils.e("height****" + linearParams.height);
        dialog_jpush_img.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>

    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }
    public void setImage(Context context, String imageUrl) {
        BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(context);
        bitmapUtils.display(dialog_jpush_img, imageUrl);
        dialog_jpush_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
