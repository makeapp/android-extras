/*!
 @header WGPlatform.h
 @abstract 向外界开放的接口
 @author haywoodfu
 @version 1.00 2013/11/20 Creation
 */
#ifndef __WGPLATFORM_H__
#define __WGPLATFORM_H__

#include <string>
#include "WGCommon.h"
#if defined ANDROID
#include <jni.h>
#endif

class WGPlatformObserver;

/**  
 * WeGame接口函数
 *
 * 该类封装了WeGame的外部接口
 */
class WGPlatform {
#ifdef ANDROID
    friend JNIEXPORT void JNICALL ::Java_com_tencent_msdk_api_WGPlatformObserverForSO_OnWakeupNotify(JNIEnv *, jclass,
                                                                                                     jobject);
    friend JNIEXPORT void JNICALL ::Java_com_tencent_msdk_api_WGPlatformObserverForSO_OnLoginNotify(JNIEnv *, jclass,
                                                                                                    jobject);
#endif
private:
#ifdef ANDROID
	JavaVM* m_pVM;
	WakeupRet m_lastWakeup;
	LoginRet m_lastLoginRet;
	bool needDelayWakeupNotify;
	bool needDelayLoginNotify;
#endif
	static WGPlatform * m_pInst;
    unsigned int m_nPermissions;
    WGPlatformObserver* m_pObserver;
    
    WGPlatform();
    virtual ~WGPlatform();
public:
#ifdef __APPLE__
    ePlatform m_currentPlatformType;
#endif
#ifdef ANDROID
    void init(JavaVM* pVM);
    void setVM(JavaVM* pVM);
    JavaVM* getVm();
#endif
    
    static WGPlatform* GetInstance();
    std::string registerChannelId;
    
	WGPlatformObserver* GetObserver() const;
    
	/**
	 *   OnLoginNotify: 登陆回调
	 *   OnShareNotify: 分享回调
	 *   OnWakeupNotify: 被唤起回调
	 *   OnRelationNotify: 关系链查询回调
	 * @param pObserver 游戏传入的全局回调对象
	 */
	void WGSetObserver(WGPlatformObserver* pObserver);

	/**
	 * @param loginRet 返回的记录
	 * @return 返回值为平台id, 类型为ePlatform, 返回ePlatform_None表示没有登陆记录
	 *   loginRet.platform(类型为ePlatform)表示平台id, 可能值为ePlatform_QQ, ePlatform_Weixin, ePlatform_None.
	 *   loginRet.flag(类型为eFlag)表示当前本地票据的状态, 可能值及说明如下:
	 *     eFlag_Succ: 授权票据有效
	 *     eFlag_QQ_AccessTokenExpired: 手Q accessToken已经过期, 显示授权界面, 引导用户重新授权
	 *     eFlag_WX_AccessTokenExpired: 微信accessToken票据过期，需要调用WGRefreshWXToken刷新
	 *     eFlag_WX_RefreshTokenExpired: 微信refreshToken, 显示授权界面, 引导用户重新授权
	 *   ret.token是一个Vector<TokenRet>, 其中存放的TokenRet有type和value, 通过遍历Vector判断type来读取需要的票据. type类型定义如下:
	 *   	eToken_QQ_Access = 1,
			eToken_QQ_Pay,
        	eToken_WX_Access,
    		eToken_WX_Refresh,
	 */
	int WGGetLoginRecord(LoginRet& loginRet);

	/**
	 * @param platform 游戏传入的平台类型, 可能值为: ePlatform_QQ, ePlatform_Weixin
	 * @return void
	 *   通过游戏设置的全局回调的OnLoginNotify(LoginRet& loginRet)方法返回数据给游戏
	 *     loginRet.platform表示当前的授权平台, 值类型为ePlatform, 可能值为ePlatform_QQ, ePlatform_Weixin
	 *     loginRet.flag值表示返回状态, 可能值(eFlag枚举)如下：
	 *       eFlag_Succ: 返回成功, 游戏接收到此flag以后直接读取LoginRet结构体中的票据进行游戏授权流程.
	 *       eFlag_QQ_NoAcessToken: 手Q授权失败, 游戏接收到此flag以后引导用户去重新授权(重试)即可.
	 *       eFlag_QQ_UserCancel: 用户在授权过程中
	 *       eFlag_QQ_LoginFail: 手Q授权失败, 游戏接收到此flag以后引导用户去重新授权(重试)即可.
	 *       eFlag_QQ_NetworkErr: 手Q授权过程中出现网络错误, 游戏接收到此flag以后引导用户去重新授权(重试)即可.
	 *     loginRet.token是一个Vector<TokenRet>, 其中存放的TokenRet有type和value, 通过遍历Vector判断type来读取需要的票据. type(TokenType)类型定义如下:
	 *       eToken_QQ_Access,
	 *       eToken_QQ_Pay,
	 *       eToken_WX_Access,
	 *       eToken_WX_Refresh
	 */
	void WGLogin(ePlatform platform);

	/**
	 * @return bool 返回值已弃用, 全都返回true
	 */
	bool WGLogout();

	/**
	 * @param permissions ePermission枚举值 或 运算的结果, 表示需要的授权项目
	 * @return void
	 */
	void WGSetPermission(int permissions);

