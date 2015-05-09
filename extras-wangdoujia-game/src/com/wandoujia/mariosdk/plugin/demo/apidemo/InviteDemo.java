package com.wandoujia.mariosdk.plugin.demo.apidemo;

import java.util.List;

import android.content.Context;

import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnInviteFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnInviteSentListener;
import com.wandoujia.mariosdk.plugin.api.model.model.AccountType;
import com.wandoujia.mariosdk.plugin.api.model.model.InvitedStatus;
import com.wandoujia.mariosdk.plugin.api.model.model.Invitee;
import com.wandoujia.mariosdk.plugin.api.model.model.InviteeHistory;
import com.wandoujia.mariosdk.plugin.api.model.model.Inviter;
import com.wandoujia.mariosdk.plugin.api.model.model.InviterHistory;
import com.wandoujia.mariosdk.plugin.api.model.model.WandouPlayer;
import com.wandoujia.mariosdk.plugin.api.model.model.result.InviteResult;
import com.wandoujia.mariosdk.plugin.api.model.model.result.WandouGamesError;
import com.wandoujia.mariosdk.plugin.demo.MarioPluginApplication;

/**
 * @author liuxv@wandoujia.com
 */
public class InviteDemo {

  private WandouGamesApi wandouGamesApi;
  private Context context;

  public InviteDemo(){
    wandouGamesApi = MarioPluginApplication.getWandouGamesApi();
  }

  public void apiDemo() {

    /**
     * 获取当前用户的豌豆荚好友，此好友必须参与了当前游戏。
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * 
     * @param start 起始值，从0开始
     * @param length 获取长度
     * @return list of {@link com.wandoujia.mariosdk.plugin.api.model.model.WandouPlayer}
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        List<WandouPlayer> wandouPlayerList = wandouGamesApi.getFriends(0, 10);
      }
    }).start();

    /**
     * 获取当前用户可邀请的豌豆荚好友
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * 
     * @param start 起始值，从0开始
     * @param length 获取长度
     * @return list of {@link com.wandoujia.mariosdk.plugin.api.model.model.Invitee}
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        List<Invitee> invitees = wandouGamesApi.getAvailablesInvitees(0, 10);
      }
    }).start();



    /**
     * 获取当前用户邀请过其他用户的信息
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * 
     * @param start 起始值，从0开始
     * @param length 获取长度
     * @param invitedStatus 区分被邀请人是否成功加入了游戏
     *          {@link com.wandoujia.mariosdk.plugin.api.model.model.InvitedStatus}
     * 
     * @return 当前用户邀请过谁 {@link com.wandoujia.mariosdk.plugin.api.model.model.Inviter}
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        List<InviteeHistory> inviteeHistories =
            wandouGamesApi.getInviteeHistory(0, 10, InvitedStatus.SUCCESS);
      }
    }).start();


    /**
     * 获取当前用户被邀请信息, 即当前用户响应了谁的邀请
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * 
     * @param start 起始值，从0开始
     * @param length 获取长度
     * @return 当前用户被谁邀请过 {@link com.wandoujia.mariosdk.plugin.api.model.model.Inviter}
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        List<InviterHistory> inviterHistories = wandouGamesApi.getInviterHistory(0, 10);
      }
    }).start();


    /**
     * 邀请豌豆荚好友，将由豌豆荚决定通过豌豆荚推送或短信的方式邀请。
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param invitee 邀请人信息 {@link com.wandoujia.mariosdk.plugin.api.model.model.Invitee}
     * @param listener 回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnInviteSentListener}
     */
    wandouGamesApi.inviteFriend(new Invitee() {
      @Override
      public AccountType getIdtype() {
        return null;
      }

      @Override
      public String getId() {
        return null;
      }

      @Override
      public String getRemark() {
        return null;
      }

      @Override
      public String getAvatar() {
        return null;
      }

      @Override
      public String getWdjNick() {
        return null;
      }
    }, new OnInviteSentListener() {
      @Override
      public void sentSuccess(Invitee invitee) {

      }

      @Override
      public void sentFailed(Invitee invitee, WandouGamesError error) {

      }
    });


    /**
     * 启动 SDK 默认的邀请好友界面
     * 
     * @param listener 回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnInviteFinishedListener}
     */
    wandouGamesApi.startInviteActivity(new OnInviteFinishedListener() {
      @Override
      public void onInviteFinished(InviteResult result) {

      }
    });



  }


}
