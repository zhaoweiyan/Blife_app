package com.blife.blife_app.mine.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.mine.api.API_Extends;
import com.blife.blife_app.mine.bean.BeanScan;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.share.ShareUtils;
import com.blife.blife_app.tools.view.CircleImageView;
import com.blife.blife_app.utils.code.qrcode.QRCodeManager;
import com.blife.blife_app.utils.exception.filexception.FileException;
import com.blife.blife_app.utils.file.FileManager;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.Contant;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.PixelUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.factory.BitmapFactory;

import java.io.File;

/**
 * Created by Somebody on 2016/9/19.
 */
public class ActivityExtends extends BaseActivity implements View.OnClickListener {
    private TextView extends_tv_name;
    private CircleImageView extends_iv_header;
    private ImageView extends_iv_scan;
    private String header = "";
    private String nickname = "";
    private LinearLayout no_identify_lin, noidentify_lin_go;
    private TextView noidentify_tv;
    private ScrollView scroll_identify;
    private LinearLayout withdraw_lin_no_iddentify;
    private ShareUtils shareUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extends);
        showLoadingDialog();
        initPre();
        initView();
        initClick();
        initShare();
        initExtends();
    }

    private void initShare() {
        shareUtils = new ShareUtils(instance, baseActivity);
    }

    private void initClick() {
        noidentify_lin_go.setOnClickListener(this);
    }

    private void initPre() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            header = bundle.getString(Constants.MINEHEADER, "");
            nickname = bundle.getString(Constants.MINE_NICKNAME, "");
        }
    }
    private void initExtends() {
        API_Extends api_extends = new API_Extends(ACCESS_TOKEN);
        dataManager.getServiceData(api_extends);
    }

    private void initView() {
//        initBackTopBar(R.string.extend);
        initBackTopBar(R.string.extend, R.string.share);
        scroll_identify = (ScrollView) findViewById(R.id.scroll_identify);
        no_identify_lin = (LinearLayout) findViewById(R.id.no_identify_lin);
        withdraw_lin_no_iddentify = (LinearLayout) findViewById(R.id.withdraw_lin_no_iddentify);
        noidentify_lin_go = (LinearLayout) findViewById(R.id.noidentify_lin_go);
        noidentify_tv = (TextView) findViewById(R.id.noidentify_tv);


//        //0.未认证，1.审核中2。认证通过3.认证拒绝4.其他
//        if (identify == 2) {
//            scroll_identify.setVisibility(View.VISIBLE);
//            withdraw_lin_no_iddentify.setVisibility(View.GONE);
//            noidentify_lin_go.setEnabled(false);
//        } else {
//            scroll_identify.setVisibility(View.GONE);
//            withdraw_lin_no_iddentify.setVisibility(View.VISIBLE);
//            noidentify_tv.setText(getString(R.string.identify_withdraw));
//            if (identify == 0) {
//                noidentify_lin_go.setEnabled(true);
//            } else if (identify == 1) {
//                noidentify_lin_go.setEnabled(false);
//            } else if (identify == 3) {
//                noidentify_lin_go.setEnabled(true);
//            }
//        }

        extends_tv_name = (TextView) findViewById(R.id.extends_tv_name);
        extends_iv_header = (CircleImageView) findViewById(R.id.extends_iv_header);
        extends_iv_scan = (ImageView) findViewById(R.id.extends_iv_scan);

        BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(this);
        bitmapUtils.display(extends_iv_header, header);
        extends_tv_name.setText("\"" + nickname + "\"");
    }

    @Override
    protected void TopRightClick() {
        super.TopRightClick();
        shareUtils.showSharePopWindow(extends_tv_name);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_Extends.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("推广***" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivityExtends.this);
                        return;
                    }
                    BeanScan beanScan = (BeanScan) JsonObjUItils.fromJson(json, BeanScan.class);
                    if (beanScan.getPromotion_link() != null) {
                        shareUtils.setShareImagePath(shardPreferName.getStringData(Constants.EXTENDS_SHARE_IAMGEURL, ""));
                        shareUtils.setShareLink(beanScan.getPromotion_link());
                        shareUtils.setShareText(shardPreferName.getStringData(Constants.EXTENDS_SHARE_CONTENT, ""));
                        if (!TextUtils.isEmpty(shardPreferName.getStringData(Constants.MINE_NICKNAME, ""))) {
                            shareUtils.setShareTitle(shardPreferName.getStringData(Constants.EXTENDS_SHARE_TITLE, "").replace(Constants.NICKNAME, shardPreferName.getStringData(Constants.MINE_NICKNAME, "")));
                        } else {
                            shareUtils.setShareTitle(shardPreferName.getStringData(Constants.EXTENDS_SHARE_TITLE, "").replace(Constants.NICKNAME, nickname));
                        }
                        String filePath = Contant.AppDirPath + File.separator + Constants.IMAGE_CACHE + File.separator;
                        try {
                            //将字符串截图字母和数字。特殊字符不能创建图片
                            File file = FileManager.makeFilePath(filePath, beanScan.getPromotion_link().trim().replaceAll("[^a-z^A-Z^0-9]", "") + ".png");
                            Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(filePath + File.separator + beanScan.getPromotion_link().trim().replaceAll("[^a-z^A-Z^0-9]", "") + ".png");
                            if (bitmap != null) {
                                extends_iv_scan.setImageBitmap(bitmap);
                            } else {
                                Bitmap qrCode = QRCodeManager.createQRCode(beanScan.getPromotion_link().trim(), PixelUtils.dip2px(ActivityExtends.this, PixelUtils.getWidth(extends_iv_scan)), PixelUtils.dip2px(ActivityExtends.this, PixelUtils.getHight(extends_iv_scan)), filePath);
                                extends_iv_scan.setImageBitmap(qrCode);
                            }
                        } catch (FileException e) {
                            Bitmap qrCode = QRCodeManager.createQRCode(beanScan.getPromotion_link().trim(), PixelUtils.dip2px(ActivityExtends.this, PixelUtils.getWidth(extends_iv_scan)), PixelUtils.dip2px(ActivityExtends.this, PixelUtils.getHight(extends_iv_scan)), filePath);
                            extends_iv_scan.setImageBitmap(qrCode);
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noidentify_lin_go:
                startFinishActivity(ActivityPassIdentify.class);
                break;
        }
    }
}
