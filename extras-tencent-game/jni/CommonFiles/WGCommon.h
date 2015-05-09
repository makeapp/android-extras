//
//  Common.h
//  WGPlatform
//
//  Created by fly chen on 2/21/13.
//  Copyright (c) 2013 tencent.com. All rights reserved.
//

#ifndef WGPlatform_Common_h
#define WGPlatform_Common_h
#include <string>
#include <vector>
#include "WGPublicDefine.h"

#ifdef ANDROID
#include <android/log.h>
#include <jni.h>

#define LOGI(fmt, ...)   __android_log_print(ANDROID_LOG_INFO, "WeGame  cpp", fmt, __VA_ARGS__)
#define LOGD(fmt, ...)   __android_log_print(ANDROID_LOG_DEBUG, "WeGame  cpp", fmt, __VA_ARGS__)
#define LOGW(fmt, ...)   __android_log_print(ANDROID_LOG_WARN, "WeGame  cpp", fmt, __VA_ARGS__)
#define LOGE(fmt, ...)   __android_log_print(ANDROID_LOG_ERROR, "WeGame  cpp", fmt, __VA_ARGS__)
#endif


typedef struct {
    int type;
    std::string value;
    long long expiration;
}TokenRet;

typedef struct loginRet_ {
    int flag;               //返回标记，标识成功和失败类型
    std::string desc;       //返回描述
    int platform;           //当前登录的平台
    std::string open_id;
    std::vector<TokenRet> token;
    std::string user_id;    //用户ID，先保留，等待和微信协商
    std::string pf;
    std::string pf_key;
#ifdef __APPLE__
    loginRet_ ():flag(-1),platform(0){};
#endif
}LoginRet;

typedef struct
{
    std::string key;
    std::string value;
    
}KVPair;

typedef struct
{
    int flag;                //错误码
    int platform;               //被什么平台唤起
    std::string media_tag_name; //wx回传得meidaTagName
    std::string open_id;        //qq传递的openid
    std::string desc;           //描述
    std::string lang;          //语言     目前只有微信5.1以上用，手Q不用
    std::string country;       //国家     目前只有微信5.1以上用，手Q不用
    std::string messageExt;    //游戏分享传入自定义字符串，平台拉起游戏不做任何处理返回         目前只有微信5.1以上用，手Q不用
    std::vector<KVPair> extInfo;  //游戏－平台携带的自定义参数手Q专用
}WakeupRet;

typedef struct
{
    int platform;           //平台类型
    int flag;            //操作结果
    std::string desc;       //结果描述（保留）
    std::string extInfo;   //游戏分享是传入的自定义字符串，用来标示分享
}ShareRet;

typedef struct
{
    std::string name;           //地点名称
    std::string addr;           //具体地址
    int distance;               //离此次定位地点的距离
}AddressInfo;

typedef struct {
    std::string nickName;         //昵称
    std::string openId;           //帐号唯一标示
    std::string gender;           //性别
    std::string pictureSmall;     //小头像
    std::string pictureMiddle;    //中头像
    std::string pictureLarge;     //datouxiang
    std::string provice;          //省份(老版本属性，为了不让外部app改代码，没有放在AddressInfo)
    std::string city;             //城市(老版本属性，为了不让外部app改代码，没有放在AddressInfo)
    bool        isFriend;         //是否好友
    int         distance;         //离此次定位地点的距离
    std::string lang;             //语言
    std::string country;          //国家
}PersonInfo;

typedef struct {
    int flag;     //查询结果flag，0为成功
    std::string desc;    // 描述
    std::vector<PersonInfo> persons;//保存好友或个人信息
    std::string extInfo; //游戏查询是传入的自定义字段，用来标示一次查询
}RelationRet;

typedef struct
{
    std::string msg_id;			//公告id
    std::string open_id;		//用户open_id
    std::string msg_content;	//公告内容
    std::string msg_title;		//公告标题
	std::string msg_url;		//公告跳转链接
	eMSG_NOTICETYPE msg_type;	//公告类型，eMSG_NOTICETYPE
	std::string msg_scene;		//公告展示的场景，管理端后台配置
	std::string start_time;		//公告有效期开始时间
	std::string end_time;		//公告有效期结束时间
}NoticeInfo;

