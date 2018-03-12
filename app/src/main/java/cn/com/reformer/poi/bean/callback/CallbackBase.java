package cn.com.reformer.poi.bean.callback;

import com.google.gson.Gson;
import cn.com.reformer.poi.bean.response.ResponseBase;
import cn.com.reformer.poi.security.DES;
import cn.com.reformer.poi.util.GsonUtils;
import cn.com.reformer.poi.util.JsonUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Administrator on 2016-11-04.
 */
public abstract class CallbackBase extends Callback<ResponseBase> {
    @Override
    public ResponseBase parseNetworkResponse(Response response) throws IOException {
        String message = response.body().string();
        String string = DES.getDecrypt(message);
        if (JsonUtils.isBadJson(string))
            return null;
        return GsonUtils.getInstance().fromJson(string, ResponseBase.class);
    }
}
