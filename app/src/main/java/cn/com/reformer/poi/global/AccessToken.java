package cn.com.reformer.poi.global;

import cn.com.reformer.poi.security.AESEncrypt;
import cn.com.reformer.poi.util.DateUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015-06-17.
 */
public class AccessToken {
    private static final String APP_KEY = "mdp9u4z5roem7w0tcnxrvr49";
    private static final Integer TYPE = 1;
    private static final Integer LIFE = 3600;//有效期   单位 秒
    private static final String APPSECRET = "33ztpd0hxuomtw5jbvl2f383lld0nfr8";

    public static String generate(String serial) {
        Date currentDate = new Timestamp(System.currentTimeMillis());
        Date expires = DateUtils.getDateByOffset(currentDate, Calendar.MINUTE,LIFE/60);

        StringBuilder builder = new StringBuilder();
        builder.append(TYPE).append(LIFE).append(expires.getTime()).append(serial).append(APP_KEY).append(APPSECRET);

        String sign = AESEncrypt.encrypt256(builder.toString());

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(APP_KEY).append("|").append(TYPE).append(".").append(sign).append(".")
                .append(LIFE).append(".").append(expires.getTime()).append("-").append(serial);
        return tokenBuilder.toString();
    }
}
