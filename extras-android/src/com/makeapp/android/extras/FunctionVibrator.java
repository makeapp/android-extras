package com.makeapp.android.extras;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * Created by yuanyou on 2014/7/8.
 */
public class FunctionVibrator
        extends FunctionAndroid<String, Void> {
    @Override
    public Void apply(String s) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(Long.parseLong(s));
        return null;
    }

}
