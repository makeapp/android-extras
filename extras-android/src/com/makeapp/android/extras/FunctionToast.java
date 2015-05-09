package com.makeapp.android.extras;

import android.widget.Toast;

/**
 * Created by yuanyou on 2015/1/2.
 */
public class FunctionToast
        extends FunctionAndroidUI<String, String> {

    @Override
    public String applyMain(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        return "";
    }
}
