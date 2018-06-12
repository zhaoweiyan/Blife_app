package com.blife.blife_app.mine.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.interfacer.DialogListener;
import com.blife.blife_app.adv.advsend.api.API_UploadImage;
import com.blife.blife_app.adv.advsend.bean.BeanUploadImage;
import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.index.receiver.JPushReceiver;
import com.blife.blife_app.login.activity.ActivityPasswordLogin;
import com.blife.blife_app.login.activity.ActivityResetPwd;
import com.blife.blife_app.login.api.API_LoginPwd;
import com.blife.blife_app.mine.api.API_MeInfoCommit;
import com.blife.blife_app.mine.api.API_Mine;
import com.blife.blife_app.mine.bean.BeanMine;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.SDCardUtil;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.tools.view.CircleImageView;
import com.blife.blife_app.tools.view.ConfirmDialog;
import com.blife.blife_app.tools.view.MenuPopWindows;
import com.blife.blife_app.tools.view.BirthdayView;
import com.blife.blife_app.utils.bitmap.BitmapManager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.DateFormatUtils;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.lidroid.xutils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Somebody on 2016/9/13.
 */
public class ActivityMine extends BaseActivity implements View.OnClickListener, InterfacePermissionResult {
    private FrameLayout mecenter_fl_header;
    private CircleImageView mecenter_iv_header;
    private LinearLayout mecenter_lin_nickname, mecenter_lin_sex, mecenter_lin_identify, mecenter_lin_birtday, mecenter_lin_resetpwd, mecenter_lin_quit;
    private TextView mecenter_tv_birthday;
    private RadioGroup mecenter_rg;
    private RadioButton mecenter_rb_man, mecenter_rb_woman;
    private TextView meceter_tv_identify;
    private EditText mecenter_et_nickname;
    private MenuPopWindows menuPopWindows;
    private Button button_photo, button_carmer, button_cancel;
    private String compressPath;
    private Uri cameraUri;
    private Uri saveCorpUri;
    private String uploadImagepath;
    private MenuPopWindows timeSelectWindows;
    private Button button_selecttime;
    private BirthdayView pickerView;
    //生日
    private String DateFormat = "yyyy-MM-dd";
    private long birthday = 0;
    private String nickName;
    private int gender = 1;
    private int citizenOrCompany = -1;
    private ConfirmDialog confirmDialog;
    private API_Mine api_mecenter_info;
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        showLoadingDialog();
        confirmDialog = new ConfirmDialog(this, R.layout.dialog_confirm, "确认退出?");
        initView();
        initCilck();
        checkPermission();
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(instance, ActivityMine.this);
        permissionUtils.setInterfacePermissionResult(this);
        permissionUtils.checkPermission(Manifest.permission.CAMERA, 103);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGranted() {

    }

    @Override
    public void onDenied() {
    }

    @Override
    public void onNotShowRationale() {

    }

    @Override
    public void onCancelShowRationale() {

    }

    @Override
    public void onDeniedDialogPositive() {

    }

