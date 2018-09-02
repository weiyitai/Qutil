package com.qianbajin.qutil.util;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
/*
 * @des
 * @Created  wWX407408  at 2017/4/21  17:54 
 *
 */
public class CheckFileUtil implements LoaderManager.LoaderCallbacks<Cursor> {

    private static CheckFileUtil sInstance;
    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608
    private final FragmentActivity mContext;
    private OnImagesLoadedListener mListener;

    public CheckFileUtil(FragmentActivity context, OnImagesLoadedListener listener) {
        mListener = listener;
        mContext = context;
        init(context);
    }
//            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
//            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
//            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
//            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
//            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg

    public static CheckFileUtil getInstance(FragmentActivity context, OnImagesLoadedListener listener) {
        if (sInstance == null) {
            sInstance = new CheckFileUtil(context,listener);
        }
        return sInstance;
    }


    private boolean init(FragmentActivity activity) {
        LoaderManager loaderManager = activity.getSupportLoaderManager();

        Bundle bundle = new Bundle();
        bundle.putString("path", Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/tencent");
       loaderManager.initLoader(1, bundle, this);
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'", null,
                IMAGE_PROJECTION[6] + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null&&mListener != null) {
            mListener.onImagesLoaded(data);
//            while (data.moveToNext()) {
//                String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
//                if (imageName.equals("1491606363777.jpeg")) {
//                    break;
//                }
//            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * 所有图片加载完成的回调接口
     */
    public interface OnImagesLoadedListener {

        void onImagesLoaded(Cursor data);
    }

}
