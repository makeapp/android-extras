package com.wandoujia.mariosdk.plugin.demo;

import android.app.Application;
import android.content.Context;

import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;

/**
 * @author liuxv@wandoujia.com
 */
public class MarioPluginApplication extends Application {

  private static final long APP_KEY = 100008237;
  private static final String SECURITY_KEY = "10159606448b775c8de9d0e79a4bfff3";

  private static WandouGamesApi wandouGamesApi;

  public static WandouGamesApi getWandouGamesApi() {
    return wandouGamesApi;
  }


  @Override
  protected void attachBaseContext(Context base) {
    WandouGamesApi.initPlugin(base, APP_KEY, SECURITY_KEY);
    super.attachBaseContext(base);
  }


  @Override
  public void onCreate() {
    wandouGamesApi = new WandouGamesApi.Builder(this, APP_KEY, SECURITY_KEY).create();
    wandouGamesApi.setLogEnabled(true);
    super.onCreate();
  }
}
