package com.makeapp.android.extras;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import com.makeapp.android.extras.FunctionAndroid;
import org.fun.FunctionFactory;

/**
 * Created by yuanyou on 2014/8/12.
 */
public class FunctionOperatorPay
        extends FunctionAndroid<String, Boolean> {

    String operator;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        operator = telephonyManager.getSimOperator();
    }

    @Override
    public Boolean apply(String s) {

        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002")) {
                // operatorName="中国移动";
                String name = getConfig("mobile");
                FunctionFactory.callFunction(name, s);
            } else if (operator.equals("46001")) {
                // operatorName="中国联通";
                String name = getConfig("unicom");
                FunctionFactory.callFunction(name, s);
            } else if (operator.equals("46003")) {
                // operatorName="中国电信";
                String name = getConfig("telcom");
                FunctionFactory.callFunction(name, s);
            }
        }

        return null;
    }


}
