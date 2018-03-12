package cn.com.reformer.poi.event;

/**
 * Created by Administrator on 2016-11-04.
 */
public class MessageEvent {
    public static final String MSG_WAIT = "msg_wait_put_card";
    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }
}
