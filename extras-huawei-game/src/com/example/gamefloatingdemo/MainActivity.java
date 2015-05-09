package com.example.gamefloatingdemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.huawei.pay.plugin.IHuaweiPay;
import com.android.huawei.pay.plugin.IPayHandler;
import com.android.huawei.pay.plugin.PayParameters;
import com.android.huawei.pay.util.HuaweiPayUtil;
import com.android.huawei.pay.util.Rsa;
import com.example.gamefloatingdemo.HwUtil.PartnerConfig;
import com.huawei.deviceCloud.microKernel.core.MicroKernelFramework;
import com.huawei.deviceCloud.microKernel.util.EXLogger;
import com.huawei.gamebox.buoy.sdk.IBuoyCallBack;
import com.huawei.gamebox.buoy.sdk.IBuoyOpenSDK;
import com.huawei.gamebox.buoy.sdk.InitParams;
import com.huawei.gamebox.buoy.sdk.util.BuoyConstant;
import com.huawei.gamebox.buoy.sdk.util.DebugConfig;
import com.huawei.hwid.openapi.out.IHwIDCallBack;
import com.huawei.hwid.openapi.out.microkernel.IHwIDOpenSDK;
import com.tiger.zgjm.R;

/**
 * DEMO主界面，提供给开发商参考
 * 开发商可以根据自己的实际业务进行简单调整，但前提要保证业务和功能的稳定以及合理
 * 比如：开发商如果在界面恢复的时候不想显示悬浮窗，那么就去掉onResume中的如下逻辑：
 * if (hwBuoy != null)
        {
            hwBuoy.showSamllWindow(getApplicationContext());
        }
 * @author  c00206870
 * @version  [版本号, 2014-6-4]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MainActivity extends Activity implements OnClickListener
{
    private static final String TAG = MainActivity.class.getSimpleName();
    
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
    
    //demo控件
    private TextView noticeText;
    
    private Button huaweiPay;
    
    private Button login;
    
    private Button logout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noticeText = (TextView)findViewById(R.id.textView1);
        noticeText.setText("未开始");
        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(this);
        logout.setVisibility(View.GONE);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(this);
        login.setVisibility(View.VISIBLE);
        huaweiPay = (Button)findViewById(R.id.huaweipay);
        huaweiPay.setOnClickListener(this);
        huaweiPay.setVisibility(View.GONE);
        p =
            new InitParams(PartnerConfig.applicationID, PartnerConfig.cpId, PartnerConfig.BUOY_PRIVATEKEY,
                new FloatListenerByCpImpl());
        //开启悬浮窗的log
        DebugConfig.setLog(true);
        
        //开启插件加载的log
        EXLogger.setLevel(Log.VERBOSE);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        //在demo界面恢复的时候又显示浮标，和onPause配合使用
        if (hwBuoy != null)
        {
            hwBuoy.showSamllWindow(getApplicationContext());
        }
    }
    
    /** 
     * 在界面暂停的时候可以影藏窗口
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        //在demo界面暂停的时候，隐藏浮标，和onResume配合使用
        if (hwBuoy != null)
        {
            hwBuoy.hideSmallWindow(getApplicationContext());
            hwBuoy.hideBigWindow(getApplicationContext());
        }
    }
    
    /**
     * 在界面销毁时销毁浮标
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //在退出的时候销毁浮标
        if (hwBuoy != null)
        {
            hwBuoy.destroy(getApplicationContext());
        }
        //清空帐号资源
        if (null != hwId)
        {
            hwId.releaseResouce();
        }
    }
    
    /**
     * 初始化微内核
     * @see [类、类#方法、类#成员]
     */
    private void initMicroKernel()
    {
        try
        {
            if (null == framework)
            {
                //getInstance的参数不能为空，否则会导致后面的逻辑失败
                framework = MicroKernelFramework.getInstance(this);
            }
            framework.start();
        }
        catch (Exception e)
        {
        }
    }
    
    /**
     * 加载浮标插件
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    private boolean checkBuoyPluginLoad()
    {
        initMicroKernel();
        if (framework != null)
        {
            framework.checkSinglePlugin(BuoyConstant.PLUGIN_NAME, new UpdateNotifierHandler(framework));
            List<Object> services = framework.getService(BuoyConstant.PLUGIN_NAME);
            if (null == services || services.size() == 0)
            {
                DebugConfig.d(TAG, "第一次getService为空，并开始loadPlugin：" + BuoyConstant.PLUGIN_NAME);
                framework.loadPlugin(BuoyConstant.PLUGIN_NAME);
                DebugConfig.d(TAG, "loadPlugin结束： " + BuoyConstant.PLUGIN_NAME);
                services = framework.getService(BuoyConstant.PLUGIN_NAME);
            }
            else
            {
                DebugConfig.d(TAG, BuoyConstant.PLUGIN_NAME + " 第一次getService不为空，services size=" + services.size());
            }
            
            if (null != services && !services.isEmpty())
            {
                DebugConfig.d(TAG, BuoyConstant.PLUGIN_NAME + " 第二次getService不为空:" + services.get(0));
                hwBuoy = (IBuoyOpenSDK)(services.get(0));
            }
            else
            {
                DebugConfig.d(TAG, BuoyConstant.PLUGIN_NAME + " 第二次getService为空");
            }
            if (null == hwBuoy)
            {
                DebugConfig.d(TAG, "获取插件为空 :  " + BuoyConstant.PLUGIN_SERVICE_NAME);
            }
            else
            {
                return true;
            }
            
        }
        return false;
    }
    
    /**
     * 加载帐号插件
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean checkAccountPluginLoad()
    {
        initMicroKernel();
        if (framework != null)
        {
            framework.checkSinglePlugin(HwUtil.HWID_PLUS_NAME, new UpdateNotifierHandler(framework));
            @SuppressWarnings("unchecked")
            List<Object> services = framework.getService(HwUtil.HWID_PLUS_NAME);
            if (null == services || services.size() == 0)
            {
                DebugConfig.d(TAG, "第一次getService为空，并开始loadPlugin：" + HwUtil.HWID_PLUS_NAME);
                framework.loadPlugin(HwUtil.HWID_PLUS_NAME);
                DebugConfig.d(TAG, "loadPlugin结束：" + HwUtil.HWID_PLUS_NAME);
                services = framework.getService(HwUtil.HWID_PLUS_NAME);
            }
            else
            {
                DebugConfig.d(TAG, HwUtil.HWID_PLUS_NAME + " 第一次getService不为空，services size=" + services.size());
            }
            
            if (null != services && !services.isEmpty())
            {
                DebugConfig.d(TAG, HwUtil.HWID_PLUS_NAME + " 第二次getService不为空:" + services.get(0));
                hwId = (IHwIDOpenSDK)(services.get(0));
            }
            else
            {
                DebugConfig.d(TAG, "获取插件为空 :  " + HwUtil.HWID_PLUS_NAME);
            }
            if (null == hwId)
            {
                DebugConfig.d(TAG, "获取插件为空 :  " + HwUtil.HWID_PLUS_NAME);
            }
            else
            {
                return true;
            }
            
        }
        return false;
    }
    
    /**
     * 加载支付插件
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean checkHuaweiPayPluginLoad()
    {
        initMicroKernel();
        if (framework != null)
        {
            framework.checkSinglePlugin(HwUtil.HuaweiPayPlugin, new UpdateNotifierHandler(framework));
            @SuppressWarnings("unchecked")
            List<Object> services = framework.getService(HwUtil.HuaweiPayPlugin);
            if (null == services || services.size() == 0)
            {
                DebugConfig.d(TAG, "第一次getService为空，并开始loadPlugin：" + HwUtil.HuaweiPayPlugin);
                framework.loadPlugin(HwUtil.HuaweiPayPlugin);
                DebugConfig.d(TAG, "loadPlugin结束：" + HwUtil.HuaweiPayPlugin);
            }
            else
            {
                DebugConfig.d(TAG, HwUtil.HuaweiPayPlugin + " 第一次getService不为空，services size=" + services.size());
            }
            
            Object payObject = framework.getPluginContext().getService(IHuaweiPay.interfaceName).get(0);
            
            if (null != payObject)
            {
                hwPay = (IHuaweiPay)payObject;
                DebugConfig.d(TAG, HwUtil.HuaweiPayPlugin + " 第二次getService不为空:");
            }
            else
            {
                DebugConfig.d(TAG, HwUtil.HuaweiPayPlugin + " 第二次getService为空:");
            }
            if (null == hwPay)
            {
                DebugConfig.d(TAG, "获取插件为空 :  " + HwUtil.HuaweiPayPlugin);
            }
            else
            {
                return true;
            }
            
        }
        return false;
    }
    
    //帐号登录的回调
    private class LoginCallBack implements IHwIDCallBack
    {
        
        /**
         * Key  userState   userValidStatus userID  userName    languageCode    accesstoken
         */
        @Override
        public void onUserInfo(HashMap userInfo)
        {
            if (null == userInfo)
            {
                huaweiPay.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
                HwUtil.setNoticeText(noticeText, "登录失败！ \n userInfo为空");
            }
            else if (HwUtil.isNull((String)userInfo.get("accesstoken")))
            {
                huaweiPay.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
                HwUtil.setNoticeText(noticeText, "登录失败！ \n accesstoken为空 \n MResReqHandler=" + userInfo.toString());
            }
            else
            {
                DebugConfig.d(TAG, "ResReqHandler onComplete=" + userInfo.toString());
                HwUtil.setNoticeText(noticeText, "登录成功 \n MResReqHandler =  " + userInfo.toString());
                huaweiPay.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                //加载浮标插件
                if (checkBuoyPluginLoad())
                {
                    //初始化浮标
                    hwBuoy.init(getApplicationContext(), p);
                }
                else
                {
                    HwUtil.setNoticeText(noticeText, "浮标插件加载失败");
                }
            }
        }
        
    }
    
    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.login)
        {
            if (checkAccountPluginLoad())
            {
                HwUtil.setNoticeText(noticeText, "帐号插件加载成功，开始登录");
                hwId.setLoginProxy(MainActivity.this, p.getAppid(), new LoginCallBack(), new Bundle());
                hwId.login(new Bundle());
            }
            else
            {
                HwUtil.setNoticeText(noticeText, "帐号插件加载失败");
            }
        }
        else if (v.getId() == R.id.logout)
        {
            //退出帐号，清除
            if (null != hwId)
            {
                hwId.logout();
                huaweiPay.setVisibility(View.GONE);
                if (null != hwBuoy)
                {
                    hwBuoy.destroy(getApplicationContext());
                }
                logout.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                //清空帐号资源
                hwId.releaseResouce();
            }
        }
        else if (v.getId() == R.id.huaweipay)
        {
            if (checkHuaweiPayPluginLoad())
            {
                HwUtil.setNoticeText(noticeText, "支付插件加载成功，开始启动支付");
                HwUtil.startPay(hwId, hwPay, MainActivity.this, handler);
            }
            else
            {
                HwUtil.setNoticeText(noticeText, "支付插件加载失败");
            }
        }
    }
    
    private IPayHandler handler = new IPayHandler()
    {
        @Override
        public void onFinish(Map<String, String> payResp)
        {
            DebugConfig.d(TAG, "支付结束：payResp= " + payResp);
            // 处理支付结果
            String pay = "支付未成功！";
            DebugConfig.d(TAG, "支付结束，返回码： returnCode=" + payResp.get(PayParameters.returnCode));
            if ("0".equals(payResp.get(PayParameters.returnCode)))
            {
                if ("success".equals(payResp.get(PayParameters.errMsg)))
                {
                    // 支付成功，验证信息的安全性
                    payResp.remove(PayParameters.returnCode);
                    String sign = payResp.remove(PayParameters.sign);

                    // 待验签字符串需要去除returnCode和sign两个参数
                    String noSigna = HuaweiPayUtil.getSignData(payResp);

                    boolean s = Rsa.doCheck(noSigna, sign, PartnerConfig.RSA_PUBLIC);

                    if (s)
                    {
                        pay = "支付成功！";
                    }
                    else
                    {
                        pay = "支付成功，但验签失败！";
                    }
                    DebugConfig.d(TAG, "支付结束：pay= " + sign + " sign " + noSigna + "Rsa.doChec = " + s);
                }
                else
                {
                    DebugConfig.d(TAG, "支付失败 errMsg= " + payResp.get(PayParameters.errMsg));
                }
            }
            else if ("30002".equals(payResp.get(PayParameters.returnCode)))
            {
                pay = "支付结果查询超时！";
            }
            DebugConfig.d(TAG, " 支付结果 result = " + pay);
            Toast.makeText(MainActivity.this, pay, Toast.LENGTH_SHORT).show();
        }
    };
    
    /**
     * 和浮标sdk交互的回调
     * 
     * @author  c00206870
     * @version  [版本号, 2014-4-1]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    private class FloatListenerByCpImpl implements IBuoyCallBack
    {
        protected FloatListenerByCpImpl()
        {
        }
        
        @Override
        public void onInitStarted()
        {
            HwUtil.setNoticeText(noticeText, "初始化开始");
        }
        
        @Override
        public void onInitFailed(int errorCode)
        {
            HwUtil.setNoticeText(noticeText, "初始化失败 ===  " + errorCode);
        }
        
        @Override
        public void onInitSuccessed()
        {
            HwUtil.setNoticeText(noticeText, "初始化成功");
            if (hwBuoy != null)
            {
                hwBuoy.showSamllWindow(getApplicationContext());
            }
        }
        
        @Override
        public void onShowSuccssed()
        {
            HwUtil.setNoticeText(noticeText, "显示成功");
        }
        
        @Override
        public void onShowFailed(int errorCode)
        {
            HwUtil.setNoticeText(noticeText, "显示失败 ===  " + errorCode);
        }
        
        @Override
        public void onHidenSuccessed()
        {
            HwUtil.setNoticeText(noticeText, "隐藏成功");
        }
        
        @Override
        public void onHidenFailed(int errorCode)
        {
            HwUtil.setNoticeText(noticeText, "隐藏失败 ===" + errorCode);
        }
        
        @Override
        public void onDestoryed()
        {
            HwUtil.setNoticeText(noticeText, "退出完成");
        }
    }
}
