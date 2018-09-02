package com.qianbajin.qutil.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
/*
 * @des
 * @Created  wWX407408
 *
 */
public class FileUtils {

    private static boolean sContain;

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }

        long dirSize = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    dirSize += file.length();
                } else if (file.isDirectory()) {
                    if (file.getName().equals("instant-run")) {
                        continue;
                    }
//                    dirSize += file.length();
                    dirSize += getDirSize(file); // 递归调用继续统计
                }
            }
        }
        return dirSize;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
//        fileSizeString = Formatter.formatFileSize(context, fileS);
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static void storeByteArray(File file, ByteArrayOutputStream byteStream) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byteStream.writeTo(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        byteStream.close();

    }

//    public static String getRealPathFromURI(Uri contentUri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(column_index);
//        cursor.closeIO();
//        return result;
//    }

    @SuppressLint("NewApi")
    public static String uri2FilePath(Context context, Uri uri) throws URISyntaxException {
        String filePath = "";
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                switch (type) {
                    case "image":
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                        default:
                            break;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                    cursor.close();
                    return filePath;
                }
            } catch (Exception e) {
                /*三星手机在调用系统的文件管理器选择文件时获取不到路径,占时只能采取这种方式*/
                String s = uri.toString();
                int index = s.indexOf("/storage");
                if (index > 0) {
                    filePath = s.substring(index);
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return filePath;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getNewPath(Context context, String s) {
        String path = s;
        File origin = new File(s);
        long length = origin.length();
        if (length > 500 * 1024) {      //大于500kb就剪裁,否则发送原图
            File newFile = new File(context.getExternalFilesDir("pic"), origin.getName());
            String newFilepath = newFile.getAbsolutePath();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmp = BitmapFactory.decodeFile(s, options);
            double w = bmp.getWidth();
            double h = bmp.getHeight();
            double newHeight = 800.0;
            double newWidth = newHeight * w / h;

            Bitmap bigBMP = Bitmap.createScaledBitmap(bmp, (int) newWidth, (int) newHeight, true);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bigBMP.compress(Bitmap.CompressFormat.JPEG, 100, os);
            try {
                storeByteArray(newFile, os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            path = newFilepath;

        }
        return path;
    }

    public static String getFileType(File file) {

        final String[][] MIME_MapTable = {
                //{后缀名， MIME类型}
                {".3gp", "video/3gpp"},
                {".apk", "application/vnd.android.package-archive"},
                {".asf", "video/x-ms-asf"},
                {".avi", "video/x-msvideo"},
                {".bin", "application/octet-stream"},
                {".bmp", "image/bmp"},
                {".c", "text/plain"},
                {".class", "application/octet-stream"},
                {".conf", "text/plain"},
                {".cpp", "text/plain"},
                {".doc", "application/msword"},
                {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
                {".xls", "application/vnd.ms-excel"},
                {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
                {".exe", "application/octet-stream"},
                {".gif", "image/gif"},
                {".gtar", "application/x-gtar"},
                {".gz", "application/x-gzip"},
                {".h", "text/plain"},
                {".htm", "text/html"},
                {".html", "text/html"},
                {".jar", "application/java-archive"},
                {".java", "text/plain"},
                {".jpeg", "image/jpeg"},
                {".jpg", "image/jpeg"},
                {".js", "application/x-javascript"},
                {".log", "text/plain"},
                {".m3u", "audio/x-mpegurl"},
                {".m4a", "audio/mp4a-latm"},
                {".m4b", "audio/mp4a-latm"},
                {".m4p", "audio/mp4a-latm"},
                {".m4u", "video/vnd.mpegurl"},
                {".m4v", "video/x-m4v"},
                {".mov", "video/quicktime"},
                {".mp2", "audio/x-mpeg"},
                {".mp3", "audio/x-mpeg"},
                {".mp4", "video/mp4"},
                {".mpc", "application/vnd.mpohun.certificate"},
                {".mpe", "video/mpeg"},
                {".mpeg", "video/mpeg"},
                {".mpg", "video/mpeg"},
                {".mpg4", "video/mp4"},
                {".mpga", "audio/mpeg"},
                {".msg", "application/vnd.ms-outlook"},
                {".ogg", "audio/ogg"},
                {".pdf", "application/pdf"},
                {".png", "image/png"},
                {".pps", "application/vnd.ms-powerpoint"},
                {".ppt", "application/vnd.ms-powerpoint"},
                {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
                {".prop", "text/plain"},
                {".rc", "text/plain"},
                {".rmvb", "audio/x-pn-realaudio"},
                {".rtf", "application/rtf"},
                {".sh", "text/plain"},
                {".tar", "application/x-tar"},
                {".tgz", "application/x-compressed"},
                {".txt", "text/plain"},
                {".wav", "audio/x-wav"},
                {".wma", "audio/x-ms-wma"},
                {".wmv", "audio/x-ms-wmv"},
                {".wps", "application/vnd.ms-works"},
                {".xml", "text/plain"},
                {".z", "application/x-compress"},
                {".zip", "application/x-zip-compressed"},
                {"", "*/*"}
        };
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
         /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex).toLowerCase();
        if (end == "") {
            return type;
        }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0])) {
                type = MIME_MapTable[i][1];
            }
        }
        return type;
    }

    /**
     * 文件夹是否包含文件
     *
     * @param dirs     文件夹
     * @param fileName 是否包含这个文件的文件名
     */
    public static boolean containFile(File dirs, String fileName) {
        sContain = false;
        if (!dirs.isDirectory()) {
            return sContain;
        }
        File[] files = dirs.listFiles();
        for (File dir : files) {
//            Log.d("FileUtils", "开始for循环 contain++++ = " + sContain);
            if (sContain) {
                return sContain;
            }
            if (dir.isFile()) {
                if (dir.getName().equals(fileName)) {
                    sContain = true;
//                    Log.d("FileUtils ", "contain = " + sContain);
                    return sContain;
                }
            } else {
                containFile(dir, fileName);
            }
        }
//        Log.d("FileUtils", "return contain---> = " + sContain);
        return sContain;
    }
}
