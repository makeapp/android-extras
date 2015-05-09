package com.makeapp.android.extras.jpush;

import android.content.Context;
import cn.jpush.android.api.JPushInterface;
import com.makeapp.android.extras.FunctionAndroid;

/**
 * Created by yuanyou on 2015/2/5.
 */
public class JPushFunctionPush
        extends FunctionAndroid<String, Void> {

    @Override
    protected void onAppCreate(Context activity) {
        super.onAppCreate(activity);
        JPushInterface.setDebugMode("true".equals(getConfig("debug")));
        JPushInterface.init(activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(activity);
    }

    @Override
    public Void apply(String s) {
        return null;
    }
}