    @Override
    public void onDeniedDialogNegative() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        initMeCenterInfo();
    }

    private void initMeCenterInfo() {
        api_mecenter_info = new API_Mine(ACCESS_TOKEN, shardPreferName.getBooleanData(Constants.IDENTITY_PASS, false));
        dataManager.getServiceData(api_mecenter_info);
    }

    private void initCilck() {
        mecenter_fl_header.setOnClickListener(this);
        mecenter_lin_nickname.setOnClickListener(this);
        mecenter_lin_sex.setOnClickListener(this);
        mecenter_lin_birtday.setOnClickListener(this);
        mecenter_lin_resetpwd.setOnClickListener(this);
        mecenter_lin_identify.setOnClickListener(this);
        mecenter_lin_quit.setOnClickListener(this);

        mecenter_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == mecenter_rb_man.getId()) {
                    mecenter_rb_man.setChecked(true);
                    mecenter_rb_woman.setChecked(false);
                    mecenter_rb_woman.setTextColor(getResources().getColor(R.color.color_user_agreement));
                    mecenter_rb_man.setTextColor(getResources().getColor(R.color.color_fea000));
                } else if (checkedId == mecenter_rb_woman.getId()) {
                    mecenter_rb_woman.setChecked(true);
                    mecenter_rb_man.setChecked(false);
                    mecenter_rb_man.setTextColor(getResources().getColor(R.color.color_user_agreement));
                    mecenter_rb_woman.setTextColor(getResources().getColor(R.color.color_fea000));
                }
            }
        });


        menuPopWindows = new MenuPopWindows(instance, R.layout.pop_usericon);
        button_photo = (Button) menuPopWindows.getRootView().findViewById(R.id.button_pop_photo);
        button_carmer = (Button) menuPopWindows.getRootView().findViewById(R.id.button_pop_camera);
        button_cancel = (Button) menuPopWindows.getRootView().findViewById(R.id.button_pop_cancle);
        button_photo.setOnClickListener(this);
        button_carmer.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
    }

    private void initView() {
        initBackTopBar(R.string.me_center, R.string.infoSave);
        //头像
        mecenter_fl_header = (FrameLayout) findViewById(R.id.mecenter_fl_header);
        mecenter_iv_header = (CircleImageView) findViewById(R.id.mecenter_iv_header);
        //昵称
        mecenter_lin_nickname = (LinearLayout) findViewById(R.id.mecenter_lin_nickname);
        mecenter_et_nickname = (EditText) findViewById(R.id.mecenter_et_nickname);
        mecenter_et_nickname.setCursorVisible(false);
        mecenter_et_nickname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mecenter_et_nickname.setCursorVisible(true);
                }
                return false;
            }
        });
        //性别
        mecenter_lin_sex = (LinearLayout) findViewById(R.id.mecenter_lin_sex);
        mecenter_rg = (RadioGroup) findViewById(R.id.mecenter_rg);
        mecenter_rb_man = (RadioButton) findViewById(R.id.mecenter_rb_man);
        mecenter_rb_woman = (RadioButton) findViewById(R.id.mecenter_rb_woman);
        //出生年月
        mecenter_lin_birtday = (LinearLayout) findViewById(R.id.mecenter_lin_birtday);
        mecenter_tv_birthday = (TextView) findViewById(R.id.mecenter_tv_birthday);
        //时间选择
        timeSelectWindows = new MenuPopWindows(instance, R.layout.pop_selectbirthday);
        timeSelectWindows.setViewDismiss(true);
        button_selecttime = (Button) timeSelectWindows.getRootView().findViewById(R.id.btn_selecttime);
        button_selecttime.setOnClickListener(this);
        pickerView = (BirthdayView) timeSelectWindows.getRootView().findViewById(R.id.pickview);
