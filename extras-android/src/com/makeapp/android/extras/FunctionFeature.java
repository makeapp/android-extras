package com.makeapp.android.extras;

import android.util.Log;

/**
 * Created by yuanyou on 2015/5/9.
 */
public class FunctionFeature
        extends FunctionAndroid<String, String> {
    @Override
    public String apply(String s) {
        String feature = getConfig(s);
        if ("true".equals(feature) || "yes".equalsIgnoreCase(feature)) {
            return "true";
        }
        return "false";
    }
}
