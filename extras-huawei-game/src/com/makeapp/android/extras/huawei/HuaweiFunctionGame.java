package com.makeapp.android.extras.huawei;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.huawei.pay.plugin.IHuaweiPay;
import com.android.huawei.pay.plugin.IPayHandler;
import com.android.huawei.pay.plugin.PayParameters;
import com.android.huawei.pay.util.HuaweiPayUtil;
import com.android.huawei.pay.util.Rsa;
import com.hianalytics.android.intf.IHiAnalytics;
import com.huawei.deviceCloud.microKernel.core.MicroKernelFramework;
import com.huawei.deviceCloud.microKernel.util.EXLogger;
import com.huawei.gamebox.buoy.sdk.IBuoyCallBack;
import com.huawei.gamebox.buoy.sdk.IBuoyOpenSDK;
import com.huawei.gamebox.buoy.sdk.InitParams;
import com.huawei.gamebox.buoy.sdk.util.BuoyConstant;
import com.huawei.gamebox.buoy.sdk.util.DebugConfig;
import com.huawei.hwid.openapi.out.IHwIDCallBack;
import com.huawei.hwid.openapi.out.microkernel.IHwIDOpenSDK;
import com.makeapp.android.extras.FunctionGame;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanyou on 2014/8/18.
 */
public class HuaweiFunctionGame extends FunctionGame {

    private static final String TAG = HuaweiFunctionGame.class.getSimpleName();

    //悬浮窗参数
    private InitParams p;

    //悬浮窗接口
    private IBuoyOpenSDK hwBuoy;

    //华为支付接口
    private IHuaweiPay hwPay;

    //华为帐号接口
    private IHwIDOpenSDK hwId;

    //SDK框架
    private MicroKernelFramework framework;

    boolean payLogin = false;
    String payName = null;

    public static final String HWID_PLUS_NAME = "hwIDOpenSDK";
    public static final String HuaweiPayPlugin = "HuaweiPaySDK";
    public static final String BuoyOpenSDKPlugin = "BuoyOpenSDK";
    public static final String HiAnalyticsSDKPlugin = IHiAnalytics.plusName;

    public static final HashMap<String, String> allPluginName = new HashMap<String, String>()
    {
        {
            put(HWID_PLUS_NAME, "华为帐号");
            put(BuoyOpenSDKPlugin, "游戏浮标");
            put(HuaweiPayPlugin, "华为支付");
            put(HiAnalyticsSDKPlugin, "华为统计");
        }
    };

    public static final int PAY_RESULT = 1000;


