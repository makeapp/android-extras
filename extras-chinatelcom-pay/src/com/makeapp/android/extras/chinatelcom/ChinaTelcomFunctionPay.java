package com.makeapp.android.extras.chinatelcom;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import com.ffcs.inapppaylib.PayHelper;
import com.ffcs.inapppaylib.bean.Constants;
import com.ffcs.inapppaylib.bean.response.PayResponse;
import com.makeapp.android.extras.FunctionAndroid;
import org.fun.FunctionFactory;

/**
 * Created by yuanyou on 2014/8/8.
 */
public class ChinaTelcomFunctionPay
        extends FunctionAndroid<String, Boolean> {

    PayHelper payHelper;

    @Override
    protected void onAppCreate(Context activity) {
        super.onAppCreate(activity);
        payHelper = PayHelper.getInstance(FunctionAndroid.context);
        payHelper.init(getConfig("app_id"), getConfig("app_key"));
        payHelper.settimeout(8000);    //设置超时时间（可不设，默认为8s）
    }

    @Override
    public Boolean apply(String s) {

        String[] params = s.split(" ");
        String command = params[0];
        String name = params[1];
        if ("pay".equalsIgnoreCase(params[0])) {
            String code = getConfig(name + ".code");
            payHelper.pay(activity, code, handler);
        }
        return true;
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            PayResponse payResp = (PayResponse) msg.obj;

            switch (msg.what) {
                case Constants.RESULT_VALIDATE_FAILURE:
                    //合法性验证失败
                    Log.d("LIHONG-Debug>>", "返回码：" + payResp.getRes_code() + "，描述：" + payResp.getRes_message());
                    onResult("pay validate_failure");
                    break;
                case Constants.RESULT_PAY_SUCCESS:
                    //支付成功
                    Log.d("LIHONG-Debug>>", "返回码：" + payResp.getRes_code() + "，描述：" + payResp.getRes_message());
                    onResult("pay success " + payResp.getRes_message());
                    break;
                case Constants.RESULT_PAY_FAILURE:
                    //支付失败
                    Log.d("LIHONG-Debug>>", "返回码：" + payResp.getRes_code() + "，描述：" + payResp.getRes_message());
                    onResult("pay failure " + payResp.getRes_message());
                    break;
                default:
                    break;
            }
        }

        ;
    };

}
