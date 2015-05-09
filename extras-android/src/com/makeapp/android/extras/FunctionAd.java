package com.makeapp.android.extras;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yuanyou on 2014/8/8.
 */
public class FunctionAd
        extends FunctionAndroidUI<String, Boolean> {

    public static final int ID = 6000;

    protected ViewGroup rootView;

    @Override
    public Boolean applyMain(String s) {
        String[] params = s.split(" ");
        if ("show".equalsIgnoreCase(params[0])) {
            if ("banner".equalsIgnoreCase(params[1])) {
                return showBanner(params[2], params);
            } else if ("interstitial".equalsIgnoreCase(params[1])) {
                return showInterstitial();
            } else if ("splash".equalsIgnoreCase(params[1])) {
                showSplash();
            } else if ("appwall".equalsIgnoreCase(params[1])) {
                showAppWall();
            }
        } else if ("hidden".equalsIgnoreCase(params[0])) {
            View view = rootView.findViewById(ID);
            if (view != null) {
                rootView.removeView(view);
            }
            return null;
        }

        return false;
    }

    public void showAppWall() {

    }

    @Override
    public void onShow(View rootView) {
        super.onShow(rootView);
        this.rootView = (ViewGroup) rootView;
    }

    public void showSplash() {

    }

    public Boolean showInterstitial() {
        return null;
    }

    public Boolean showBanner(String param, String[] params) {
        return null;
    }
}
