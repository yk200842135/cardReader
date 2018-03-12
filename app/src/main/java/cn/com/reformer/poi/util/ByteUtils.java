package cn.com.reformer.poi.util;

/**
 * Created by Administrator on 2016-11-03.
 */
public class ByteUtils {

    public static String byte2HexString(byte b[]) {
        return byte2HexString(b,"");
    }

    public static String byte2HexString(byte b[],String div) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
            if (i < b.length - 1)
                hexString.append(div);
        }
        return hexString.toString().toUpperCase();
    }

    public static byte[] hexString2Byte(String ss) {
        return hexString2Byte(ss, "");
    }

    public static byte[] hexString2Byte(String ss,String div) {
        if (div != null && !div.equals("")) {
            ss = ss.replaceAll(div, "");
        }
        byte digest[] = new byte[ss.length() / 2];
        for(int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte)byteValue;
        }
        return digest;
    }

    public static byte[] AscllString2Byte(String ss) {
        byte digest[] = new byte[ss.length()];
        for(int i = 0; i < digest.length; i++) {
            char c = ss.charAt( i);
            digest[i] = (byte)c;
        }
        return digest;
    }

    public static void main(String args[]){

    }
}
