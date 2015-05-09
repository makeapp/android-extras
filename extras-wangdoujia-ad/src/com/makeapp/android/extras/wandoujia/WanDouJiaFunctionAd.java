package com.makeapp.android.extras.wandoujia;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import com.baidu.mobads.InterstitialAd;
import com.makeapp.android.extras.FunctionAd;
import com.pubukeji.diandeows.adviews.DiandeBanner;
import com.wandoujia.ads.sdk.Ads;
import org.fun.FunctionFactory;

/**
 * Created by yuanyou on 2014/7/28.
 */
public class WanDouJiaFunctionAd
        extends FunctionAd {

    DiandeBanner bannerAD;

    InterstitialAd interAd;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        try {
            String wandoujiaAppid = getConfig("app_id");
            String wandoujiaAppkey = getConfig("app_key");
            Ads.init(FunctionAndroid.context, wandoujiaAppid, wandoujiaAppkey);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showSplash() {
        String wandoujiaAppid = getConfig("splash_id");
        Ads.showAppWidget(FunctionAndroid.context, null, wandoujiaAppid, Ads.ShowMode.FULL_SCREEN);
    }

    @Override
    public Boolean showBanner(String param, String[] params) {
        if (bannerAD == null) {
            RelativeLayout adLayer = new RelativeLayout(FunctionAndroid.context);
            if ("Top".equalsIgnoreCase(param)) {
                adLayer.setHorizontalGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            } else if ("Bottom".equalsIgnoreCase(param)) {
                adLayer.setVerticalGravity(Gravity.BOTTOM);
                adLayer.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            }

            if (params.length > 4) {
                int width = Integer.parseInt(params[3]);
                int height = Integer.parseInt(params[4]);
                adLayer.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
            }

            RelativeLayout sdkLayer = new RelativeLayout(FunctionAndroid.context);
            rootView.addView(sdkLayer);

            //banner
//            RelativeLayout bannerLayer = new RelativeLayout(context);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(600, 100);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            bannerLayer.setLayoutParams(layoutParams);
//            sdkLayer.setHorizontalGravity(Gravity.CENTER);
            String bannerID = getConfig("banner_id");
            Ads.showBannerAd(FunctionAndroid.context, adLayer, bannerID);
            sdkLayer.addView(adLayer);

            // 创建广告View
//            Ads.showBannerAd(context, adLayer, bannerID);
//            rootView.addView(adLayer);
        }
        return true;
    }

    public Boolean showInterstitial() {
        String bannerID = getConfig("interstitial_id");
        Ads.showAppWidget(FunctionAndroid.context, null, bannerID, Ads.ShowMode.FULL_SCREEN, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionFactory.getInstance().call("adInterstitialClicked", "");
            }
        });
        return true;
    }
}
