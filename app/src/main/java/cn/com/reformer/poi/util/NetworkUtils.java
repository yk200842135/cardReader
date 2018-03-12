package cn.com.reformer.poi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Administrator on 2016-11-04.
 */
public class NetworkUtils {
    /**
     * 检查当前手机网络
     *
     * @param context
     * @return
     */
    public static boolean checkNetState(Context context) {
        // 判断连接方式
        boolean wifiConnected = isWIFIConnected(context);
        boolean mobileConnected = isMOBILEConnected(context);
        if (wifiConnected == false && mobileConnected == false) {
            // 如果都没有连接返回false，提示用户当前没有网络
            return false;
        }
        return true;
    }

    /**
     * 判断手机是否采用wifi连接
     */
    public static boolean isWIFIConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断手机是否采用3G/2G连接
     */
    public static boolean isMOBILEConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    /**
     * ping阻塞结果
     * @return
     */
    public static final boolean ping() {
        Log.d("----result---", "start");
        String result = "default";
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 3 " + ip);// ping网址3次
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            Log.d("----result---", "result = " + result);
        }
        return false;
    }
}
