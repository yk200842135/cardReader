package cn.com.reformer.poi.bean.request;

/**
 * Created by Administrator on 2016-09-09.
 */
public class RequestGetNewApk {
    private String token;
    private String deviceId;
    private int version;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
