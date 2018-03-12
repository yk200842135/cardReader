package cn.com.reformer.poi.bean.response;

/**
 * Created by Administrator on 2016-09-09.
 */
public class ResponseGetNewApk {
    private int result;
    private String resultInfo;
    private int version;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
