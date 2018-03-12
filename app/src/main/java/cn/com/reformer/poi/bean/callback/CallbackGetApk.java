package cn.com.reformer.poi.bean.callback;

import com.google.gson.Gson;
import cn.com.reformer.poi.bean.response.ResponseGetNewApk;
import cn.com.reformer.poi.security.DES;
import cn.com.reformer.poi.util.GsonUtils;
import cn.com.reformer.poi.util.JsonUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by Administrator on 2016-09-09.
 */
public abstract class CallbackGetApk extends Callback<ResponseGetNewApk> {
    @Override
    public ResponseGetNewApk parseNetworkResponse(Response response) throws IOException {
        String message = response.body().string();
        if (JsonUtils.isBadJson(message))
            return null;
        return GsonUtils.getInstance().fromJson(message, ResponseGetNewApk.class);
    }
}
