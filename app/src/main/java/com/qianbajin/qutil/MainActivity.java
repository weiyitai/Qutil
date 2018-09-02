package com.qianbajin.qutil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.Executors;

import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    public static final String CMD = "chmod 755 /data/data/com.qianbajin.qutil/shared_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString("heihei", "你是谁").apply();

//        registerReceiver(new SaveDataReceive(), new IntentFilter("PACKAGE_RESTARTED"));
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//                Log.d("MainActivity", action);
//                chmod();
//                Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
//            }
//        },new IntentFilter("com.qianbajin.test"));

        PathClassLoader pathClassLoader = new PathClassLoader("", ClassLoader.getSystemClassLoader());
        try {
            Class<?> aClass = pathClassLoader.loadClass("");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void click(View view) {
//        boolean chmod = chmod();
//        Log.d("MainActivity", "chmod:" + chmod);
        File file = new File("sdcard/qq.apk");
        boolean exists = file.exists();

//        Method[] classField = DexUtil.getClassField("data/app/com.tencent.mobileqq-1/base.apk", "com.tencent.mobileqq.msf.service.MsfService");
//        for (Method method : classField) {
//            Log.d("MainActivity", "method.getName():" + method.getName());
//        }

        Executors.newFixedThreadPool(5).submit(new Runnable() {
            @Override
            public void run() {
                Method[] classField = DexUtil.getClassField("data/app/com.eg.android.AlipayGphone-1/base.apk", "com.alipay.mobile.healthcommon.accountsync.SyncService");
                for (Method method : classField) {
                    Log.d("MainActivity", "method.getName():" + method.getName());
                }

                Method[] classField1 = DexUtil.getClassField("data/app/com.eg.android.AlipayGphone-1/base.apk", "com.alipay.mobile.healthcommon.stepcounter.APExtStepService");
                for (Method method : classField1) {
                    Log.d("MainActivity", "method.getName():" + method.getName());
                }

                Method[] classField2 = DexUtil.getClassField("data/app/com.eg.android.AlipayGphone-1/base.apk", "com.alipay.mobile.healthcommon.stepcounter.APMainStepService");
                for (Method method : classField2) {
                    Log.d("MainActivity", "method.getName():" + method.getName());
                }
            }
        });
    }

    public static boolean chmod() {
        try {
            Runtime.getRuntime().exec("chmod 755 /data/data/com.qianbajin.qutil/shared_prefs");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
