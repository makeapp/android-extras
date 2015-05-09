package com.makeapp.android.extras.baidu;

import android.app.Activity;
import com.baidu.mobstat.StatService;
import com.makeapp.android.extras.FunctionAndroid;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class BaiduFunctionAnalysis
        extends FunctionAndroid<String, Void> {

    @Override
    public Void apply(String s) {
        if ("onResume".equalsIgnoreCase(s)) {
            // 页面开始
            StatService.onResume(FunctionAndroid.context);
        } else if ("onPause".equalsIgnoreCase(s)) {
            StatService.onPause(FunctionAndroid.context);
        } else {
            StatService.onEvent(FunctionAndroid.context, s, "");
        }
        return null;
    }

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        StatService.setDebugOn(true);
        StatService.onPageStart(FunctionAndroid.context, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(FunctionAndroid.context);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(FunctionAndroid.context);
    }
}
