//
//  PublicDefine.h
//  WGPlatform
//
//  Created by fly chen on 2/25/13.
//  Copyright (c) 2013 tencent.com. All rights reserved.
//

#ifndef WGPlatform_PublicDefine_h
#define WGPlatform_PublicDefine_h

#define DEPRECATED(_version) __attribute__((deprecated))

typedef enum _ePlatform
{
    ePlatform_None,
    ePlatform_Weixin,          
    ePlatform_QQ
#ifdef ANDROID
	,
    ePlatform_WTLogin,
    ePlatform_QQHall
#endif
}ePlatform;

typedef enum _eFlag
{
    eFlag_Succ              = 0,
    eFlag_QQ_NoAcessToken   = 1000,     //QQ&QZone login fail and can't get accesstoken
    eFlag_QQ_UserCancel     = 1001,     //QQ&QZone user has cancelled login process (tencentDidNotLogin)
    eFlag_QQ_LoginFail      = 1002,     //QQ&QZone login fail (tencentDidNotLogin)
    eFlag_QQ_NetworkErr     = 1003,     //QQ&QZone networkErr
    eFlag_QQ_NotInstall     = 1004,     //QQ is not install
    eFlag_QQ_NotSupportApi  = 1005,     //QQ don't support open api
    eFlag_QQ_AccessTokenExpired = 1006, // QQ Actoken失效, 需要重新登陆
    eFlag_QQ_PayTokenExpired = 1007,    // QQ Pay token过期
    eFlag_QQ_UnRegistered = 1008,    // 没有在qq注册
    eFlag_QQ_MessageTypeErr = 1009,    // QQ消息类型错误
    eFlag_QQ_MessageContentEmpty = 1010,    // QQ消息为空
    eFlag_QQ_MessageContentErr = 1011,     // QQ消息不可用（超长或其他）

    eFlag_WX_NotInstall     = 2000,     //Weixin is not installed
    eFlag_WX_NotSupportApi  = 2001,     //Weixin don't support api
    eFlag_WX_UserCancel     = 2002,     //Weixin user has cancelled
    eFlag_WX_UserDeny       = 2003,     //Weixin User has deny
    eFlag_WX_LoginFail      = 2004,     //Weixin login fail
    eFlag_WX_RefreshTokenSucc = 2005, // Weixin 刷新票据成功
    eFlag_WX_RefreshTokenFail = 2006, // Weixin 刷新票据失败
    eFlag_WX_AccessTokenExpired = 2007, // Weixin AccessToken失效, 此时可以尝试用refreshToken去换票据
    eFlag_WX_RefreshTokenExpired = 2008, // Weixin refresh token 过期, 需要重新授权
    eFlag_Error				= -1,
    eFlag_Local_Invalid = -2, // 本地票据无效, 要游戏现实登陆界面重新授权
	eFlag_LbsNeedOpenLocationService = -4, // 需要引导用户开启定位服务
	eFlag_LbsLocateFail = -5, // 定位失败
    eFlag_UrlTooLong = -6     // for WGOpenUrl
#ifdef ANDROID
    ,
    eFlag_NotInWhiteList = -3 // 不在白名单
#endif
}eFlag;


typedef enum _eShare
{
    eShare_Succ              = 0,
}eShare;

typedef enum _eTokenType
{
    eToken_QQ_Access = 1, // 手Q accessToken
    eToken_QQ_Pay,	// 手Q payToken
    eToken_WX_Access, // 微信accessToken
    eToken_WX_Code, // 微信code, 已弃用
    eToken_WX_Refresh, // 微信refreshToken
}eTokenType;

typedef enum _eWXMessageType
{
    eWXMessageType_Text = 1,
    eWXMessageType_Image,      //图片
    eWXMessageType_Video,      //视频
    eWXMessageType_Link,       //链接
}eWXMessageType;//WX GameCenter 消息类型

typedef enum _eWXButtonType
{
    eWXButtonType_Rank = 1,//跳排行榜
    eWXButtonType_App,     //唤起app
    eWXButtonType_Web,     //跳网页
}eWXButtonType;//button响应类型

typedef enum _ePermission
{
    eOPEN_NONE                              = 0,
    eOPEN_PERMISSION_GET_USER_INFO          = 0x2,
    eOPEN_PERMISSION_GET_SIMPLE_USER_INFO   = 0x4,
    eOPEN_PERMISSION_ADD_ALBUM              = 0x8, 
    eOPEN_PERMISSION_ADD_IDOL               = 0x10,
    eOPEN_PERMISSION_ADD_ONE_BLOG           = 0x20,
    eOPEN_PERMISSION_ADD_PIC_T              = 0x40,
    eOPEN_PERMISSION_ADD_SHARE              = 0x80,
    eOPEN_PERMISSION_ADD_TOPIC              = 0x100,
    eOPEN_PERMISSION_CHECK_PAGE_FANS        = 0x200,
    eOPEN_PERMISSION_DEL_IDOL               = 0x400,
    eOPEN_PERMISSION_DEL_T                  = 0x800,
    eOPEN_PERMISSION_GET_FANSLIST           = 0x1000,
    eOPEN_PERMISSION_GET_IDOLLIST           = 0x2000,
    eOPEN_PERMISSION_GET_INFO               = 0x4000,
    eOPEN_PERMISSION_GET_OTHER_INFO         = 0x8000,
    eOPEN_PERMISSION_GET_REPOST_LIST        = 0x10000,
    eOPEN_PERMISSION_LIST_ALBUM             = 0x20000,
    eOPEN_PERMISSION_UPLOAD_PIC             = 0x40000,
    eOPEN_PERMISSION_GET_VIP_INFO           = 0x80000,
    eOPEN_PERMISSION_GET_VIP_RICH_INFO          = 0x100000,
    eOPEN_PERMISSION_GET_INTIMATE_FRIENDS_WEIBO = 0x200000,
    eOPEN_PERMISSION_MATCH_NICK_TIPS_WEIBO      = 0x400000,
    eOPEN_PERMISSION_GET_APP_FRIENDS            = 0x800000,
    eOPEN_ALL                                   = 0xffffff,
}ePermission;

#ifdef ANDROID
typedef enum _ApiName {
	eApiName_WGSendToQQWithPhoto = 0,
}eApiName;
#endif

typedef enum _eMSG_NOTICETYPE
{
    eMSG_NOTICETYPE_ALERT = 0,
    eMSG_NOTICETYPE_SCROLL = 1,
    eMSG_NOTICETYPE_ALL,
}eMSG_NOTICETYPE;
#endif

typedef enum _eWechatScene
{
    WechatScene_Session  = 0,
    WechatScene_Timeline = 1,
}eWechatScene;
typedef enum _eQQScene
{
    QQScene_QZone  = 1,//默认弹出分享到Qzone的弹框
    QQScene_Session = 2,//手Q中选择好友
}eQQScene;
