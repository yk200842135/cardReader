package cn.com.reformer.poi.util;

/**
 * Created by Administrator on 2016-11-03.
 */
public class BitUtils {
    public static int gitBit(int num){
        int bit = 1;
        while (num > 9){
            num /= 10;
            bit++;
        }
        return bit;
    }
}
