package com.makeapp.android.extras.tencent;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import com.makeapp.android.extras.FunctionAd;
import com.qq.e.ads.*;
import com.qq.e.gridappwall.GridAppWall;
import com.qq.e.gridappwall.GridAppWallListener;
import com.qq.e.splash.SplashAd;
import com.qq.e.splash.SplashAdListener;

/**
 * Created by yuanyou on 2014/7/8.
 */
public class TencentFunctionAd
        extends FunctionAd {

    InterstitialAd interAd;
    String appId = null;

    @Override
    protected void onAppCreate(Context activity) {
        super.onAppCreate(activity);
        appId = getConfig("app_id");
    }


    public void showSplash() {
        new SplashAd(activity, rootView, appId, getConfig("splash_id"),
                new SplashAdListener() {

                    @Override
                    public void onAdPresent() {
                        onResult("splash", "adPresent");
                    }

                    @Override
                    public void onAdFailed(int arg0) {
                        onResult("splash", "adFailed");
                    }

                    @Override
                    public void onAdDismissed() {
                        onResult("splash", "adDismissed");
                    }
                });
    }

    public void showAppWall() {
        String appwallId = getConfig("appwall_id");
        final GridAppWall g = new GridAppWall(appId, appwallId, activity, new GridAppWallListener() {

            @Override
            public void onAdPresent() {
                onResult("appwall", "adPresent");
            }

            @Override
            public void onAdFailed(int errCode) {
                onResult("appwall", "adFailed");
            }

            @Override
            public void onAdDismissed() {
                onResult("appwall", "adDismissed");
            }
        });
        g.showRelativeTo(0, 0);
    }

    public Boolean showInterstitial() {
//        if (interAd == null) {
        String interstitialId = getConfig("interstitial_id");
        interAd = new InterstitialAd(activity, appId, interstitialId);
        interAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onFail() {
                onResult("interstitial", "fail");
            }

            @Override
            public void onBack() {
                onResult("interstitial", "back");
            }

            public void onExposure() {
                onResult("interstitial", "exposure");
            }

            public void onClicked() {
                onResult("interstitial", "clicked");
            }

            @Override
            public void onAdReceive() {
                interAd.show(activity);
                onResult("interstitial", "adReceive");
            }
        });
//        }
        interAd.loadAd();
        return true;
    }

    public Boolean showBanner(String param, String[] params) {
        View view = rootView.findViewById(TencentFunctionAd.ID);
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
        String bannerId = getConfig("banner_id");
        AdView adView = new AdView(activity, AdSize.SMART_BANNER, appId, bannerId);
        adView.fetchAd(new AdRequest());
        adLayer.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onNoAd() {
                onResult("banner", "noAd");
            }

            @Override
            public void onAdReceiv() {
                onResult("banner", "adReceiv");
            }

            @Override
            public void onAdExposure() {
                onResult("banner", "adExposure");
            }

            @Override
            public void onBannerClosed() {
                onResult("banner", "bannerClosed");
            }
        });
        rootView.addView(adLayer);
        adLayer.setId(FunctionAd.ID);
        return true;
    }
}
