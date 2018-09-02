package com.qianbajin.qutil.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
 * @des
 * @Created  Administrator  at 2017/8/19 0019  14:50 
 *
 */
public class TencentHelper {

    public static final String TAG = "FileUtil";
    private static boolean success;
    private static int count;
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MobileQQ/shortvideo/";

    /**
     * 删除腾讯 shortvideo 目录下的空文件夹,即文件夹下只有一个.nomedia文件,记得在子线程调用
     *
     * @return 成功删除的空文件夹个数
     */
    public static int deleteEmptyDir() {
        count = 0;
        File file = new File(path);
        boolean exists = file.exists();
        if (exists) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                Log.d(TAG, "shortvideo目录下总的文件夹和文件个数>files.length:" + files.length);
                for (File file1 : files) {
                    if (file1.isDirectory()) {
                        File[] dir = file1.listFiles();
                        if (dir.length == 1) {
                            File files2 = dir[0];
                            boolean equals = ".nomedia".equals(files2.getName());
                            if (equals) {
                                deleteDir(file1.getAbsolutePath());
                                count++;
                            }
                        } else if (dir.length == 0) {
                            deleteDir(file1.getAbsolutePath());
                            count++;
                        } else {
//                                    String name = file1.getName();
//                                    Log.d(TAG, "empty name:" + name);
                        }
                    } else {
//                        Log.d(TAG, "file1.getName():" + file1.getName());
                    }

                }
            }
        }
        Log.d(TAG, "count:" + count);
        return count;
    }

    /**
     * 用adb命令删除文件夹及文件夹下的文件
     *
     * @param path 命令
     */
    private static void deleteDir(String path) {
        Log.d(TAG, "deleteDir path:" + path);
        try {
            Process exec = Runtime.getRuntime().exec("rm -r " + path);
            Log.d(TAG, "exec:" + exec);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "deleteDir e:" + e);
        }
    }

    /**
     * 删除腾讯 shortvideo 目录下一个文件夹里的多余文件,记得在子线程调用
     *
     * @return 是否有文件删除成功
     */
    public static long deleteUnnecessaryFile() {
        long deleteLength = 0;
        File file = new File(path);
        boolean exists = file.exists();
        if (exists) {
            List<File> fileList = new ArrayList<>();
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                Log.d(TAG, "shortvideo目录下总的文件夹和文件个数>files.length:" + files.length);
                for (File file1 : files) {
                    if (file1.isDirectory()) {
                        File[] dir = file1.listFiles();
                        //如果这个文件夹里文件个数大于2，则肯定有多余的重复文件
                        if (dir.length > 2) {
                            fileList.clear();
                            for (File file2 : dir) {
                                String name = file2.getName();
                                if (!name.equals(".nomedia")) {
                                    fileList.add(file2);
//                                    Log.d(TAG, "name:" + name);
                                }
                            }
                            int size = fileList.size();
                            Log.d(TAG, "文件夹:" + file1.getName() + ":里重复文件总数>>size:" + size);
                            for (int i = 1; i < size; i++) {
                                File file2 = fileList.get(i);
                                Log.d(TAG, "文件:" + file2.getName());
                                long length = file2.length();
                                boolean delete = file2.delete();
                                if (delete) {
                                    deleteLength += length;
                                }
                                Log.d(TAG, "delete:" + delete);
                            }
                        } else {
                            //只有一个文件在这个文件夹里
                        }
                    } else {
                        Log.d(TAG, "file1.getName():" + file1.getName());
                    }
                }
            }
        }
        return deleteLength;
    }

    /**
     * 移动shortvideo下子目录的视频文件到shortvideo目录下
     *
     * @return 移动的文件个数
     */
    public static int move2Parent() {
//        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/1/";
        String dir = path;
        int success = 0, fail = 0;
        File file = new File(dir);
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.isDirectory()) {
                File[] files1 = file1.listFiles();
                for (File file2 : files1) {
                    String file2Name = file2.getName();
                    if (!".nomedia".equals(file2Name)) {
                        boolean copy = copyFile(file2, new File(dir, file2Name));
                        boolean delete = false;
                        if (copy) {
                            delete = file2.delete();
                        }
                        if (copy && delete) {
                            success++;
                        } else {
                            fail++;
                            Log.d(TAG, "copy:" + copy + "  file2Name:" + file2Name + "  getParent:" + file2.getParent());
                        }
                    }
                }
            }
        }
        Log.d(TAG, "success:" + success + "  fail:" + fail);
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
            Log.d(TAG, "copyFile e:" + e);
        } finally {
            IoUtil.closeIO(bis, bos);
        }

        return success;
    }

}
