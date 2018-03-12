package cn.com.reformer.poi.http;

import android.content.Context;

import cn.com.reformer.poi.R;
import cn.com.reformer.poi.bean.callback.CallbackEmplyMsg;
import cn.com.reformer.poi.bean.callback.CallbackGetApk;
import cn.com.reformer.poi.bean.request.RequestBase;
import cn.com.reformer.poi.bean.request.RequestGetNewApk;
import cn.com.reformer.poi.bean.request.RequestLogin;
import cn.com.reformer.poi.bean.request.RequestQueryEmplyMsg;
import cn.com.reformer.poi.bean.request.RequestSearchDevice;
import cn.com.reformer.poi.bean.callback.CallbackBase;
import cn.com.reformer.poi.bean.response.ResponseBase;
import cn.com.reformer.poi.bean.response.ResponseEmplyMsg;
import cn.com.reformer.poi.bean.response.ResponseGetNewApk;
import cn.com.reformer.poi.event.GetNewApkEvent;
import cn.com.reformer.poi.event.LoginEvent;
import cn.com.reformer.poi.event.LogoutEvent;
import cn.com.reformer.poi.event.QueryEmplyMsgEvent;
import cn.com.reformer.poi.event.base.EventCode;
import cn.com.reformer.poi.event.SearchDeviceEvent;
import cn.com.reformer.poi.global.AccessToken;
import cn.com.reformer.poi.global.Constant;
import cn.com.reformer.poi.security.DES;
import cn.com.reformer.poi.util.AppInfoUtils;
import cn.com.reformer.poi.util.GsonUtils;
import cn.com.reformer.poi.util.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;

/**
 * Created by Administrator on 2016-11-04.
 */
public class KHttp {
    private static final String HTTP = "http://";
//    private static final String PORT = ":80";//8088
    private static final String ACCESSTOKEN = "accessToken";
    private static final String PARAM = "param";
    private static final String SUCCESS = "success";
    private static final String APP_TOKEN = "cn.com.reformer.poi";
    private static final String ERROR_SERVER = "Server error";

    public static String IP = null;
    public static String PORT = null;

    /**
     * 获取URL
     * @param action
     * @return
     */
    private static String getURL(String domain,String action) {
        if (IP == null) {
            IP = SPUtils.getConnectIp();
        }
        if (PORT == null){
            PORT = SPUtils.getConnectPort();
        }
        return new StringBuilder()
                .append(HTTP)
                .append(IP)
                .append(":")
                .append(PORT)
                .append(domain)
                .append(action).toString();
    }

    /**
     * 查找设备
     * @param context
     * @param requestSearchDevice
     */
    public static void searchDevice(Context context,RequestSearchDevice requestSearchDevice){
        if (context == null || requestSearchDevice == null)
            return;
        OkHttpUtils.post()
                .url(getURL(context.getString(R.string.domain),context.getString(R.string.url_searchDevice)))
                .addParams(ACCESSTOKEN, AccessToken.generate(Constant.DeviceId))
                .addParams(PARAM,DES.getEncrypt(GsonUtils.getInstance().toJson(requestSearchDevice)))
                .build().execute(new CallbackBase() {
            @Override
            public void onResponse(ResponseBase response){
                EventBus.getDefault().post(new SearchDeviceEvent(EventCode.SUCCESS,SUCCESS,response));
            }

            @Override
            public void onError(Call call, Exception e) {
                EventBus.getDefault().post(new SearchDeviceEvent(EventCode.ERROR,ERROR_SERVER,null));//e.getMessage()
            }
        });
    }

