package com.makeapp.android.extras.chinaunicom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.makeapp.android.extras.FunctionAndroid;
import com.unicom.woopenoneway.utiltools.RSACoder;
import org.fun.FunctionFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yuanyou on 2014/8/8.
 */
public class ChinaUnicomFunctionPay
        extends FunctionAndroid<String, Boolean> {

    private String appId;
    private String appName;
    private String appCompany;
    private String appDevPhone;
    private String type;

    int SDK_DATA_REQ_OFLINE = 1000;
    int SDK_DATA_REQ_ONLINE = 1001;

    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        appId = getConfig("app_id");
        appName = getConfig("app_name");
        appCompany = getConfig("app_vender");
        appDevPhone = getConfig("app_vender_phone");
        type = getConfig("type");
    }

    private String getAmount(String price, String quantity) {
        return String.valueOf(Integer.parseInt(price) * Integer.parseInt(quantity));
    }

    private String genTradeNo() {
        return dateFormat.format(new Date());
    }

    @Override
    public Boolean apply(String s) {

        String[] params = s.split(" ");
        String command = params[0];
        String name = params[1];

        if ("online".equalsIgnoreCase(type)) {
            Intent intent = new Intent(FunctionAndroid.context, com.unicom.woopenoneway.UnicomWoOpenPaymentMainActivity.class);
            Bundle goodBundle = new Bundle();

            String goodid = getConfig(name + ".id");// 商品ID
            String goodprice = getConfig(name + ".price");// 商品单价
            String goodamount = goodprice;// 总价

            goodBundle.putString("appid", appId);
            goodBundle.putString("goodid", goodid);
            goodBundle.putString("goodprice", goodprice);
            goodBundle.putString("goodcount", "1");
            goodBundle.putString("goodamount", goodamount);
            goodBundle.putString("access_token", "");
            goodBundle.putString("devloperpayid", "123456789" + getUUID() + "");
            intent.putExtras(goodBundle);
            activity.startActivityForResult(intent, SDK_DATA_REQ_ONLINE);
        } else {

            Intent intent = new Intent(FunctionAndroid.context, com.unicom.woopensmspayment.UnicomWoOpenPaymentMainActivity.class);
            String product = getConfig(name + ".name");   //商品名称
            String productId = getConfig(name + ".id"); //商品ID
            String money = getConfig(name + ".price");    //金额
            String cpTradeId = genTradeNo(); //交易号

            Bundle bundle = new Bundle();
            bundle.putString("appId", appId);
            bundle.putString("product", product);
            bundle.putString("productId", productId);
            bundle.putString("money", money);
            bundle.putString("cpTradeId", cpTradeId);
            bundle.putString("appName", appName);
            bundle.putString("company", appCompany);
            bundle.putString("developerServicePhone", appDevPhone);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, SDK_DATA_REQ_OFLINE);
        }
        return true;
    }

    public static String getUUID() {
        String var0 = UUID.randomUUID().toString();
        return var0.substring(0, 8) + var0.substring(9, 13) + var0.substring(14, 18) + var0.substring(19, 23) + var0.substring(24);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == SDK_DATA_REQ_OFLINE) {
            if (data.getIntExtra("result", 1) == 0) {// 支付成功
                onResult("pay success");
            } else {
                String error = data.getStringExtra("errorstr");// 支付失败
                onResult("pay error " + error);
            }
        } else if (requestCode == SDK_DATA_REQ_ONLINE) {
            if (data.getIntExtra("result", 1) == 0) {
                Bundle bundle = data.getExtras();
                String returnJson = bundle.getString("returnJson");
                String serverSignature = bundle.getString("serverSignature");
                try {
                    boolean sigFlag = RSACoder.verify(returnJson.getBytes(), RSACoder.publicKey, serverSignature);
                    if (sigFlag) {
                        // 验签成功
                        System.out.println("支付成功");
                        onResult("pay success");
                    } else {
                        // 验签失败
                        System.out.println("支付失败");
                        onResult("pay error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 支付失败
                    System.out.println("支付失败");
                    onResult("pay error " + e.getMessage());
                }
            } else {
                String error = data.getStringExtra("errorstr");
                onResult("pay error " + error);
            }
        }
    }
}
