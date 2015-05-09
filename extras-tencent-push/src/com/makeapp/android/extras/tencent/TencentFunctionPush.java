package com.makeapp.android.extras.tencent;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.makeapp.android.extras.FunctionAndroid;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.tencent.android.tpush.service.cache.CacheManager;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class TencentFunctionPush
        extends FunctionAndroid<String, Void> {

    @Override
    public Void apply(String s) {
        return null;
    }

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);

        XGPushConfig.enableDebug(FunctionAndroid.context, true);
// 1.获取设备Token
        Handler handler = new Handler();
       final Message m = handler.obtainMessage();
        XGPushManager.registerPush(FunctionAndroid.context.getApplicationContext(),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        CacheManager.getRegisterInfo();
                        m.sendToTarget();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.w(Constants.LogTag,
                                "+++ register push fail. token:" + data
                                        + ", errCode:" + errCode + ",msg:"
                                        + msg);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        XGPushClickedResult click = XGPushManager.onActivityStarted(activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(activity);
    }
}
