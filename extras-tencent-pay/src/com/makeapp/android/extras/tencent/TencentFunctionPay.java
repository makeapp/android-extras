package com.makeapp.android.extras.tencent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Log;
import com.tencent.unipay.plugsdk.IUnipayServiceCallBack;
import com.tencent.unipay.plugsdk.UnipayPlugAPI;
import org.fun.FunctionFactory;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class TencentFunctionPay
        extends TencentFunctionQQLogin {

    UnipayPlugAPI unipayAPI = null;

    private String userId = "";
    private String userKey = "";
    private String sessionId = "";
    private String sessionType = "";
    private String zoneId = "";
    private String saveValue = "";
    private String pf = "";
    private String pfKey = "";
    private String acctType = "";
    private String tokenUrl = "";
    private int resId = 0;
    private int retCode = -100;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);

        unipayAPI = new UnipayPlugAPI(context);
        unipayAPI.setCallBack(unipayStubCallBack);
        unipayAPI.bindUnipayService();


        sessionId = "openid";
        sessionType = "kp_actoken";

        zoneId = "1";
        pf = "huyu_m_android-1000-android";
        acctType = UnipayPlugAPI.ACCOUNT_TYPE_COMMON;
        saveValue = "60";
        unipayAPI.setEnv(getConfig("env"));
        unipayAPI.setOfferId(getConfig("app_id"));
        unipayAPI.setLogEnable(true);
    }

    private byte[] getResData(String resName) {
        resId = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
        if (resId > 0) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resId);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        return new byte[0];
    }

    IUnipayServiceCallBack.Stub unipayStubCallBack = new IUnipayServiceCallBack.Stub() {

        @Override
        public void UnipayNeedLogin() throws RemoteException {
            Log.i("UnipayPlugAPI", "UnipayNeedLogin");
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doLogin();
                }
            });
        }

        @Override
        public void UnipayCallBack(int resultCode, int payChannel, int payState, int providerState, int saveNum, String resultMsg, String extendInfo) throws RemoteException {
            Log.i("UnipayPlugAPI", "UnipayCallBack \n" +
                    "\nresultCode = " + resultCode +
                    "\npayChannel = " + payChannel +
                    "\npayState = " + payState +
                    "\nproviderState = " + providerState);
            onResult("pay " + (resultCode == 0 ? "success" : "error") + " " + saveNum);
        }
    };

    public void onLoginSuccess(JSONObject values) {
        super.onLoginSuccess(values);
    }

    @Override
    public Boolean apply(String s) {
        String[] params = s.split(" ");

        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        userId = preferences.getString("tencent_openid", "xx");
        userKey = preferences.getString("tencent_pay_token", "xx");//getMetaString("skey");
        pfKey = preferences.getString("tencent_pay_pfKey", "xx");
        pf = preferences.getString("tencent_pay_pf", "xx");

        if ("payWithoutNum".equalsIgnoreCase(params[0])) {
            try {
                String name = params[1];
                byte[] appResData = getResData(getConfig(name + ".res_name"));

                unipayAPI.SaveGameCoinsWithoutNum(userId, userKey, sessionId, sessionType, zoneId, pf, pfKey, acctType, appResData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ("pay".equalsIgnoreCase(params[0])) {
            try {
                String name = params[1];
                byte[] appResData = getResData(getConfig(name + ".res_name"));
                String saveValue = getConfig(name + ".price");
                unipayAPI.SaveGameCoinsWithNum(userId, userKey, sessionId, sessionType, zoneId, pf, pfKey, acctType, saveValue, false, appResData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ("payOrder".equalsIgnoreCase(params[0])) {
            String name = params[1];
            byte[] appResData = getResData(getConfig(name + ".res_name"));
            String serviceCode = "xxjzgw";
            String serviceName = "";
            String remark = "";
            try {
                unipayAPI.OpenServiceWithoutNum(userId, userKey, sessionId, sessionType, pf, pfKey, serviceCode, serviceName, appResData, remark);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if ("Goods".equalsIgnoreCase(params[0])) {
            tokenUrl = "/v1/m02/900000490/mobile_goods_info?token_id=24692F55F6A9B84563B828BB6D50BBEF31109";
            String remark = "";
            try {
                String name = params[1];
                byte[] appResData = getResData(getConfig(name + ".res_name"));
                unipayAPI.SaveGoods(userId, userKey, sessionId, sessionType, zoneId, pf, pfKey, tokenUrl, appResData, remark);//(userId, userKey, sessionId, sessionType, zoneId, pf, pfKey, acctType, resId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        unipayAPI.unbindUnipayService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
