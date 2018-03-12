package cn.com.reformer.poi.event;

import cn.com.reformer.poi.bean.response.ResponseEmplyMsg;
import cn.com.reformer.poi.event.base.EventCode;

/**
 * Created by Administrator on 2017-09-20.
 */

public class QueryEmplyMsgEvent{
    public final EventCode code;
    public final String message;
    public final ResponseEmplyMsg responseEmplyMsg;

    public QueryEmplyMsgEvent(EventCode code, String message, ResponseEmplyMsg responseEmplyMsg) {
        this.code = code;
        this.message = message;
        this.responseEmplyMsg = responseEmplyMsg;
    }
}