#ifdef __APPLE__
typedef struct
{
    std::string openId;         //用户帐号id（account），例如openid、uin
    std::string openKey;        //用户session（skey具体值）
    std::string session_id;     //用户账户类型(uin还是openid)
    std::string session_type;   //session类型(skey)
    std::string payItem;        //结果描述（保留）
    std::string productId;      //物品id
    std::string pf;             //平台来源
    std::string pfKey;          //跳转到应用首页后，URL后会带该参数。由平台直接传给应用，应用原样传给平台
    bool isDepositGameCoin;     //是否是托管游戏币
    int productType;            //物品类型(0 单笔 ,游戏币 2 包月＋自动续费 3 包月＋非自动续费)
    int quantity;               //购买产品的数量
    std::string zoneId;         //游戏币字段
    std::string varItem;        //业务的扩展字段
    int changeKeyType;          //发货失败区分补发货失败和发货失败  since 1.3.5
    std::string billno;
}IAPPayRequestInfo;


typedef struct
{
    std::string rate;        //平台类型
    std::vector<std::string> mplist;            //操作结果
    std::vector<std::string> mpValueList;
    std::vector<std::string> mpPresentList;
    std::string first_present_count;        //结果描述（保留）
    std::string beginTime;
    std::string endTime;
}IAPMpInfo;

typedef void(*CallbackFun)(LoginRet lr);

#define AUTH_FILE    "WeGameSDKAuth.dat"
#endif