	/**
	 * @param scene 指定分享到朋友圈, 或者微信会话, 可能值和作用如下:
	 *   WechatScene_Session: 分享到微信会话
	 *   WechatScene_Timeline: 分享到微信朋友圈 (此种消息已经限制不能分享到朋友圈)
	 * @param title 结构化消息的标题
	 * @param desc 结构化消息的概要信息
	 * @param url 结构化消息的目标URL, 此URL已经弃用
	 * @param mediaTagName 请根据实际情况填入下列值的一个, 此值会传到微信供统计用, 在分享返回时也会带回此值, 可以用于区分分享来源
		 "MSG_INVITE";                   // 邀请
		 "MSG_SHARE_MOMENT_HIGH_SCORE";    //分享本周最高到朋友圈
		 "MSG_SHARE_MOMENT_BEST_SCORE";    //分享历史最高到朋友圈
		 "MSG_SHARE_MOMENT_CROWN";         //分享金冠到朋友圈
		 "MSG_SHARE_FRIEND_HIGH_SCORE";     //分享本周最高给好友
		 "MSG_SHARE_FRIEND_BEST_SCORE";     //分享历史最高给好友
		 "MSG_SHARE_FRIEND_CROWN";          //分享金冠给好友
		 "MSG_friend_exceed"         // 超越炫耀
		 "MSG_heart_send"            // 送心
	 * @param thumbImgData 结构化消息的缩略图
	 * @param thumbImgDataLen 结构化消息的缩略图数据长度
	 * @deprecated 此接口不能支持微信回传游戏分享时候带出去的自定义数据, 其他功能完全可以通过 WGSendToWeixin(unsigned char* title, unsigned char* desc, unsigned char* mediaTagName, unsigned char* thumbImgData, const int& thumbImgDataLen, unsigned char* messageExt) 接口完成, 建议使用新接口
	 * @return void
	 *   通过游戏设置的全局回调的OnShareNotify(ShareRet& shareRet)回调返回数据给游戏, shareRet.flag值表示返回状态, 可能值及说明如下:
	 *     eFlag_Succ: 分享成功
	 *     eFlag_Error: 分享失败
	 */
	void WGSendToWeixin(
		const eWechatScene& scene,
		unsigned char* title,
		unsigned char* desc, 
		unsigned char* url,
		unsigned char* mediaTagName, 
		unsigned char* thumbImgData,
		const int& thumbImgDataLen
	);

	/**
	 * @param title 结构化消息的标题
	 * @param desc 结构化消息的概要信息
	 * @param mediaTagName 请根据实际情况填入下列值的一个, 此值会传到微信供统计用, 在分享返回时也会带回此值, 可以用于区分分享来源
		 "MSG_INVITE";                   // 邀请
		 "MSG_SHARE_MOMENT_HIGH_SCORE";    //分享本周最高到朋友圈
		 "MSG_SHARE_MOMENT_BEST_SCORE";    //分享历史最高到朋友圈
		 "MSG_SHARE_MOMENT_CROWN";         //分享金冠到朋友圈
		 "MSG_SHARE_FRIEND_HIGH_SCORE";     //分享本周最高给好友
		 "MSG_SHARE_FRIEND_BEST_SCORE";     //分享历史最高给好友
		 "MSG_SHARE_FRIEND_CROWN";          //分享金冠给好友
		 "MSG_friend_exceed"         // 超越炫耀
		 "MSG_heart_send"            // 送心
	 * @param thumbImgData 结构化消息的缩略图
	 * @param thumbImgDataLen 结构化消息的缩略图数据长度
	 * @param messageExt 游戏分享是传入字符串，通过此消息拉起游戏会通过 OnWakeUpNotify(WakeupRet ret)中ret.messageExt回传给游戏
	 * @return void
	 *   通过游戏设置的全局回调的OnShareNotify(ShareRet& shareRet)回调返回数据给游戏, shareRet.flag值表示返回状态, 可能值及说明如下:
	 *     eFlag_Succ: 分享成功
	 *     eFlag_Error: 分享失败
	 */
	void WGSendToWeixin(
		unsigned char* title,
		unsigned char* desc,
		unsigned char* mediaTagName,
		unsigned char* thumbImgData,
		const int& thumbImgDataLen,
		unsigned char* messageExt
	);
	
	/**
	 * @param scene 指定分享到朋友圈, 或者微信会话, 可能值和作用如下:
	 *   WechatScene_Session: 分享到微信会话
	 *   WechatScene_Timeline: 分享到微信朋友圈
	 * @param mediaTagName 请根据实际情况填入下列值的一个, 此值会传到微信供统计用, 在分享返回时也会带回此值, 可以用于区分分享来源
		 "MSG_INVITE";                   // 邀请
		 "MSG_SHARE_MOMENT_HIGH_SCORE";    //分享本周最高到朋友圈
		 "MSG_SHARE_MOMENT_BEST_SCORE";    //分享历史最高到朋友圈
		 "MSG_SHARE_MOMENT_CROWN";         //分享金冠到朋友圈
		 "MSG_SHARE_FRIEND_HIGH_SCORE";     //分享本周最高给好友
		 "MSG_SHARE_FRIEND_BEST_SCORE";     //分享历史最高给好友
		 "MSG_SHARE_FRIEND_CROWN";          //分享金冠给好友
		 "MSG_friend_exceed"         // 超越炫耀
		 "MSG_heart_send"            // 送心
	 * @param imgData 原图文件数据
	 * @param imgDataLen 原图文件数据长度(图片大小不能超过10M)
	 * @return void
	 *   通过游戏设置的全局回调的OnShareNotify(ShareRet& shareRet)回调返回数据给游戏, shareRet.flag值表示返回状态, 可能值及说明如下:
	 *     eFlag_Succ: 分享成功
	 *     eFlag_Error: 分享失败
	 */
	void WGSendToWeixinWithPhoto(
		const eWechatScene& scene,
		unsigned char* mediaTagName,
		unsigned char* imgData, 
		const int& imgDataLen
	);