    public static boolean isNull(String string)
    {
        if (string != null)
        {
            string = string.trim();
            if (string.length() != 0)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        p = new InitParams(getConfig("app_id"), getConfig("cp_id"), getConfig("rsa_private"), new FloatListenerByCpImpl());
        //开启悬浮窗的log
        DebugConfig.setLog(true);

        //开启插件加载的log
        EXLogger.setLevel(Log.VERBOSE);
    }

    @Override
    public String applyMain(String cmd, String[] params) {
        payLogin = false;
        if ("login".equalsIgnoreCase(cmd)) {
            doLogin();
        } else if ("pay".equalsIgnoreCase(cmd)) {
            payLogin = true;
            payName = params[0];
            doLogin();
        }
        return super.applyMain(cmd, params);
    }

    /**
     * 启动支付
     * amount：商品价格
     * productName：商品名称
     * productDesc：商品介绍
     * userName：商户名
     *
     * @see [类、类#方法、类#成员]
     */
    public void startPay(String name, IHwIDOpenSDK hwId, IHuaweiPay hwPay, IPayHandler handler) {
        String productName = getConfig(name + ".name");
        String productDesc = getConfig(name + ".desc");
        String price = getConfig(name + ".price");

        DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
        String requestId = format.format(new Date());


        Map<String, String> params = new HashMap<String, String>();
        params.put("userID", getConfig("user_id"));
        params.put("applicationID", getConfig("app_id"));
        params.put("amount", price);
        params.put("productName", productName);
        params.put("productDesc", productDesc);
        params.put("requestId", requestId);

        String noSign = HuaweiPayUtil.getSignData(params);
        String sign = Rsa.sign(noSign, getConfig("rsa_private"));

        DebugConfig.d("startPay", "签名前： " + noSign);
        DebugConfig.d("startPay", " 签名后： " + sign);

        Map<String, Object> payInfo = new HashMap<String, Object>();
        payInfo.put("amount", price);
        payInfo.put("productName", productName);
        payInfo.put("requestId", requestId);
        payInfo.put("productDesc", productDesc);
        payInfo.put("userName", getConfig("user_name"));
        payInfo.put("applicationID", getConfig("app_id"));
        payInfo.put("userID", getConfig("user_id"));
        payInfo.put("sign", sign);

        String notifyUrl = getConfig("notify_url");
        payInfo.put("notifyUrl", notifyUrl);
        payInfo.put("environment", HuaweiPayUtil.environment_live);
        String accessToken = "";
        HashMap userInfo = hwId.getDefaultUserInfo();
        if (userInfo != null) {
            accessToken = (String) userInfo.get("accesstoken");
        }

        payInfo.put("accessToken", accessToken);
        //      调试期可打开日志，发布时注释掉
        payInfo.put("showLog", true);

        DebugConfig.d("startPay", "支付请求参数 : " + payInfo.toString());

        /**
         * 开始支付
         */
        hwPay.startPay(activity, payInfo, handler);
    }

    private void doPay(String name) {
        if (checkHuaweiPayPluginLoad()) {
            startPay(name, hwId, hwPay, handler);
        }
    }

    private void doLogin() {
        if (checkAccountPluginLoad()) {
            hwId.setLoginProxy(activity, p.getAppid(), new LoginCallBack(), new Bundle());
            hwId.login(new Bundle());
        }
    }

    /**
     * 加载帐号插件
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean checkAccountPluginLoad() {
        initMicroKernel();
        if (framework != null) {
            framework.checkSinglePlugin(HWID_PLUS_NAME, new UpdateNotifierHandler(framework));
            @SuppressWarnings("unchecked")
            List<Object> services = framework.getService(HWID_PLUS_NAME);
            if (null == services || services.size() == 0) {
                DebugConfig.d(TAG, "第一次getService为空，并开始loadPlugin：" + HWID_PLUS_NAME);
                framework.loadPlugin(HWID_PLUS_NAME);
                DebugConfig.d(TAG, "loadPlugin结束：" + HWID_PLUS_NAME);
                services = framework.getService(HWID_PLUS_NAME);
            } else {
                DebugConfig.d(TAG, HWID_PLUS_NAME + " 第一次getService不为空，services size=" + services.size());
            }

            if (null != services && !services.isEmpty()) {
                DebugConfig.d(TAG, HWID_PLUS_NAME + " 第二次getService不为空:" + services.get(0));
                hwId = (IHwIDOpenSDK) (services.get(0));
            } else {
                DebugConfig.d(TAG, "获取插件为空 :  " + HWID_PLUS_NAME);
            }
            if (null == hwId) {
                DebugConfig.d(TAG, "获取插件为空 :  " + HWID_PLUS_NAME);
            } else {
                return true;
            }

        }
        return false;
    }


    /**
     * 初始化微内核
     *
     * @see [类、类#方法、类#成员]
     */
    private void initMicroKernel() {
        try {
            if (null == framework) {
                //getInstance的参数不能为空，否则会导致后面的逻辑失败
                framework = MicroKernelFramework.getInstance(FunctionAndroid.context);
            }
            framework.start();
        } catch (Exception e) {
        }
    }

    /**
     * 加载浮标插件
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    private boolean checkBuoyPluginLoad() {
        initMicroKernel();
        if (framework != null) {
            framework.checkSinglePlugin(BuoyConstant.PLUGIN_NAME, new UpdateNotifierHandler(framework));
            List<Object> services = framework.getService(BuoyConstant.PLUGIN_NAME);
            if (null == services || services.size() == 0) {
                DebugConfig.d(TAG, "第一次getService为空，并开始loadPlugin：" + BuoyConstant.PLUGIN_NAME);
                framework.loadPlugin(BuoyConstant.PLUGIN_NAME);
                DebugConfig.d(TAG, "loadPlugin结束： " + BuoyConstant.PLUGIN_NAME);
                services = framework.getService(BuoyConstant.PLUGIN_NAME);
            } else {
                DebugConfig.d(TAG, BuoyConstant.PLUGIN_NAME + " 第一次getService不为空，services size=" + services.size());
            }

            if (null != services && !services.isEmpty()) {
                DebugConfig.d(TAG, BuoyConstant.PLUGIN_NAME + " 第二次getService不为空:" + services.get(0));
                hwBuoy = (IBuoyOpenSDK) (services.get(0));
            } else {
                DebugConfig.d(TAG, BuoyConstant.PLUGIN_NAME + " 第二次getService为空");
            }
            if (null == hwBuoy) {
                DebugConfig.d(TAG, "获取插件为空 :  " + BuoyConstant.PLUGIN_SERVICE_NAME);
            } else {
                return true;
            }

        }
        return false;
    }

    //帐号登录的回调
    private class LoginCallBack
            implements IHwIDCallBack {

        /**
         * Key  userState   userValidStatus userID  userName    languageCode    accesstoken
         */
        @Override
        public void onUserInfo(HashMap userInfo) {
            if (null == userInfo) {
                onResult("login", "fault");
            } else if (isNull((String) userInfo.get("accesstoken"))) {
                onResult("login", "fault");
            } else {
                onResult("login", "success");
                //加载浮标插件
                if (checkBuoyPluginLoad()) {
                    //初始化浮标
                    hwBuoy.init(FunctionAndroid.context, p);
                }

                if (payLogin) {
                    doPay(payName);
                }
            }
        }

    }

