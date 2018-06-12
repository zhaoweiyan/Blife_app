package com.blife.blife_app.mine.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advsend.api.API_UploadImage;
import com.blife.blife_app.adv.advsend.bean.BeanUploadImage;
import com.blife.blife_app.base.fragment.BaseFragment;
import com.blife.blife_app.mine.api.API_GetCitizeninfo;
import com.blife.blife_app.mine.api.API_IdentifyCitizen;
import com.blife.blife_app.mine.api.API_IdentifyCitizenInfo;
import com.blife.blife_app.mine.bean.BeanIdentify;
import com.blife.blife_app.mine.bean.BeanVerfyResult;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.SDCardUtil;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.permissionutil.InterfacePermissionResult;
import com.blife.blife_app.tools.permissionutil.PermissionUtils;
import com.blife.blife_app.tools.view.MenuPopWindows;
import com.blife.blife_app.utils.bitmap.BitmapManager;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.blife.blife_app.utils.net.request.UIResultCallback;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.lidroid.xutils.BitmapUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Somebody on 2016/9/13.
 */
public class FragmentIdentifyCitizen extends BaseFragment implements View.OnClickListener, InterfacePermissionResult {
    private FrameLayout identify_fl_face, identify_fl_inverse;
    private ImageView identify_iv_face, identify_iv_inverse;
    private EditText identify_et_realName, identify_et_card;
    private Button button_photo;
    private Button button_carmer, button_cancel;
    private String compressPath;
    private Uri cameraUri;
    private Uri saveCorpUri;
    private List<String> uploadImagepath1 = new ArrayList<>();
    private List<String> uploadImagepath2 = new ArrayList<>();
    private TextView identify_tv_commit;
    private TextView identify_tv_face, identify_tv_inverse;
    private TextView identify_warning;
    private TextView citizen_tv_identify_fail;
    private PermissionUtils permissionUtils;

    @Override
    public void init() {
        showLoadingDialog();
        initView();
        initClick();
        initGetCitizenInfo();
        checkPermission();
    }

    private void checkPermission() {
        permissionUtils = new PermissionUtils(instance, getActivity());
        permissionUtils.setInterfacePermissionResult(this);
        permissionUtils.checkPermission(Manifest.permission.CAMERA, 103);
    }

    private void initGetCitizenInfo() {
        API_GetCitizeninfo api_getCitizeninfo = new API_GetCitizeninfo(ACCESS_TOKEN);
        dataManager.getServiceData(api_getCitizeninfo);
    }

