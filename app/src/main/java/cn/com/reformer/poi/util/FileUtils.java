package cn.com.reformer.poi.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016-11-04.
 */
public class FileUtils {

    public static String readFile(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                if (file.exists()) {
                    FileInputStream stream = new FileInputStream(file);
                    byte[] b = new byte[stream.available()];
                    stream.read(b);
                    String strContent = new String(b);
                    return strContent;
                }
            } catch (Exception e) {
                Log.e("readFile", "Error on read File.");
            }
        }
        return null;
    }

    public static void writeFile(String fileName,String message) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),fileName);
            if (!file.exists() && file.createNewFile()) {
                FileOutputStream stream = new FileOutputStream(file);
                byte[] buf = message.getBytes();
                stream.write(buf);
                stream.close();
            }
        } catch (Exception e) {
            Log.e("writeFile", "Error on write File.");
        }
    }
}
