package com.makeapp.android.extras.chinamobile;

import android.app.Activity;
import android.content.Context;
import com.makeapp.android.extras.FunctionAndroid;
import mm.purchasesdk.OnPurchaseListener;
import mm.purchasesdk.Purchase;
import mm.purchasesdk.PurchaseCode;

import java.util.HashMap;

/**
 * Created by yuanyou on 2014/8/8.
 */
public class ChinaMobileFunctionPay
        extends FunctionAndroid<String, Boolean> implements OnPurchaseListener {

    Purchase purchase;

    @Override
    protected void onAppCreate(Context activity) {
        super.onAppCreate(activity);
        purchase = Purchase.getInstance();
        /**
         * step3.向Purhase传入应用信息。APPID，APPKEY。 需要传入参数APPID，APPKEY。 APPID，见开发者文档
         * APPKEY，见开发者文档
         */
        try {
            purchase.setAppInfo(getConfig("app_id"), getConfig("app_key"));
            purchase.init(FunctionAndroid.context, this);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public Boolean apply(String s) {

        String[] params = s.split(" ");
        String command = params[0];

        if ("renew".equalsIgnoreCase(params[0])) {
            int count = Integer.parseInt(params[2]);
            purchase.order(FunctionAndroid.context, params[1], count, null, true, this);
        } else if ("pay".equalsIgnoreCase(params[0])) {
            String name = params[1];
            String code = getConfig(name + ".code");
//            purchase.order(context, code, 1, getConfig(name + ".desc"), false, this);
            purchase.order(FunctionAndroid.context, code, this);
        } else if ("query".equalsIgnoreCase(params[0])) {
            try {
                purchase.query(FunctionAndroid.context, params[1], null, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("clean".equalsIgnoreCase(params[0])) {
            try {
                purchase.clearCache(FunctionAndroid.context);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else if ("unsubscribe".equalsIgnoreCase(params[0])) {
            purchase.unsubscribe(FunctionAndroid.context, params[1], this);
        }
        return true;
    }

    @Override
    public void onBeforeApply() {

    }

    @Override
    public void onAfterApply() {

    }

    @Override
    public void onBeforeDownload() {

    }

    @Override
    public void onAfterDownload() {

    }

    @Override
    public void onQueryFinish(int i, HashMap hashMap) {

    }

    @Override
    public void onBillingFinish(int code, HashMap hashMap) {
        if (code == PurchaseCode.ORDER_OK || (code == PurchaseCode.AUTH_OK)) {
            onResult("pay success");
        } else {
            onResult("pay error");
        }
    }

    @Override
    public void onUnsubscribeFinish(int i) {

    }

    @Override
    public void onInitFinish(int i) {

    }

}
