package com.makeapp.android.extras;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import org.fun.Function;

/**
 * Created by yuanyou on 2014/7/8.
 */
public class FunctionBrowser
        extends FunctionAndroid<String, Boolean> {
    Activity context;

    public Boolean apply(String s) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(s);
        intent.setData(content_url);
        context.startActivity(intent);
        return true;
    }


}
