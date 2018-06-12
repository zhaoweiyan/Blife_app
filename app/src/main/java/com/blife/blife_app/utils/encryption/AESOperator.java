package com.blife.blife_app.utils.encryption;



import com.blife.blife_app.utils.logcat.L;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by w on 2016/7/29.
 */
public class AESOperator {

    public static String VIPARA = "0000000000000000";
    public static String charsetName = "GBK";

    public static void setVIPARA(String VIPARA) {
        AESOperator.VIPARA = VIPARA;
    }

    public static void setBm(String charsetName) {
        AESOperator.charsetName = charsetName;
    }

    public static String encrypt(String dataPassword, String cleartext) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(cleartext.getBytes(charsetName));
            return EncryptionUtils.encryptBase64(encryptedData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            L.e("AES NoSuchAlgorithmException " + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            L.e("AES InvalidKeyException " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            L.e("AES InvalidAlgorithmParameterException " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            L.e("AES NoSuchPaddingException " + e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            L.e("AES BadPaddingException " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            L.e("AES UnsupportedEncodingException " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            L.e("AES IllegalBlockSizeException " + e.getMessage());
        }
        return dataPassword;
    }

    public static String decrypt(String dataPassword, String encrypted) {
        try {
            byte[] byteMi = EncryptionUtils.decryptBase64(encrypted, charsetName);
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(dataPassword.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData, charsetName);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            L.e("AES NoSuchAlgorithmException " + e.getMessage());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            L.e("AES InvalidKeyException " + e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            L.e("AES InvalidAlgorithmParameterException " + e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            L.e("AES NoSuchPaddingException " + e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            L.e("AES BadPaddingException " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            L.e("AES UnsupportedEncodingException " + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            L.e("AES IllegalBlockSizeException " + e.getMessage());
        }
        return dataPassword;
    }

}
