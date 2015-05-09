package com.wandoujia.mariosdk.plugin.demo.apidemo;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.demo.MarioPluginApplication;
import com.wandoujia.mariosdk.plugin.demo.R;

/**
 * @author liuxv@wandoujia.com
 */
public class BaseDemo {

  private WandouGamesApi wandouGamesApi;
  private Context context;
  private static final long APP_KEY = 100000355;
  private static final String SECURITY_KEY = "8a0cd8df3c7a48b5cd8cafcdc8a70ae0";

  public BaseDemo() {
    wandouGamesApi = MarioPluginApplication.getWandouGamesApi();
  }


  public void apiDemo() {

    /**
     * 异步调用构造，构造需要1S 左右的时间
     */
    new WandouGamesApi.Builder(context, APP_KEY, SECURITY_KEY)
        .setIconBitmap(
            BitmapFactory.decodeResource(context.getResources(), R.drawable.wdj_login_logo))
        .create(new WandouGamesApi.Builder.InitListener() {
          @Override
          public void initComplete(WandouGamesApi wandouGamesApi) {
            BaseDemo.this.wandouGamesApi = wandouGamesApi;
          }

          @Override
          public void initError() {

          }
        });



    /**
     * 同步构造，阻塞方法，需要1S左右的时间，会卡UI
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        wandouGamesApi =
            new WandouGamesApi.Builder(context, APP_KEY, SECURITY_KEY)
                .setIconBitmap(
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.wdj_login_logo))
                .create();
        wandouGamesApi.setLogEnabled(true);
      }
    }).start();



    /**
     * 设置豌豆荚 SDK 是否输出 log
     * <p>
     * <b>注意：请在发布产品前将此值置为false<b/>
     * <p/>
     * 
     * @param enabled 是否输出开发日志
     */
    wandouGamesApi.setLogEnabled(true);

  }


}