	/**
	 * @param scene 指定分享到朋友圈, 或者微信会话, 可能值和作用如下:
	 *   WechatScene_Session: 分享到微信会话
	 *   WechatScene_Timeline: 分享到微信朋友圈
	 * @param mediaTagName 请根据实际情况填入下列值的一个, 此值会传到微信供统计用, 在分享返回时也会带回此值, 可以用于区分分享来源
		 "MSG_INVITE";                   // 邀请
		 "MSG_SHARE_MOMENT_HIGH_SCORE";    //分享本周最高到朋友圈
		 "MSG_SHARE_MOMENT_BEST_SCORE";    //分享历史最高到朋友圈
		 "MSG_SHARE_MOMENT_CROWN";         //分享金冠到朋友圈
		 "MSG_SHARE_FRIEND_HIGH_SCORE";     //分享本周最高给好友
		 "MSG_SHARE_FRIEND_BEST_SCORE";     //分享历史最高给好友
		 "MSG_SHARE_FRIEND_CROWN";          //分享金冠给好友
		 "MSG_friend_exceed"         // 超越炫耀
		 "MSG_heart_send"            // 送心
	 * @param imgData 原图文件数据
	 * @param imgDataLen 原图文件数据长度(图片大小不能超过10M)
	 * @param messageExt 游戏分享是传入字符串，通过此消息拉起游戏会通过 OnWakeUpNotify(WakeupRet ret)中ret.messageExt回传给游戏
	 * @param messageAction scene为1(分享到微信朋友圈)的情况下才起作用
	 *   WECHAT_SNS_JUMP_SHOWRANK       跳排行
	 *   WECHAT_SNS_JUMP_URL            跳链接
	 *   WECHAT_SNS_JUMP_APP           跳APP
	 * @return void
	 *   通过游戏设置的全局回调的OnShareNotify(ShareRet& shareRet)回调返回数据给游戏, shareRet.flag值表示返回状态, 可能值及说明如下:
	 *     eFlag_Succ: 分享成功
	 *     eFlag_Error: 分享失败
	 */
	void WGSendToWeixinWithPhoto(
		const eWechatScene &scene,
		unsigned char *mediaTagName,
		unsigned char *imgData, 
		const int &imgDataLen,
		unsigned char *messageExt,
		unsigned char *messageAction
	);

#ifdef __APPLE__
	/**  分享内容到QQ
	 *
	 * 分享时调用
     * @param scene QQScene_QZone:空间，默认弹框 QQScene_Session:好友
	 * @param title 标题
	 * @param desc 内容
	 * @param url  内容的跳转url，填游戏对应游戏中心详情页
	 * @param imgData 图片文件数据
	 * @param imgDataLen 数据长度
	 * @return void
	 */
	void WGSendToQQ(
		const eQQScene& scene,
		unsigned char* title, 
		unsigned char* desc, 
		unsigned char* url, 
		unsigned char* imgData, 
		const int& imgDataLen
	);
    /*
	 *
	 * 分享时调用
	 * @param QQScene_QZone:空间, 默认弹框 QQScene_Session:好友
	 * @param imgData 图片文件数据
	 * @param imgDataLen 数据长度
	 */
	void WGSendToQQWithPhoto(
		const eQQScene& scene,
		unsigned char* imgData, 
		const int& imgDataLen
	);

#endif

#ifdef ANDROID
	/**
	 * @param scene 标识发送手Q会话或者Qzone
	 * 		eQQScene.QQScene_QZone: 分享到空间(4.5以上版本支持)
	 * 		eQQScene.QQScene_Session: 分享到手Q会话
	 * @param title 结构化消息的标题
	 * @param desc 结构化消息的概要信息
	 * @param url  内容的跳转url，填游戏对应游戏中心详情页，游戏被分享消息拉起时, MSDK会给游戏OnWakeup(WakeupRet& wr)回调, wr.extInfo中会以key-value的方式带回所有的自定义参数.
	 * @param imgUrl 分享消息说略图URL
	 * @param imgUrlLen 分享消息说略图URL长度
	 * @return void
	 *   通过游戏设置的全局回调的OnShareNotify(ShareRet& shareRet)回调返回数据给游戏, shareRet.flag值表示返回状态, 可能值及说明如下:
	 *     eFlag_Succ: 分享成功
	 *     eFlag_Error: 分享失败
	 *
	 *     @return void
	 *	通过游戏设置的全局回调的OnShareNotify(ShareRet& shareRet)回调返回数据给游戏, shareRet.flag值表示返回状态, 可能值及说明如下:
	 *     eFlag_Succ: 分享成功
	 *     eFlag_Error: 分享失败
	 *   注意:
	 *     分享需要SD卡, 没有SD卡不能保证分享成功
	 *     由于手Q客户端4.6以前的版本返回的回调是有问题的, 故不要依赖此回调做其他逻辑. (当前flag全都返回均为eFlag_Succ)
	 *
	 */
	void WGSendToQQ(
		const eQQScene& scene,
		unsigned char* title, 
		unsigned char* desc,
		unsigned char* url, 
		unsigned char* imgUrl,
		const int& imgUrlLen
	);

/**
 * 分享纯图到手Q或者QQ空间
 * @param scene 标识发送手Q会话或者Qzone
 * 		eQQScene.QQScene_QZone: 分享到空间
 * 		eQQScene.QQScene_Session: 分享到手Q会话
 * @param imgFilePath 需要分享图片的本地文件路径, 图片需放在sd卡
 * 	每次分享的图片路径不能相同，相同会导致图片显示有问题，游戏需要自行保证每次分享图片的地址不相同
 *
 * @return void
 *   通过游戏设置的全局回调的OnShareNotify(ShareRet& shareRet)回调返回数据给游戏, shareRet.flag值表示返回状态, 可能值及说明如下:
 *     eFlag_Succ: 分享成功
 *     eFlag_Error: 分享失败
 *   注意:
 *    1. 由于手Q客户端4.6以前的版本返回的回调是有问题的, 故不要依赖此回调做其他逻辑. (当前flag全都返回均为eFlag_Succ)
 *    2. 图片路径需要使用sdcard的路径, 不能分享保存在应用目录下的图片
 *
 */
void WGSendToQQWithPhoto(const eQQScene& scene, unsigned char* imgFilePath);
#endif

