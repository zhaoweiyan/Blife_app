package com.blife.blife_app.tools.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blife.blife_app.R;
import com.blife.blife_app.tools.view.MenuPopWindows;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.util.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by w on 2016/9/20.
 */
public class ShareUtils implements View.OnClickListener, UMShareListener {

    private Context context;
    private Activity activity;
    private MenuPopWindows popWindows;
    private TextView tv_wechat, tv_timeline, tv_qq, tv_qzone, tv_sine;
    private ImageView iv_cancel;

    //分享
    private ShareAction shareAction;
    UMImage umImage;

    private String shareTitle;
    private String shareText;
    private String shareImagePath;
    private String shareLink;

    public ShareUtils(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        shareAction = new ShareAction(activity);
        shareAction.setCallback(this);
    }

    /**
     * 初始化View
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_share, null);
        popWindows = new MenuPopWindows(context, view);
        popWindows.setViewDismiss(false);
        tv_timeline = (TextView) view.findViewById(R.id.tv_share_timeline);
        tv_wechat = (TextView) view.findViewById(R.id.tv_share_wechat);
        tv_qq = (TextView) view.findViewById(R.id.tv_share_qq);
        tv_qzone = (TextView) view.findViewById(R.id.tv_share_qzone);
        tv_sine = (TextView) view.findViewById(R.id.tv_share_sine);
        iv_cancel = (ImageView) view.findViewById(R.id.iv_share_cancel);

        tv_timeline.setOnClickListener(this);
        tv_wechat.setOnClickListener(this);
        tv_qq.setOnClickListener(this);
        tv_qzone.setOnClickListener(this);
        tv_sine.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
    }

    /**
     * 显示分享窗口
     *
     * @param view
     */
    public void showSharePopWindow(View view) {
        if (popWindows != null) {
            popWindows.show(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (popWindows != null)
            popWindows.dismiss();
        if (TextUtils.isEmpty(shareTitle) ||
                TextUtils.isEmpty(shareText) ||
                TextUtils.isEmpty(shareImagePath) ||
                TextUtils.isEmpty(shareLink)) {
            return;
        }
        setShareData();
        switch (v.getId()) {
            case R.id.tv_share_timeline:
                shareWECHAT_CIRCLE();
                break;
            case R.id.tv_share_wechat:
                shareWECHAT();
                break;
            case R.id.tv_share_qq:
                shareQQ();
                break;
            case R.id.tv_share_qzone:
                shareQZONE();
                break;
            case R.id.tv_share_sine:
                shareSINA();
                break;
        }
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public void setShareImagePath(String shareImagePath) {
        this.shareImagePath = shareImagePath;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    private void setShareData() {
        //分享的图片
        umImage = new UMImage(context, shareImagePath);
        shareAction.withTitle(shareTitle);
        shareAction.withText(shareText);
        shareAction.withMedia(umImage);
        shareAction.withTargetUrl(shareLink);
    }

    private void shareQQ() {
        shareAction.setPlatform(SHARE_MEDIA.QQ);
        shareAction.share();
    }

    private void shareQZONE() {
        shareAction.setPlatform(SHARE_MEDIA.QZONE);
        shareAction.share();
    }

    private void shareSINA() {
        shareAction.setPlatform(SHARE_MEDIA.SINA);
        shareAction.share();
    }

    private void shareWECHAT() {
        shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
        shareAction.share();
    }

    private void shareWECHAT_CIRCLE() {
        shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        shareAction.share();
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        L.e("TAG", "shareSuccess");
//        ToastUtils.showShort(context, R.string.share_success);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        L.e("TAG", "shareError" + throwable.toString());
//        ToastUtils.showShort(context, R.string.share_failed);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
//        ToastUtils.showShort(context, R.string.share_cancel);
        L.e("TAG", "shareCancel");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
        L.e("TAG", "onActivityResult");
    }
}