//        pickerView.viewCurrentAddHour(1);
        pickerView.setOnScrollListener(new BirthdayView.onScrollListener() {
            @Override
            public void onScroll(String year, String month, String day) {
//                mecenter_tv_birthday.setText(year + "-" + month + "-" + day);
            }
        });

        //重置密码
        mecenter_lin_resetpwd = (LinearLayout) findViewById(R.id.mecenter_lin_resetpwd);
        //实名认证
        mecenter_lin_identify = (LinearLayout) findViewById(R.id.mecenter_lin_identify);
        meceter_tv_identify = (TextView) findViewById(R.id.meceter_tv_identify);
        //退出登录
        mecenter_lin_quit = (LinearLayout) findViewById(R.id.mecenter_lin_quit);
    }

    @Override
    protected void TopRightClick() {
        super.TopRightClick();
        showLoadingDialog();
        if (mecenter_et_nickname.getText().toString() == null) {
            nickName = "";
        } else {
            nickName = mecenter_et_nickname.getText().toString();
        }

        if (mecenter_rb_man.isChecked()) {
            gender = 1;
        } else if (mecenter_rb_woman.isChecked()) {
            gender = 2;
        }

        if (mecenter_tv_birthday.getText().toString() == null) {
            birthday = System.currentTimeMillis() / 1000;
        } else {
            birthday = (DateFormatUtils.getlongHTime(mecenter_tv_birthday.getText().toString().trim(), DateFormatUtils.formatYMD)) / 1000;
            LogUtils.e("birthday****" + birthday);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nickname", nickName);
            jsonObject.put("gender", gender);
            jsonObject.put("birthday", birthday);
            if (uploadImagepath != null) {
                jsonObject.put("headimg", uploadImagepath);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        API_MeInfoCommit api_meInfoCommit = new API_MeInfoCommit(ACCESS_TOKEN, json);
        dataManager.getServiceData(api_meInfoCommit);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_Mine.TAG)) {
                    LogUtils.e("个人中心****" + json);
                    if (tag.equals(API_Mine.TAG)) {
                        cancelLoadingDialog();
                        if (TextUtils.isEmpty(json)) {
                            JsonObjUItils.isEmptyJson(ActivityMine.this);
                            return;
                        }
                        LogUtils.e("获取我的信息**" + json);
                        BeanMine beanMine = (BeanMine) JsonObjUItils.fromJson(json, BeanMine.class);
                        if (beanMine.getVerify_status() == 0) {
                            meceter_tv_identify.setText("未认证");
                            mecenter_lin_identify.setEnabled(true);
                            dataManager.clearCache(api_mecenter_info);
                            shardPreferName.setBooleanData(Constants.IDENTITY_PASS, false);
                        } else if (beanMine.getVerify_status() == 1) {
                            meceter_tv_identify.setText("审核中");
                            dataManager.clearCache(api_mecenter_info);
                            mecenter_lin_identify.setEnabled(false);
                            shardPreferName.setBooleanData(Constants.IDENTITY_PASS, false);
                        } else if (beanMine.getVerify_status() == 2) {
                            meceter_tv_identify.setText("已认证");
                            mecenter_lin_identify.setEnabled(false);
                            shardPreferName.setBooleanData(Constants.IDENTITY_PASS, true);
                        } else if (beanMine.getVerify_status() == 3) {
                            dataManager.clearCache(api_mecenter_info);
                            meceter_tv_identify.setText("认证失败");
                            mecenter_lin_identify.setEnabled(true);
                            shardPreferName.setBooleanData(Constants.IDENTITY_PASS, false);
                        } else {
                            meceter_tv_identify.setText("未认证");
                            dataManager.clearCache(api_mecenter_info);
                            mecenter_lin_identify.setEnabled(true);
                            shardPreferName.setBooleanData(Constants.IDENTITY_PASS, false);
                        }
                        citizenOrCompany = beanMine.getType();
                        if (beanMine.getHeadimg() != null) {
                            BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(ActivityMine.this);
                            bitmapUtils.display(mecenter_iv_header, beanMine.getHeadimg());
                        }

                        if (beanMine.getNickname() != null) {
                            mecenter_et_nickname.setText(beanMine.getNickname());
                        }
                        if (beanMine.getGender() == 2) {
                            mecenter_rb_woman.setChecked(true);
                            mecenter_rb_man.setChecked(false);
                            mecenter_rb_man.setTextColor(getResources().getColor(R.color.color_user_agreement));
                            mecenter_rb_woman.setTextColor(getResources().getColor(R.color.color_fea000));
                        } else {
                            mecenter_rb_man.setChecked(true);
                            mecenter_rb_woman.setChecked(false);
                            mecenter_rb_woman.setTextColor(getResources().getColor(R.color.color_user_agreement));
                            mecenter_rb_man.setTextColor(getResources().getColor(R.color.color_fea000));
                        }
                        LogUtils.e("mecenter_tv_birthday****" + beanMine.getBirthday());
                        if (beanMine.getBirthday() == 0) {
                            mecenter_tv_birthday.setText(DateFormatUtils.getTimeHStr(System.currentTimeMillis(), DateFormatUtils.formatYMD));
                            birthday = System.currentTimeMillis() / 1000;
                        } else {
                            mecenter_tv_birthday.setText(DateFormatUtils.getTimeHStr(beanMine.getBirthday() * 1000, DateFormatUtils.formatYMD));
                            birthday = Long.valueOf(beanMine.getBirthday());
                        }
                    }
                }
                if (tag.equals(API_UploadImage.TAG)) {
                    LogUtils.e("图片上传****" + json);
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivityMine.this);
                        return;
                    }
                    BeanUploadImage beanUploadImage = (BeanUploadImage) JsonObjUItils.fromJson(json, BeanUploadImage.class);
                    if (beanUploadImage.getAdvimg() != null) {
                        BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(ActivityMine.this);
                        bitmapUtils.display(mecenter_iv_header, beanUploadImage.getAdvimg());
                        uploadImagepath = beanUploadImage.getAdvimg();
                    }
                }
                if (tag.equals(API_MeInfoCommit.TAG)) {
                    LogUtils.e("个人中心的更新****" + json);
//                    ToastUtils.showShort(ActivityMine.this, R.string.commitSuccess);
                    dataManager.clearCache(api_mecenter_info);
                    cancelLoadingDialog();
                    finish();
                }
            }

            @Override
            public void onError(Object tag, String message) {
                LogUtils.e("个人中心的更新****error****" + message);
                if (tag.equals(API_MeInfoCommit.TAG)) {
                    JsonObjUItils.getJsonCode(message, button_photo, ActivityMine.this, R.string.get_filer);
                }
                cancelLoadingDialog();
            }
        };
    }


    private static final int FLAG_FROM_PHOTO = 100;
    private static final int FLAG_FROM_CARME = 101;
    private static final int FLAG_CROP = 102;
    private int TYPE_PHOTO = 103;
    private int TYPE_CAMERA = 104;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mecenter_fl_header:
                menuPopWindows.show(mecenter_iv_header);
                break;
            case R.id.mecenter_lin_birtday:
