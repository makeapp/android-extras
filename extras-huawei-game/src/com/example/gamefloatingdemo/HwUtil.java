/*
 * 文 件 名:  HwUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  c00206870
 * 修改时间:  2014-5-30
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.example.gamefloatingdemo;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;

import com.android.huawei.pay.plugin.IHuaweiPay;
import com.android.huawei.pay.plugin.IPayHandler;
import com.android.huawei.pay.util.HuaweiPayUtil;
import com.android.huawei.pay.util.Rsa;
import com.hianalytics.android.intf.IHiAnalytics;
import com.huawei.gamebox.buoy.sdk.util.DebugConfig;
import com.huawei.hwid.openapi.out.microkernel.IHwIDOpenSDK;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  c00206870
 * @version  [版本号, 2014-5-30]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HwUtil
{
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
    
    public static String getPayResult(String payResult)
    {
        //处理支付结果
        String pay = "支付失败！";
        try
        {
            DebugConfig.d("getPayResult", "GET PAY RESULT = " + payResult);
            JSONObject jsonObject = new JSONObject(payResult);
            String returnCode = jsonObject.getString("returnCode");
            if (returnCode.equals("0"))
            {
                String errMsg = jsonObject.getString("errMsg");
                if (errMsg.equals("success"))
                {
                    
                    //支付成功，验证信息的安全性
                    String amount = jsonObject.getString("amount");
                    String sign = jsonObject.getString("sign");
                    String orderID = jsonObject.getString("orderID");
                    String requestId = jsonObject.getString("requestId");
                    String userName = jsonObject.getString("userName");
                    String time = jsonObject.getString("time");
                    Map<String, String> paramsa = new HashMap<String, String>();
                    paramsa.put("userName", userName);
                    paramsa.put("orderID", orderID);
                    paramsa.put("amount", amount);
                    paramsa.put("errMsg", errMsg);
                    paramsa.put("time", time);
                    paramsa.put("requestId", requestId);
                    
                    String noSigna = HuaweiPayUtil.getSignData(paramsa);
                    
                    boolean s = Rsa.doCheck(noSigna, sign, PartnerConfig.RSA_PUBLIC);
                    
                    if (s)
                    {
                        pay = "支付成功！";
                    }
                    else
                    {
                        pay = "支付成功，但验签失败！";
                    }
                    
                    DebugConfig.d("getPayResult", "Rsa.doChec = " + s);
                }
            }
            else if (returnCode.equals("30002"))
            {
                pay = "支付结果查询超时！";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return pay;
    }
    
    /**
     * 启动支付
     * amount：商品价格
     * productName：商品名称
     * productDesc：商品介绍
     * userName：商户名
     * @see [类、类#方法、类#成员]
     */
    public static void startPay(IHwIDOpenSDK hwId, IHuaweiPay hwPay, Activity activity, IPayHandler handler)
    {
        DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
        String requestId = format.format(new Date());
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("userID", PartnerConfig.userID);
        params.put("applicationID", PartnerConfig.applicationID);
        params.put("amount", "0.10");
        params.put("productName", "精品娃娃");
        params.put("productDesc", "宜家宜精品娃娃，超柔短毛绒海豚抱枕 75厘米 全国包邮");
        params.put("requestId", requestId);
        
        String noSign = HuaweiPayUtil.getSignData(params);
        String sign = Rsa.sign(noSign, PartnerConfig.RSA_PRIVATE);
        
        DebugConfig.d("startPay", "签名前： " + noSign);
        DebugConfig.d("startPay", " 签名后： " + sign);
        Map<String, Object> payInfo = new HashMap<String, Object>();
        payInfo.put("amount", "0.10");
        payInfo.put("productName", "精品娃娃");
        payInfo.put("requestId", requestId);
        payInfo.put("productDesc", "宜家宜精品娃娃，超柔短毛绒海豚抱枕 75厘米 全国包邮");
        payInfo.put("userName", "华为软件技术有限公司");
        payInfo.put("applicationID", PartnerConfig.applicationID);
        payInfo.put("userID", PartnerConfig.userID);
        payInfo.put("sign", sign);
        
        String notifyUrl = null;
        payInfo.put("notifyUrl", notifyUrl);
        payInfo.put("environment", HuaweiPayUtil.environment_live);
        String accessToken = "";
        HashMap userInfo = hwId.getDefaultUserInfo();
        if (userInfo != null)
        {
            accessToken = (String)userInfo.get("accesstoken");
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
    
    /**
     * 显示DEMO界面的文字提示，可变更
     * @param noticeText
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public static void setNoticeText(TextView noticeText, String msg)
    {
        if (null != noticeText)
        {
            noticeText.setText(msg);
        }
    }
    
    /**
     *  测试时只需要修改userID、applicationID、devPriKey、devPubKey这四个变量，
     *  若能够调起支付页面，且支付没问题，即可集成
     *  userID为开放联盟的支付id，applicationID为应用id
     *  devPubKey，devPriKey分别为开发者公钥、私钥，从开放联盟上获取
     *
     */
    public interface PartnerConfig
    {
      //游戏浮标的RSA私钥
        public static final String BUOY_PRIVATEKEY =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALDQTtHj3VXtggImeqYGDYklldZK\nazo2Sg2MwXbo/BNBrbSVcDJ6MnteFGgTWhd1lp8/pd2tJbbfS0ELfoV7EqcH7VjC03KSptlHWCpj\nIxOcJj99i8yu343KVg98/qtNv+USPTPblXjVeYxZKxcOyJHGNXwKK5UTK4XLAV2xPyx/AgMBAAEC\ngYBJB7EWllRsUm5ZbwVHn8ZleEW8Pf6uC5BoqlOaRr8fQQh7RyIRZ1GEjSZGmn+ioun0mrhqqEKk\nlvQhSJsYjO31qPL7gBxG5BishSXxQHcwAMr1P7RB9o9ogTU2s3QE3DZQSO48FlRubnPwAssp5cxy\ni4Y8sC/d6BML15swVQSg8QJBAOt8wsD9d/i/7tMFo53+6pYSjvXPXVcETdrzP43mU8YQtdwrZjle\nkUgvE4tVitxtXKCzW2NDQfvo6HQl3qhxq4cCQQDANygrR6fiTUalXRYjmy+AjxaT6/h1ABhGVtSo\nBD3+OubtOXK5gL/cVOYfqD2Wfxq0QlEp+woqhAgYwqlyz2VJAkB+sjUmGDlAACPCLTqGeuxDqeB4\nqASUGKC6uDztX4qa+cqelkr9er+3knx1bqSzS7OWUmlM0pbhrcHDG8zb26xpAkEApge+wcuuX1KC\noFoMworMeE6goPsl7OI9FZzxKYQojE4SpHyH9WYZ09bdxCCNuk5mIaha9VkrydesKr8SoOI2SQJB\nAJF9ktxI0B5W8yXqZgRPUmZ2zwgJv9pkzpB4Q2CjXupaGbcAaxPTbB8fqGiB+lY8HInOKm/wIqvm\nngYKscAfhiI=";
        
        //cpid
        public static final String cpId = "1";
        
        //支付ID。登录http://developer.huawei.com/后，开通支付权益即可获取。
        public static final String userID = "10086000000000293";
        
        // 应用id。在“应用管理”页面点击“创建应用”按钮后，可先保存为草稿之后，即可获取。
        public static final String applicationID = "10129306";
        
        // 开发者联盟上获取的（RSA）私钥,登录http://developer.huawei.com/后，开通支付权益即可获取。
        public static final String RSA_PRIVATE =
            "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAgv/vOAb0oKObkh4YXOryhVrEKoUn36b/esYxdTan0r5scs9HWbdz1MxlEaORltNnQb9Us87oHGI/7D5Uz5nd8QIDAQABAkATVcUwJs6qext2KJz98euTxT7Y68hj2Vkx/NjF7Sg+EYgQouZ5/yMmwQDESaJgTzrmq8KuclhFnxoCiAzhmiYFAiEA3cd5ujeP3xNJ6kU4pTgcGn8It4BAEt8BerwCCo/OND8CIQCXNpHWJ3wAeRihdPBYKkWgE4w1Byy7rNBie2wTXn6hzwIhAJIWrgaORwUozY22H0QmG80QVQubPZmwsGbKpYWTiL89AiBduq6VLy5W4Lkaw3CDRdiYi+VZrVPWFR2qHdT1AJq/0wIgL4lKdiI6fXV0hO8KLQ4KF/WTwtk7pXJWvDuOMtK/GL4=";
        
        // 开发者联盟上获取的（RSA）公钥,登录http://developer.huawei.com/后，开通支付权益即可获取。
        public static final String RSA_PUBLIC =
            "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIL/7zgG9KCjm5IeGFzq8oVaxCqFJ9+m/3rGMXU2p9K+bHLPR1m3c9TMZRGjkZbTZ0G/VLPO6BxiP+w+VM+Z3fECAwEAAQ==";
    }
}
