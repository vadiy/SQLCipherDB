package vip.vadiy.GetDB.Utils;


import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by AiXin on 2019-9-13.
 */
public class RSAUtils {

    public static final String PUBLICK_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCzwkQh+SVKtIjBOocQ15RmLzvnJ4TKQuM0+EWwVjC7UXPRp/B6ZGx5OUyPnhvXTIUsyHfVa3kixORDM1Bq6gtc5Bon1nZyCrEogsfEe3qK6RSM/hOogMdPnYmpBX72jm5VRyH7ApwtQirPWNihfakMpx73MJkC7ReCaaFpESGWhQIDAQAB";
    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALPCRCH5JUq0iME6hxDXlGYvO+cnhMpC4zT4RbBWMLtRc9Gn8HpkbHk5TI+eG9dMhSzId9VreSLE5EMzUGrqC1zkGifWdnIKsSiCx8R7eorpFIz+E6iAx0+diakFfvaOblVHIfsCnC1CKs9Y2KF9qQynHvcwmQLtF4JpoWkRIZaFAgMBAAECgYEAh8Y7KiICfdvA05p+fEUo7MnOiDk8Pn2pGdBQ6vEfG81ZGsmrhPanTCTwZCX7R3KfwISmpsfnllB7TRrMKgUfkMFz6YtUsxqxncNf3tobRYOvUY+KWWUVLKkt/WjJHvEKeabLf/JOZylhq/l3M3qR5eG3yzEnfNwfINLHY30KED0CQQD5IY6LhagDQIN7Y1JCUlgltdkxULEh4Wgyrs1E0M/HjLNrZd8CA8CwYci10pVxwYDvZsJVncKcND8pJI/+QiRHAkEAuLcPGHwtakK5OVzFNQOQhlSLRUlXNCA5v2WmrsdRZTKjBpYEQVo/oRgWc22pHV9KyO1cWz/blyoztjFHabzQ0wJBAPgMNUUtmo9CWKctyOVH34QMf2fek77ME1cDPFXcIkTpDmtMTrJO0jfL5G9EcI+Gvr2ebreYEAA+9PQd91CMwlkCQBh4V82clb+f+z24Jn/xavIAvTp+jsjfBAdxBfXdfdD0NlinAwVNWWST9lVwT5kOiK+5kiScfxC1jIg0WwuM8fUCQDDKa5gtAPkEl/3JR/iBsURteaJsUyUh5s7kXMpkstN2h5nyjRBSRx8p8fsuP9RGWehW7/XoeouaYxC59yt7XqI=";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * 将base64编码后的公钥字符串转成PublicKey实例
     *
     * @param publicKey 公钥字符
     * @return publicKEY
     * @throws Exception exception
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将base64编码后的私钥字符串转成PrivateKey实例
     *
     * @param privateKey 私钥字符串
     * @return 私钥对象
     * @throws Exception exception
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * RSA加密
     *
     * @param content   待加密文本
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception exception
     */
    public static String encrypt(String content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//java默认"RSA"="RSA/ECB/PKCS1Padding"
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] data = content.getBytes();
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return new String(Base64Utils.encode(encryptedData));
    }

    public static String encrypt(String content) {
        String str = "";
        try {
            PublicKey pk = getPublicKey(PUBLICK_KEY);
            str = encrypt(content, pk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * RSA解密
     *
     * @param content    密文
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception exception
     */
    public static String decrypt(String content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedData = Base64Utils.decode(content);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }

    public static String decrypt(String content) {
        String str = "";
        try {
            PrivateKey pvk = RSAUtils.getPrivateKey(PRIVATE_KEY);
            str = RSAUtils.decrypt(content, pvk);
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


}
