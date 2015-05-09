package com.makeapp.android.extras.tencent;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import com.makeapp.android.extras.FunctionAndroid;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import org.json.JSONObject;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class TencentFunctionQQLogin
        extends FunctionAndroid<String, Boolean> {

    String appId;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        appId = getConfig("app_id");
    }

    protected void doLogin() {

        Tencent mTencent = Tencent.createInstance(appId, FunctionAndroid.context);
        if (mTencent == null) {
            return;
        }
        IUiListener listener = new TencentBaseUIListener(FunctionAndroid.context) {
            protected void doComplete(JSONObject values) {
                onLoginSuccess(values);
            }
        };
        mTencent.login(activity, "all", listener);
    }

    public void onLoginSuccess(JSONObject values) {
        String userKey = values.optString("openid");
        String pay_token = values.optString("pay_token");
        String pfkey = values.optString("pfkey");
        String pf = values.optString("pf");
        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        preferences.edit().putString("tencent_openid", userKey)
                .putString("tencent_pay_pfKey", pfkey)
                .putString("tencent_pay_pf",pf)
                .putString("tencent_pay_token", pay_token).commit();

    }

    @Override
    public Boolean apply(String s) {

        Log.i("UnipayPlugAPI", "UnipayNeedLogin");
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doLogin();
            }
        });

        return null;
    }
}
