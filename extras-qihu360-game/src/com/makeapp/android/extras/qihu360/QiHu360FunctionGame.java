package com.makeapp.android.extras.qihu360;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.makeapp.android.extras.FunctionGame;
import com.makeapp.android.extras.FunctionPay;
import com.wandoujia.sdk.plugin.paydef.*;
import com.wandoujia.sdk.plugin.paysdkimpl.PayConfig;
import com.wandoujia.sdk.plugin.paysdkimpl.WandouAccountImpl;
import com.wandoujia.sdk.plugin.paysdkimpl.WandouPayImpl;
import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by yuanyou on 2014/8/16.
 */
public class QiHu360FunctionGame
        extends FunctionGame {

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        String appId = getConfig("app_id");
        String appKey = getConfig("app_key");

        Matrix.init(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Matrix.destroy(FunctionAndroid.context);
    }

    private String mAccessToken;

    // 登录、注册的回调
    private IDispatcherCallback mLoginCallback = new IDispatcherCallback() {

        private boolean isCancelLogin(String data) {
            try {
                JSONObject joData = new JSONObject(data);
                int errno = joData.optInt("errno", -1);
                if (-1 == errno) {
//                    Toast.makeText(SdkUserBaseActivity.this, data, Toast.LENGTH_LONG).show();
                    return true;
                }
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        public void onFinished(String data) {
            // press back
            if (isCancelLogin(data)) {
                return;
            }
//            // 显示一下登录结果
            Toast.makeText(FunctionAndroid.context, data, Toast.LENGTH_LONG).show();
//            mIsInOffline = false;
//            mQihooUserInfo = null;
//            Log.d(TAG, "mLoginCallback, data is " + data);
//            // 解析User info，这里获取到的user info 没有qid，需要单独从服务器获取
//            QihooUserInfo info = parseUserInfoFromLoginResult(data);
//            // 解析access_token
//            mAccessToken = parseAccessTokenFromLoginResult(data);
//
            try {
                JSONObject joRes = new JSONObject(data);

                JSONObject joData = joRes.getJSONObject("data");
                mAccessToken = joData.getString("access_token");

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (!TextUtils.isEmpty(mAccessToken)) {
//                // 登录结果直接返回的userinfo中没有qid，需要去应用的服务器获取用access_token获取一下带qid的用户信息
//                getUserInfo(info);
//            } else {
//                Toast.makeText(SdkUserBaseActivity.this, "get access_token failed!", Toast.LENGTH_LONG).show();
//            }
        }
    };

    // 支付的回调
    private IDispatcherCallback mPayCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
            JSONObject jsonRes;
            try {
                jsonRes = new JSONObject(data);
                // error_code 状态码 0 支付成功 -1 支付取消 1 支付失败 2 支付进行中
                // error_msg 状态描述
                int errorCode = jsonRes.getInt("error_code");
                String errorMsg = jsonRes.getString("error_msg");

                String text = "状态码：" + Integer.toString(errorCode) + ", 状态描述：" + errorMsg;//getString("状态码:%d, 状态描述：%s", errorCode, errorMsg);
//                Toast.makeText(SdkUserBaseActivity.this, text, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public String applyMain(String s) {
        String[] params = s.split(" ");
        if ("pay".equalsIgnoreCase(params[0])) {
            String name = params[1];
            Intent intent = getPayIntent(name);
            // 启动接口
            Matrix.invokeActivity(FunctionAndroid.context, intent, mPayCallback);
        } else if ("login".equalsIgnoreCase(params[0])) {
            Intent intent = getLoginIntent(false);
            IDispatcherCallback callback = mLoginCallback;
            Matrix.execute(FunctionAndroid.context, intent, callback);
        }
        return null;
    }


    /**
     * 生成调用360SDK登录接口的Intent
     *
     * @param isLandScape 是否横屏
     * @return intent
     */
    private Intent getLoginIntent(boolean isLandScape) {

        Intent intent = new Intent(FunctionAndroid.context, ContainerActivity.class);

        // 界面相关参数，360SDK界面是否以横屏显示。
        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // 必需参数，使用360SDK的登录模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_LOGIN);

        //是否显示关闭按钮
//        intent.putExtra(ProtocolKeys.IS_LOGIN_SHOW_CLOSE_ICON, getCheckBoxBoolean(R.id.isShowClose));

        // 可选参数，是否支持离线模式，默认值为false
//        intent.putExtra(ProtocolKeys.IS_SUPPORT_OFFLINE, getCheckBoxBoolean(R.id.isSupportOffline));

        // 可选参数，是否在自动登录的过程中显示切换账号按钮，默认为false
//        intent.putExtra(ProtocolKeys.IS_SHOW_AUTOLOGIN_SWITCH, getCheckBoxBoolean(R.id.isShowSwitchButton));

        // 可选参数，是否隐藏欢迎界面
//        intent.putExtra(ProtocolKeys.IS_HIDE_WELLCOME, getCheckBoxBoolean(R.id.isHideWellcome));

        // 可选参数，登录界面的背景图片路径，必须是本地图片路径
//        intent.putExtra(ProtocolKeys.UI_BACKGROUND_PICTRUE, getUiBackgroundPicPath());

        //-- 以下参数仅仅针对自动登录过程的控制
        // 可选参数，自动登录过程中是否不展示任何UI，默认展示。
//        intent.putExtra(ProtocolKeys.IS_AUTOLOGIN_NOUI, getCheckBoxBoolean(R.id.isAutoLoginHideUI));

        // 可选参数，静默自动登录失败后是否显示登录窗口，默认不显示
//        intent.putExtra(ProtocolKeys.IS_SHOW_LOGINDLG_ONFAILED_AUTOLOGIN, getCheckBoxBoolean(R.id.isShowDlgOnFailedAutoLogin));

        return intent;
    }

    private Intent getPayIntent(String name) {
        String id = getConfig(name + ".id");
        String title = getConfig(name + ".name");
        String appName = getConfig(name + ".appName");
        String desc = getConfig(name + ".desc");
        String price = getConfig(name + ".price");
        String rate = getConfig(name + ".rate");
        String callback = getConfig(name + ".callback_url");
        Bundle bundle = new Bundle();
        String userId = "";
        String username = "";
        // 界面相关参数，360SDK界面是否以横屏显示。
//            bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // *** 以下非界面相关参数 ***

        // 设置QihooPay中的参数。

        // 必需参数，360账号id，整数。
        bundle.putString(ProtocolKeys.QIHOO_USER_ID, userId);

        // 必需参数，所购买商品金额, 以分为单位。金额大于等于100分，360SDK运行定额支付流程； 金额数为0，360SDK运行不定额支付流程。
        bundle.putString(ProtocolKeys.AMOUNT, price);

        // 必需参数，人民币与游戏充值币的默认比例，例如2，代表1元人民币可以兑换2个游戏币，整数。
        bundle.putString(ProtocolKeys.RATE, rate);

        // 必需参数，所购买商品名称，应用指定，建议中文，最大10个中文字。
        bundle.putString(ProtocolKeys.PRODUCT_NAME, title);

        // 必需参数，购买商品的商品id，应用指定，最大16字符。
        bundle.putString(ProtocolKeys.PRODUCT_ID, id);

        // 必需参数，应用方提供的支付结果通知uri，最大255字符。360服务器将把支付接口回调给该uri，具体协议请查看文档中，支付结果通知接口–应用服务器提供接口。
        bundle.putString(ProtocolKeys.NOTIFY_URI, callback);

        // 必需参数，游戏或应用名称，最大16中文字。
        bundle.putString(ProtocolKeys.APP_NAME, appName);

        // 必需参数，应用内的用户名，如游戏角色名。 若应用内绑定360账号和应用账号，则可用360用户名，最大16中文字。（充值不分区服，
        // 充到统一的用户账户，各区服角色均可使用）。
        bundle.putString(ProtocolKeys.APP_USER_NAME, username);

        // 必需参数，应用内的用户id。
        // 若应用内绑定360账号和应用账号，充值不分区服，充到统一的用户账户，各区服角色均可使用，则可用360用户ID最大32字符。
//            bundle.putString(ProtocolKeys.APP_USER_ID, pay.getAppUserId());

        // 可选参数，应用扩展信息1，原样返回，最大255字符。
//            bundle.putString(ProtocolKeys.APP_EXT_1, pay.getAppExt1());

        // 可选参数，应用扩展信息2，原样返回，最大255字符。
//            bundle.putString(ProtocolKeys.APP_EXT_2, pay.getAppExt2());

        // 可选参数，应用订单号，应用内必须唯一，最大32字符。
//            bundle.putString(ProtocolKeys.APP_ORDER_ID, pay.getAppOrderId());

        // 必需参数，使用360SDK的支付模块。
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);

        Intent intent = new Intent(FunctionAndroid.context, ContainerActivity.class);
        intent.putExtras(bundle);
        return intent;
    }
}