//                if (birthday != 0) {
                setPickerViewData(birthday);
//                }
                timeSelectWindows.show(mecenter_tv_birthday);
                break;
            case R.id.mecenter_lin_resetpwd:
                startActivity(ActivityResetPwd.class);
                break;
            case R.id.mecenter_lin_identify:
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.CITIZEN_OR_COMPANY, citizenOrCompany);
                startActivity(ActivityPassIdentify.class, bundle);
                break;
            case R.id.btn_selecttime:
                setAdvTime();
                break;
            case R.id.mecenter_lin_quit:
                quitDialog(v);
                break;
            case R.id.button_pop_photo:
                openPhotosAndCamera(TYPE_PHOTO);
                break;
            case R.id.button_pop_camera:
                openPhotosAndCamera(TYPE_CAMERA);
                break;
            case R.id.button_pop_cancle:
                menuPopWindows.dismiss();
                break;
        }
    }

    private void quitDialog(View view) {
        confirmDialog.show(view);
        confirmDialog.setDialogListener(new DialogListener() {
            @Override
            public void dialogConfirmListener() {
                try {
                    sqLiteCacheManager.deleteNetCache(Constants.CACHE_ACCESS_TOKEN_KEY);
                } catch (DBCacheException e) {
                    e.printStackTrace();
                }
                JPushReceiver.unRegisterSmsBoardCast(BlifeApplication.AppContext);
                activityTask.finishAllActivity();
                startActivity(new Intent(ActivityMine.this, ActivityPasswordLogin.class));
                confirmDialog.dismiss();
            }

            @Override
            public void dialogCacleListener() {
                confirmDialog.dismiss();
            }
        });

    }

    private void setPickerViewData(long data) {
//        if (data <= 0)
//            return;
        String year = DateFormatUtils.getTimeHStr(data * 1000, "yyyy");
        String month = DateFormatUtils.getTimeHStr(data * 1000, "MM");
        String day = DateFormatUtils.getTimeHStr(data * 1000, "dd");
        if (pickerView != null)
            pickerView.setData(year, month, day);
    }

    /**
     * 生日
     */
    private void setAdvTime() {
        mecenter_tv_birthday.setText(pickerView.getCurrentYear() + "-" + pickerView.getCurrentMonth() + "-" + pickerView.getCurrentDay());
        timeSelectWindows.dismiss();
        String time = pickerView.getCurrentYear() + "-" + pickerView.getCurrentMonth() + "-" + pickerView.getCurrentDay();
        birthday = DateFormatUtils.getlongHTime(time, DateFormat) / 1000;
        LogUtils.e("birthday****" + birthday);
    }

    /**
     * 打开相册或相机
     *
     * @param type
     */
    private void openPhotosAndCamera(int type) {
        menuPopWindows.dismiss();
        try {
            compressPath = SDCardUtil.AppCorpDirPath + System.currentTimeMillis() + "_compress.png";
            if (cameraUri == null) {
                String Path = SDCardUtil.AppCorpDirPath + System.currentTimeMillis() + "_camera.png";
                cameraUri = Uri.fromFile(new File(Path));
            }
            if (saveCorpUri == null) {
                String Path = SDCardUtil.AppCorpDirPath + System.currentTimeMillis() + "_corp.png";
                saveCorpUri = Uri.fromFile(new File(Path));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (type == TYPE_PHOTO) {
            openPhotos();
        }
        if (type == TYPE_CAMERA) {
            openCamera();
        }
    }

    /**
     * 打开相册
     */
    private void openPhotos() {
        if (ActivityCompat.checkSelfPermission(instance, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionUtils.showDeniedDialog(getString(R.string.permission_empty_storage));
            return;
        }
        Intent openPhotoIntent = new Intent(Intent.ACTION_PICK);
        openPhotoIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(openPhotoIntent, FLAG_FROM_PHOTO);
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        if (ActivityCompat.checkSelfPermission(instance, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionUtils.showDeniedDialog(getString(R.string.permission_empty_camera));
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的方向
        cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        // 指定调用相机拍照后照片的储存路径
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(cameraIntent, FLAG_FROM_CARME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //拍照成功
            if (resultCode == RESULT_OK && requestCode == FLAG_FROM_CARME && cameraUri != null) {
                startPhotoZoom(cameraUri, 1, 1);
            }
            //剪切图片成功
            if (resultCode == RESULT_OK && requestCode == FLAG_CROP) {
                new CompressTask().execute(new String[]{saveCorpUri.getPath()});
            }
            //选择图片成功
            if (resultCode == RESULT_OK && requestCode == FLAG_FROM_PHOTO) {// selected image
                if (data != null) {
                    startPhotoZoom(data.getData(), 1, 1);
                } else {
                    L.e("选择图片失败");
                }
            }
        } catch (Exception e) {
            L.e("选择图片失败-Exception");
        }
    }

    /**
     * 调用系统裁剪功能
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri, int w, int h) {
        if (saveCorpUri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", w);
        intent.putExtra("aspectY", h);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        //不启用人脸识别
        intent.putExtra("noFaceDetection", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveCorpUri);
        startActivityForResult(intent, FLAG_CROP);
    }

    class CompressTask extends AsyncTask<String, Void, File> {

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null)
                uploadImage(file);
        }

        @Override
        protected File doInBackground(String... params) {
            if (TextUtils.isEmpty(params[0]))
                return null;
            return BitmapManager.saveCompressBitmap(params[0], Constants.COMPRESS_KB_VALUE, compressPath);
        }
    }

    /**
     * 上传文件
     *
     * @param file
     */
    private void uploadImage(File file) {
        showLoadingDialog();
        API_UploadImage api_uploadImage = new API_UploadImage();
        api_uploadImage.setParams(ACCESS_TOKEN, file);
        dataManager.getServiceData(api_uploadImage);
    }

}
