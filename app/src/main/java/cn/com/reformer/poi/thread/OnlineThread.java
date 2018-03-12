package cn.com.reformer.poi.thread;

import cn.com.reformer.poi.global.Constant;
import cn.com.reformer.poi.util.DateUtils;
import cn.com.reformer.poi.util.GsonUtils;
import cn.com.reformer.poi.util.SPUtils;
import greendao.LoginRecord;

/**
 * Created by Administrator on 2016-11-10.
 */
public class OnlineThread extends Thread {

    @Override
    public void run() {
        super.run();
        while(!isInterrupted()) {
            try {
                LoginRecord loginRecord = new LoginRecord();
                loginRecord.setState(1);
                loginRecord.setUser(Constant.UserName);
                loginRecord.setTime(DateUtils.getCurrentDate(DateUtils.dateFormatYMDHM));
                SPUtils.setLastOnlineInfo(GsonUtils.getInstance().toJson(loginRecord));
                Thread.sleep(60 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
