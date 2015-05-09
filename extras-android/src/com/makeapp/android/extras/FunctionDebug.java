package com.makeapp.android.extras;

import android.util.Log;
import org.fun.Function;

/**
 * Created by yuanyou on 2014/12/16.
 */
public class FunctionDebug extends FunctionAndroid<String, String> {
    @Override
    public String apply(String s) {
        Log.i("FunctionDebug", s);
        return s;
    }
}
