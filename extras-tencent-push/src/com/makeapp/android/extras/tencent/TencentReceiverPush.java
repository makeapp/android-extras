package com.makeapp.android.extras.tencent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.tencent.android.tpush.*;
import org.fun.FunctionFactory;

public class TencentReceiverPush
        extends XGPushBaseReceiver {
    public static final String LogTag = "TPushReceiver";

        // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
        FunctionFactory.callFunction("tencent_push", "onNotifactionShowedResult");
    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = null;
        if (errorCode == SUCCESS) {
            text = "反注册成功";
            FunctionFactory.callFunction("tencent_push", "onUnregisterResult success");
        } else {
            text = "反注册失败" + errorCode;
            FunctionFactory.callFunction("tencent_push", "onUnregisterResult error");
        }
        Log.d(LogTag, text);
    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = null;
        if (errorCode == SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = null;
        if (errorCode == SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult message) {
        if (context == null || message == null) {
            return;
        }
        String text = null;
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            text = "通知被打开 :" + message;
        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :" + message;
        }
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        FunctionFactory.callFunction("tencent_push", "onNotifactionClickedResult " + customContent);
        // APP自主处理的过程。。。
        Log.d(LogTag, text);
    }

    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        String text = null;
        if (errorCode == SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败，错误码：" + errorCode;
        }
        Log.d(LogTag, text);
    }

    // 消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        String text = "收到消息:" + message.toString();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        FunctionFactory.callFunction("tencent_push", "onTextMessage " + customContent);
        // APP自主处理消息的过程...
        Log.d(LogTag, text);
    }
}
