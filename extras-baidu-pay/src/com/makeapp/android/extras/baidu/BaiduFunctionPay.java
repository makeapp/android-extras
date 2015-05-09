package com.makeapp.android.extras.baidu;

import android.app.Activity;
import android.util.Log;
import com.baifubao.mpay.ifmgr.*;
import com.baifubao.mpay.tools.PayRequest;
import com.makeapp.android.extras.FunctionAndroid;
import com.makeapp.android.extras.FunctionPay;
import org.fun.FunctionFactory;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class BaiduFunctionPay
        extends FunctionPay {

    String appkey = "";
    String appId = "";
    String notifyurl = "";
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        appkey = getConfig("app_key");
        appId = getConfig("app_id");
        notifyurl = getConfig("notify_url");
        String sdkType = getConfig("sdk_type");
        if ("PORTRAIT".equalsIgnoreCase(sdkType)) {
            SDKApi.init(activity, SDKApi.PORTRAIT, appId);
        } else {
            SDKApi.init(activity, SDKApi.LANDSCAPE, appId);
        }
    }

    public void callPayCancel(String num, String msg) {
        onResult("pay error " + num + " " + msg);
    }

    public void callPayOk(String num, String o) {
        onResult("pay success " + num + " " + o);
    }

    @Override
    public Boolean applyMain(String s) {

        final String[] params = s.split(" ");
        if ("login".equalsIgnoreCase(params[0])) {
            final String time = System.currentTimeMillis() + "";
            final String sign = MD5(Sign(appId, appkey, time));
            SDKApi.loginUIEx(activity, appId, time, sign, new ILoginCallbackEx() {

                @Override
                public void onCallBack(int retcode, String token, String uid, String uname, String sign) {
                    if (retcode == ILoginCallback.RETCODE_SUCCESS) {
                        onResult("login scuess " + token + " " + uname);
                    } else if (retcode == ILoginCallback.RETCODE_FAIL) {
                        onResult("login error");
                    }
                }
            }, true, true);
        }
        if ("pay".equalsIgnoreCase(params[0])) {
            final String time = System.currentTimeMillis() + "";
            final String sign = MD5(Sign(appId, appkey, time));

            SDKApi.loginUIEx(activity, appId, time, sign, new ILoginCallbackEx() {

                @Override
                public void onCallBack(int retcode, String token, String uid, String uname, String sign) {
                    if (retcode == ILoginCallback.RETCODE_SUCCESS) {
                        startPay(params);
                    } else if (retcode == ILoginCallback.RETCODE_FAIL) {
                        onResult("login error");
                    }
                }
            }, true, false);
        } else if ("userCenter".equalsIgnoreCase(params[0])) {

            final String time = System.currentTimeMillis() + "";
            final String sign = MD5(Sign(appId, appkey, time));
            SDKApi.loginUIEx(activity, appId, time, sign, new ILoginCallbackEx() {
                @Override
                public void onCallBack(int retcode, String token, String uid, String uname, String sign) {
                    if (retcode == ILoginCallback.RETCODE_SUCCESS) {
                        SDKApi.userCenter(activity, new UserCenterManager.OnLogoutLinstener() {
                            @Override
                            public void logoutCancle() {
                                onResult("logout error cancle");
                            }

                            @Override
                            public void noLoginState() {
                                onResult("logout error nologinstate");
                            }

                            @Override
                            public void logoutSuc() {
                                onResult("logout success");
                            }

                            @Override
                            public void logoutFail() {
                                onResult("logout error");
                            }
                        });
                    } else if (retcode == ILoginCallback.RETCODE_FAIL) {
                        onResult("login error");
                    }
                }
            }, true, false);
        }

        return true;
    }

    private String getAmount(String price, String quantity) {
        return String.valueOf(Integer.parseInt(price) * Integer.parseInt(quantity));
    }

    private void startPay(String[] params) {
        String name = params[1];
        final String price = getConfig(name + ".price");
        final String no = getTradeNo();
        PayRequest payRequest = new PayRequest();
        payRequest.addParam("notifyurl", notifyurl);
        payRequest.addParam("appid", appId);
        payRequest.addParam("waresid", getConfig(name + ".id"));
        payRequest.addParam("exorderno", no); //外部订单号，请保证唯一性
        payRequest.addParam("price", price);
        payRequest.addParam("cpprivateinfo", "" + price);
        String paramUrl = payRequest.genSignedUrlParamString(appkey);
        //        if (misAysn) { //是否异步支付 true:异步
//            payRequest.addParam("asyncflag", 1);
//        }
        SDKApi.startPay(activity, paramUrl, new IPayResultCallback() {
            @Override
            public void onPayResult(int resultCode, String signValue, String resultInfo) {
                //resultInfo = 应用编号&商品编号&外部订单号
                if (SDKApi.PAY_SUCCESS == resultCode) {
                    Log.e("doPay", "signValue = " + signValue);
                    if (null == signValue) {
                        callPayCancel(no, "sign");
                    }
                    if (PayRequest.isLegalSign(signValue, resultInfo, appkey)) {
                        callPayOk(no, price);
                    } else {
                        callPayOk(no, price);
                    }
                } else if (SDKApi.PAY_CANCEL == resultCode) {
                    callPayCancel(no, "cancel");
                } else if (SDKApi.PAY_HANDLING == resultCode) {
                    callPayCancel(no, "handling");
                } else {
                    Log.e("doPay", "return Error");
                    callPayCancel(no, "");
                }
            }
        });
    }

    /**
     * 签名拼写
     *
     * @param appid
     * @param appKey
     * @param CurrentTime
     * @return
     */
    public String Sign_Login_Callback(String appid, String appKey, String CurrentTime, String userName, long userID) {

        StringBuilder sb = new StringBuilder();
        // 按照升序排列
        sb.append(appid).append(appKey).append(CurrentTime).append(userName).append(userID);
        String unSignValue = sb.toString();
        Log.e("tag", "unSignValue:" + unSignValue);
        return unSignValue;

    }

    public String Sign(String appid, String appKey, String CurrentTime) {

        StringBuilder sb = new StringBuilder();

        sb.append(appid).append(appKey).append(CurrentTime);
        String unSignValue = sb.toString();

        return unSignValue;

    }

    // MD5加密，32位
    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
