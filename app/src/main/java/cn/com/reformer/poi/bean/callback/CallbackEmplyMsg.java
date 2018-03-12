package cn.com.reformer.poi.bean.callback;

import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import cn.com.reformer.poi.bean.response.ResponseEmplyMsg;
import cn.com.reformer.poi.security.DES;
import cn.com.reformer.poi.util.GsonUtils;
import cn.com.reformer.poi.util.JsonUtils;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-09-19.
 */

public abstract class CallbackEmplyMsg extends Callback<ResponseEmplyMsg> {
    @Override
    public ResponseEmplyMsg parseNetworkResponse(Response response) throws IOException {
        String message = response.body().string();
        String string = DES.getDecrypt(message);
        if (JsonUtils.isBadJson(string))
            return null;
        return GsonUtils.getInstance().fromJson(string, ResponseEmplyMsg.class);
    }
}
