package com.makeapp.android.extras.tencent;

import android.app.Activity;
import android.content.Context;
import com.makeapp.android.extras.FunctionAndroid;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class TencentFunctionCrash
        extends FunctionAndroid<String, Void> {

    @Override
    protected void onAppCreate(Context activity) {
        super.onAppCreate(activity);

        StatConfig.setDebugEnable(true);
        String appId = getConfig("app_id");
        String debug = getConfig("debug");
        Context appContext = activity.getApplicationContext();
        CrashReport.initCrashReport(appContext, appId, "true".equals(debug));  //初始化SDK
    }

    @Override
    public Void apply(String s) {

        if ("javaCrash".equalsIgnoreCase(s)) {
            // 页面开始
            CrashReport.testJavaCrash();
        } else if ("nativeCrash".equalsIgnoreCase(s)) {
            CrashReport.testNativeCrash();
        } else {
            String[] params = s.split(" ");
            if (params.length > 1) {
                if ("setUserId".equals(params[0])) {
                    CrashReport.setUserId(params[1]);
                }
            }
        }
        return null;
    }

}