	/**
	 * 用户反馈接口, 反馈内容查看http://mcloud.ied.com/queryLogSystem/ceQuery.html?token=07899ab75c30e499d5b33181c2d8ddc7&gameid=0&projectid=ce
	 * @param game 游戏名称, 游戏使用自己app的名称即可
	 * @param txt 反馈内容
	 */
	int WGFeedback(unsigned char* game, unsigned char* txt);

	/**
	 * 用户反馈接口, 反馈内容查看链接(Tencent内网):
	 * 		http://mcloud.ied.com/queryLogSystem/ceQuery.html?token=545bcbcfada62a4d84d7b0ee8e4b44bf&gameid=0&projectid=ce
	 * @param body 反馈的内容, 内容由游戏自己定义格式, SDK对此没有限制
	 * @return 通过OnFeedbackNotify回调反馈接口调用结果
	 */
	void WGFeedback(unsigned char* body);

	/**
	 * @param bRDMEnable 是否开启RDM的crash异常捕获上报
	 * @param bMTAEnable 是否开启MTA的crash异常捕获上报
	 */
	void WGEnableCrashReport(bool bRDMEnable, bool bMTAEnable);

	/**
	 * 自定义数据上报, 此接口仅支持一个key-value的上报, 从1.3.4版本开始, 建议使用void WGReportEvent( unsigned char* name, std::vector<KVPair>& eventList, bool isRealTime)
	 * @param name 事件名称
	 * @param body 事件内容
	 * @param isRealTime 是否实时上报
	 * @return void
	 */
	void WGReportEvent(
		unsigned char* name, 
		unsigned char * body, 
		bool isRealTime
	) DEPRECATED(1.3.4);

	/**
	 * @param name 事件名称
	 * @param eventList 事件内容, 一个key-value形式的vector
	 * @param isRealTime 是否实时上报
	 * @return void
	 */
	void WGReportEvent(
		unsigned char* name, 
		std::vector<KVPair>& eventList, 
		bool isRealTime
	);

	/**
     * 返回MSDK版本号
	 * @return MSDK版本号
	 */
    const std::string WGGetVersion();
	
    /**
	 * 如果没有再读assets/channel.ini中的渠道号, 故游戏测试阶段可以自己写入渠道号到assets/channel.ini用于测试. 
	 * IOS返回plist中的CHANNEL_DENGTA字段
     * @return 安装渠道
     */
	const std::string WGGetChannelId();

	/**
	 * @return 注册渠道
	 */
	const std::string WGGetRegisterChannelId();

	/**
	 * 此接口用于刷新微信的accessToken
	 * refreshToken的用途就是刷新accessToken, 只要refreshToken不过期就可以通过refreshToken刷新accessToken。
	 * 有两种情况需要刷新accessToken,
	 * @return void
	 *   通过游戏设置的全局回调的OnLoginNotify(LoginRet& loginRet)方法返回数据给游戏
	 *     因为只有微信平台有refreshToken, loginRet.platform的值只会是ePlatform_Weixin
	 *     loginRet.flag值表示返回状态, 可能值(eFlag枚举)如下：
	 *       eFlag_WX_RefreshTokenSucc: 刷新票据成功, 游戏接收到此flag以后直接读取LoginRet结构体中的票据进行游戏授权流程.
	 *       eFlag_WX_RefreshTokenFail: WGRefreshWXToken调用过程中网络出错, 刷新失败, 游戏自己决定是否需要重试 WGRefreshWXToken
	 */
	void WGRefreshWXToken();

	/**
	 * @param platformType 游戏传入的平台类型, 可能值为: ePlatform_QQ, ePlatform_Weixin
	 * @return 平台的支持情况, false表示平台不支持授权, true则表示支持
	 */
	bool WGIsPlatformInstalled(ePlatform platformType);

	/**
	 * 检查平台是否支持SDK API接口
	 * @param platformType 游戏传入的平台类型, 可能值为: ePlatform_QQ, ePlatform_Weixin
	 * @return 平台的支持情况, false表示平台不支持授权, true则表示支持
	 */
    bool WGIsPlatformSupportApi(ePlatform platformType);

#ifdef ANDROID
    /**
     * 获取pf, 用于支付, 和pfKey配对使用
     * @param cGameCustomInfo 默认可以填空, 部分游戏经分有特殊需求可以通过此自定义字段传入特殊需求数据
     * @return pf
     */
	const std::string WGGetPf(unsigned char * cGameCustomInfo);
#endif
#ifdef  __APPLE__
    /**
     * 获取pf, 用于支付, 和pfKey配对使用
     * @return pf
     */
	const std::string WGGetPf();
#endif

