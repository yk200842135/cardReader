package cn.com.reformer.poi.security;

import android.util.Base64;


/**
 * 功能描述:
 * <p/>
 * 版权所有：杭州立方控股
 * <p/>
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分
 *
 * @author jhon yang 新增日期：2016-10-20
 * @author jhon yang 修改日期：2016-10-20
 * @version 1.0.0
 * @since 1.0.0
 */
public class Base64Util
{
    /**
     * 解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception
    {
        return Base64.decode(key.getBytes(), Base64.DEFAULT);
    }

    /**
     * 加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception
    {
        return Base64.encodeToString(key, Base64.DEFAULT);
    }
}
