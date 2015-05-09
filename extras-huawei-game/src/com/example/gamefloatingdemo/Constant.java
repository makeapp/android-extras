package com.example.gamefloatingdemo;


import java.util.HashMap;

import com.hianalytics.android.intf.IHiAnalytics;

public class Constant {

	public static final String HWID_PLUS_NAME = "hwIDOpenSDK";
	public static final String HuaweiPayPlugin = "HuaweiPaySDK";
	public static final String BuoyOpenSDKPlugin = "BuoyOpenSDK";
	public static final String HiAnalyticsSDKPlugin = IHiAnalytics.plusName;
	
	public static final HashMap<String, String> allPluginName = new HashMap<String, String>()
	{
		{
			put(HWID_PLUS_NAME, "华为帐号");
			put(BuoyOpenSDKPlugin, "游戏浮标");
			put(HuaweiPayPlugin, "华为支付");
			put(HiAnalyticsSDKPlugin, "华为统计");
			
		}
	};
	
}