    private void initClick() {
        identify_fl_face.setOnClickListener(this);
        identify_fl_inverse.setOnClickListener(this);
        menuPopWindows = new MenuPopWindows(instance, R.layout.pop_usericon);
        button_photo = (Button) menuPopWindows.getRootView().findViewById(R.id.button_pop_photo);
        button_carmer = (Button) menuPopWindows.getRootView().findViewById(R.id.button_pop_camera);
        button_cancel = (Button) menuPopWindows.getRootView().findViewById(R.id.button_pop_cancle);
        button_photo.setOnClickListener(this);
        button_carmer.setOnClickListener(this);
        button_cancel.setOnClickListener(this);
        identify_tv_commit.setOnClickListener(this);


        identify_et_realName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(identify_et_realName.getText().toString().trim()) && !TextUtils.isEmpty(identify_et_card.getText().toString().trim()) && uploadImagepath1.size() >= 1 && uploadImagepath2.size() >= 1) {
                    identify_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
                    identify_tv_commit.setEnabled(true);
                } else {
                    identify_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
                    identify_tv_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        identify_et_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(identify_et_realName.getText().toString().trim()) && !TextUtils.isEmpty(identify_et_card.getText().toString().trim()) && uploadImagepath1.size() >= 1 && uploadImagepath2.size() >= 1) {
                    identify_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
                    identify_tv_commit.setEnabled(true);
                } else {
                    identify_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
                    identify_tv_commit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initView() {
        //照相
        identify_fl_face = (FrameLayout) rootView.findViewById(R.id.identify_fl_face);
        identify_fl_inverse = (FrameLayout) rootView.findViewById(R.id.identify_fl_inverse);
        identify_iv_face = (ImageView) rootView.findViewById(R.id.identify_iv_face);
        identify_iv_inverse = (ImageView) rootView.findViewById(R.id.identify_iv_inverse);

        //其他
        citizen_tv_identify_fail = (TextView) rootView.findViewById(R.id.citizen_tv_identify_fail);
        identify_et_realName = (EditText) rootView.findViewById(R.id.identify_et_realName);
        identify_et_card = (EditText) rootView.findViewById(R.id.identify_et_card);
        identify_tv_commit = (TextView) rootView.findViewById(R.id.identify_tv_commit);
        identify_tv_face = (TextView) rootView.findViewById(R.id.identify_tv_face);
        identify_tv_inverse = (TextView) rootView.findViewById(R.id.identify_tv_inverse);
        identify_warning = (TextView) rootView.findViewById(R.id.identify_warning);

        String[] str = {getResources().getString(R.string.identify_warining), getResources().getString(R.string.identify_phone)};
        int[] color = new int[]{getResources().getColor(R.color.colorLoginAgreement), getResources().getColor(R.color.colorRedLoginNormal)};
        SpannableStringBuilder spannableStringBuilder = StringUtils.setDiffColorText(str, color);
        identify_warning.setText(spannableStringBuilder);


        if (!TextUtils.isEmpty(identify_et_realName.getText().toString().trim()) && !TextUtils.isEmpty(identify_et_card.getText().toString().trim()) && uploadImagepath1.size() == 2) {
            identify_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
            identify_tv_commit.setEnabled(true);
        } else {
            identify_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
            identify_tv_commit.setEnabled(false);
        }

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_identify_citizen;
    }

    //当前点击的图片

    private ImageView addPicImageview;
    private MenuPopWindows menuPopWindows;
    private static final int FLAG_FROM_PHOTO = 100;
    private static final int FLAG_FROM_CARME = 101;
    private static final int FLAG_CROP = 102;
    private int TYPE_PHOTO = 103;
    private int TYPE_CAMERA = 104;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.identify_fl_face:
                addPicImageview = identify_iv_face;
                menuPopWindows.show(addPicImageview);
                break;
            case R.id.identify_fl_inverse:
                addPicImageview = identify_iv_inverse;
                menuPopWindows.show(addPicImageview);
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
            case R.id.identify_tv_commit:
                identifyCommit();
                break;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            //拍照成功
            if (resultCode == getActivity().RESULT_OK && requestCode == FLAG_FROM_CARME && cameraUri != null) {
                startPhotoZoom(cameraUri, 8, 5);
            }
            //剪切图片成功
            if (resultCode == getActivity().RESULT_OK && requestCode == FLAG_CROP) {
                new CompressTask().execute(new String[]{saveCorpUri.getPath()});
            }
            //选择图片成功
            if (resultCode == getActivity().RESULT_OK && requestCode == FLAG_FROM_PHOTO) {// selected image
                if (data != null) {
                    startPhotoZoom(data.getData(), 8, 5);
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


    /**
     * 广告信息Json上传
     */
    private void identifyCommit() {
        if (TextUtils.isEmpty(uploadImagepath1.get(0)) || TextUtils.isEmpty(uploadImagepath2.get(0))) {
            showFailedDialog(R.string.uploadadv_two_images, identify_tv_commit);
            return;
        }
        String name = identify_et_realName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showFailedDialog(R.string.uploadadv_empty_citizen, identify_tv_commit);
            return;
        }
        String card = identify_et_card.getText().toString().trim();
        if (TextUtils.isEmpty(card)) {
            showFailedDialog(R.string.uploadadv_empty_card, identify_tv_commit);
            return;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("iden_id", card);
            object.put("name", name);
            JSONObject contentObject = new JSONObject();
            JSONArray array = new JSONArray();
//            for (String path : uploadImagepath) {
//                array.put(path);
//            }
            array.put(uploadImagepath1.get(0));
            array.put(uploadImagepath2.get(0));
            contentObject.put("images", array);
            object.put("extra_content", contentObject);
        } catch (JSONException e) {
            e.printStackTrace();
            showLoadingDialog();
        }
        String json = object.toString();
        showLoadingDialog();
        LogUtils.e("JSON++++" + json);
        API_IdentifyCitizenInfo api_identifyCitizenInfo = new API_IdentifyCitizenInfo(ACCESS_TOKEN, json);
        dataManager.getServiceData(api_identifyCitizenInfo);
    }

    @Override
    public UIResultCallback getUIRequestCallback() {
        return new UIResultCallback() {
            @Override
            public void onSuccess(Object tag, String json) {
                if (tag.equals(API_UploadImage.TAG)) {
                    cancelLoadingDialog();
                    LogUtils.e("图片上传****" + json);
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    BeanUploadImage beanUploadImage = (BeanUploadImage) JsonObjUItils.fromJson(json, BeanUploadImage.class);
//                    ImageLoader.getInstance().loadImage(beanUploadImage.getAdvimg(), addPicImageview, true);
                    BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(getActivity());
                    bitmapUtils.display(addPicImageview, beanUploadImage.getAdvimg());
                    addPath(beanUploadImage.getAdvimg());
                }
                if (tag.equals(API_IdentifyCitizenInfo.TAG)) {
                    LogUtils.e("先是公民身份认证****" + json);
                    API_IdentifyCitizen api_identifyCitizen = new API_IdentifyCitizen(ACCESS_TOKEN);
                    dataManager.getServiceData(api_identifyCitizen);
                }
                if (tag.equals(API_IdentifyCitizen.TAG)) {
                    LogUtils.e("最后提交认证****" + json);
                    cancelLoadingDialog();
                    getActivity().finish();
                }
                if (tag.equals(API_GetCitizeninfo.TAG)) {
                    LogUtils.e("获取公民信息 ****" + json);
                    cancelLoadingDialog();
                    if (TextUtils.isEmpty(json)) {
                        JsonObjUItils.isEmptyJson(getActivity());
                        return;
                    }
                    BeanIdentify beanIdentify = (BeanIdentify) JsonObjUItils.fromJson(json, BeanIdentify.class);
                    if (beanIdentify.getInfo().getName() != null) {
                        identify_et_realName.setText(beanIdentify.getInfo().getName().trim());
                    }

                    if (beanIdentify.getInfo().getIden_id() != null) {
                        identify_et_card.setText(beanIdentify.getInfo().getIden_id());
                    }
                    if (beanIdentify.getVerify_result() != null) {
                        BeanVerfyResult verify_result = beanIdentify.getVerify_result();
                        if (verify_result.getVerify_status() == 0) {
                            citizen_tv_identify_fail.setVisibility(View.GONE);
                        } else if (verify_result.getVerify_status() == 1) {
                            citizen_tv_identify_fail.setVisibility(View.GONE);
                        } else if (verify_result.getVerify_status() == 2) {
                            citizen_tv_identify_fail.setVisibility(View.GONE);
                        } else if (verify_result.getVerify_status() == 3) {
                            citizen_tv_identify_fail.setVisibility(View.VISIBLE);
                            if (verify_result.getVerify_reason() != null)
                                citizen_tv_identify_fail.setText("注:" + verify_result.getVerify_reason());
                        } else {
                            citizen_tv_identify_fail.setVisibility(View.GONE);
                        }
                    }

                    if (beanIdentify.getInfo().getExtra_content() != null && beanIdentify.getInfo().getExtra_content().getImages() != null && beanIdentify.getInfo().getExtra_content().getImages().size() >= 2) {
                        BitmapUtils bitmapUtils = BitmapHelp.getBitmapUtils(getActivity());
                        bitmapUtils.display(identify_iv_face, beanIdentify.getInfo().getExtra_content().getImages().get(0).trim());
                        bitmapUtils.display(identify_iv_inverse, beanIdentify.getInfo().getExtra_content().getImages().get(1).trim());
                        uploadImagepath1.add(beanIdentify.getInfo().getExtra_content().getImages().get(0).trim());
                        uploadImagepath2.add(beanIdentify.getInfo().getExtra_content().getImages().get(1).trim());
                        identify_tv_face.setVisibility(View.GONE);
                        identify_tv_inverse.setVisibility(View.GONE);
                    }
                    if (beanIdentify.getInfo().getName() != null && beanIdentify.getInfo().getIden_id() != null && beanIdentify.getInfo().getExtra_content() != null && beanIdentify.getInfo().getExtra_content().getImages() != null && beanIdentify.getInfo().getExtra_content().getImages().size() >= 2) {
                        identify_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
                        identify_tv_commit.setEnabled(true);
                    } else {
                        identify_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
                        identify_tv_commit.setEnabled(false);
                    }
                }
            }

            @Override
            public void onError(Object tag, String message) {
                cancelLoadingDialog();
            }
        };
    }

    /**
     * 收集图片Url
     *
     * @param path
     */
    private void addPath(String path) {
        if (addPicImageview.getId() == identify_iv_face.getId()) {
            if (uploadImagepath1.size() >= 1) {
                uploadImagepath1.remove(0);
                uploadImagepath1.add(0, path);
            } else {
                uploadImagepath1.add(path);
            }
            identify_tv_face.setVisibility(View.GONE);
        }
        if (addPicImageview.getId() == identify_iv_inverse.getId()) {
            if (uploadImagepath2.size() >= 1) {
                uploadImagepath2.remove(0);
                uploadImagepath2.add(0, path);
            } else {
                uploadImagepath2.add(path);
            }
            identify_tv_inverse.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(identify_et_realName.getText().toString().trim()) && !TextUtils.isEmpty(identify_et_card.getText().toString().trim()) && uploadImagepath1.size() >= 1 && uploadImagepath2.size() >= 1) {
            identify_tv_commit.setBackgroundResource(R.drawable.selector_myadv_send);
            identify_tv_commit.setEnabled(true);
        } else {
            identify_tv_commit.setBackgroundResource(R.drawable.shape_myadv_send_defalut);
            identify_tv_commit.setEnabled(false);
        }
    }

}
