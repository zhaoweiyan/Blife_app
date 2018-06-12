package com.blife.blife_app.adv.advsend.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.api.API_MyadvDetail;
import com.blife.blife_app.adv.advmine.bean.BeanMyAdvDetail;
import com.blife.blife_app.adv.advsend.api.API_AdvInfo;
import com.blife.blife_app.adv.advsend.api.API_UpdateAdvInfo;
import com.blife.blife_app.adv.advsend.api.API_UploadImage;
import com.blife.blife_app.adv.advsend.bean.BeanADVImage;
import com.blife.blife_app.adv.advsend.bean.BeanAdvInfo;
import com.blife.blife_app.adv.advsend.bean.BeanUploadImage;
import com.blife.blife_app.base.activity.BaseActivity;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.SDCardUtil;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.tools.view.MenuPopWindows;
import com.blife.blife_app.utils.activity.ActivityManager;
import com.blife.blife_app.utils.bitmap.BitmapManager;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.logcat.LogcatManager;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/8/29.
 */
public class ActivitySendAdv extends BaseActivity implements View.OnClickListener, InterfacePermissionResult {

    private ImageView iv_addpic_a, iv_addpic_b, iv_addpic_c, iv_addpic_d;
    private EditText et_adv_businessname, et_adv_title, et_adv_content, et_adv_phone, et_adv_link, et_adv_link_name;
    private LinearLayout lin_adv_location;
    private Button button_adv_next;
    private MenuPopWindows menuPopWindows;
    private Button button_photo, button_carmer, button_cancel;
    private TextView tv_adv_location;
    //图片控件Margin
    private int IMAGE_VIEW_MARGIN_RATIO = 40;
    //图片选择
    private List<ImageView> imageViewList;
    private int CURRENT_ADV_IMAGE_INDEX = 0;
    private List<BeanADVImage> beanADVImageList;
    private int MaxADVImageLength = 3;
    private String compressPath;
    private Uri cameraUri;
    private Uri saveCorpUri;
    private static final int FLAG_FROM_PHOTO = 100;
    private static final int FLAG_FROM_CARME = 101;
    private static final int FLAG_CROP = 102;
    private int TYPE_PHOTO = 103;
    private int TYPE_CAMERA = 104;
    //定位
    private String LocationLat = "", LocationLng = "", LocationAddress = "";
    private int REQUEST_CODE = 105;
    //广告ID
    private String ADV_ID;
    private int ADV_TYPE;
    private long startTime = 0, endTime = 0;

