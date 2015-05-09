//
//  WGPlatformObserver.h
//  WGPlatform
//
//  Created by fly chen on 2/22/13.
//  Copyright (c) 2013 tencent.com. All rights reserved.
//

#ifndef WGPlatform_WGPlatformObserver_h
#define WGPlatform_WGPlatformObserver_h

#include <string>
#include "WGCommon.h"

/*! @brief WGPlatform通知类
 *
 * SDK通过通知类和外部调用者通讯
 */
class WGPlatformObserver
{
public:
	/*! @brief 登录回调
	 *
	 * 登录时通知上层App，并传递结果
	 * @param loginRet 参数
	 * @return void
	 */
	virtual void OnLoginNotify(LoginRet& loginRet) = 0;
    
    
	/*! @brief 分享结果
	 *
	 * 将分享的操作结果通知上层App
	 * @param shareRet 分享结果
	 * @return void
	 */
	virtual void OnShareNotify(ShareRet& shareRet) = 0;
    
    
	/*! @brief 被其他应用拉起
	 *
	 *被其他平台拉起
	 * @param wakeupRet  唤起参数
	 * @return void
	 */
	virtual void OnWakeupNotify(WakeupRet& wakeupRet) = 0;
    
    
    virtual void OnRelationNotify(RelationRet& relationRet) = 0;
    
    virtual void OnLocationNotify(RelationRet& relationRet) = 0;

    virtual void OnFeedbackNotify(int flag, std::string desc) = 0;
    
    #ifdef ANDROID
    virtual std::string OnCrashExtMessageNotify() = 0;
    #endif

	virtual ~WGPlatformObserver() {};
	
#ifdef __OBJC__
    //下单成功回调
    virtual void onOrderFinish(const char *billno,IAPPayRequestInfo &IAPRequestInfo) = 0;
    //下单失败回调
    virtual void onOrderFailue(IAPPayRequestInfo &IAPRequestInfo,const char* errorMessage ,int code) = 0;
    
    
    //苹果支付成功回调
    virtual void onIAPPayFinish(IAPPayRequestInfo &IAPRequestInfo)= 0;
    //苹果支付失败回调
    virtual void onIAPPayFailue(IAPPayRequestInfo &IAPRequestInfo,const char* errorString) = 0;
    
    //发货成功回调
    virtual void onDistributeGoodsFinish(IAPPayRequestInfo &IAPRequestInfo) = 0;
    //发货失败回调
    virtual void onDistributeGoodsFailue(IAPPayRequestInfo &IAPRequestInfo,const char *errorMessage,int code) = 0;
    
    //补发货的数目回调
//    virtual void OnDistributeGoodsWithCount(int code) = 0;
    //补发货成功回调(针对非消耗性商品)
    virtual void onRestorNon_ConsumableFinish(IAPPayRequestInfo &IAPRequestInfo) = 0;
    //补发货失败回调(针对非消耗性商品)
    virtual void onRestorNon_ConsumableFailue(IAPPayRequestInfo &IAPRequestInfo,const char* errorMessage,int code) = 0;
    //读取补发商品信息失败
    virtual void getrestoreInfoFailue(const char* errorString) = 0;
    
    //拉取营销活动回调
    virtual void onLaunMpFinish(IAPPayRequestInfo &IAPRequestInfo,IAPMpInfo &mapinfo) = 0;
    virtual void onLaunMpFailue(IAPPayRequestInfo &IAPRequestInfo,const char* errorMessage,int code) = 0;
    
    //拉取产品信息失败回调此接口
    virtual void onGetProductInfoFailue(IAPPayRequestInfo &IAPRequestInfo) = 0;
    
    //网络错误，参数：具体在进行哪一步的时候发生网络错误
    //1.下单 2 苹果支付 3 发货
    virtual void onNetWorkEorror(int state,IAPPayRequestInfo &IAPRequestInfo) = 0;
    
    //登陆态失效回调
    virtual void onLoginExpiry(IAPPayRequestInfo &info) = 0;
#endif
};

#endif
