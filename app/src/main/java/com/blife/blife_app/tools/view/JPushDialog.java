package com.blife.blife_app.tools.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blife.blife_app.R;
import com.blife.blife_app.tools.BitmapHelp;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by w on 2016/6/10.
 */
public class JPushDialog extends Dialog {

    private View rootView;
    private ImageView imageView;

    public JPushDialog(Context context) {
        super(context, R.style.jpush_dialog);
        initView(context);
    }

    public void initView(Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_jpush, null);
        this.setContentView(rootView);
        this.setCancelable(false);// 不可以用“返回键”取消
        imageView = (ImageView) rootView.findViewById(R.id.dialog_jpush_img);
    }


    public void displayImage(Context context, String url) {
        BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(context);
        bitmapUtils.display(imageView, url);
    }

    public void showDialog() {
        this.show();
    }

    public void cancle() {
        this.dismiss();
    }
}
