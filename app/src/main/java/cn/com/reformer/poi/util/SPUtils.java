package cn.com.reformer.poi.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016-11-04.
 */
public class SPUtils {
    public static final String PREFS_NAME = "pos_prefs.xml";
    public static final String DEVICE_ID = "deviceId";
    public static final String CONNECT_IP = "connect_ip";
    public static final String CONNECT_PORT = "connect_port";
    public static final String MODE_VERSION = "mode_version";
    public static final String MODE_TYPE = "mode_type";
    public static final String MODE_NAME = "mode_name";
    public static final String MODE_CONTENT = "mode_content";
    public static final String LAST_MODE = "last_mode";
    public static final String LAST_ONLINE_INFO = "last_online_info";
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public synchronized static void init(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
            editor = sp.edit();
        }
    }

    // 设备Id
    public static void setDeviceId(String deviceId) {
        if (sp == null)
            return;
        editor.putString(DEVICE_ID, deviceId);
        editor.commit();
    }

    public static String getDeviceId() {
        if (sp == null)
            return "";
        return sp.getString(DEVICE_ID, "");
    }

    //ip
    public static void setConnectIp(String connectIp) {
        if (sp == null)
            return;
        editor.putString(CONNECT_IP, connectIp);
        editor.commit();
    }

    public static String getConnectIp() {
        if (sp == null)
            return "";
        return sp.getString(CONNECT_IP, "");
    }
    //port
    public static void setConnectPort(String port) {
        if (sp == null)
            return;
        editor.putString(CONNECT_PORT, port);
        editor.commit();
    }

    public static String getConnectPort() {
        if (sp == null)
            return "";
        return sp.getString(CONNECT_PORT, "");
    }
    //mode version
    public static void setModeVersion(String modeVersion) {
        if (sp == null)
            return;
        editor.putString(MODE_VERSION, modeVersion);
        editor.commit();
    }

    public static String getModeVersion() {
        if (sp == null)
            return "";
        return sp.getString(MODE_VERSION, "");
    }
    //mode content
    public static void setModeContent(String modeContent) {
        if (sp == null)
            return;
        editor.putString(MODE_CONTENT, modeContent);
        editor.commit();
    }

    public static String getModeContent() {
        if (sp == null)
            return "";
        return sp.getString(MODE_CONTENT, "");
    }
    //mode type
    public static void setModeType(int modeType) {
        if (sp == null)
            return;
        editor.putInt(MODE_TYPE, modeType);
        editor.commit();
    }

    public static int getModeType() {
        if (sp == null)
            return 0;
        return sp.getInt(MODE_TYPE, 0);
    }
    //mode name
    public static void setModeName(String modeName) {
        if (sp == null)
            return;
        editor.putString(MODE_NAME, modeName);
        editor.commit();
    }

    public static String getModeName() {
        if (sp == null)
            return "";
        return sp.getString(MODE_NAME, "");
    }
    //last mode
    public static void setLastMode(int lastMode) {
        if (sp == null)
            return;
        editor.putInt(LAST_MODE, lastMode);
        editor.commit();
    }

    public static int getLastMode() {
        if (sp == null)
            return 0;
        return sp.getInt(LAST_MODE, 0);
    }
    //last online time
    public static void setLastOnlineInfo(String lastOnlineInfo) {
        if (sp == null)
            return;
        editor.putString(LAST_ONLINE_INFO, lastOnlineInfo);
        editor.commit();
    }

    public static String getLastOnlineInfo() {
        if (sp == null)
            return "";
        return sp.getString(LAST_ONLINE_INFO, "");
    }
}
