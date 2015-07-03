package com.makeapp.android.extras.tencent;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.makeapp.android.extras.FunctionAndroid;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class TencentFunctionAnalysis
        extends FunctionAndroid<String, Void> {

    @Override
    public Void apply(String s) {
        String[] params = s.split(" ");

        if ("onEvent".equals(params[0])) {
            if (params.length >= 3) {
                StatService.trackCustomBeginEvent(FunctionAndroid.context, params[1], params[2]);
                StatService.trackCustomEndEvent(FunctionAndroid.context, params[1], params[2]);
            } else if(params.length >= 2){
                StatService.trackCustomBeginEvent(FunctionAndroid.context, params[1], "");
                StatService.trackCustomEndEvent(FunctionAndroid.context, params[1], "");
            }
        } else if ("onEventBegin".equals(params[0])) {
            if (params.length >= 3) {
                StatService.trackCustomBeginEvent(FunctionAndroid.context, params[1], params[2]);
            } else if(params.length >= 2){
                StatService.trackCustomBeginEvent(FunctionAndroid.context, params[1], "");
            }
        } else if ("onEventEnd".equals(params[0])) {
            if (params.length >= 3) {
                StatService.trackCustomEndEvent(FunctionAndroid.context, params[1], params[2]);
            } else if(params.length >= 2){
                StatService.trackCustomEndEvent(FunctionAndroid.context, params[1], "");
            }
        }
        return null;
    }

    @Override
    protected void onAppCreate(Context activity) {
        super.onAppCreate(activity);
        StatConfig.setDebugEnable(true);
        String taAppkey = getConfig("app_key");
        try {
            StatService.startStatService(FunctionAndroid.context, taAppkey, com.tencent.stat.common.StatConstants.VERSION);
        } catch (MtaSDkException e) {
            e.printStackTrace();
        }

//        StatService.startNewSession(FunctionAndroid.context);
    }


    @Override
    protected void onResume(Activity activity) {
        super.onResume(activity);
        Log.i("Mta","onResume "+ activity);
        StatService.onResume(activity);
    }

    @Override
    protected void onPause(Activity activity) {
        super.onPause(activity);
        Log.i("Mta","onPause "+ activity);
        StatService.onPause(activity);
    }

    @Override
    public void onStop(Activity activity) {
        super.onStop(activity);
    }

    @Override
    protected void onAppDestory() {
        super.onAppDestory();
//        StatService.stopSession();
    }

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);//15921864573
//        StatService.trackBeginPage(activity,activity.getLocalClassName());
    }

    @Override
    public void onDestroy(Activity activity) {
        super.onDestroy(activity);
//        StatService.trackEndPage(activity,activity.getLocalClassName());
    }
}
