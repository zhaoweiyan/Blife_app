package com.blife.blife_app.welcome;

import android.content.Context;
import android.text.TextUtils;

import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.utils.cache.SQLiteCacheManager;
import com.blife.blife_app.utils.exception.cacheException.DBCacheException;

/**
 * Created by w on 2016/9/30.
 */
public class CheckTokenStatus {

    private SQLiteCacheManager sqLiteCacheManager;
    //Token
    private String oldToken;

    //回调
    private onTokenStatus onStatus;

    interface onTokenStatus {
        void onEmpty();

        void onRefresh(String token);

        void onContinue();
    }

    public void setOnStatus(onTokenStatus onStatus) {
        this.onStatus = onStatus;
    }

    public CheckTokenStatus(Context context) {
        sqLiteCacheManager = SQLiteCacheManager.getInstance(context);

    }

    public void check() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkAccessTokenExisted();
            }
        }).start();
    }

    /**
     * 检查AccessToken是否存在
     */
    private void checkAccessTokenExisted() {
        try {
            oldToken = sqLiteCacheManager.getNetCache(Constants.CACHE_ACCESS_TOKEN_KEY);
            if (TextUtils.isEmpty(oldToken)) {
                if (onStatus != null) {
                    onStatus.onEmpty();
                }
            } else {
                checkAccessTokenFailureTime(oldToken);
            }
        } catch (DBCacheException e) {
            e.printStackTrace();
            if (onStatus != null) {
                onStatus.onEmpty();
            }
        }
    }

    /**
     * 检token失效时间
     */
    private void checkAccessTokenFailureTime(String token) {
        long clearTime = 0;
        try {
            clearTime = sqLiteCacheManager.getClearTime(Constants.CACHE_ACCESS_TOKEN_KEY);
        } catch (DBCacheException e) {
            e.printStackTrace();
        }
        long current = System.currentTimeMillis();
        if (clearTime - current > Constants.TOKEN_REFRESH_TIME) {
            if (onStatus != null) {
                onStatus.onContinue();
            }
        } else {
            if (onStatus != null) {
                onStatus.onRefresh(token);
            }
        }
    }


}
