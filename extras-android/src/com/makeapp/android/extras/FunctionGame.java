package com.makeapp.android.extras;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuanyou on 2014/8/12.
 */
public abstract class FunctionGame extends FunctionAndroidUI<String, String> {

    @Override
    public String applyMain(String s) {
        String params[] = s.split(" ");
        String[] p = new String[params.length - 1];
        System.arraycopy(params, 1, p, 0, p.length);
        return applyMain(params[0], p);
    }

    public String applyMain(String cmd, String[] params) {

        return null;
    }
}
