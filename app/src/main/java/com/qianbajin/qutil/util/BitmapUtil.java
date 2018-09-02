package com.qianbajin.qutil.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
/*
 * @des
 * @Created  wWX407408  at 2017/8/21  17:24 
 *
 */
public class BitmapUtil {

    /**
     * 压缩bitmap到文件
     *
     * @param bitmap
     * @param file
     * @return
     */
    public static boolean bitmap2File(Bitmap bitmap, File file) {
        try {
            FileOutputStream fis = new FileOutputStream(file);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            bao.writeTo(fis);
            bao.flush();
            fis.close();
            bao.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将bitmap转成String字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmap2String(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
    }

    /**
     * 将String字符串转成bitmap
     *
     * @param s
     * @return
     */
    public static Bitmap string2Bitmap(String s) {
        byte[] bytes = Base64.decode(s, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
