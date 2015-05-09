package com.makeapp.android.extras.domob;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import cn.domob.android.ads.DomobAdView;
import cn.domob.android.ads.DomobInterstitialAd;
import cn.domob.android.ads.DomobSplashAd;
import com.makeapp.android.extras.FunctionAd;
import com.makeapp.android.extras.FunctionAndroid;

/**
 * Created by yuanyou on 2014/7/8.
 */
public class DomobFunctionAd
        extends FunctionAd {

    String pubId;
    String bannerPPId;
    String interstitialPPId;
    String splashPPID;

    DomobInterstitialAd interAd;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        pubId = getConfig("pub_id");
        bannerPPId = getConfig("banner_id");
        interstitialPPId = getConfig("interstitial_id");
        splashPPID = getConfig("splash_id");
    }

    public void showSplash() {
        final RelativeLayout adLayer = new RelativeLayout(FunctionAndroid.context);
        rootView.addView(adLayer);

        DomobSplashAd splashAd = new DomobSplashAd(FunctionAndroid.context, pubId, splashPPID, DomobSplashAd.DomobSplashMode.DomobSplashModeFullScreen);
        if (splashAd.isSplashAdReady()) {
            splashAd.splash(FunctionAndroid.context, adLayer);
        }
    }

    public Boolean showBanner(String param, String[] params) {
        View view = rootView.findViewById(DomobFunctionAd.ID);
        if (view != null) {
            rootView.removeView(view);
        }

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
        // 创建广告View
        DomobAdView adView = new DomobAdView(FunctionAndroid.context, pubId, bannerPPId);
        adLayer.addView(adView);
        rootView.addView(adLayer);
        adLayer.setId(FunctionAd.ID);
        return true;
    }

    public Boolean showInterstitial() {
        if (interAd == null) {
            interAd = new DomobInterstitialAd(FunctionAndroid.context, pubId, interstitialPPId,
                    DomobInterstitialAd.INTERSITIAL_SIZE_300X250);
        }
        if (interAd.isInterstitialAdReady()) {
            interAd.showInterstitialAd(FunctionAndroid.context);
            return true;
        } else {
            interAd.loadInterstitialAd();
            return false;
        }
    }
}
