package cn.com.reformer.poi.global;


import android.app.Application;
import android.content.Context;
import org.xutils.x;
import android.util.Log;

import cn.com.reformer.poi.db.DaoMaster;
import cn.com.reformer.poi.db.DaoSession;
import cn.com.reformer.poi.util.ByteUtils;
import cn.com.reformer.poi.util.FileUtils;
import cn.com.reformer.poi.util.SPUtils;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-11-04.
 */
public class SysApplication extends Application {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private static final String RFBUS_CONF = "rfPos.conf";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        x.Ext.init(this);
    }

    private void init(){
        SPUtils.init(this);
        Constant.DeviceId = SPUtils.getDeviceId();
        Log.e("init_deivceId",Constant.DeviceId);
        if (Constant.DeviceId.equals("")){
            String content = FileUtils.readFile(RFBUS_CONF);
            boolean needGenerateDeviceId = true;
            if (content != null) {
                Pattern pattern = Pattern.compile("deviceId:\\w{32}[\\s\\S]*");
                Matcher matcher = pattern.matcher(content);
                needGenerateDeviceId = !matcher.matches();
            }
            if (needGenerateDeviceId) {
                UUID uuid = UUID.randomUUID();
                ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
                bb.putLong(uuid.getMostSignificantBits());
                bb.putLong(uuid.getLeastSignificantBits());
                Constant.DeviceId = ByteUtils.byte2HexString(bb.array()).toUpperCase();
            }else {
                Constant.DeviceId = content.substring(9,41);
            }
            SPUtils.setDeviceId(Constant.DeviceId);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(SPUtils.DEVICE_ID).append(":").append(Constant.DeviceId).append("\n");
        FileUtils.writeFile(RFBUS_CONF,sb.toString());
        CrashHandler.getInstance().init(getApplicationContext());
    }

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,"myDb",null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
