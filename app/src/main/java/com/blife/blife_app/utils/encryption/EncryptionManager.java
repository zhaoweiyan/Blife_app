package com.blife.blife_app.utils.encryption;

/**
 * Created by w on 2016/7/22.
 */
public class EncryptionManager {

    /**
     * MD5加密32位
     *
     * @param str 待加密的字符串
     * @return 加密结果
     */
    public static String MD5_32(String str) {
        return EncryptionUtils.MD5(str, true);
    }

    /**
     * MD5加密16位
     *
     * @param str 待加密的字符串
     * @return 加密结果
     */
    public static String MD5_16(String str) {
        return EncryptionUtils.MD5(str, false);
    }

    /**
     * Base64加密
     *
     * @param code 待编码串
     * @return 编码结果
     */
    public static String encodeBase64(String code) {
        return EncryptionUtils.encryptBase64(code);
    }

    /**
     * @param encode 待解码串
     * @return 解码结果
     */
    public static String decodeBase64(String encode) {
        return EncryptionUtils.decryptBase64(encode);
    }

    /**
     * SHA1加密
     *
     * @param code 待加密串
     * @return 加密结果
     */
    public static String SHA1(String code) {
        return EncryptionUtils.SHA1(code);
    }

    /**
     * AES 加密
     *
     * @param code 待加密串
     * @param key  密钥
     * @return 加密结果
     */
    public static String AESEncode(String code, String key) {
        return AESOperator.encrypt(key, code);
    }

    /**
     * AES解密
     *
     * @param encode 加密串
     * @param key    密钥
     * @return 解密结果
     */
    public static String AESDecode(String encode, String key) {
        return AESOperator.decrypt(key, encode);
    }


}
