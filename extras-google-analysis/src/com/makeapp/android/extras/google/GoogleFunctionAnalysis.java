package com.makeapp.android.extras.google;

import android.app.Activity;
import android.os.Bundle;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.makeapp.android.extras.FunctionAndroid;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class GoogleFunctionAnalysis
        extends FunctionAndroid<String, Void> {
    Bundle metaData;

    Tracker tracker;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        tracker = GoogleAnalytics.getInstance(FunctionAndroid.context).getTracker(getConfig("tracker_id"));
    }

    @Override
    public Void apply(String s) {

        if (tracker != null) {
            tracker.sendEvent("game", s, "", 0l);
        }

        return null;
    }
}
