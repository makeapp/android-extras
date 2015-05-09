package com.wandoujia.mariosdk.plugin.demo.apidemo;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnAchievementFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnAchievementUpdatedListener;
import com.wandoujia.mariosdk.plugin.api.model.model.Achievement;
import com.wandoujia.mariosdk.plugin.api.model.model.result.WandouGamesError;
import com.wandoujia.mariosdk.plugin.demo.MarioPluginApplication;
import com.wandoujia.mariosdk.plugin.demo.R;

/**
 * @author liuxv@wandoujia.com
 */
public class AchievementDemo {

  private WandouGamesApi wandouGamesApi;
  private Context context;

  private static final String GAME_ACHIEVEMENT = "19830125";

  public AchievementDemo(){
    wandouGamesApi = MarioPluginApplication.getWandouGamesApi();
  }

  public void apiDemo() {


    /**
     * 增加带有进度的成就数值，<b>推荐使用此接口<b/>
     * <p>
     * 调用者不需要关注网络状态等其他失败情况，豌豆荚 SDK 会在调用失败后，在网络状态良好时重试，直到成功。
     * <p/>
     * 
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param id 成就ID
     * @param value 进度增加的数值
     */
    wandouGamesApi.increaseAchievement(GAME_ACHIEVEMENT, 100);


    /**
     * 增加带有进度的成就数值
     * <p>
     * 无论成功或者失败，都立即返回，豌豆荚 SDK 将不会重试。
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param id 成就ID
     * @param value 进度增加的数值
     * @param listener 在调用成功或者失败时回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnAchievementUpdatedListener}
     */
    wandouGamesApi.increaseAchievementOneShot(GAME_ACHIEVEMENT, 100,
        new OnAchievementUpdatedListener() {
          @Override
          public void onUpdatedSuccess(String id, boolean isFirstUnlock) {

          }

          @Override
          public void onUpdatedFailed(String id, WandouGamesError error) {

          }
        }
        );


    /**
     * 设置成就状态 {@link com.wandoujia.mariosdk.plugin.api.model.model.AchievementStatus}
     * 从隐藏到可见，<b>推荐使用此接口<b/>
     * <p>
     * 调用者不需要关注网络状态等其他失败情况，豌豆荚 SDK 会在调用失败后，在网络状态良好时重试，直到成功。
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param id 成就ID
     */
    wandouGamesApi.revealAchievement(GAME_ACHIEVEMENT);



    /**
     * 设置成就状态 {@link com.wandoujia.mariosdk.plugin.api.model.model.AchievementStatus} 从隐藏到到可见
     * <p>
     * 无论成功或者失败，都立即返回，豌豆荚 SDK 将不会重试。
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param id 成就ID
     * @param listener 在调用成功或者失败时回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnAchievementUpdatedListener}
     */
    wandouGamesApi.revealAchievementOneShot(GAME_ACHIEVEMENT, new OnAchievementUpdatedListener() {
      @Override
      public void onUpdatedSuccess(String id, boolean isFirstUnlock) {

      }

      @Override
      public void onUpdatedFailed(String id, WandouGamesError error) {

      }
    });



    /**
     * 解锁成就，<b>推荐使用此接口<b/>
     * <p>
     * 调用者不需要关注网络状态等其他失败情况，豌豆荚 SDK 会在调用失败后，在网络状态良好时重试，直到成功。
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param id 成就ID
     */
    wandouGamesApi.unlockAchievement(GAME_ACHIEVEMENT);



    /**
     * 解锁成就
     * <p>
     * 无论成功或者失败，都立即返回，豌豆荚 SDK 将不会重试。
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param id 成就ID
     * @param listener 在调用成功或者失败时回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnAchievementUpdatedListener}
     */
    wandouGamesApi.unlockAchievementOneShot(GAME_ACHIEVEMENT, new OnAchievementUpdatedListener() {
      @Override
      public void onUpdatedSuccess(String id, boolean isFirstUnlock) {

      }

      @Override
      public void onUpdatedFailed(String id, WandouGamesError error) {

      }
    });


    /**
     * 获取成就列表
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @return list of {@link com.wandoujia.mariosdk.plugin.api.model.model.Achievement}
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        List<Achievement> achievementList = wandouGamesApi.getCurrentPlayerAchievements();
      }
    }).start();


    /**
     * 获取成就列表
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param start 起始值，从0开始
     * @param length 获取长度
     * @return list of {@link com.wandoujia.mariosdk.plugin.api.model.model.Achievement}
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        List<Achievement> achievementList = wandouGamesApi.getCurrentPlayerAchievements(0, 10);
      }
    }).start();


    /**
     * 启动 SDK 默认的成就列表界面
     * 
     * @param listener 回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnAchievementFinishedListener}
     */
    wandouGamesApi.startAchievementActivity(new OnAchievementFinishedListener() {
      @Override
      public void onAchievementFinished() {

      }
    });


    /**
     * 启动 SDK 默认的解锁成就通知界面
     * <p>
     * 将在顶部弹出一个View
     * <p/>
     * 
     * @param iconBitmap 图标的Bitmap
     * @param title 通知标题
     * @param description 通知内容
     */
    wandouGamesApi.showAchievementDefualtView(
        BitmapFactory.decodeResource(context.getResources(), R.drawable.wdj_login_logo), "成就标题",
        "成就内容");


  }


}