    private IPayHandler handler = new IPayHandler() {
        @Override
        public void onFinish(Map<String, String> payResp) {
            DebugConfig.d(TAG, "支付结束：payResp= " + payResp);
            // 处理支付结果
            String pay = "支付未成功！";
            DebugConfig.d(TAG, "支付结束，返回码： returnCode=" + payResp.get(PayParameters.returnCode));
            if ("0".equals(payResp.get(PayParameters.returnCode))) {
                if ("success".equals(payResp.get(PayParameters.errMsg))) {
                    // 支付成功，验证信息的安全性
                    payResp.remove(PayParameters.returnCode);
                    String sign = payResp.remove(PayParameters.sign);

                    // 待验签字符串需要去除returnCode和sign两个参数
                    String noSigna = HuaweiPayUtil.getSignData(payResp);

                    boolean s = Rsa.doCheck(noSigna, sign,getConfig("rsa_public"));

                    if (s) {
                        pay = "支付成功！";
                    } else {
                        pay = "支付成功，但验签失败！";
                    }
                    onResult("pay", "success");
                } else {
                    onResult("pay", "fault");
                }
            } else if ("30002".equals(payResp.get(PayParameters.returnCode))) {
                pay = "支付结果查询超时！";
                onResult("pay", "fault");
            }
        }
    };

    /**
     * 加载支付插件
     *
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean checkHuaweiPayPluginLoad() {
        initMicroKernel();
        if (framework != null) {
            framework.checkSinglePlugin(HuaweiPayPlugin, new UpdateNotifierHandler(framework));
            @SuppressWarnings("unchecked")
            List<Object> services = framework.getService(HuaweiPayPlugin);
            if (null == services || services.size() == 0) {
                DebugConfig.d(TAG, "第一次getService为空，并开始loadPlugin：" + HuaweiPayPlugin);
                framework.loadPlugin(HuaweiPayPlugin);
                DebugConfig.d(TAG, "loadPlugin结束：" + HuaweiPayPlugin);
            } else {
                DebugConfig.d(TAG, HuaweiPayPlugin + " 第一次getService不为空，services size=" + services.size());
            }

            Object payObject = framework.getPluginContext().getService(IHuaweiPay.interfaceName).get(0);

            if (null != payObject) {
                hwPay = (IHuaweiPay) payObject;
                DebugConfig.d(TAG, HuaweiPayPlugin + " 第二次getService不为空:");
            } else {
                DebugConfig.d(TAG, HuaweiPayPlugin + " 第二次getService为空:");
            }
            if (null == hwPay) {
                DebugConfig.d(TAG, "获取插件为空 :  " + HuaweiPayPlugin);
            } else {
                return true;
            }

        }
        return false;
    }

    /**
     * 和浮标sdk交互的回调
     *
     * @author c00206870
     * @version [版本号, 2014-4-1]
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    private class FloatListenerByCpImpl implements IBuoyCallBack {
        protected FloatListenerByCpImpl() {
        }

        @Override
        public void onInitStarted() {
        }

        @Override
        public void onInitFailed(int errorCode) {
        }

        @Override
        public void onInitSuccessed() {
            if (hwBuoy != null) {
                hwBuoy.showSamllWindow(FunctionAndroid.context);
            }
        }

        @Override
        public void onShowSuccssed() {
        }

        @Override
        public void onShowFailed(int errorCode) {
        }

        @Override
        public void onHidenSuccessed() {
        }

        @Override
        public void onHidenFailed(int errorCode) {
        }

        @Override
        public void onDestoryed() {
        }
    }
}
