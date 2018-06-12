package com.blife.blife_app.tools.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.blife.blife_app.R;
import com.blife.blife_app.utils.util.LogUtils;


/**
 * Created by w on 2016/6/10.
 */
public class LoadingDialog extends Dialog {

    private View rootView;
    private ImageView imageView;
    private AnimationDrawable animationDrawable;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        initView(context);
    }
    public void initView(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        this.setContentView(rootView);
        this.setCancelable(false);// 不可以用“返回键”取消
        imageView = (ImageView) rootView.findViewById(R.id.dialog_loading);
        animationDrawable = ((AnimationDrawable) imageView.getBackground());
    }
    public void showDialog() {
        this.show();
        LogUtils.e("********animationDrawable show"+animationDrawable);
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    public void cancle() {
        LogUtils.e("********animationDrawable cancle"+animationDrawable);
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
        this.dismiss();
    }
}
