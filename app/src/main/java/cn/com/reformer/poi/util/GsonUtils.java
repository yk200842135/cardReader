package cn.com.reformer.poi.util;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016-11-03.
 */
public class GsonUtils {
    private static Gson gson = new Gson();
    public static Gson getInstance(){
        return gson ;
    }

    public static <T> T fromJson(String content, Class<T> clz) {
        if (JsonUtils.isBadJson(content))
            return null;
        return gson.fromJson(content,clz);
    }
}
