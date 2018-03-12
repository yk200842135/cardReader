package cn.com.reformer.poi.security;

import cn.com.reformer.poi.util.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Administrator on 2016-11-02.
 */
public class DES {
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private static final String DESKEY = "H9Z8L7F6";
    //解密数据
    public static String decrypt(String message,String key) throws Exception {

        byte[] bytesrc = ByteUtils.hexString2Byte(message);
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    public static byte[] encrypt(String message, String key)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    public static String getEncrypt(String message) {
        try {
            return ByteUtils.byte2HexString(DES.encrypt(message, DESKEY)).toUpperCase();
        }catch (Exception e){
            return "";
        }
    }

    public static String getDecrypt(String message) {
        try {
            return decrypt(message,DESKEY);
        }catch (Exception e){
            return "";
        }
    }


    public static void main(String[] args) throws Exception {

        String p = "0123456789abcdef1";
        String strp = ByteUtils.byte2HexString(DES.encrypt(p, DESKEY)).toUpperCase();
        System.out.println(strp);

        strp = "003CA59185FD40B0083674CFEE3A9FD6F914294D2022100C953B73C81D3EEABD33A7BF119DB14AE8700856BE1DECC75255C72384F42D0FE3003CA59185FD40B0A89FA7738CB158ED8203294A215370FC33504B2637C0E84484CFBC880B24863F882A2B5C494D69A0";
        String c = decrypt(strp,DESKEY);
        System.out.println(c);
    }

}
