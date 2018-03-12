package cn.com.reformer.poi.event;

import cn.com.reformer.poi.bean.response.ResponseBase;
import cn.com.reformer.poi.event.base.BaseEvent;
import cn.com.reformer.poi.event.base.EventCode;

/**
 * Created by Administrator on 2016-11-04.
 */
public class LoginEvent extends BaseEvent {
    public LoginEvent(EventCode code, String message, ResponseBase responseBase) {
        super(code,message,responseBase);
    }
}
