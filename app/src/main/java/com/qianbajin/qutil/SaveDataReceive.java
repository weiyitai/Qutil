package com.qianbajin.qutil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
/**
 * @author Administrator
 * @Created at 2017/11/25 0025  0:20
 * @des
 */

public class SaveDataReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("SaveDataReceive", "action:" + action);
        Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "收到", Toast.LENGTH_SHORT).show();
        Log.d("SaveDataReceive", "收到信息");
        if (action.equals("com.qianbajin.test")) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().putString("xixi", action).apply();
            Intent intent1 = new Intent(BuildConfig.APPLICATION_ID);
            Bundle bundle = new Bundle();
            bundle.putString("heihei","我收到小希了");
            intent1.putExtra("tata",bundle);
            intent1.putExtra("ss", "String lai");
            context.sendBroadcast(intent1);
        }
    }
}
