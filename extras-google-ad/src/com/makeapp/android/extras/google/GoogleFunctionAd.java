package com.makeapp.android.extras.google;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.ads.*;
import com.makeapp.android.extras.FunctionAd;
import org.fun.FunctionFactory;

/**
 * Created by yuanyou on 2014/7/8.
 */
public class GoogleFunctionAd
        extends FunctionAd {

    InterstitialAd interAd;

    public void showSplash() {

    }

    public void showAppWall() {
    }

    public Boolean showInterstitial() {
        if (interAd == null) {
            String admobPublisherId = getMetaString("publisher_id");
            interAd = new InterstitialAd(activity, admobPublisherId);
            interAd.setAdListener(new AdListener() {
                @Override
                public void onReceiveAd(Ad ad) {

                }

                @Override
                public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode errorCode) {

                }

                @Override
                public void onPresentScreen(Ad ad) {

                }

                @Override
                public void onDismissScreen(Ad ad) {
                    interAd.loadAd(new AdRequest());
                    FunctionFactory.getInstance().call("adInterstitialClosed", "");
                }

                @Override
                public void onLeaveApplication(Ad ad) {

                }
            });
        }
        interAd.loadAd(new AdRequest());
        interAd.show();
        return true;
    }

    public Boolean showBanner(String param,String[] args) {
        View view = rootView.findViewById(GoogleFunctionAd.ID);
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
        // 创建广告View
        String admobPublisherId = getMetaString("publisher_id");

        AdView adView = new AdView(activity, AdSize.BANNER, admobPublisherId);
        adView.loadAd(new AdRequest());
        adLayer.addView(adView);
        rootView.addView(adLayer);
        adLayer.setId(FunctionAd.ID);
        return true;
    }
}