	/**
     * 获取pfkey，pfKey由msdk 服务器加密生成，支付过程校验
	 * @return 返回当前pf加密后对应fpKey字符串
	 */
	const std::string WGGetPfKey();
    
#ifdef __APPLE__  //支付相关接口 uin支付体系openId填 QQ帐号     openKey填 SKeyValue     sessionId 填 “uin“      sessionType 填”skey“
    /**
     *
     *  @param offerId     必填参数 申请到的offerId
     *  @param openId      必填参数 填openId 的值
     *  @param openKey     必填参数 手Q：paytoken 微信：accesstoken
     *  @param sessionId   必填参数 手Q："openid"  微信："wechatid"
     *  @param sessionType 必填参数 手Q："kp_actoken"  微信："wc_actoken"
     *  @param pf          调用WGGetPf()获取
     *  @param pfKey       调用WGGetPfKey()获取
     */
    void WGRegisterPay(unsigned char* offerId, unsigned char* openId, unsigned char* openKey, unsigned char* sessionId, unsigned char* sessionType, unsigned char* custom);// since 1.2.6
    /**
     *
     *  @param offerId           同WGRegisterPay
     *  @param openId            同WGRegisterPay
     *  @param openKey           同WGRegisterPay
     *  @param sessionId         同WGRegisterPay
     *  @param sessionType       同WGRegisterPay
     *  @param payItem           1.如果是 单笔 直接由业务自己订 2.如果是包月 字符串 是 开月的月数 3如果是游戏币,则是充值个数
     *  @param productId         苹果的产品id
     *  @param pf                同WGRegisterPay
     *  @param pfKey             同WGRegisterPay
     *  @param isDepositGameCoin 是否是托管的游戏币类型在拉取营销信息接口的时候不需要此字段）
     *  @param productType       苹果产品的类型 见IAPPayRequestInfo 的roducType(0.消费类产品 1.非消费类产品 2.包月+自动续费 3.免费 4.包月+非自动续费)
     *  @param zoneId            应用在MSDK支付平台注册信息
     *  @param varItem           应用端的扩展字段，透传给业务(在拉取营销信息接口的时候不需要此字段)
     */
    void WGPay(unsigned char* offerId, unsigned char* openId, unsigned char* openKey, unsigned char* sessionId, unsigned char* sessionType, unsigned char* payItem, unsigned char* productId, bool isDepositGameCoin, uint32_t productType, uint32_t quantity, unsigned char* zoneId, unsigned char* varItem, unsigned char* custom);
    /**
     *
     *  @param openId  同WGRegisterPay
     *  @param openKey 同WGRegisterPay
     */
//    void WGIAPRestoreFailueDistributeGoods(unsigned char* openId,unsigned char* openKey);
    /**
     *
     *  @param payItem           同WGPay
     *  @param productId         同WGPay
     *  @param isDepositGameCoin 同WGPay
     *  @param productType       同WGPay
     *  @param quantity          同WGPay
     *  @param zoneId            同WGPay
     *  @param varItem           同WGPay
     */
    void WGIAPRestoreCompletedTransactions(unsigned char* offerId, unsigned char* openId, unsigned char* openKey, unsigned char* sessionId, unsigned char* sessionType, unsigned char* payItem, unsigned char* productId, bool isDepositGameCoin, uint32_t productType, uint32_t quantity, unsigned char* zoneId, unsigned char* varItem, unsigned char* custom);
    /**
     *  拉取营销信息
     *
     *  @param payItem           同WGPay
     *  @param productId         同WGPay
     *  @param isDepositGameCoin 同WGPay
     *  @param productType       同WGPay
     *  @param quantity          同WGPay
     *  @param zoneId            同WGPay
     *  @param varItem           同WGPay
     */
    void WGIAPLaunchMpInfo(unsigned char* offerId, unsigned char* openId, unsigned char* openKey, unsigned char* sessionId, unsigned char* sessionType, unsigned char* payItem, unsigned char* productId, bool isDepositGameCoin, uint32_t productType, uint32_t quantity, unsigned char* zoneId, unsigned char* varItem, unsigned char* custom);
    /**
     *  注销支付
     */
    void WGDipose();//since 1.2.6
    /**
     *
     *  @return true:支持   false:不支持
     */
    bool WGIsSupprotIapPay();//since 1.2.6
    /**
     *  设置支付id
     *
     *  @param offerId 同WGRegisterPay
     */
    void WGSetOfferId(unsigned char* offerId);//since 1.2.6
    /**
     *  设置支付环境
     *
     *  @param envirenment dev 开发环境  test 沙箱环境   release现网环境
     */
    void WGSetIapEnvirenment(unsigned char* envirenment);
    /**
     *  设置支付log开关
     *
     *  @param enabled true:打开 false:关闭
     */
    void WGSetIapEnalbeLog(bool enabled);
    /**
     *  获取paytoken有效期
     *
     *  @return paytoken有效期 seconds
     */
    int WGGetPaytokenValidTime();
#endif
    /**
     *  输出msdk依赖平台版本号
     */
    void WGLogPlatformSDKVersion();//log出msdk用到的各sdk版本号
    
    
#ifdef __APPLE__
    /**
     *
     *  @return 手Q版本号
        typedef enum QQVersion
        {
        kQQVersion3_0,
        kQQVersion4_0,      //支持sso登陆
        kQQVersion4_2_1,    //ios7兼容
        kQQVersion4_5,      //4.5版本，wpa会话
        kQQVersion4_6,      //4.6版本，sso登陆信令通道切换
        } QQVersion;
     */
    int WGGetIphoneQQVersion();//获取手Q版本号
    /**
     *
     *  @param enabled true:打开 false:关闭
     */
    void WGOpenMSDKLog(bool enabled);
#endif

#ifdef ANDROID
	/**
	 *     地址形如: http://180.153.81.37/monitor/monitor.jsp, IP如: 119.147.19.241:80 (需要端口号)
	 * @param 需要测速的地址(IP)列表
	 */
	void WGTestSpeed(std::vector<std::string> &addrList);

	/**
	 * @return 接口的支持情况
	 */
	bool WGCheckApiSupport(eApiName);
protected:
	void setWakeup(WakeupRet& wakeup);
	WakeupRet& getWakeup();

	void setLoginRet(LoginRet& lr);
	LoginRet& getLoginRet();
public:
#endif

	/**
	 * 获取自己的QQ资料
	 * @return void
	 *   此接口的调用结果通过OnRelationCallBack(RelationRet& relationRet) 回调返回数据给游戏,
	 *   RelationRet对象的persons属性是一个Vector<PersonInfo>, 取第0个即是用户的个人信息.
	 *   nickname, openId, gender, pictureSmall, pictureMiddle, pictureLarge, 其他字段为空.
	 */
	bool WGQueryQQMyInfo();

	/**
	 * 获取QQ好友信息, 回调在OnRelationNotify中,
	 * 其中RelationRet.persons为一个Vector, Vector中的内容即使好友信息, QQ好友信息里面province和city为空
	 * @return void
	 * 此接口的调用结果通过OnRelationNotify(RelationRet& relationRet)
	 *   回调返回数据给游戏, RelationRet对象的persons属性是一个Vector<PersonInfo>,
	 *   其中的每个PersonInfo对象即是好友信息,
	 *   好友信息包含: nickname, openId, gender, pictureSmall, pictureMiddle, pictureLarge
	 */
	bool WGQueryQQGameFriendsInfo();

