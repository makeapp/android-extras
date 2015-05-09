package com.makeapp.android.extras.xiaomi;

import android.app.Activity;
import android.os.Bundle;
import com.makeapp.android.extras.FunctionGame;
import com.makeapp.android.extras.FunctionPay;
import com.xiaomi.gamecenter.sdk.*;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfo;

import java.util.UUID;

/**
 * Created by yuanyou on 2014/8/18.
 */
public class XiaoMiFunctionPay extends FunctionGame
        implements OnPayProcessListener, OnLoginProcessListener {

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        MiAppInfo appInfo = new MiAppInfo();
        appInfo.setAppId(getConfig("app_id"));
        appInfo.setAppKey(getConfig("app_key"));
//		appInfo.setOrientation( ScreenOrientation.horizontal ); //横竖屏
        MiCommplatform.Init(FunctionAndroid.context, appInfo);
    }

    @Override
    public String applyMain(String s) {

        String[] params = s.split(" ");
        String cmd = params[0];

        if ("pay".equalsIgnoreCase(cmd)) {
            String name = params[1];
            String id = getConfig(name + ".code");
            final MiBuyInfo miBuyInfo = new MiBuyInfo();
            miBuyInfo.setCpOrderId(UUID.randomUUID().toString());
            miBuyInfo.setCpUserInfo("");
            miBuyInfo.setCount(1);
            miBuyInfo.setProductCode(id);
            Bundle mBundle = new Bundle();
            miBuyInfo.setExtraInfo(mBundle);
            MiCommplatform.getInstance().miLogin(activity, new OnLoginProcessListener() {
                @Override
                public void finishLoginProcess(int i, MiAccountInfo miAccountInfo) {
                    if (MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS == i) {
                        MiCommplatform.getInstance().miUniPay(activity, miBuyInfo, XiaoMiFunctionPay.this);
                    } else if (MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_LOGINOUT_FAIL == i) {
                        onResult("login", "fault");
                    }
                }
            });
        } else if ("login".equalsIgnoreCase(cmd)) {
            MiCommplatform.getInstance().miLogin(activity, this);
        }

        return null;
    }

    @Override
    public void finishLoginProcess(int arg0, MiAccountInfo arg1) {

        System.out.println("----login-------" + arg0 + "uid:" + (null != arg1 ? arg1.getUid() : ""));
        if (MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS == arg0) {
            onResult("login", "success");
        } else if (MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_LOGINOUT_FAIL == arg0) {
            onResult("login", "fault");
        } else if (MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_ACTION_EXECUTED == arg0) {
            onResult("login", "executed");
        } else {
            onResult("login", "error");
        }
    }

    @Override
    public void finishPayProcess(int arg0) {
        if (arg0 == MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS) {
            onResult("pay", "success");
        } else if (arg0 == MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_CANCEL || arg0 == MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_PAY_CANCEL) {
            onResult("pay", "cancel");
        } else if (arg0 == MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_PAY_FAILURE) {
            onResult("pay", "fault");
        } else if (arg0 == MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_ACTION_EXECUTED) {
            onResult("pay", "executed");
        } else if (arg0 == MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_LOGIN_FAIL) {
            onResult("pay", "login_fail");
        }
    }
}
