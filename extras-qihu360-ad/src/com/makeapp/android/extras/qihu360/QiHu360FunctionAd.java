package com.makeapp.android.extras.qihu360;

import android.app.Activity;
import android.view.Gravity;
import android.widget.RelativeLayout;
import com.makeapp.android.extras.FunctionAd;
import com.pubukeji.diandeows.AdType;
import com.pubukeji.diandeows.adviews.DiandeAdView;
import com.pubukeji.diandeows.adviews.DiandeBanner;
import com.pubukeji.diandeows.adviews.DiandeResultCallback;

/**
 * Created by yuanyou on 2014/7/28.
 */
public class QiHu360FunctionAd
        extends FunctionAd {

    public static final int ID = 6000;

    DiandeBanner bannerAD;

    public void showSplash() {
        String  insertID = getConfig("splash_id");
        DiandeAdView full = new DiandeAdView(FunctionAndroid.context, insertID, AdType.FULLSCREEN);

        full.setRequestCallBack(new DiandeResultCallback() {

            @Override
            public void onSuccess(boolean result, String message) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailed(String errorMessage) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAdShowSuccess(int code, String message) {
            }
        });
        full.load();
        full.show();
    }

    public Boolean showBanner(String param, String[] params) {
        if (bannerAD == null) {
            RelativeLayout adLayer = new RelativeLayout(FunctionAndroid.context);
            if ("Top".equalsIgnoreCase(param)) {
                adLayer.setHorizontalGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            } else if ("Bottom".equalsIgnoreCase(param)) {
                adLayer.setVerticalGravity(Gravity.BOTTOM);
                adLayer.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            }
            // 创建广告View
            String  bannerID = getConfig("banner_id");
            bannerAD = new DiandeBanner(FunctionAndroid.context, bannerID);
            adLayer.addView(bannerAD);
            bannerAD.show();
            rootView.addView(adLayer);
        }
        return true;
    }

    public Boolean showInterstitial() {
        String fullID = getConfig("interstitial_id");
        DiandeAdView insert = new DiandeAdView(FunctionAndroid.context, fullID, AdType.INSERTSCREEN);
        insert.setRequestCallBack(new DiandeResultCallback() {

            @Override
            public void onSuccess(boolean result, String message) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailed(String errorMessage) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAdShowSuccess(int code, String message) {
            }
        });
        insert.load();
        insert.show();
        return true;
    }
}
