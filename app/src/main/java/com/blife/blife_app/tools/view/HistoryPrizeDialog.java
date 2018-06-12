package com.blife.blife_app.tools.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.interfacer.DialogListener;
import com.blife.blife_app.tools.BitmapHelp;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by w on 2016/8/23.
 */
public class HistoryPrizeDialog extends PopupWindow {

    private View rootView;
    private boolean viewDismiss = false;

    private DialogListener dialogListener;
    private Animation animation;
    private AnimalDismissLisnter animalDismissLisnter;

    public HistoryPrizeDialog(Context context, int layout, String title) {
        init(context, layout, title);
    }

    public HistoryPrizeDialog(Context context, int layout) {
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
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_animal, null);
        this.setContentView(rootView);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colortrans)));
        this.setOutsideTouchable(true);
        setDismissListener();
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public void setImage(Context context, String imageUrl) {
        BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(context);
    }

    public void setDismissAnima(AnimalDismissLisnter animalDismissLisnter) {
        this.animalDismissLisnter = animalDismissLisnter;
    }

    public void startAnima(Context context) {
        animation = AnimationUtils.loadAnimation(context, R.anim.clear_cache_anim);
        if (animation != null) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animalDismissLisnter.animalDismiss();
                    dismiss();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public interface AnimalDismissLisnter {
        void animalDismiss();
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
