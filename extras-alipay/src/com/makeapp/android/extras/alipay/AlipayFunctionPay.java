package com.makeapp.android.extras.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.alipay.sdk.app.PayTask;
import com.makeapp.android.extras.FunctionAndroid;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class AlipayFunctionPay
        extends FunctionAndroid<String, Void> {

    public static final String TAG = "alipay-sdk";

    private static final int RQF_PAY = 1;

    public String DEFAULT_PARTNER = "";

    public String DEFAULT_SELLER = "";

    public String PRIVATE = "";

    public String PUBLIC = "";

    public String callbackURL = "";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        DEFAULT_PARTNER = getConfig("partner_id");
        DEFAULT_SELLER = getConfig("seller_name");
        PRIVATE = getConfig("private_key");
        PUBLIC = getConfig("public_key");
        callbackURL = getConfig("notify_url");
    }

    public static Map<String, String> stringToMap(String result) {
        Map<String, String> resultMap = new HashMap();
        if (result != null) {
            String[] returnParams = result.split("&");
            for (String param : returnParams) {
                String[] kv = param.split("=");
                if (kv != null && kv.length > 1) {
                    String value = kv[1];
                    if (value.startsWith("\"")) {
                        value = value.substring(1);
                    }
                    if (value.endsWith("\"")) {
                        value = value.substring(0, value.length() - 1);
                    }
                    resultMap.put(kv[0], value);
                }
            }
        }
        return resultMap;
    }

    @Override
    public Void apply(String s) {

        String params[] = s.split(" ");
        Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        Result resultObj = new Result((String) msg.obj);
                        String resultStatus = resultObj.resultStatus;
                        String result = resultObj.result;
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
//                            Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            Map map = stringToMap(result);

                            onResult("pay", "success " + map.get("out_trade_no"));
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
//                                Toast.makeText(PayDemoActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                                onResult("pay", "process");
                            } else {
//                                Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                                onResult("pay", "fault");
                            }
                        }
                        break;
                    }
                    case SDK_CHECK_FLAG: {
                        onResult("pay", "fault");
//                        Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;
                }
            }

            ;
        };

        if ("pay".equals(params[0])) {
            String payCode = params[1];
            String orderId = params[2];
            String price = getConfig(payCode + ".price");
            String subject = getConfig(payCode + ".subject");
            String body = getConfig(payCode + ".body", subject);
            pay(orderId, subject, body, price, mHandler);
        } else if ("config".equals(params[0])) {
            if ("notify_url".equals(params[1])) {
                callbackURL = params[2];
            }
        }

        return null;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, PRIVATE);
    }


    public void pay(String orderId, String subject, String body, String fee, final Handler mHandler) {
        try {
            Log.i("ExternalPartner", "onItemClick");
            String orderInfo = getOrderInfo(orderId, subject, body, fee);
            String sign = sign(orderInfo);
            try {
                // 仅需对sign 做URL编码
                sign = URLEncoder.encode(sign, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                    + getSignType();

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(activity);
                    // 调用支付接口
                    String result = alipay.pay(payInfo);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            Thread payThread = new Thread(payRunnable);
            payThread.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String orderId, String subject, String body, String price) {
        // 合作者身份ID
        String orderInfo = "partner=" + "\"" + DEFAULT_PARTNER + "\"";

        // 卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + DEFAULT_SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + callbackURL + "\"";

        // 接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=\"" + callbackURL + "\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
