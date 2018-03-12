package cn.com.reformer.poi.util;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * Created by Administrator on 2016-11-04.
 */
public class JsonUtils {
    public static boolean isBadJson(String json) {
        return !isGoodJson(json);
    }

    public static boolean isGoodJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }
}
