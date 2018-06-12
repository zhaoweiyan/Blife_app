package com.blife.blife_app.tools.http;

import com.blife.blife_app.utils.encryption.EncryptionUtils;
import com.blife.blife_app.utils.logcat.L;
import com.blife.blife_app.utils.util.NumberUtils;

/**
 * Created by w on 2016/8/22.
 */
public class HttpUtil {


    /**
     * 获取服务器Common配置签名
     *
     * @return
     */
    public static String sign(String random, String time) {
        L.e("随机数:" + random);
        L.e("时间戳:" + time);
        String code = Constants.APP_ID + Constants.APP_SECRET_KEY + random + time;
        L.e("加密码:" + code);
        String enCode = EncryptionUtils.SHA1(code);
        L.e("结果:" + enCode);
        return enCode;
    }

}
