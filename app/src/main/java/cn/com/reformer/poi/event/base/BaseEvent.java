package cn.com.reformer.poi.event.base;

import cn.com.reformer.poi.bean.response.ResponseBase;

/**
 * Created by Administrator on 2016-11-04.
 */
public abstract class BaseEvent {
    public final EventCode code;
    public final String message;
    public final ResponseBase responseBase;

    public BaseEvent(EventCode code, String message, ResponseBase responseBase) {
        this.code = code;
        this.message = message;
        this.responseBase = responseBase;
    }
}