	/**
	 *   回调在OnRelationNotify中,其中RelationRet.persons为一个Vector, Vector的第一项即为自己的资料
	 *   个人信息包括nickname, openId, gender, pictureSmall, pictureMiddle, pictureLarge, provice, city
	 */
	bool WGQueryWXMyInfo();

	/**
	 * 获取微信好友信息, 回调在OnRelationNotify中,
	 *   回调在OnRelationNotify中,其中RelationRet.persons为一个Vector, Vector中的内容即为好友信息
	 *   好友信息包括nickname, openId, gender, pictureSmall, pictureMiddle, pictureLarge, provice, city
	 */
	bool WGQueryWXGameFriendsInfo();

	/**
	 * @param act 好友点击分享消息拉起页面还是直接拉起游戏, 传入 1 拉起游戏, 传入 0, 拉起targetUrl
	 * @param fopenid 好友的openId
	 * @param title 分享的标题
	 * @param summary 分享的简介
	 * @param targetUrl 内容的跳转url，填游戏对应游戏中心详情页，游戏被分享消息拉起时, MSDK会给游戏OnWakeup(WakeupRet& wr)回调, wr.extInfo中会以key-value的方式带回所有的自定义参数.
	 * @param imageUrl 分享缩略图URL
	 * @param previewText 可选, 预览文字
	 * @param gameTag 可选, 此参数必须填入如下值的其中一个
				 MSG_INVITE                //邀请
				 MSG_FRIEND_EXCEED       //超越炫耀
				 MSG_HEART_SEND          //送心
				 MSG_SHARE_FRIEND_PVP    //PVP对战
	 */
	bool WGSendToQQGameFriend(
		int act, 
		unsigned char* fopenid,
		unsigned char *title, 
		unsigned char *summary,
		unsigned char *targetUrl, 
		unsigned char *imgUrl,
		unsigned char* previewText, 
		unsigned char* gameTag
	);
	/**
	 * @param act 好友点击分享消息拉起页面还是直接拉起游戏, 传入 1 拉起游戏, 传入 0, 拉起targetUrl
	 * @param fopenid 好友的openId
	 * @param title 分享的标题
	 * @param summary 分享的简介
	 * @param targetUrl 内容的跳转url，填游戏对应游戏中心详情页，
	 * @param imageUrl 分享缩略图URL
	 * @param previewText 可选, 预览文字
	 * @param gameTag 可选, 此参数必须填入如下值的其中一个
				 MSG_INVITE                //邀请
				 MSG_FRIEND_EXCEED       //超越炫耀
				 MSG_HEART_SEND          //送心
				 MSG_SHARE_FRIEND_PVP    //PVP对战
	* @param extMsdkInfo 游戏自定义透传字段，通过分享结果shareRet.extInfo返回给游戏，游戏可以用extInfo区分request
	*/
    bool WGSendToQQGameFriend(
        int act,
        unsigned char* fopenid,
        unsigned char *title,
        unsigned char *summary,
        unsigned char *targetUrl,
        unsigned char *imgUrl,
        unsigned char* previewText,
        unsigned char* gameTag,
        unsigned char* msdkExtInfo
      );

	/**
	 * 此接口类似WGSendToQQGameFriend, 此接口用于分享消息到微信好友, 分享必须指定好友openid
	 * @param fOpenId 好友的openid
	 * @param title 分享标题
	 * @param description 分享描述
	 * @param mediaId 图片的id 通过uploadToWX接口获取
	 * @param messageExt 游戏分享是传入字符串，通过此消息拉起游戏会通过 OnWakeUpNotify(WakeupRet ret)中ret.messageExt回传给游戏
	 * @param mediaTagName 请根据实际情况填入下列值的一个, 此值会传到微信供统计用, 在分享返回时也会带回此值, 可以用于区分分享来源
		 "MSG_INVITE";                   // 邀请
		 "MSG_SHARE_MOMENT_HIGH_SCORE";    //分享本周最高到朋友圈
		 "MSG_SHARE_MOMENT_BEST_SCORE";    //分享历史最高到朋友圈
		 "MSG_SHARE_MOMENT_CROWN";         //分享金冠到朋友圈
		 "MSG_SHARE_FRIEND_HIGH_SCORE";     //分享本周最高给好友
		 "MSG_SHARE_FRIEND_BEST_SCORE";     //分享历史最高给好友
		 "MSG_SHARE_FRIEND_CROWN";          //分享金冠给好友
		 "MSG_friend_exceed"         // 超越炫耀
		 "MSG_heart_send"            // 送心
	 */
	bool WGSendToWXGameFriend(
		unsigned char *fOpenId, 
		unsigned char *title,
		unsigned char *description, 
		unsigned char *mediaId,
		unsigned char* messageExt,
		unsigned char *mediaTagName
	);

	/**
	 * 此接口类似WGSendToQQGameFriend, 此接口用于分享消息到微信好友, 分享必须指定好友openid
	 * @param fOpenId 好友的openid
	 * @param title 分享标题
	 * @param description 分享描述
	 * @param mediaId 图片的id 通过uploadToWX接口获取
	 * @param messageExt 游戏分享是传入字符串，通过此消息拉起游戏会通过 OnWakeUpNotify(WakeupRet ret)中ret.messageExt回传给游戏
	 * @param mediaTagName 请根据实际情况填入下列值的一个, 此值会传到微信供统计用, 在分享返回时也会带回此值, 可以用于区分分享来源
		 "MSG_INVITE";                   // 邀请
		 "MSG_SHARE_MOMENT_HIGH_SCORE";    //分享本周最高到朋友圈
		 "MSG_SHARE_MOMENT_BEST_SCORE";    //分享历史最高到朋友圈
		 "MSG_SHARE_MOMENT_CROWN";         //分享金冠到朋友圈
		 "MSG_SHARE_FRIEND_HIGH_SCORE";     //分享本周最高给好友
		 "MSG_SHARE_FRIEND_BEST_SCORE";     //分享历史最高给好友
		 "MSG_SHARE_FRIEND_CROWN";          //分享金冠给好友
		 "MSG_friend_exceed"         // 超越炫耀
		 "MSG_heart_send"            // 送心
	 * @param msdkExtInfo 游戏自定义透传字段，通过分享结果shareRet.extInfo返回给游戏
	 */
    bool WGSendToWXGameFriend(
        unsigned char* fOpenId,
        unsigned char* title,
        unsigned char* description,
        unsigned char* mediaId,
        unsigned char* messageExt,
        unsigned char* mediaTagName,
        unsigned char* msdkExtInfo
    );

