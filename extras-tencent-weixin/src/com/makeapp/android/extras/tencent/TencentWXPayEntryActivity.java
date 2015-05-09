package com.makeapp.android.extras.tencent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.fun.Function;
import org.fun.FunctionFactory;

public class TencentWXPayEntryActivity
        extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        Function function = FunctionFactory.getInstance().getFunction("weixin");
        if (function instanceof TencentFunctionWeiXin) {
            TencentFunctionWeiXin handler = (TencentFunctionWeiXin) function;
            api = WXAPIFactory.createWXAPI(this,handler.getAppId());
            api.handleIntent(getIntent(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.d(TAG, "onReq" + req);
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode + " " + resp.errStr);

        if (resp.errCode == 0) {
            FunctionFactory.callLocal("analysis", "onEvent 11");
        } else {
            FunctionFactory.callLocal("analysis", "onEvent 12");
        }

        Function function = FunctionFactory.getInstance().getFunction("weixin");
        if (function instanceof IWXAPIEventHandler) {
            IWXAPIEventHandler handler = (IWXAPIEventHandler) function;
            handler.onResp(resp);
        }
//
//        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
//                FunctionFactory.callLocal("pay_callback", "pay success 1");
//            }
//        }
        finish();
    }
}