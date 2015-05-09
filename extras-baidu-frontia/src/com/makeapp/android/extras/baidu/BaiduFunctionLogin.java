package com.makeapp.android.extras.baidu;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaApplication;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaAuthorizationListener;
import com.makeapp.android.extras.FunctionAndroid;

import java.security.MessageDigest;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class BaiduFunctionLogin
        extends FunctionAndroid<String, Boolean> {

    // MD5加密，32位
    private static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    //签名拼写
    private static String Sign(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String obj : strings) {
            sb.append(obj);
        }
        return sb.toString();
    }


    @Override
    public Boolean apply(String s) {

        final String time = System.currentTimeMillis() + "";
        final String sign = MD5(Sign(getConfig("app_id"), getConfig("app"), time));
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        Bundle bundle = new Bundle();
        Message msg = new Message();
        msg.setData(bundle);
        msg.what = BaiduHandler.H_LOGIN_VIEW;
        new BaiduHandler().sendMessage(msg);
        return true;
    }

    class BaiduHandler extends Handler {
        public static final int H_LOGIN_VIEW = 1;

        public BaiduHandler() {
            //百度初始化
            FrontiaApplication.initFrontiaApplication(FunctionAndroid.context.getApplicationContext());

            Frontia.init(FunctionAndroid.context.getApplicationContext(), "SOrcrIaXeRVpobyR8r23ldGg");
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case H_LOGIN_VIEW:
                    FrontiaAuthorization auth = Frontia.getAuthorization();
                    auth.authorize(activity, FrontiaAuthorization.MediaType.BAIDU.toString(), new FrontiaAuthorizationListener.AuthorizationListener() {
                        String mAccessToken;

                        @Override
                        public void onSuccess(FrontiaUser user) {
                            mAccessToken = user.getAccessToken();
                            Log.d("doLogin", "APP获取Token结果:" + mAccessToken + "成功,验证签名成功！");
                        }

                        @Override
                        public void onFailure(int errCode, String errMsg) {
                            Log.d("doLogin", "APP获取Token结果:" + mAccessToken + "成功,验证签名失败！");
                        }

                        @Override
                        public void onCancel() {
                            Log.d("doLogin", "APP获取Token结果:" + mAccessToken + "成功,验证签名失败！");
                        }
                    });
                    break;
            }
            super.handleMessage(msg);
        }
    }

}