#ifdef ANDROID
typedef struct {
    std::string ip;
    int port;
} SchedulingInfo;
// 获取某个java对象的值(String), 再赋值给本地对象
#define JniGetAndSetStringField(fieldName, fieldNameStr, jOriginClass, jOriginObj, targetObj) \
jfieldID j##fieldName##FieldId = env->GetFieldID(jOriginClass, fieldNameStr, "Ljava/lang/String;"); \
jstring j##fieldName##FieldValue = (jstring) (env->GetObjectField(jOriginObj, j##fieldName##FieldId)); \
if (j##fieldName##FieldValue == NULL) {\
    targetObj.fieldName = ""; \
} else { \
    char const * c##fieldName##FieldValue = env->GetStringUTFChars(j##fieldName##FieldValue, NULL); \
    targetObj.fieldName = c##fieldName##FieldValue; \
    LOGD("c%sFieldValue %s", fieldNameStr, c##fieldName##FieldValue); \
    env->ReleaseStringUTFChars(j##fieldName##FieldValue, c##fieldName##FieldValue); \
} \
env->DeleteLocalRef(j##fieldName##FieldValue);

#define JniGetAndSetIntField(fieldName, fieldNameStr, jOriginClass, jOriginObj, targetObj) \
jfieldID j##fieldName##FieldId = env->GetFieldID(jOriginClass, fieldNameStr, "I"); \
targetObj.fieldName = (int) (env->GetIntField(jOriginObj, j##fieldName##FieldId));

// 获取某个java对象的值(long), 再赋值给本地对象
#define JniGetAndSetLongField(fieldName, fieldNameStr, jOriginClass, jOriginObj, targetObj) \
jfieldID j##fieldName##FieldId = env->GetFieldID(jOriginClass, fieldNameStr, "J"); \
targetObj.fieldName = (int) (env->GetLongField(jOriginObj, j##fieldName##FieldId));

// 获取某个java对象的值(float), 再赋值给本地对象
#define JniGetAndSetFloatField(fieldName, fieldNameStr, jOriginClass, jOriginObj, targetObj) \
jfieldID j##fieldName##FieldId = env->GetFieldID(jOriginClass, fieldNameStr, "F"); \
targetObj.fieldName = (int) (env->GetFloatField(jOriginObj, j##fieldName##FieldId));

// 获取某个java对象的值(boolean), 再赋值给本地对象
#define JniGetAndSetBooleanField(fieldName, fieldNameStr, jOriginClass, jOriginObj, targetObj) \
jfieldID j##fieldName##FieldId = env->GetFieldID(jOriginClass, fieldNameStr, "Z"); \
targetObj.fieldName = (int) (env->GetBooleanField(jOriginObj, j##fieldName##FieldId));
#endif

class WXMessageButton
{
public:
#ifdef __APPLE__
    virtual std::string parserToJsonString() = 0;
#endif
#ifdef ANDROID
    virtual jobject getJavaObject() = 0;
#endif
    WXMessageButton(std::string aName);
    virtual ~WXMessageButton();
protected:
//    eWXButtonType type; // 按钮类型
    std::string name; // 按钮名称
};

class ButtonApp : public WXMessageButton
{
public:
    ButtonApp(std::string aName, std::string aMessageExt);
#ifdef __APPLE__
    std::string parserToJsonString();
#endif
#ifdef ANDROID
    virtual jobject getJavaObject();
#endif
    ~ButtonApp();
protected:
    std::string messageExt; // 附加自定义信息，通过按钮拉起应用时会带回游戏
};

class ButtonWebview : public WXMessageButton
{
public:
    ButtonWebview(std::string aName, std::string aWebViewUrl);
#ifdef __APPLE__
    std::string parserToJsonString();
#endif
#ifdef ANDROID
    virtual jobject getJavaObject();
#endif
    ~ButtonWebview();
protected:
    std::string webViewUrl; // 点击按钮后要跳转的页面
};

class ButtonRankView : public WXMessageButton
{
public:
#ifdef __APPLE__
    std::string parserToJsonString();
#endif
#ifdef ANDROID
    virtual jobject getJavaObject();
#endif
    ButtonRankView(std::string aName, std::string aTitle, std::string aButtonName, std::string aMessageExt);
    ~ButtonRankView();
protected:
    std::string title; // 排行榜名称
    std::string rankViewButtonName; // 排行榜中按钮的名称
    std::string messageExt; // 附加自定义信息，通过排行榜中按钮拉起应用时会带回游戏
};

class WXMessageTypeInfo
{
public:
    WXMessageTypeInfo(std::string aPictureUrl);
#ifdef __APPLE__
    virtual std::string parserToJsonString(eWXMessageType &type) = 0;
#endif
#ifdef ANDROID
    virtual jobject getJavaObject() = 0;
#endif
    virtual ~WXMessageTypeInfo();
protected:
    std::string pictureUrl; // 在消息中心的消息图标Url（图片消息中，此链接则为图片URL）
};

class TypeInfoImage : public WXMessageTypeInfo
{
public:
    TypeInfoImage(std::string aPictureUrl, int aHeight, int aWidth);
#ifdef __APPLE__
    virtual std::string parserToJsonString(eWXMessageType &type);
#endif
#ifdef ANDROID
    virtual jobject getJavaObject();
#endif
    virtual ~TypeInfoImage();
protected:
    int height; // 图片高度
    int width; // 图片宽度
};

class TypeInfoVideo : public TypeInfoImage
{
public:
#ifdef __APPLE__
    std::string parserToJsonString(eWXMessageType &type);
#endif
#ifdef ANDROID
    virtual jobject getJavaObject();
#endif
    TypeInfoVideo(std::string aPictureUrl, int aHeight, int aWidth, std::string aMediaUrl);
    ~TypeInfoVideo();
protected:
    std::string mediaUrl; // 相比图片消息，链接消息多此mediaUrl表示视频URL
};

class TypeInfoLink : public WXMessageTypeInfo
{
public:
#ifdef __APPLE__
    std::string parserToJsonString(eWXMessageType &type);
#endif
#ifdef ANDROID
    virtual jobject getJavaObject();
#endif
    TypeInfoLink(std::string aPictureUrl, std::string aTargetUrl);
protected:
    std::string targetUrl; // 链接消息的目标URL，点击消息拉起此链接
};

class TypeInfoText : public WXMessageTypeInfo
{
public:
#ifdef __APPLE__
    std::string parserToJsonString(eWXMessageType &type);
#endif
#ifdef ANDROID
    virtual jobject getJavaObject();
#endif
    TypeInfoText();
};
#endif
