package com.qianbajin.qutil.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPOutputStream;
/**
 * @author wWX407408
 * @Created at 2017/11/27  10:18
 * @des asset管理工具类
 */

public class AssetUtil {

    /**
     * 从资源文件获取输入流
     *
     * @param context   上下文
     * @param assetName 资源文件名
     * @return
     * @throws IOException
     */
    private static InputStream getAssetInput(Context context, String assetName) throws IOException {
        return context.getAssets().open(assetName);
    }

    public static String asset2String(Context context, String assetName) {
        String result = "";
        InputStream open = null;
        try {
            open = getAssetInput(context, assetName);
            result = is2String(open);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeIO(open);
        }
        return result;
    }

    public static String is2String(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder(1024 * 8);
        int read;
        char[] chars = new char[1024 * 8];
        while ((read = br.read(chars)) != -1) {
            sb.append(chars, 0, read);
        }
        return sb.toString();
    }

    public static boolean asset2File(Context context, String assetName, String desPath) {
        return asset2File(context, assetName, new File(desPath));
    }

    public static boolean asset2File(Context context, String assetName, File desFile) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            InputStream input = getAssetInput(context, assetName);
            FileOutputStream fos = new FileOutputStream(desFile);
            bis = new BufferedInputStream(input);
            bos = new BufferedOutputStream(fos);
            byte[] bytes = new byte[1024 * 8];
            int read;
            while ((read = bis.read(bytes)) != -1) {
                bos.write(bytes, 0, read);
            }
            bos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeIO(bis, bos);
        }
        return false;
    }

    public static boolean asset2ZipFile(Context context, String assetName, File desFile) {
        BufferedInputStream bis = null;
        GZIPOutputStream gos = null;
        try {
            InputStream input = getAssetInput(context, assetName);
            FileOutputStream fos = new FileOutputStream(desFile);
            bis = new BufferedInputStream(input);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            gos = new GZIPOutputStream(bos);
            byte[] bytes = new byte[1024 * 8];
            int read;
            while ((read = bis.read(bytes)) != -1) {
                gos.write(bytes, 0, read);
            }
            bos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeIO(bis, gos);
        }
        return false;
    }

}
