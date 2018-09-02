package com.qianbajin.qutil.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
/**
 * @author wWX407408
 * @Created at 2017/11/27  11:49
 * @des
 */

public class GsonUtil {

    public static <T> T json2Obj(String json, Class<T> aClass) {
        return new Gson().fromJson(json, aClass);
    }

    public static String obj2Json(Object obj) {
        return new Gson().toJson(obj);
    }

    public static <T> T asset2Obj(Context context, String assetName, Class<T> aClass) {
        InputStream open = null;
        try {
            open = context.getAssets().open(assetName);
            BufferedReader br = new BufferedReader(new InputStreamReader(open));
            return new Gson().fromJson(br, aClass);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeIO(open);
        }
        return null;
    }

    public static <T> List<T> asset2ObjArray(Context context, String assetName, Type type) {
        InputStream open = null;
        try {
            open = context.getAssets().open(assetName);
            BufferedReader br = new BufferedReader(new InputStreamReader(open));
            return new Gson().fromJson(br, type);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeIO(open);
        }
        return null;
    }

    public static Type getType(Class aClass) {
        TypeVariable[] parameters = aClass.getTypeParameters();
        return new TypeToken<List<? extends Object>>() {
        }.getType();
    }

}
