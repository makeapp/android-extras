package com.makeapp.android.extras;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yuanyou on 2014/8/12.
 */
public abstract class FunctionPay extends FunctionAndroidUI<String, Boolean> {
    private DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public String getTradeNo() {
        return dateFormat.format(new Date());
    }


}
