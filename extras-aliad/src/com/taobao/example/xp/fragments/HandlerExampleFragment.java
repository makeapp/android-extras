
package com.taobao.example.xp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.taobao.munion.demo.R;
import com.taobao.newxp.common.ExchangeConstants;
import com.taobao.newxp.controller.ExchangeDataService;
import com.taobao.newxp.view.ExchangeViewManager;

//import com.taobao.munion.cache.CacheManager;

/**
* <b>注意：</b> SDK 集成方式中涉及两个和后台表表及数据关联的关键码“appkey”和 "slotid" </br>
* <p>
* <b>APPKEY:</b> </br> APPKEY用于交换网路的用户，用户可以申请多个APPKEY进行自主广告分组管理。 </br>
* Appkey的添加方式有三种，按优先级顺序排列： </br> </br> 1.exchangeDataService.appkey = "xxxxxxx"
* 该种方式可以在一个App中设置多个Appkey 优先级：高 </br> 2.ExchangeConstants.APPKEY = "xxxxx"
* 该种方式可以在App中指定一个Appkey，用于和其他SDK做区分 优先级：中 </br> 3.Manifest 文件中配置
* 该种方式可以在App中指定一个Appkey 优先级低 </br> </br> 如果在使用 new ExchagneDataService() 或 new
* ExchangeDataService("") 的方式创建，数据将与appkey做关联 </br> </br> <b>SLOT ID</b> </br>
* SLOTID用于UFP（Umeng For Publish）用户。 </br> Slot id 的集成方式只有一种，new
* ExchangeDataService("slot_id"); </br> </br> <b>如果用户同时集成了slotid 和
* appkey,SDK将优先采用slot_id作为关联码.</b> </br>
* </p>
*
* @author Jhen
*/
public class HandlerExampleFragment extends AnalyticFragment {
    protected Context mContext;
    String slotId;
    int flag = -1;//电商墙0，应用墙1，团购2
    private ExchangeViewManager exchangeViewManager;

    public HandlerExampleFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        Intent intent = activity.getIntent();
        slotId = intent.getStringExtra("slot_id");
        flag = intent.getIntExtra("feature", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.taobao_example_xp_handler, container,
                false);
        switch (flag) {
            case 0:
                root.setBackgroundResource(R.drawable.moji);
                break;
            case 1:
            case 2:
                root.setBackgroundResource(R.drawable.app_wall_bg);
                break;

        }
        addUmengFeature(root);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void addUmengFeature(View root) {
        // ===============云端图片入口集成方式==================
        final ExchangeDataService dateService2 = new ExchangeDataService(slotId);
        // 电商墙
        //if (flag == 0) {
        //    ExchangeConstants.MTOP_APPKEY = Configuration.TAOBAO_WALL_APP_KEY;
        //    ExchangeConstants.MTOP_APP_SECRET = Configuration.TAOBAO_WALL_APP_SECRET;
        //    ExchangeConstants.MTOP_APP_SIGNATURE = Configuration.TAOBAO_WALL_APP_SIGN;
        //}
        RelativeLayout relayout2 = (RelativeLayout) root.findViewById(R.id.rlayout);
        exchangeViewManager = new ExchangeViewManager(mContext,
                dateService2);
        exchangeViewManager.addView(ExchangeConstants.type_list_curtain, relayout2);
    }

    @Override
    public void onPause() {
        super.onPause();
//        exchangeViewManager.destroy();
    }

    @Override
    public String getPageName() {
        String name = null;
        switch (flag) {
            case 0:
                name = "电商墙";
                break;
            case 1:
                name = "应用墙";
                break;
            case 2:
                name = "团购墙";
                break;

        }
        return name;
    }

}
