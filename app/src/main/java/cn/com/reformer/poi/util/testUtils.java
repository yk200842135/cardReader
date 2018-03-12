package cn.com.reformer.poi.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2018-03-12.
 */

public class testUtils {
    public static void main(String[] args)throws UnsupportedEncodingException{
        byte[] b_gbk = "深圳".getBytes("GBK");
        byte[] b_utf8 = "深圳".getBytes("UTF-8");
        byte[] b_iso88591 = "深圳".getBytes("ISO8859-1");
        byte[] b_unicode = "深圳".getBytes("unicode");

        System.out.println("深圳:"+"深圳".length());
        System.out.println("b_gbk:"+b_gbk.length);
        System.out.println("b_utf8:"+b_utf8.length);
        System.out.println("b_iso88591:"+b_iso88591.length);
        System.out.println("b_unicode:"+b_unicode.length);
    }
}
