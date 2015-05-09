package com.makeapp.android.extras.tencent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import com.makeapp.android.extras.FunctionGame;
import com.makeapp.android.extras.FunctionPay;
import com.tencent.msdk.WeGame;
import com.tencent.msdk.api.*;
import com.tencent.msdk.consts.CallbackFlag;
import com.tencent.msdk.consts.EPlatform;
import com.tencent.msdk.consts.TokenType;
import com.tencent.msdk.notice.eMSG_NOTICETYPE;
import com.tencent.msdk.remote.api.RelationRet;
import com.tencent.msdk.tools.CommonUtil;
import com.tencent.msdk.tools.Logger;
import com.tencent.msdk.weixin.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yuanyou on 2014/8/16.
 */
public class TencentFunctionGame
        extends FunctionGame {

    // TODO GAME 要加载必要的动态库 START
    static {
        System.loadLibrary("NativeRQD"); // 游戏需要加载此动态库, 数据上报用
    }

    int platform = -1;
    String appIcon = null;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);

        MsdkBaseInfo baseInfo = new MsdkBaseInfo();
        baseInfo.qqAppId = getConfig("qq_app_id");
        baseInfo.qqAppKey = getConfig("qq_app_key");
        baseInfo.wxAppId = getConfig("wx_app_id");
        baseInfo.wxAppKey = getConfig("wx_app_key");
        baseInfo.offerId = getConfig("offerId");
        appIcon = getConfig("app_icon");

        WGPlatform.Initialized(activity, baseInfo);
        // 设置拉起QQ时候需要用户授权的项
        WGPlatform.WGSetPermission(WGQZonePermissions.eOPEN_ALL);

        WGPlatform.WGSetObserver(new MsdkCallback());

        WGPlatform.handleCallback(activity.getIntent()); // 接收平台回调

        if (WGPlatform.wakeUpFromHall(activity.getIntent())) {
            Logger.d("LoginPlatform is Hall");
        } else {
            Logger.d("LoginPlatform is not Hall");
            WGPlatform.handleCallback(activity.getIntent()); // 接收平台回调
        }

        // 模拟游戏自动登录 START
        LoginRet ret = new LoginRet();
        WGPlatform.WGGetLoginRecord(ret);
        if (ret.platform == WeGame.QQPLATID
                || ret.platform == WeGame.WXPLATID) {
//            letUserLogin();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        WGPlatform.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        WGPlatform.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WGPlatform.onDestory(activity);
    }

    public boolean isQQUser() {
        if (platform == WeGame.QQPLATID
                || platform == WeGame.QQHALL) {
            return true;
        }
        return false;
    }

    public boolean isWeiXinUser() {
        if (platform == WeGame.WXPLATID) {
            return true;
        }
        return false;
    }

    @Override
    public String applyMain(String s) {
        String[] params = s.split(" ");
        String cmd = params[0];

        LoginRet ret = new LoginRet();
        platform = WGPlatform.WGGetLoginRecord(ret);

        if ("login".equalsIgnoreCase(cmd)) {
            String type = params[1];
            if ("weixin".equalsIgnoreCase(type)) {
                WGPlatform.WGLogin(EPlatform.ePlatform_Weixin);
            } else {
                WGPlatform.WGLogin(EPlatform.ePlatform_QQ);
            }
        } else if ("notice".equalsIgnoreCase(params[0])) {
            String type = params[1];
            if ("show".equalsIgnoreCase(type)) {
                String sceneID = params[2];
                WGPlatform.WGShowNotice(eMSG_NOTICETYPE.eMSG_NOTICETYPE_SCROLL, sceneID);
            } else if ("hiden".equalsIgnoreCase(type)) {
                WGPlatform.WGHideScrollNotice();
            }
        } else if ("sendSession".equalsIgnoreCase(cmd)
                || "sendTimeline".equalsIgnoreCase(cmd)
                || "sendFavorite".equalsIgnoreCase(cmd)) {
            String title = params[1];
            String summary = params[2];
            String attachType = params[3];
            String attach = params[3];
            if (isQQUser()) {
                WGPlatform.WGSendToQQ(eQQScene.QQScene_Session, title, summary, attach, attach, attach.length());
            } else if (isWeiXinUser()) {
                byte[] imgData = getAppIconData();
                if ("sendTimeline".equalsIgnoreCase(cmd)) {
                    WGPlatform.WGSendToWeixin(title, summary, "MSG_INVITE", imgData, imgData.length, "msgExt");
                } else if ("sendSession".equalsIgnoreCase(cmd)) {
//                    WGPlatform.WGSendToWeixin(eWechatScene.WechatScene_Timeline, title, summary, "", "MSG_INVITE", imgData, imgData.length);
                    WGPlatform.WGSendToWeixinWithPhoto(eWechatScene.WechatScene_Timeline, "MSG_INVITE", imgData, imgData.length, "", "WECHAT_SNS_JUMP_APP");
                }
            }
        } else if ("sendFriend".equalsIgnoreCase(cmd)) {
            String fopenid = params[1];
            String title = params[1];
            String summary = params[2];
            String attachType = params[3];
            String attachUrl = params[3];
            String action = params[3];
            if (isQQUser()) {
            } else if (isWeiXinUser()) {
                BtnBase buttonApp = null;
                MsgBase infoImg = null;
                if ("image".equalsIgnoreCase(attachType)) {
                    infoImg = new MsgImage(attachUrl, 512, 512);
                } else if ("link".equalsIgnoreCase(attachType)) {
                    infoImg = new MsgLink(attachUrl, attachUrl);
                } else if ("video".equalsIgnoreCase(attachType)) {
                    infoImg = new MsgVideo(attachUrl, attachUrl, 100, 100);
                } else {
                    infoImg = new MsgText();
                }
                if ("launchApp".equalsIgnoreCase(action)) {
                    buttonApp = new BtnApp("玩一局", "MessageExt1");
                } else if ("rank".equalsIgnoreCase(action)) {
                    buttonApp = new BtnRank("排行榜", "title", "sendFriend", "sendFriend");
                } else if ("link".equalsIgnoreCase(action)) {
                    buttonApp = new BtnWeb("详情页", "http://www.qq.com");
                }
                WGPlatform.WGSendMessageToWechatGameCenter(fopenid, title, summary, infoImg, buttonApp, "sendFriend");
            }
        } else if ("channel".equalsIgnoreCase(cmd)) {
            return WGPlatform.WGGetChannelId();
        } else if ("user".equalsIgnoreCase(cmd)) {
            if (isQQUser()) {
                WGPlatform.WGQueryWXMyInfo();
            } else if (isWeiXinUser()) {
                WGPlatform.WGQueryQQMyInfo();
            }
        } else if ("friends".equalsIgnoreCase(cmd)) {
            if (isQQUser()) {
                WGPlatform.WGQueryQQGameFriendsInfo();
            } else if (isWeiXinUser()) {
                WGPlatform.WGQueryWXGameFriendsInfo();
            }
        } else if ("nearby".equalsIgnoreCase(cmd)) {
            if (isQQUser()) {
                WGPlatform.WGGetNearbyPersonInfo();
            } else if (isWeiXinUser()) {
                WGPlatform.WGGetNearbyPersonInfo();
            }
        } else if ("ams".equalsIgnoreCase(cmd)) {
            if (isQQUser()) {
                WGPlatform.WGOpenAmsCenter("areaid=111");
            } else if (isWeiXinUser()) {
                WGPlatform.WGOpenAmsCenter("areaid=111");
            }
        } else if ("cleanLocation".equalsIgnoreCase(cmd)) {
            WGPlatform.WGCleanLocation();
        } else if ("feedback".equalsIgnoreCase(cmd)) {
            WGPlatform.WGFeedback(params[1]);
        } else if ("event".equalsIgnoreCase(cmd)) {
            WGPlatform.WGReportEvent(params[1], params[2], true);
        } else if ("pfkey".equalsIgnoreCase(cmd)) {
            return WGPlatform.WGGetPfKey();
        } else if ("token".equalsIgnoreCase(cmd)) {
            WGPlatform.WGRefreshWXToken();
        }
        return "true";
    }

    private byte[] getAppIconData() {
        Bitmap thumb = BitmapFactory.decodeResource(FunctionAndroid.context.getResources(), FunctionAndroid.context.getResources().getIdentifier(appIcon, "drawable", FunctionAndroid.context.getPackageName()));
        return CommonUtil.bitmap2Bytes(thumb);
    }


    class MsdkCallback implements WGPlatformObserver { // 游戏需要根据自己的逻辑实现自己的MsdkCallback对象
        @SuppressWarnings("unused")
        public void OnLoginNotify(LoginRet ret) {
            // game todo
//            toastCallbackInfo(ret.platform, "登录", ret.flag, ret.desc);
            Logger.d("called");
            Logger.d("ret.flag" + ret.flag);
            switch (ret.flag) {
                case CallbackFlag.eFlag_Succ:
//                    stopWaiting();
                    // 登陆成功, 读取各种票据
                    String openId = ret.open_id;
                    String pf = ret.pf;
                    String pfKey = ret.pf_key;
//                    MainActivity.platform = ret.platform;
                    String wxAccessToken = "";
                    long wxAccessTokenExpire = 0;
                    String wxRefreshToken = "";
                    long wxRefreshTokenExpire = 0;
                    for (TokenRet tr : ret.token) {
                        switch (tr.type) {
                            case TokenType.eToken_WX_Access:
                                wxAccessToken = tr.value;
                                wxAccessTokenExpire = tr.expiration;
                                break;
                            case TokenType.eToken_WX_Refresh:
                                wxRefreshToken = tr.value;
                                wxRefreshTokenExpire = tr.expiration;
                                break;
                            default:
                                break;
                        }
                    }
//                    letUserLogin();
                    break;
                case CallbackFlag.eFlag_WX_UserCancel:
                case CallbackFlag.eFlag_WX_NotInstall:
                case CallbackFlag.eFlag_WX_NotSupportApi:
                case CallbackFlag.eFlag_WX_LoginFail:
                    // 登陆失败处理
                    Logger.d(ret.desc);
                    break;
                case CallbackFlag.eFlag_Local_Invalid:
                    // 显示登陆界面
//                    stopWaiting();
                default:
                    break;
            }
        }

        public void OnShareNotify(ShareRet ret) {
            // game todo
//            toastCallbackInfo(ret.platform, "分享", ret.flag, ret.desc);
            Logger.d("called");
            switch (ret.flag) {
                case CallbackFlag.eFlag_Succ:
                    // 分享成功
//                    MainActivity.platform = ret.platform;
                    break;
                case CallbackFlag.eFlag_QQ_UserCancel:
                case CallbackFlag.eFlag_QQ_NetworkErr:
                    // 分享失败处理
                    Logger.d(ret.desc);
                    break;
                case CallbackFlag.eFlag_WX_UserCancel:
                case CallbackFlag.eFlag_WX_NotInstall:
                case CallbackFlag.eFlag_WX_NotSupportApi:
                    // 分享失败处理
                    Logger.d(ret.desc);
                    break;
                default:
                    break;
            }
        }

        public void OnWakeupNotify(WakeupRet ret) {
            // game todo
//            toastCallbackInfo(ret.platform, "拉起", ret.flag, ret.desc);

            Logger.d("OnWakeupNotify called");
            this.logCallbackRet(ret);
//            MainActivity.platform = ret.platform;
            if (ret.flag == CallbackFlag.eFlag_Succ) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        letUserLogin();
//                    }
//                });
            }
        }

        private void logCallbackRet(CallbackRet cr) {
            Logger.d(cr.toString() + ":flag:" + cr.flag);
            Logger.d(cr.toString() + "desc:" + cr.desc);
            Logger.d(cr.toString() + "platform:" + cr.platform);
        }

        @Override
        public void OnRelationNotify(RelationRet relationRet) {
            Logger.d("OnRelationNotify" + relationRet);
        }

        @Override
        public void OnLocationNotify(RelationRet relationRet) {
            Logger.d(relationRet);
        }

        @Override
        public void OnFeedbackNotify(int flag, String desc) {
            Logger.d(String.format(Locale.CHINA, "flag: %d; desc: %s;", flag, desc));
        }

        @Override
        public String OnCrashExtMessageNotify() {
            Logger.d(String.format(Locale.CHINA, "OnCrashExtMessageNotify called"));
            Date nowTime = new Date();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return "Upload extra crashing message on " + time.format(nowTime);
        }
    }
}