     /**
     *  @since 1.5.0
     */
    void WGLoginWithLocalInfo();

    /*
     * @param type   要显示的公告类型
     * 	  eMSG_NOTICETYPE_ALERT: 弹出公告
     * 	  eMSG_NOTICETYPE_SCROLL: 滚动公告
     * 	  eMSG_NOTICETYPE_ALL: 弹出公告&&滚动公告
     * @param scene 公告场景ID，不能为空
     */
    void WGShowNotice(eMSG_NOTICETYPE type, unsigned char *scene);

    /**
     * 隐藏滚动公告
     */
    void WGHideScrollNotice();

    /**
     *  @param openUrl 要打开的url
     */
    void WGOpenUrl(unsigned char * openUrl);
    
    /**
     *  获取附近人的信息
     *  @return 回调到OnLocationNotify
	 *  @return void
	 *   通过游戏设置的全局回调的OnLocationNofity(RelationRet& rr)方法返回数据给游戏
	 *     rr.platform表示当前的授权平台, 值类型为ePlatform, 可能值为ePlatform_QQ, ePlatform_Weixin
	 *     rr.flag值表示返回状态, 可能值(eFlag枚举)如下：
	 * 			eFlag_LbsNeedOpenLocationService: 需要引导用户开启定位服务
	 *  		eFlag_LbsLocateFail: 定位失败, 可以重试
	 *  		eFlag_Succ: 获取附近的人成功
	 *  		eFlag_Error:  定位成功, 但是请求附近的人失败, 可重试
	 *     rr.persons是一个Vector, 其中保存了附近玩家的信息
     */
    void WGGetNearbyPersonInfo();

    /**
     *  @return 回调到OnLocationNotify
	 *  @return void
	 *   通过游戏设置的全局回调的OnLocationNofity(RelationRet& rr)方法返回数据给游戏
	 *     rr.platform表示当前的授权平台, 值类型为ePlatform, 可能值为ePlatform_QQ, ePlatform_Weixin
	 *     rr.flag值表示返回状态, 可能值(eFlag枚举)如下：
	 *  		eFlag_Succ: 清除成功
	 *  		eFlag_Error: 清楚失败
     */
    bool WGCleanLocation();

    /**
     * 此接口会分享消息到微信游戏中心内的消息中心，这种消息主要包含两部分，消息体和附加按钮，消息体主要包含展示内容
     * 附加按钮主要定义了点击以后的跳转动作（拉起APP，拉起页面、拉起排行榜），消息类型和按钮类型可以任意组合
     * @param fopenid 好友的openid
     * @param title 游戏消息中心分享标题
     * @param content 游戏消息中心分享内容
     * @param pInfo 消息体，这里可以传入四种消息类型，均为WXMessageTypeInfo的子类：
     * 		TypeInfoImage: 图片消息（下面的几种属性全都要填值）
     * 			std::string pictureUrl; // 图片缩略图
     * 			int height; // 图片高度
     * 			int width; // 图片宽度
     * 		TypeInfoVideo: 视频消息（下面的几种属性全都要填值）
     * 			std::string pictureUrl; // 视频缩略图
     * 			int height; // 视频高度
     * 			int width; // 视频宽度
     * 			std::string mediaUrl; // 视频链接
     * 		TypeInfoLink: 链接消息（下面的几种属性全都要填值）
     * 			std::string pictureUrl; // 在消息中心的消息图标Url（图片消息中，此链接则为图片URL)
     * 			std::string targetUrl; // 链接消息的目标URL，点击消息拉起此链接
     * 		TypeInfoText: 文本消息
     *
     * @param pButton 按钮效果，这里可以传入三种按钮类型，均为WXMessageButton的子类：
     * 		ButtonApp: 拉起应用（下面的几种属性全都要填值）
     * 			std::string name; // 按钮名称
     * 			std::string messageExt; // 附加自定义信息，通过按钮拉起应用时会带回游戏
     * 		ButtonWebview: 拉起web页面（下面的几种属性全都要填值）
     * 			std::string name; // 按钮名称
     * 			std::string webViewUrl; // 点击按钮后要跳转的页面
     * 		ButtonRankView: 拉起排行榜（下面的几种属性全都要填值）
     * 			std::string name; // 按钮名称
     * 			std::string title; // 排行榜名称
     * 			std::string rankViewButtonName; // 排行榜中按钮的名称
     * 			td::string messageExt; // 附加自定义信息，通过排行榜中按钮拉起应用时会带回游戏
     * @param msdkExtInfo 游戏自定义透传字段，通过分享结果shareRet.extInfo返回给游戏
     *  @return 参数异常或未登陆
     */
    bool WGSendMessageToWechatGameCenter(unsigned char* fOpenid,
	    unsigned char* title,
	    unsigned char* content,
	    WXMessageTypeInfo *pInfo,
	    WXMessageButton *pButton,
	    unsigned char* msdkExtInfo
    );

