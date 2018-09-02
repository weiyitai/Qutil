package com.qianbajin.qutil.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPOutputStream;
/*
 * @des
 * @Created  wWX407408  at 2017/8/21  11:49 
 *
 */
public class FileUtil {

    public static final String TAG = "FileUtil";

    /**
     * 将assets目录下的文件拷贝到SD卡根目录,拷贝文件名相同
     *
     * @param context 上下文
     * @param name    文件名
     * @return 是否拷贝成功
     */
    public static boolean copyFile(Context context, String name) {
        boolean success = false;
        try {
            InputStream in = null;
            in = context.getAssets().open(name);
            File file = new File(Environment.getExternalStorageDirectory(), name);
            FileOutputStream out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);

            byte[] buffer = new byte[2048];
            int read;
            while ((read = in.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.flush();
            in.close();
            bos.close();
            success = true;
            Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean copyFile(File srcFile, File desFile) {
        boolean success = false;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(srcFile));
            bos = new BufferedOutputStream(new FileOutputStream(desFile));
            byte[] bytes = new byte[1024 * 8];
            int len;
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeIO(bis, bos);
        }

        return success;
    }

    /**
     * 将assets目录下的文件压缩到SD卡sdcard/Android/包名目录下
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return 是否压缩成功
     */
    public static boolean compressAssets2File(Context context, String fileName) {
        boolean success = false;
        GZIPOutputStream zos = null;
        InputStream is = null;
        try {
            File file = new File(context.getExternalFilesDir("compress"), fileName);
            zos = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            is = context.getAssets().open(fileName);
            byte[] buffer = new byte[1024 * 8];
            int len = -1;
            while ((len = is.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
            // 刷新
            zos.flush();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeIO(is, zos);
        }
        return success;
    }

    public static boolean string2File(String s, File file) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(s.getBytes());
            bos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeIO(bos);
        }
        return false;
    }

    public static String file2String(String path) {
        return file2String(new File(path));
    }

    public static String file2String(File file) {
        InputStreamReader reader = null;
        String result = null;
        char[] buffer;
        int len;
        try {
            reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            long size = file.length();
            if (size > 1024 * 12) {
                buffer = new char[1024 * 4];
                StringBuffer sb = new StringBuffer(1024 * 24);
                while ((len = reader.read(buffer)) != -1) {
                    sb.append(buffer, 0, len);
                }
                result = sb.toString();
            } else {
                buffer = new char[(int) size];
                len = reader.read(buffer);
                result = new String(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeIO(reader);
        }
        return result;
    }

}
