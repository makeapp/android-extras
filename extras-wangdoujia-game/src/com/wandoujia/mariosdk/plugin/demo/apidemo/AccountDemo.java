package com.wandoujia.mariosdk.plugin.demo.apidemo;

import android.content.Context;

import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnCheckLoginCompletedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnLoginFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnLogoutFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnUserInfoSettingFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.model.LoginFinishType;
import com.wandoujia.mariosdk.plugin.api.model.model.LogoutFinishType;
import com.wandoujia.mariosdk.plugin.api.model.model.UnverifiedPlayer;
import com.wandoujia.mariosdk.plugin.api.model.model.WandouPlayer;
import com.wandoujia.mariosdk.plugin.api.model.model.result.UserInfoSettingResult;
import com.wandoujia.mariosdk.plugin.demo.MarioPluginApplication;

/**
 * @author liuxv@wandoujia.com
 */
public class AccountDemo {

  private WandouGamesApi wandouGamesApi;
  private Context context;

  public AccountDemo(){
    wandouGamesApi = MarioPluginApplication.getWandouGamesApi();
  }


  public void apiDemo() {

    /**
     * 检查豌豆荚账户是否登录
     * 
     * 第一次判断时会阻塞UI ，第一次判断建议使用带回调的isLogin
     * 
     * @return true / false
     */
    boolean wdjIsLogin = wandouGamesApi.isLoginned();


    /**
     * 检查豌豆荚账户是否登录
     * 
     * @param listener {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnCheckLoginCompletedListener}
     */
    wandouGamesApi.isLoginned(new OnCheckLoginCompletedListener() {
      @Override
      public void onCheckCompleted(boolean isLogin) {
        boolean wdjIsLogin = isLogin;
      }
    });

    /**
     * 登录豌豆荚，如果豌豆荚客户端没有账号登录则调用豌豆荚登录/注册界面
     * <p>
     * 如果豌豆荚客户端中已有账号，将会进行自动登录，触发
     * {@link com.wandoujia.mariosdk.plugin.api.model.callback.WandouAccountListener}
     * ，并把豌豆荚中的用户信息写入SDK。
     * <p/>
     * 
     */
    wandouGamesApi.login();


    /**
     * 登录豌豆荚，如果豌豆荚客户端没有账号登录则调用豌豆荚登录/注册界面
     * <p>
     * 如果豌豆荚客户端中已有账号，将会进行自动登录，触发
     * {@link com.wandoujia.mariosdk.plugin.api.model.callback.WandouAccountListener}
     * 并把豌豆荚中的用户信息写入SDK。
     * <p/>
     * 
     * @param listener 当界面结束时回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnLoginFinishedListener}
     */
    wandouGamesApi.login(new OnLoginFinishedListener() {
      @Override
      public void onLoginFinished(LoginFinishType loginFinishType, UnverifiedPlayer unverifiedPlayer) {
        String token = unverifiedPlayer.getToken();
      }
    });



    /**
     * 登出豌豆荚。
     * 
     * @param listener 当登出操作结束后回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnLogoutFinishedListener}
     */
    wandouGamesApi.logout(new OnLogoutFinishedListener() {
      @Override
      public void onLoginFinished(LogoutFinishType logoutFinishType) {

      }
    });



    /**
     * 获取当前用户的Token 字符串
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * 
     * @param duration 有效时间，秒为单位
     * @return Token 字符串
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        String token = wandouGamesApi.getToken((long) 3600 * 24 * 30);
      }
    }).start();


    /**
     * 获取当前用户信息
     * <p>
     * 如ID，昵称，头像
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @return 当前用户信息 {@link com.wandoujia.mariosdk.plugin.api.model.model.WandouPlayer}
     */
    WandouPlayer wandouPlayer = wandouGamesApi.getCurrentPlayerInfo();



    /**
     * 启动 SDK 默认的修改用户信息界面
     * <p>
     * 当现游戏中的豌豆荚账户与豌豆荚客户端得豌豆荚账户不同时，将不能启动。
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     *
     * @param listener 回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnUserInfoSettingFinishedListener}
     */
    wandouGamesApi.startUserInfoSettingActivity(new OnUserInfoSettingFinishedListener() {
      @Override
      public void onUserInfoSettingFinishedListener(UserInfoSettingResult result) {

      }
    });


  }


}
