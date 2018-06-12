package com.blife.blife_app.tools.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.blife.blife_app.application.BlifeApplication;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.net.request.HttpRequest;
import com.blife.blife_app.utils.net.request.UIResultCallback;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by w on 2016/8/26.
 */
public class UploadLocationService extends Service implements UIResultCallback, InterfaceLocationCallback {

    private API_UploadLocation api_uploadLocation;
    private SQLiteCacheManager sqLiteCacheManager;
    private LocationUtil locationUtil;
    private Timer timer;
    private LocationTimeTask timerTask;
    //网络请求
    private HttpRequest httpRequest;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.e("TAG", "onCreate");
        api_uploadLocation = new API_UploadLocation();
        sqLiteCacheManager = SQLiteCacheManager.getInstance(BlifeApplication.AppContext);
        locationUtil = LocationUtil.getInstance(null);
        locationUtil.setShowDialog(false);
        httpRequest = new HttpRequest(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.e("TAG", "onHandleIntent");
        if (timer == null)
            timer = new Timer();
        if (timerTask == null) {
            timerTask = new LocationTimeTask();
            timer.schedule(timerTask, 0, Constants.UPLOAD_LOCATION_TIME);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    class LocationTimeTask extends TimerTask {
        @Override
        public void run() {
            location();
        }
    }


    /**
     * 定位
     */

    private void location() {
        L.e("TAG", "location");
        locationUtil.setShowDialog(false);
        locationUtil.startLocation(this);
    }

    /**
     * 上传位置信息
     */
    private void upload(String lng, String lat) {
        L.e("TAG", "location");
        String AccessToken = "";
        try {
            AccessToken = sqLiteCacheManager.getNetCache(Constants.CACHE_ACCESS_TOKEN_KEY);
        } catch (DBCacheException e) {
            e.printStackTrace();
            AccessToken = "";
        }
        L.e("TAG", "location--AccessToken--" + AccessToken);
        if (TextUtils.isEmpty(AccessToken)) {
            return;
        }
        UploadLocationParams params = new UploadLocationParams();
        params.setACCESS_TOKEN(AccessToken);
        params.setLat(lat);
        params.setLng(lng);
        api_uploadLocation.setParams(params);
        httpRequest.getParams().putAll(api_uploadLocation.Params());
        httpRequest.getHeader().putAll(api_uploadLocation.Header());
        api_uploadLocation.HttpRequest(httpRequest);
    }

    @Override
    public void onLocationSuccess(BDLocation bdLocation) {
        L.e("TAG", "getLngAndLat");
        upload(bdLocation.getLongitude() + "", bdLocation.getLatitude() + "");
    }

    @Override
    public void onLocationError() {
        L.e("TAG", "onError");
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
    public void onSuccess(Object tag, String json) {
        L.e("TAG", "onSuccess");
    }

    @Override
    public void onError(Object tag, String message) {
        L.e("TAG", "onError***");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
