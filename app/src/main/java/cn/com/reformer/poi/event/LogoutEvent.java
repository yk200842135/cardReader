package cn.com.reformer.poi.event;

import cn.com.reformer.poi.bean.response.ResponseBase;
import cn.com.reformer.poi.event.base.BaseEvent;
import cn.com.reformer.poi.event.base.EventCode;

/**
 * Created by Administrator on 2016-11-07.
 */
public class LogoutEvent extends BaseEvent {
    public LogoutEvent(EventCode code, String message, ResponseBase responseBase) {
        super(code,message,responseBase);
    }
}
