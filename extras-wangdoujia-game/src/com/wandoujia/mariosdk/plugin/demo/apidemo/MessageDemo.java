package com.wandoujia.mariosdk.plugin.demo.apidemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnMessageReceivedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnMessageSentListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnMessageStatusUpdatedListener;
import com.wandoujia.mariosdk.plugin.api.model.model.MessageEntity;
import com.wandoujia.mariosdk.plugin.api.model.model.MessageList;
import com.wandoujia.mariosdk.plugin.api.model.model.MessageStatus;
import com.wandoujia.mariosdk.plugin.api.model.model.MessageStatusModel;
import com.wandoujia.mariosdk.plugin.api.model.model.WandouMessage;
import com.wandoujia.mariosdk.plugin.api.model.model.result.WandouGamesError;
import com.wandoujia.mariosdk.plugin.demo.MarioPluginApplication;

/**
 * @author liuxv@wandoujia.com
 */
public class MessageDemo {

  private WandouGamesApi wandouGamesApi;
  private Context context;

  private static final long TO_UID = 11315753L;

  public MessageDemo(){
    wandouGamesApi = MarioPluginApplication.getWandouGamesApi();
  }


  public void apiDemo() {

    /**
     * 发送消息给指定的用户
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param message 信息 {@link com.wandoujia.mariosdk.plugin.api.model.model.WandouMessage}
     * @param listener 回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnMessageSentListener}
     */
    wandouGamesApi.sendMessage(new WandouMessage.Builder(""+TO_UID, "hello world",
        (long) 31 * 24 * 3600).create(), new OnMessageSentListener() {
      @Override
      public void onMessageSentSuccess(WandouMessage msg) {

      }

      @Override
      public void onMessageSentFailed(WandouMessage msg, WandouGamesError error) {

      }
    });


    /**
     * 获取当前用户的消息列表
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * 
     * @param start 起始值，从0开始
     * @param length 获取长度
     * @param type 消息状态类型 {@link com.wandoujia.mariosdk.plugin.api.model.model.MessageStatus}
     * @return {@link com.wandoujia.mariosdk.plugin.api.model.model.MessageList}
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        MessageList messageList = wandouGamesApi.getMessageList(0, 10, MessageStatus.UNREAD);
      }
    }).start();


    /**
     * 设置消息状态 {@link com.wandoujia.mariosdk.plugin.api.model.model.MessageStatus}
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param messageStatus 改变消息状态类型
     *          {@link com.wandoujia.mariosdk.plugin.api.model.model.MessageStatusModel}
     * @param listener 回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnMessageStatusUpdatedListener}
     * @return 是否发送成功，如果没有登录，则始终返回false
     */
    wandouGamesApi.setMessageStatus(
        new ArrayList<MessageStatusModel>(),
        new OnMessageStatusUpdatedListener() {
          @Override
          public void onMessageStatusUpdatedSuccess(List<MessageStatusModel> messageStatus) {

          }

          @Override
          public void onMessageStatusUpdatedFailed(List<MessageStatusModel> messageStatus,
              WandouGamesError error) {

          }
        });


    /**
     * 注册消息接收 {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnMessageReceivedListener}
     * ，当用户接到消息时回调
     * 
     * @param listener
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnMessageReceivedListener}
     */
    wandouGamesApi.registerMessageListener(new OnMessageReceivedListener() {
      @Override
      public void onMessageReceived(MessageEntity entity) {

      }
    });

    /**
     * 反注册消息接收 {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnMessageReceivedListener}
     * 
     * @param listener
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnMessageReceivedListener}
     */
    wandouGamesApi.unregisterMessageListener(new OnMessageReceivedListener() {
      @Override
      public void onMessageReceived(MessageEntity entity) {

      }
    });

  }


}
