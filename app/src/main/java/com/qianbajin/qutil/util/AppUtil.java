package com.qianbajin.qutil.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/*
 * @des
 * @Created  wWX407408  at 2017/8/21  14:24 
 *
 */
public class AppUtil {

    /**
     *
     * @param context
     */
    public static void getKeyHash(Context context) {
        try {

            PackageInfo info = context.getPackageManager().getPackageInfo("com.huawei.dhott_v6", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                String KeyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);

                Log.d("KeyHash:", "KeyHash:" + KeyHash);//两次获取的不一样  此处取第一个的值

//kqiUicrC+Dvy0I1zzvUBOAhCqRY=

                Toast.makeText(context, "FaceBook HashKey:" + KeyHash, Toast.LENGTH_SHORT).show();

            }

        } catch (NoSuchAlgorithmException e) {

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }
    }

}
