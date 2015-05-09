package com.makeapp.android.extras;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yuanyou on 2014/8/13.
 */
public class FunctionLoading
        extends FunctionAndroidUI<String, Void> {
    View loadingView = null;

    @Override
    public Void applyMain(String s) {
        String[] params = s.split(" ");
        if ("close".equalsIgnoreCase(params[0])) {
            removeLoading();
        }
        return null;
    }

    private void removeLoading() {
        if (loadingView != null) {
            ViewGroup viewGroup = (ViewGroup) loadingView.getParent();
            viewGroup.removeView(loadingView);
            loadingView = null;
        }
    }

    @Override
    public void onShow(View rootView) {
        super.onShow(rootView);
        ViewGroup root = (ViewGroup) rootView;
        String name = getConfig("layout");
        int id = context.getResources().getIdentifier(name, "layout", context.getPackageName());
        if (id > 0) {
            Activity acitivity = (Activity) rootView.getContext();
            loadingView = acitivity.getLayoutInflater().inflate(id, null);
            root.addView(loadingView);
            final Runnable action = new Runnable() {
                @Override
                public void run() {
                    removeLoading();
                }
            };
            new Handler().postDelayed(action, 5000);
        }

    }
}
