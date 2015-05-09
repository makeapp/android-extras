package com.makeapp.android.extras.wandoujia;

import android.app.Activity;
import android.util.Log;
import com.makeapp.android.extras.FunctionAd;
import com.makeapp.android.extras.FunctionAndroidUI;
import com.makeapp.android.extras.FunctionPay;
import com.wandoujia.sdk.plugin.paydef.*;
import com.wandoujia.sdk.plugin.paydef.MSG;
import com.wandoujia.sdk.plugin.paysdkimpl.PayConfig;
import com.wandoujia.sdk.plugin.paysdkimpl.WandouAccountImpl;
import com.wandoujia.sdk.plugin.paysdkimpl.WandouPayImpl;
import com.wandoujia.wandoujiapaymentplugin.utils.*;

/**
 * Created by yuanyou on 2014/8/16.
 */
public class WanDouJiaFunctionPay
        extends FunctionPay {

    private WandouAccount account = new WandouAccountImpl();

    private WandouPay wandoupay = new WandouPayImpl();

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        String appId = getConfig("app_id");
        String appKey = getConfig("app_key");
        PayConfig.init(activity, appId, appKey);
    }

    @Override
    public Boolean applyMain(String s) {
        String[] params = s.split(" ");
        if ("pay".equalsIgnoreCase(params[0])) {
            String name = params[1];
            String title = getConfig(name + ".name");
            String desc = getConfig(name + ".desc");
            String price = getConfig(name + ".price");
            WandouOrder order = new WandouOrder(title, desc, Long.parseLong(price));
            // 设置游戏订单号，最长50个字符
            order.setOut_trade_no(getTradeNo());
            // 触发支付
            wandoupay.pay(FunctionAndroid.context, order, new PayCallBack() {

                @Override
                public void onSuccess(User user, WandouOrder order) {
                    Log.w("DemoPay", "onSuccess:" + order);
                    onResult("pay", "success");
                }

                @Override
                public void onError(User user, WandouOrder order) {
                    Log.w("DemoPay", "onError:" + order);
                    onResult("pay", "error");
                }
            });
        } else if ("login".equalsIgnoreCase(params[0])) {
            account.doLogout(FunctionAndroid.context, new LoginCallBack() {

                @Override
                public void onSuccess(User user, int type) {

                    Log.d("logout", "success:+" + user);
                    onResult("login", "success");
                }

                @Override
                public void onError(int returnCode, String info) {
                    // 请不要在这里重新调用 doLogin
                    // 游戏界面上应该留有一个登录按钮，来触发 doLogin登录
                    onResult("login", "error " + info);
                }
            });
        } else if ("logout".equalsIgnoreCase(params[0])) {
            account.doLogout(FunctionAndroid.context, new LoginCallBack() {

                @Override
                public void onSuccess(User user, int type) {

                    Log.d("logout", "success:+" + user);
                    onResult("logout", "success");
                }

                @Override
                public void onError(int returnCode, String info) {
                    // 请不要在这里重新调用 doLogin
                    // 游戏界面上应该留有一个登录按钮，来触发 doLogin登录
                    onResult("logout", "error " + info);
                }
            });
        } else if ("relogin".equalsIgnoreCase(params[0])) {
            account.reLogin(FunctionAndroid.context, new LoginCallBack() {

                @Override
                public void onSuccess(User user, int type) {

                    Log.d("logout", "success:+" + user);
                    onResult("relogin", "success");
                }

                @Override
                public void onError(int returnCode, String info) {
                    // 请不要在这里重新调用 doLogin
                    // 游戏界面上应该留有一个登录按钮，来触发 doLogin登录
                    onResult("relogin", "error " + info);
                }
            });
        }
        return null;
    }
}
