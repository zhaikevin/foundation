package com.github.foundation.authentication.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;

/**
 * BASE64加密解密工具类
 */
public final class MD5Utils {

    private static final String UTF = "utf-8";

    private static final String MD5 = "PBEWithMD5AndDES";

    private static final int ITERATIONCOUNT = 1000;

    /**
     * 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key, String salt) throws IOException, Exception {
        if (data == null) {
            return null;
        }

        byte[] buf = Base64.decodeBase64(data);
        byte[] bt = decrypt(buf, key, salt.getBytes(UTF));
        return new String(bt);
    }

    /**
     * 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, String key, byte[] salt) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray());
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MD5);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(MD5);
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATIONCOUNT);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        return cipher.doFinal(data);
    }

    /**
     * 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key, String salt) throws Exception {
        byte[] bt = encrypt(data.getBytes(UTF), key, salt.getBytes(UTF));
        String strs = Base64.encodeBase64String(bt);
        return strs;
    }

    /**
     * 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, String key, byte[] salt) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray());
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MD5);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(MD5);
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATIONCOUNT);
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        return cipher.doFinal(data);
    }

    public static void main(String args[]) throws Exception {
        String md5 = encrypt("zhai", "kevin", "qwertyui");
        System.out.println(md5);
        String password = decrypt(md5, "kevin", "qwertyui");
        System.out.println(password);
    }
}
