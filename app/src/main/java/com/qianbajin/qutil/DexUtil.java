package com.qianbajin.qutil;

import android.util.Log;

import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;
/**
 * @author Administrator
 * @Created at 2017/12/1 0001  0:22
 * @des
 */

public class DexUtil {

    public static Method[] getClassField(String apkPath, String className) {
        PathClassLoader pathClassLoader = new PathClassLoader(apkPath, ClassLoader.getSystemClassLoader());
        try {
            Class<?> aClass = pathClassLoader.loadClass(className);
            Method[] declaredMethods = aClass.getDeclaredMethods();
            return declaredMethods;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Method[0];
    }

    public static Method[] getSuperClassField(String apkPath, String className) {
        PathClassLoader pathClassLoader = new PathClassLoader(apkPath, ClassLoader.getSystemClassLoader());
        try {
            Class<?> aClass = pathClassLoader.loadClass(className);
            Class<?> superclass = aClass.getSuperclass();
            String name = superclass.getName();
            Log.d("DexUtil", "name:" + name);
            Method[] declaredMethods = superclass.getDeclaredMethods();
            return declaredMethods;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Method[0];
    }

    public static Method[] getSuperSuperClassField(String apkPath, String className) {
        PathClassLoader pathClassLoader = new PathClassLoader(apkPath, ClassLoader.getSystemClassLoader());
        try {
            Class<?> aClass = pathClassLoader.loadClass(className);
            Class<?> superclass = aClass.getSuperclass();
            String superclassName = superclass.getName();
            Log.d("DexUtil", "superclassName:" + superclassName);
            Class<?> superclass1 = superclass.getSuperclass();
            String superclass1Name = superclass1.getName();
            Log.d("DexUtil", "superclass1Name:" + superclass1Name);
            Method[] declaredMethods = superclass1.getDeclaredMethods();
            return declaredMethods;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Method[0];
    }

}