    /**
     * 登录
     * @param context
     * @param requestLogin
     */
    public static void login(Context context,RequestLogin requestLogin){
        if (context == null || requestLogin == null)
            return;
        OkHttpUtils.post()
                .url(getURL(context.getString(R.string.domain),context.getString(R.string.url_login)))
                .addParams(ACCESSTOKEN, AccessToken.generate(Constant.DeviceId))
                .addParams(PARAM,DES.getEncrypt(GsonUtils.getInstance().toJson(requestLogin)))
                .build().execute(new CallbackBase() {
            @Override
            public void onResponse(ResponseBase response){
                EventBus.getDefault().post(new LoginEvent(EventCode.SUCCESS,SUCCESS,response));
            }

            @Override
            public void onError(Call call, Exception e) {
                EventBus.getDefault().post(new LoginEvent(EventCode.ERROR,ERROR_SERVER,null));
            }
        });
    }


    /**
     * 退出登录
     * @param context
     * @param requestBase
     */
    public static void logout(Context context,RequestBase requestBase){
        if (context == null || requestBase == null)
            return;
        OkHttpUtils.post()
                .url(getURL(context.getString(R.string.domain),context.getString(R.string.url_logout)))
                .addParams(ACCESSTOKEN, AccessToken.generate(Constant.DeviceId))
                .addParams(PARAM,DES.getEncrypt(GsonUtils.getInstance().toJson(requestBase)))
                .build().execute(new CallbackBase() {
            @Override
            public void onResponse(ResponseBase response){
                EventBus.getDefault().post(new LogoutEvent(EventCode.SUCCESS,SUCCESS,response));
            }

            @Override
            public void onError(Call call, Exception e) {
                EventBus.getDefault().post(new LogoutEvent(EventCode.ERROR,ERROR_SERVER,null));
            }
        });
    }

    /**
     * 查询人事信息
     * @param context
     * @param requestQueryEmplyMsg
     */
    public static void queryEmplyMsg(Context context,RequestQueryEmplyMsg requestQueryEmplyMsg){
        if (context == null || requestQueryEmplyMsg == null)
            return;
        OkHttpUtils.post()
                .url(getURL(context.getString(R.string.domain),context.getString(R.string.url_queryEmplyMsg)))
                .addParams(ACCESSTOKEN, AccessToken.generate(Constant.DeviceId))
                .addParams(PARAM,DES.getEncrypt(GsonUtils.getInstance().toJson(requestQueryEmplyMsg)))
                .build().execute(new CallbackEmplyMsg() {
            @Override
            public void onResponse(ResponseEmplyMsg response){
                if (response.getPhotoPath() != null){
                    response.setPhotoPath(new StringBuilder()
                            .append(HTTP)
                            .append(IP)
                            .append(":")
                            .append(PORT)
                            .append("/ocs/")
                            .append(response.getPhotoPath()).toString());
                }
                EventBus.getDefault().post(new QueryEmplyMsgEvent(EventCode.SUCCESS,SUCCESS,response));
            }

            @Override
            public void onError(Call call, Exception e) {
                EventBus.getDefault().post(new QueryEmplyMsgEvent(EventCode.ERROR,ERROR_SERVER,null));
            }
        });
    }

    /**
     * 获取新版本apk
     * @param context
     */
    public static void getNewApk(final Context context) {
        final RequestGetNewApk requestGetNewApk = new RequestGetNewApk();
        requestGetNewApk.setToken(APP_TOKEN);
        requestGetNewApk.setDeviceId(Constant.DeviceId);
        requestGetNewApk.setVersion(AppInfoUtils.getVersionCode(context));
        String param = GsonUtils.getInstance().toJson(requestGetNewApk);
        OkHttpUtils.post()
                .url(context.getString(R.string.url_getNewVersion))
                .addParams("param",param)
                .build().execute(new CallbackGetApk() {
            @Override
            public void onResponse(ResponseGetNewApk responseGetNewApk) {
                EventBus.getDefault().post(new GetNewApkEvent(EventCode.SUCCESS,SUCCESS,responseGetNewApk));
            }

            @Override
            public void onError(Call call, Exception e) {
                EventBus.getDefault().post(new GetNewApkEvent(EventCode.ERROR,ERROR_SERVER,null));
            }
        });
    }
}
