
package com.taobao.example.xp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.taobao.example.xp.config.Configuration;
import com.taobao.munion.demo.R;
import com.umeng.analytics.MobclickAgent;
import com.taobao.newxp.Promoter;
import com.taobao.newxp.common.ExchangeConstants;
import com.taobao.newxp.controller.XpListenersCenter.WelcomeAdsListener;
import com.taobao.newxp.view.ExchangeViewManager;

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
public class WelcomeActivity extends Activity {
    ImageView frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        frame = new ImageView(this);
        frame.setLayoutParams(new LayoutParams(-1, -1));
        frame.setImageResource(R.drawable.welcome_splash);
        frame.setScaleType(ScaleType.FIT_XY);
        setContentView(frame);

        ExchangeConstants.WELCOME_COUNTDOWN = true;// 设置是否显示开屏样式倒计时

        /**
         * Umeng 开屏样式集成： WelcomeAdsListener：无论是否展示都会调用onFinish 超时或请求数据完毕 |
         * onDataReviced |-没有广告-onFinish 有广告 | onShow |-发生错误-onError | |
         * onCountdown onFinish | onFinish 如果在广告加载期间启动页已经关闭，将不会回调。
         */
        new ExchangeViewManager(this).addWelcomeAds(Configuration.SLOT_WELCOME,
                new WelcomeAdsListener() {
                    @Override
                    public void onShow(View view) {
                        System.out.println("onShow ");
                    }

                    @Override
                    public void onFinish() {
                        System.out.println("onFinish ");
                        frame.setImageResource(R.drawable.welcome_content);
                    }

                    @Override
                    public void onDataReviced(Promoter data) {
                        System.out.println("onDataReviced [" + (data == null ? 0 : data.size));
                    }

                    @Override
                    public void onCountdown(int count) {
                        System.out.println("onCountdown " + count);
                    }

                    @Override
                    public void onError(String msg) {
                        System.out.println("onError " + msg);
                    }
                }, 1000, 3000);// 广告将在调用addview 1000~3000
                               // 内展示，如果该时间段内没有接收到广告数据将调用onFinish

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("开屏页");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("开屏页");
    }
}
