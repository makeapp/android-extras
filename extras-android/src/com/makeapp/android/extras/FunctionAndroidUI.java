package com.makeapp.android.extras;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by yuanyou on 2014/7/8.
 */
public abstract class FunctionAndroidUI<T, V>
        extends FunctionAndroid<T, V> {

    boolean waitResult = false;

    @Override
    public V apply(final T s) {

        MainRunner runnable = new MainRunner();
        runnable.param = s;
//        context.runOnUiThread(runnable);
        new Handler(Looper.getMainLooper()).post(runnable);
        if (waitResult) {
            synchronized (runnable) {
                try {
                    runnable.wait(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return runnable.result;
        }
        return runnable.result;
    }

    public abstract V applyMain(T s);

    class MainRunner implements Runnable {
        V result;
        T param;

        @Override
        public void run() {
            result = applyMain(param);
            if (waitResult) {
                synchronized (this) {
                    this.notifyAll();
                }
            }
        }
    }
}
