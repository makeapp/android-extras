package com.makeapp.android.extras.umeng;

import android.app.Activity;
import com.baidu.mobstat.StatService;
import com.makeapp.android.extras.FunctionAndroid;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class UmengFunctionAnalysis
        extends FunctionAndroid<String, Void> {

    public Void apply(String s) {
        String[] params = s.split(" ");
        if ("onResume".equalsIgnoreCase(params[0])) {
            // 页面开始
            MobclickAgent.onResume(FunctionAndroid.context);
        } else if ("onPause".equalsIgnoreCase(params[0])) {
            MobclickAgent.onPause(FunctionAndroid.context);
        } else if ("onEvent".equalsIgnoreCase(params[0])) {
            if (params.length > 2) {
                MobclickAgent.onEvent(this.context, params[1], params[2]);
            } else if (params.length > 1) {
                MobclickAgent.onEvent(this.context, params[1]);
            }
        }
        return null;
    }

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);

        AnalyticsConfig.setAppkey(getConfig("appkey"));
        AnalyticsConfig.setChannel(getConfig("channel"));

//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
//		MobclickAgent.setAutoLocation(true);
//		MobclickAgent.setSessionContinueMillis(1000);
        MobclickAgent.setDebugMode("true".equals(getConfig("debug")));
        MobclickAgent.updateOnlineConfig(this.context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this.context);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this.context);
    }
}
