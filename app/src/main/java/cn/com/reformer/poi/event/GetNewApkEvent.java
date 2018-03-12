package cn.com.reformer.poi.event;

import cn.com.reformer.poi.bean.response.ResponseGetNewApk;
import cn.com.reformer.poi.event.base.EventCode;

/**
 * Created by Administrator on 2016-11-10.
 */
public class GetNewApkEvent {
    public final EventCode code;
    public final String message;
    public final ResponseGetNewApk responseGetNewApk;

    public GetNewApkEvent(EventCode code, String message, ResponseGetNewApk responseGetNewApk) {
        this.code = code;
        this.message = message;
        this.responseGetNewApk = responseGetNewApk;
    }
}