    /**
	 * 把音乐消息分享给微信好友
	 * @param scene 指定分享到朋友圈, 或者微信会话, 可能值和作用如下:
	 *   WechatScene_Session: 分享到微信会话
	 *   WechatScene_Timeline: 分享到微信朋友圈 (此种消息已经限制不能分享到朋友圈)
	 * @param title 音乐消息的标题
	 * @param desc	音乐消息的概要信息
	 * @param musicUrl	音乐消息的目标URL
	 * @param musicDataUrl	音乐消息的数据URL
	 * @param mediaTagName 请根据实际情况填入下列值的一个, 此值会传到微信供统计用, 在分享返回时也会带回此值, 可以用于区分分享来源
		 "MSG_INVITE";                   // 邀请
		 "MSG_SHARE_MOMENT_HIGH_SCORE";    //分享本周最高到朋友圈
		 "MSG_SHARE_MOMENT_BEST_SCORE";    //分享历史最高到朋友圈
		 "MSG_SHARE_MOMENT_CROWN";         //分享金冠到朋友圈
		 "MSG_SHARE_FRIEND_HIGH_SCORE";     //分享本周最高给好友
		 "MSG_SHARE_FRIEND_BEST_SCORE";     //分享历史最高给好友
		 "MSG_SHARE_FRIEND_CROWN";          //分享金冠给好友
		 "MSG_friend_exceed"         // 超越炫耀
		 "MSG_heart_send"            // 送心
	 * @param imgData 原图文件数据
	 * @param imgDataLen 原图文件数据长度(图片大小不z能超过10M)
	 * @param messageExt 游戏分享是传入字符串，通过此消息拉起游戏会通过 OnWakeUpNotify(WakeupRet ret)中ret.messageExt回传给游戏
	 * @param messageAction scene为WechatScene_Timeline(分享到微信朋友圈)的情况下才起作用
	 *   WECHAT_SNS_JUMP_SHOWRANK       跳排行,查看排行榜
	 *   WECHAT_SNS_JUMP_URL            跳链接,查看详情
	 *   WECHAT_SNS_JUMP_APP            跳APP,玩一把
	 * @return void
	 *   通过游戏设置的全局回调的OnShareNotify(ShareRet& shareRet)回调返回数据给游戏, shareRet.flag值表示返回状态, 可能值及说明如下:
	 *     eFlag_Succ: 分享成功
	 *     eFlag_Error: 分享失败
	 */
    void WGSendToWeixinWithMusic(
		const eWechatScene& scene,
		unsigned char* title,
		unsigned char* desc,
		unsigned char* musicUrl,
		unsigned char* musicDataUrl,
		unsigned char *mediaTagName,
		unsigned char *imgData,
		const int &imgDataLen,
		unsigned char *messageExt,
		unsigned char *messageAction
	);
    
    /**
	 * 把音乐消息分享到手Q会话
	 * @param scene eQQScene:
	 * 			QQScene_QZone : 分享到空间
	 * 			QQScene_Session：分享到会话
	 * @param title 结构化消息的标题
	 * @param desc 结构化消息的概要信息
	 * @param musicUrl      点击消息后跳转的URL
	 * @param musicDataUrl  音乐数据URL（例如http:// ***.mp3）
	 * @param imgUrl 		分享消息缩略图URL
	 * @return void
	 *   通过游戏设置的全局回调的OnShareNotify(ShareRet& shareRet)回调返回数据给游戏, shareRet.flag值表示返回状态, 可能值及说明如下:
	 *     eFlag_Succ: 分享成功
	 *     eFlag_Error: 分享失败
	 */
    void WGSendToQQWithMusic(
		const eQQScene& scene,
		unsigned char* title,
		unsigned char* desc,
		unsigned char* musicUrl,
		unsigned char* musicDataUrl,
		unsigned char* imgUrl
	);

    /*
     * 从本地数据库读取指定scene下指定type的当前有效公告
	 * @param type 需要展示的公告类型。类型为eMSG_NOTICETYPE，具体值如下:
	 * 	  eMSG_NOTICETYPE_ALERT: 弹出公告
	 * 	  eMSG_NOTICETYPE_SCROLL: 滚动公告
	 * @param sence 这个参数和公告管理端的“公告栏”对应
	 * @return NoticeInfo结构的数组，NoticeInfo结构如下：
		typedef struct {
			std::string msg_id; //公告id
			std::string open_id; //用户open
			std::string msg_content; //公告内容
			std::string msg_title; //公告标题
			std::string msg_url; //公告跳转链接
			eMSG_NOTICETYPE msg_type; //公告类型，eMSG_NOTICETYPE
			std::string msg_scene; //公告展示的场景，管理端后台配置
			std::string start_time; //公告有效期开始时间
			std::string end_time; //公告有效期结束时间
		}NoticeInfo;
	 */
    std::vector<NoticeInfo> WGGetNoticeData(eMSG_NOTICETYPE type,unsigned char *scene);
    
    /**
     *  打开AMS营销活动中心
     *
     *  @param params 可传入附加在URL后的参数，长度限制256.格式为"key1=***&key2=***",注意特殊字符需要urlencode。
     *                不能和MSDK将附加在URL的固定参数重复，列表如下：
     *                参数名            说明	        值
     *                timestamp	       请求的时间戳
     *                appid 	       游戏ID
     *                algorithm	       加密算法标识	v1
     *                msdkEncodeParam  密文
     *                version	       MSDK版本号	    例如1.6.2i
     *                sig	           请求本身的签名
     *                encode	       编码参数	    1
     *  @return bool 返回值，正常返回true；如果params超长返回false
     */
    bool WGOpenAmsCenter(unsigned char* params);
#ifdef __APPLE__
    void WGRegisterAPNSPushNotification();
    void WGSuccessedRegisterdAPNSWithToken(NSData *data);
    void WGFailedRegisteredAPNS();
    void WGCleanBadgeNumber();
    void WGReceivedMSGFromAPNSWithDict(NSDictionary* userInfo);
#endif
};
#endif

