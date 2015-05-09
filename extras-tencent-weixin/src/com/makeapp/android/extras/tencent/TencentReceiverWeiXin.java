package com.makeapp.android.extras.tencent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class TencentReceiverWeiXin
        extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String appId = getMetaData(context).getString("WEIXIN_APPID");
        final IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
        api.registerApp(appId);
    }

    public Bundle getMetaData(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null) {
                return applicationInfo.metaData;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new Bundle();
    }
}