    //检查权限
    private PermissionUtils permissionUtils;
    private String ADV_FIRST_IMAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendadv);
        initBackTopBar(R.string.uploadadv_adv_info);
        activityTask.addFinishActivity(this);
        initView();
        initADVImageUpload();
        initData();
        checkPermission();
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(instance, ActivitySendAdv.this);
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
        initData();
    }

    @Override
    public void onDenied() {
        initData();
    }

    @Override
    public void onNotShowRationale() {
        initData();
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

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ADV_ID = bundle.getString(Constants.CREATE_ADV_ID, "");
            ADV_TYPE = bundle.getInt(Constants.ADV_CURRENT_TYPE);
            if (!TextUtils.isEmpty(ADV_ID)) {
                shardPreferName.setStringData(Constants.ADV_CURRENT_EDITING_ID, ADV_ID);
                getDraftAdvInfo();
            }
        }
    }

    private void initADVImageUpload() {
        beanADVImageList = new ArrayList<>();
        for (int i = 0; i < MaxADVImageLength; i++) {
            BeanADVImage beanADVImage = new BeanADVImage(i, imageViewList.get(i));
            beanADVImageList.add(beanADVImage);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ADV_ID = shardPreferName.getStringData(Constants.ADV_CURRENT_EDITING_ID);
        L.e("TAG", "onResume--ADV_ID:" + ADV_ID);
    }

    /**
     * 初始化View
     */
    private void initView() {
        imageViewList = new ArrayList<>();
        iv_addpic_a = (ImageView) findViewById(R.id.iv_addpic_a);
        iv_addpic_b = (ImageView) findViewById(R.id.iv_addpic_b);
        iv_addpic_c = (ImageView) findViewById(R.id.iv_addpic_c);
        iv_addpic_d = (ImageView) findViewById(R.id.iv_addpic_d);
        imageViewList.add(iv_addpic_a);
        imageViewList.add(iv_addpic_b);
        imageViewList.add(iv_addpic_c);
        et_adv_businessname = (EditText) findViewById(R.id.et_adv_businessname);
        et_adv_title = (EditText) findViewById(R.id.et_adv_title);
        et_adv_content = (EditText) findViewById(R.id.et_adv_content);
        et_adv_phone = (EditText) findViewById(R.id.et_adv_phone);
        et_adv_link = (EditText) findViewById(R.id.et_adv_link);
        et_adv_link_name = (EditText) findViewById(R.id.et_adv_link_name);
        lin_adv_location = (LinearLayout) findViewById(R.id.lin_adv_location);
        button_adv_next = (Button) findViewById(R.id.button_adv_next);
        tv_adv_location = (TextView) findViewById(R.id.tv_adv_location);
        iv_addpic_a.setOnClickListener(this);
        iv_addpic_b.setOnClickListener(this);
        iv_addpic_c.setOnClickListener(this);
        lin_adv_location.setOnClickListener(this);
        button_adv_next.setOnClickListener(this);

        menuPopWindows = new MenuPopWindows(instance, R.layout.pop_usericon);
        button_photo = (Button) menuPopWindows.getRootView().findViewById(R.id.button_pop_photo);
        button_carmer = (Button) menuPopWindows.getRootView().findViewById(R.id.button_pop_camera);
        button_cancel = (Button) menuPopWindows.getRootView().findViewById(R.id.button_pop_cancle);
        button_photo.setOnClickListener(this);
        button_carmer.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
        refreshImageViewLayout();
        Tools.setEmojiAndLengthInputFilter(et_adv_businessname, 18);
        Tools.setEmojiAndLengthInputFilter(et_adv_title, 36);
        Tools.setEmojiAndLengthInputFilter(et_adv_content, 200);
        Tools.setEmojiAndLengthInputFilter(et_adv_link_name, 4);
        Tools.setEmojiInputFilter(et_adv_link);
    }

    /**
     * 重新测量ImageView的宽高（比例3：4）
     */
    private void refreshImageViewLayout() {
        int ScreenWidth = ActivityManager.getScreenWidth(instance);
        int margin = ScreenWidth / IMAGE_VIEW_MARGIN_RATIO;
        int W = (ScreenWidth - margin * 8) / 4;
        int H = W * 4 / 3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = W;
        layoutParams.height = H;
        layoutParams.leftMargin = margin;
        layoutParams.rightMargin = margin;
        iv_addpic_a.setLayoutParams(layoutParams);
        iv_addpic_b.setLayoutParams(layoutParams);
        iv_addpic_c.setLayoutParams(layoutParams);
        iv_addpic_d.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_adv_location:
                if (!TextUtils.isEmpty(LocationAddress) && Double.valueOf(LocationLat) > 0 && Double.valueOf(LocationLng) > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.LOCATION_CURRENT_LAT, LocationLat);
                    bundle.putString(Constants.LOCATION_CURRENT_LNG, LocationLng);
                    bundle.putString(Constants.LOCATION_CURRENT_ADDRESS, LocationAddress);
                    startActivityForResult(ActivityAdvLocation.class, REQUEST_CODE, bundle);
                } else {
                    startActivityForResult(ActivityAdvLocation.class, REQUEST_CODE);
                }
                break;
            case R.id.button_adv_next:
                postData();
                break;
            case R.id.iv_addpic_a:
                createUploadADVImage(0);
                break;
            case R.id.iv_addpic_b:
                createUploadADVImage(1);
                break;
            case R.id.iv_addpic_c:
                createUploadADVImage(2);
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

    private void createUploadADVImage(int index) {
        CURRENT_ADV_IMAGE_INDEX = index;
        menuPopWindows.show(beanADVImageList.get(index).getImageView());
    }

    /**
     * 获取广告信息
     */
    private void getDraftAdvInfo() {
        showLoadingDialog();
        API_MyadvDetail api_myadvDetail = new API_MyadvDetail(ACCESS_TOKEN, ADV_ID);
        dataManager.getServiceData(api_myadvDetail);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                LogUtils.e("uploadImage****上传不知是否成功****" + json);
                if (tag.equals(API_UploadImage.TAG)) {
                    LogUtils.e("uploadImage****上传成功");
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivitySendAdv.this);
                        return;
                    }
                    BeanUploadImage beanUploadImage = (BeanUploadImage) JsonObjUItils.fromJson(json, BeanUploadImage.class);
                    L.e("TAG", "图片上传成功" + beanUploadImage.getAdvimg());
                    BeanADVImage beanADVImage = beanADVImageList.get(CURRENT_ADV_IMAGE_INDEX);
                    beanADVImage.setNetPath(beanUploadImage.getAdvimg());
                    beanADVImage.getImageView().setImageBitmap(BitmapManager.compressBitmap(beanADVImage.getDiskPath(), 2));
                }
                if (tag.equals(API_AdvInfo.TAG)) {
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivitySendAdv.this);
                        return;
                    }
                    L.e("TAG", "广告创建：" + json);
                    BeanAdvInfo beanAdvInfo = (BeanAdvInfo) JsonObjUItils.fromJson(json, BeanAdvInfo.class);
                    String AdvID = beanAdvInfo.getInfo().getAdv_id();
                    L.e("TAG", "广告ID===:" + AdvID);
                    shardPreferName.setStringData(Constants.ADV_CURRENT_EDITING_ID, AdvID);
                    shardPreferName.setIntData(Constants.ADV_CURRENT_EDITING_TYPE, Constants.ADV_CURRENT_TYPE_CREATE);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.ADV_CURRENT_FIRST_IMAGE, ADV_FIRST_IMAGE);
                    startActivity(ActivityBonusSetting.class, bundle);
                }
                if (tag.equals(API_MyadvDetail.TAG)) {
                    cancelLoadingDialog();
                    L.e("TAG", "广告草稿JSON000：" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivitySendAdv.this);
                        return;
                    }
                    BeanMyAdvDetail beanMyAdvDetail = (BeanMyAdvDetail) JsonObjUItils.fromJson(json, BeanMyAdvDetail.class);
                    setDraftAdvInfo(beanMyAdvDetail);
                }
                if (tag.equals(API_UpdateAdvInfo.TAG)) {
                    cancelLoadingDialog();
                    L.e("TAG", "草稿广告ID===:" + ADV_ID);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(ActivitySendAdv.this);
                        return;
                    }
                    shardPreferName.setStringData(Constants.ADV_CURRENT_EDITING_ID, ADV_ID);
                    shardPreferName.setIntData(Constants.ADV_CURRENT_EDITING_TYPE, Constants.ADV_CURRENT_TYPE_DRAFT);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.ADV_CURRENT_FIRST_IMAGE, ADV_FIRST_IMAGE);
                    startActivity(ActivityBonusSetting.class, bundle);
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
                if (tag.equals(API_UploadImage.TAG)) {
                    JsonObjUItils.getJsonCode(message, button_adv_next, ActivitySendAdv.this, R.string.get_filer);
                } else if (tag.equals(API_UpdateAdvInfo.TAG)) {
                    JsonObjUItils.getJsonCode(message, button_adv_next, ActivitySendAdv.this, R.string.get_filer);
                } else if (tag.equals(API_AdvInfo.TAG)) {
                    JsonObjUItils.getJsonCode(message, button_adv_next, ActivitySendAdv.this, R.string.get_filer);
                }
                LogUtils.e("uploadImage****上传错误****" + message);
            }
        };
    }

    private void setDraftAdvInfo(BeanMyAdvDetail beanMyAdvDetail) {
        et_adv_businessname.setText(beanMyAdvDetail.getInfo().getPub_name());
        et_adv_title.setText(beanMyAdvDetail.getInfo().getTitle());
        et_adv_content.setText(beanMyAdvDetail.getInfo().getDescription());
        et_adv_phone.setText(beanMyAdvDetail.getInfo().getContent().getContact_phone());
        et_adv_link_name.setText(beanMyAdvDetail.getInfo().getContent().getLink_label());
        et_adv_link.setText(beanMyAdvDetail.getInfo().getContent().getLink());
        LocationLat = beanMyAdvDetail.getInfo().getContact_lat() + "";
        LocationLng = beanMyAdvDetail.getInfo().getContact_lng() + "";
        LocationAddress = beanMyAdvDetail.getInfo().getContact_address();
        tv_adv_location.setText(LocationAddress);
        List<String> imagesList = beanMyAdvDetail.getInfo().getContent().getImages();
        if (imagesList.size() > 0) {
            for (int i = 0; i < imagesList.size(); i++) {
                BeanADVImage bean = beanADVImageList.get(i);
                bean.setNetPath(imagesList.get(i));
                ImageLoader.getInstance().loadImage(imagesList.get(i), bean.getImageView(), true);
            }
        }
        startTime = Long.valueOf(beanMyAdvDetail.getInfo().getPub_begin_time());
        endTime = Long.valueOf(beanMyAdvDetail.getInfo().getPub_end_time());
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

    /**
     * 广告信息Json上传
     */
    private void postData() {
        JSONArray array = new JSONArray();
        for (BeanADVImage bean : beanADVImageList) {
            String netPath = bean.getNetPath();
            if (!TextUtils.isEmpty(netPath)) {
                array.put(bean.getNetPath());
            }
        }
        try {
            ADV_FIRST_IMAGE = array.get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (array.length() <= 0) {
            showFailedDialog(R.string.uploadadv_empty_images, button_adv_next);
            return;
        }
        String name = et_adv_businessname.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showFailedDialog(R.string.uploadadv_empty_businessname, button_adv_next);
            return;
        }
        String title = et_adv_title.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            showFailedDialog(R.string.uploadadv_empty_title, button_adv_next);
            return;
        }
        String content = et_adv_content.getText().toString().trim();
        String phone = et_adv_phone.getText().toString().trim();
        String link = et_adv_link.getText().toString().trim();
        if (!TextUtils.isEmpty(link) && !StringUtils.isHttp(link)) {
            showFailedDialog(R.string.uploadadv_error_link, button_adv_next);
            return;
        }
        String linkName = et_adv_link_name.getText().toString().trim();
        JSONObject object = new JSONObject();
        try {
            if (!TextUtils.isEmpty(ADV_ID) && ADV_TYPE != Constants.ADV_CURRENT_TYPE_AGAIN) {
                object.put("adv_id", ADV_ID);
            }
            object.put("title", title);
            object.put("description", content);
            object.put("name", name);
            if (!TextUtils.isEmpty(ADV_ID)) {
                if (ADV_TYPE == Constants.ADV_CURRENT_TYPE_AGAIN) {
                    object.put("begin_time", 0);
                    object.put("end_time", 0);
                } else {
                    object.put("begin_time", startTime);
                    object.put("end_time", endTime);
                }
            } else {
                object.put("begin_time", startTime);
                object.put("end_time", endTime);
            }
            JSONObject contentObject = new JSONObject();
            if (array.length() > 0) {
                contentObject.put("images", array);
            }
            contentObject.put("link", link);
            contentObject.put("link_label", linkName);
            contentObject.put("contact_phone", phone);
            object.put("content", contentObject);
            if (!TextUtils.isEmpty(LocationAddress)) {
                object.put("contact_address", LocationAddress);
                object.put("contact_lat", LocationLat);
                object.put("contact_lng", LocationLng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = object.toString();
        L.e("JSON++++", json);
        if (!TextUtils.isEmpty(ADV_ID) && ADV_TYPE != Constants.ADV_CURRENT_TYPE_AGAIN) {
            L.e("JSON++++", "广告更新");
            showLoadingDialog();
            API_UpdateAdvInfo api_updateAdvInfo = new API_UpdateAdvInfo(ACCESS_TOKEN, json);
            dataManager.getServiceData(api_updateAdvInfo);
        } else {
            L.e("JSON++++", "广告创建");
            showLoadingDialog();
            API_AdvInfo api_advInfo = new API_AdvInfo();
            api_advInfo.setAccesstoken(ACCESS_TOKEN, json);
            dataManager.getServiceData(api_advInfo);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                LocationLat = bundle.getString(Constants.LOCATION_RESULT_LAT);
                LocationLng = bundle.getString(Constants.LOCATION_RESULT_LNG);
                LocationAddress = bundle.getString(Constants.LOCATION_RESULT_ADDRESS);
                if (!TextUtils.isEmpty(LocationAddress))
                    tv_adv_location.setText(LocationAddress);
            }
        }
        try {
            //拍照成功
            if (resultCode == RESULT_OK && requestCode == FLAG_FROM_CARME && cameraUri != null) {
                startPhotoZoom(cameraUri, 3, 4);
            }
            //剪切图片成功
            if (resultCode == RESULT_OK && requestCode == FLAG_CROP) {
//                showLoadingDialog();
                new CompressTask().execute(new String[]{saveCorpUri.getPath()});
//                CompressImageTask(saveCorpUri.getPath());

            }
            //选择图片成功
            if (resultCode == RESULT_OK && requestCode == FLAG_FROM_PHOTO) {// selected image
                if (data != null) {
                    startPhotoZoom(data.getData(), 3, 4);
                } else {
                    L.e("选择图片失败");
                }
            }
        } catch (Exception e) {
            L.e("选择图片失败-Exception");
        }
    }

    private void CompressImageTask(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                File file = BitmapManager.saveCompressBitmap(path, Constants.COMPRESS_KB_VALUE, compressPath);
                File file = BitmapManager.savePNGBitmap(BitmapManager.compressBitmap(path, 3), compressPath);
                if (file != null)
                    uploadImage(file);
            }
        }).start();
    }

    class CompressTask extends AsyncTask<String, Void, File> {

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                uploadImage(file);
            }
        }
        @Override
        protected File doInBackground(String... params) {
            if (TextUtils.isEmpty(params[0])) {
                return null;
            }
            beanADVImageList.get(CURRENT_ADV_IMAGE_INDEX).setDiskPath(params[0]);
            return BitmapManager.saveCompressBitmap(BitmapManager.compressBitmap(params[0], 2), Constants.COMPRESS_KB_VALUE, compressPath);
//            return BitmapManager.savePNGBitmap(BitmapManager.compressBitmap(params[0], 2), compressPath);
//            return BitmapManager.saveCompressBitmap(params[0], Constants.COMPRESS_KB_VALUE, compressPath);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!TextUtils.isEmpty(ADV_ID)) {
            shardPreferName.setStringData(Constants.ADV_CURRENT_EDITING_ID, "");
            shardPreferName.setIntData(Constants.ADV_CURRENT_EDITING_TYPE, 0);
        }
    }

}
