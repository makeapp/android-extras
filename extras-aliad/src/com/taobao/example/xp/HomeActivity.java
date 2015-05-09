
package com.taobao.example.xp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.taobao.newxp.common.Log;
import com.taobao.example.xp.config.Configuration;
import com.taobao.example.xp.ui.MainFragment;
import com.taobao.newxp.common.AlimmContext;
import com.taobao.ui.BaseSinglePaneActivity;
import com.umeng.analytics.MobclickAgent;
import com.taobao.newxp.common.ExchangeConstants;
import com.taobao.newxp.controller.ExchangeDataService;
import com.taobao.newxp.controller.XpListenersCenter.NTipsChangedListener;
//import com.taobao.newxp.view.feed.Feed;
//import com.taobao.newxp.view.feed.FeedsManager;
//import com.taobao.newxp.view.feed.FeedsManager.IncubatedListener;

//import Feed;
//import FeedsManager;
//import FeedsManager.IncubatedListener;

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
public class HomeActivity extends BaseSinglePaneActivity {
//    public static ExchangeDataService preloadDataService;
//    public static FeedsManager feedsManager;

    static {// 设置report 回调
//        ExchangeViewManager.setReportListener(new ReportListener() {
//            @Override
//            public void onReportStart(Map<String, Object> map) {
//                String str = "";
//                for (String key : map.keySet()) {
//                    str += key + "=" + map.get(key).toString() + "   ";
//                }
//                System.out.println("  ReportResponse " + str);
//            }
//
//            @Override
//            public void onReportEnd(STATUS status) {
//                System.out.println("  onReportEnd");
//            }
//        });
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.openActivityDurationTrack(false);
        mActionBar.setTitle("样式列表");
        mActionBar.clearHomeAction();

        AlimmContext.getAliContext().init(this);

//        initizeFeeds();
        // set container preload data
//        initizeContainerData();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("s", "s");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("样式列表");
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("样式列表");
    }

    @Override
    protected Fragment onCreatePane() {
        // ExchangeConstants.banner_alpha = 120;
        ExchangeConstants.full_screen = false;
        ExchangeConstants.ONLY_CHINESE = false;
        ExchangeConstants.handler_auto_expand = true;
        ExchangeConstants.DEBUG_MODE = true;
        ExchangeConstants.handler_left = true;
        ExchangeConstants.RICH_NOTIFICATION = true;
        Log.LOG = true;
        

        return new MainFragment();
    }

//    private void initizeContainerData() {
//        preloadDataService = new ExchangeDataService(Configuration.SLOT_CONTAINER);
//        preloadDataService.preloadData(this, new NTipsChangedListener() {
//            @Override
//            public void onChanged(int flag) {
//                //                    TextView view = (TextView) root
//                //                            .findViewById(R.id.umeng_example_xp_container_tips);
//                //                    if (flag == -1) {// 没有新广告
//                //                        view.setVisibility(View.INVISIBLE);
//                //                    } else if (flag > 1) {// 第一页新广告数量
//                //                        view.setVisibility(View.VISIBLE);
//                //                        view.setBackgroundResource(R.drawable.umeng_example_xp_new_tip_bg);
//                //                        view.setText("" + flag);
//                //                    } else if (flag == 0) {// 第一页全部都是新广告
//                //                        view.setVisibility(View.VISIBLE);
//                //                        view.setBackgroundResource(R.drawable.umeng_example_xp_new_tip);
//                //                    }
//            };
//        }, ExchangeConstants.type_container);
//    }

//    private void initizeFeeds() {
//        feedsManager = new FeedsManager(this);
//
//        String slot = Configuration.SLOT_FEED_ICON_ITEM;
//        //参数说明：arg1=广告位，arg2=广告位标记
//        feedsManager.addMaterial(slot);
//        slot = Configuration.SLOT_FEED_IMG_ITEM;
//        feedsManager.addMaterial(slot);
//        slot = Configuration.SLOT_FEED_SMALLIMG_APP;//小图应用
//        feedsManager.addMaterial(slot);
//        slot = Configuration.SLOT_FEED_SMALLIMG_ITEM;//小图电商
//        feedsManager.addMaterial(slot);
//
////        slot = Configuration.SLOT_FEED_ICON_APP;//4张小图应用
////        feedsManager.addMaterial(slot, slot);
//
//        feedsManager.incubate(); // 开始孵化feed广告
//
////        slot = Configuration.SLOT_FEED_ICON_ITEM;
////        Feed product = feedsManager.getProduct(slot);
////        if(product != null){
////            Toast.makeText(MainActivity.this,"Feed 广告位["+slot+"]存在缓存。", Toast.LENGTH_SHORT).show();
////            Log.i("Feed", "Feed 广告位[" + slot + "]存在缓存。" + product.getStyle());
//////            List<Promoter> promoters = product.getPromoters();
//////            ExchangeDataService dataService = product.getDataService(MainActivity.this);
//////            dataService.reportImpression(promoters.get(0));//发送一条展示report
////        }else{
////
////            Toast.makeText(MainActivity.this,"Feed 广告位["+slot+"]没有发现缓存。", Toast.LENGTH_SHORT).show();
////            Log.i("Feed","Feed 广告位["+slot+"]没有发现缓存。");
////        }
////
////        slot = Configuration.SLOT_FEED_ICON_ITEM;
////        //参数说明：arg1=广告位，arg2=广告位标记
////        feedsManager.addMaterial(slot);
////        feedsManager.incubate(); // 开始孵化feed广告
////        product = feedsManager.getProduct(slot);
////        if(product != null){
////            Toast.makeText(MainActivity.this,"Feed 广告位["+slot+"]存在缓存。", Toast.LENGTH_SHORT).show();
////            Log.i("Feed", "Feed2 广告位[" + slot + "]存在缓存。" + product.getStyle());
////            List<Promoter> promoters = product.getPromoters();
////            ExchangeDataService dataService = product.getDataService(MainActivity.this);
////            dataService.reportImpression(promoters.get(0));//发送一条展示report
////        }else{
////
////            Toast.makeText(MainActivity.this,"Feed 广告位["+slot+"]没有发现缓存。", Toast.LENGTH_SHORT).show();
////            Log.i("Feed","Feed2 广告位["+slot+"]没有发现缓存。");
////        }
//
//        feedsManager.setFeedScrollAble(true);
//        feedsManager.setIncubatedListener(new IncubatedListener() {
//            @Override
//            public void onComplete(int status, Feed feed,String slot) {
//                // 孵化过程回调
//                FragmentActivity activity = HomeActivity.this;
//                if (activity != null) {
//                    Toast.makeText(activity, "初始化feed "+slot, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

}
