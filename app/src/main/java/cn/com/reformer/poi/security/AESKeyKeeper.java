package cn.com.reformer.poi.security;


import android.util.Log;

import cn.com.reformer.poi.util.ByteUtils;

/**
 * 功能描述:
 * <p/>
 * 版权所有：杭州立方控股
 * <p/>
 * 未经本公司许可，不得以任何方式复制或使用本程序任何部分
 *
 * @author jhon yang 新增日期：2016-10-25
 * @author jhon yang 修改日期：2016-10-25
 * @version 1.0.0
 * @since 1.0.0
 */
public class AESKeyKeeper
{
    private static final String QR_CODE_AES_KEY_BASE64_ENCODE = "Pu8/KhwqHOAermHzQq734g==";
    private byte[] secretKey;

    public void init(KeeperType type, byte[] randData) throws Exception
    {
        if (type == null)
            throw new Exception();

        if (KeeperType.HANDSET_QR_CODE.equals(type))
        {


                byte[] keyBytes = Base64Util.decryptBASE64(QR_CODE_AES_KEY_BASE64_ENCODE.trim());
                int i, loc;
                for (i = 0; i < 4; i++)
                {
                    loc = i * 4 + (randData[i] & 0XFF) % 4;
                    keyBytes[loc] = (byte) ((keyBytes[loc] ^ randData[i]) & 0xFF);
                }
                secretKey = keyBytes;

        } else
            throw new Exception();
    }

    public byte[] getSecretKey()
    {
        return secretKey;
    }

    public enum KeeperType
    {
        HANDSET_QR_CODE;
    }
}
