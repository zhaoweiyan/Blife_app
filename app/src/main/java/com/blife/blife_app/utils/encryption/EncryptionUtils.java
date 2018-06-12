package com.blife.blife_app.utils.encryption;

import android.text.TextUtils;
import android.util.Base64;


import com.blife.blife_app.utils.logcat.L;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by w on 2016/7/29.
 */
public class EncryptionUtils {

    public static String CHARSET = "UTF-8";

    /**
     * MD5加密16或32位
     *
     * @param str  待加密的字符串
     * @param is32 是否输出32为加密串
     * @return 加密结果
     */
    public static String MD5(String str, boolean is32) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes(CHARSET));
            byte[] data = messageDigest.digest();
            String result;
            int i;
            StringBuilder builder = new StringBuilder();
            for (int offset = 0; offset < data.length; offset++) {
                i = data[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    builder.append("0");
                }
                builder.append(Integer.toHexString(i));
            }
            result = builder.toString();//32位的加密
            if (!is32) {
                result = result.substring(8, 24);//16位的加密
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            L.e("MD5 encry NoSuchAlgorithmException" + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            L.e("MD5 encry UnsupportedEncodingException" + e.getMessage());
        }
        return str;
    }


    /**
     * BASE64加密
     *
     * @return
     */
    public static String encryptBase64(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            byte[] encode = str.getBytes(CHARSET);
            byte[] result = Base64.encode(encode, 0, encode.length, Base64.DEFAULT);
            return new String(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            L.e("Base64 encry UnsupportedEncodingException" + e.getMessage());
        }
        return str;
    }

    /**
     * BASE64加密
     *
     * @return
     */
    public static String encryptBase64(byte[] encode) {
        if (encode == null) {
            return "";
        }
        byte[] result = Base64.encode(encode, 0, encode.length, Base64.DEFAULT);
        return new String(result);
    }


    /**
     * BASE64解密
     *
     * @return
     */
    public static String decryptBase64(String decode) {
        if (TextUtils.isEmpty(decode)) {
            return decode;
        }
        try {
            byte[] decode1 = decode.getBytes(CHARSET);
            byte[] result = Base64.decode(decode1, 0, decode1.length, Base64.DEFAULT);
            return new String(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            L.e("decryptBase64 encry UnsupportedEncodingException" + e.getMessage());
        }
        return decode;
    }

    /**
     * BASE64解密
     *
     * @return
     */
    public static String decryptBase64(byte[] encode) {
        if (encode == null) {
            return "";
        }
        byte[] result = Base64.decode(encode, 0, encode.length, Base64.DEFAULT);
        return new String(result);
    }

    /**
     * BASE64解密
     *
     * @return
     */
    public static byte[] decryptBase64(String decode, String charsetName) {
        if (TextUtils.isEmpty(decode)) {
            return null;
        }
        try {
            byte[] decode1 = decode.getBytes(charsetName);
            byte[] result = Base64.decode(decode1, 0, decode1.length, Base64.DEFAULT);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            L.e("decryptBase64 encry UnsupportedEncodingException" + e.getMessage());
        }
        return null;
    }

    /**
     * SHA1加密16或32位
     *
     * @param str 待加密的字符串
     * @return 加密结果
     */
    public static String SHA1(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(str.getBytes(CHARSET));
            byte[] data = messageDigest.digest();
            return byte2hex(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            L.e("SHA1 encry UnsupportedEncodingException" + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            L.e("SHA1 encry NoSuchAlgorithmException" + e.getMessage());
        }
        return str;
    }

    public static String byte2hex(byte[] b) {
        String result = "";
        String temp = "";
        for (int n = 0; n < b.length; n++) {
            temp = (Integer.toHexString(b[n] & 0XFF));
            if (temp.length() == 1) {
                result = result + "0" + temp;
            } else {
                result = result + temp;
            }
        }
        return result;
    }

    public static void setCHARSET(String CHARSET) {
        EncryptionUtils.CHARSET = CHARSET;
    }
}
