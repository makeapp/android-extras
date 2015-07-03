package com.makeapp.android.extras.baidu;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import com.baidu.mobads.*;
import com.makeapp.android.extras.FunctionAd;
import com.makeapp.android.extras.FunctionAndroid;
import org.fun.FunctionFactory;

/**
 * Created by yuanyou on 2014/7/8.
 */
public class BaiduFunctionAd
        extends FunctionAd {

    public static final int ID = 6000;

    InterstitialAd interAd;

    public void showSplash() {
        final RelativeLayout adLayer = new RelativeLayout(FunctionAndroid.context);
        rootView.addView(adLayer);

        new SplashAd(FunctionAndroid.context, adLayer, new SplashAdListener() {
            @Override
            public void onAdDismissed() {
                System.out.println("onAdDismissed");
                rootView.removeView(adLayer);
            }

            @Override
            public void onAdFailed(String arg0) {
                System.out.println("onAdFailed");
            }

            @Override
            public void onAdPresent() {
                System.out.println("onAdPresent");
            }
        });
    }

    public Boolean showBanner(String param, String[] params) {
        View view = rootView.findViewById(BaiduFunctionAd.ID);
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
        AdView adView = new AdView(FunctionAndroid.context);
        adLayer.addView(adView);
        rootView.addView(adLayer);
        adLayer.setId(ID);
        return true;
    }

    public Boolean showInterstitial() {
        if (interAd == null) {
            interAd = new InterstitialAd(FunctionAndroid.context);
            interAd.setListener(new InterstitialAdListener() {
                @Override
                public void onAdReady() {
                    System.out.println("onAdReady");
                }

                @Override
                public void onAdPresent() {
                    System.out.println("onAdPresent");
                    onResult("interstitialPresent");
                }

                @Override
                public void onAdClick(InterstitialAd interstitialAd) {
                    System.out.println("onAdClick");
                    onResult("interstitialClicked");
                }

                @Override
                public void onAdDismissed() {
                    System.out.println("onAdDismissed");
                    interAd.loadAd();
                    onResult("interstitialClosed");
                }

                @Override
                public void onAdFailed(String s) {
                    System.out.println("onAdFailed");
                }
            });
        }
        if (interAd.isAdReady()) {
            interAd.showAd(activity);
            return true;
        } else {
            interAd.loadAd();
            return false;
        }
    }
}
