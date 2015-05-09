package com.taobao.example.xp;
//
//package com.umeng.example.xp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import ReportResponse.STATUS;
//import ExchangeConstants;
//import ExchangeDataService;
//import XpListenersCenter.NTipsChangedListener;
//import XpListenersCenter.ReportListener;
//import ExchangeViewManager;
//import Feed;
//import FeedsManager;
//import FeedsManager.IncubatedListener;
//import com.umeng.ui.BaseSinglePaneActivity;
//
//import java.util.Map;
//
///**
// * <b>注意：</b> SDK 集成方式中涉及两个和后台表表及数据关联的关键码“appkey”和 "slotid" </br>
// * <p>
// * <b>APPKEY:</b> </br> APPKEY用于交换网路的用户，用户可以申请多个APPKEY进行自主广告分组管理。 </br>
// * Appkey的添加方式有三种，按优先级顺序排列： </br> </br> 1.exchangeDataService.appkey = "xxxxxxx"
// * 该种方式可以在一个App中设置多个Appkey 优先级：高 </br> 2.ExchangeConstants.APPKEY = "xxxxx"
// * 该种方式可以在App中指定一个Appkey，用于和其他SDK做区分 优先级：中 </br> 3.Manifest 文件中配置
// * 该种方式可以在App中指定一个Appkey 优先级低 </br> </br> 如果在使用 new ExchagneDataService() 或 new
// * ExchangeDataService("") 的方式创建，数据将与appkey做关联 </br> </br> <b>SLOT ID</b> </br>
// * SLOTID用于UFP（Umeng For Publish）用户。 </br> Slot id 的集成方式只有一种，new
// * ExchangeDataService("slot_id"); </br> </br> <b>如果用户同时集成了slotid 和
// * appkey,SDK将优先采用slot_id作为关联码.</b> </br>
// * </p>
// * 
// * @author Jhen
// */
//public class XpHome extends BaseSinglePaneActivity {
//    public static ExchangeDataService preloadDataService;
//
//    static {// 设置report 回调
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
//    }
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected Fragment onCreatePane() {
//        // ExchangeConstants.banner_alpha = 120;
//        ExchangeConstants.full_screen = false;
//        ExchangeConstants.ONLY_CHINESE = false;
//        ExchangeConstants.handler_auto_expand = true;
//        ExchangeConstants.DEBUG_MODE = true;
//        ExchangeConstants.handler_left = true;
//        ExchangeConstants.RICH_NOTIFICATION = true;
//        Log.LOG = true;
//
//        return new XpHomeFragment();
//    }
//
//    static FeedsManager feedsManager;
//
//    /**
//     * Do not change this to anonymous class as it will crash when orientation
//     * changes.
//     * 
//     * @author lucas
//     */
//    public static class XpHomeFragment extends Fragment {
//    	
//        public XpHomeFragment() {
//        }
//        
//        
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            final View root = inflater.inflate(R.layout.umeng_example_xp_home,
//                    container, false);
//
//            feedsManager = new FeedsManager(getActivity());
//            String slot = "53947";
//            feedsManager.addMaterial(slot, slot);
//            slot = "53946";
//            feedsManager.addMaterial(slot, slot);
//            feedsManager.incubate(); // 开始孵化feed广告
//            feedsManager.setIncubatedListener(new IncubatedListener() {
//                @Override
//                public void onComplete(int status, Feed feed, Object tag) {
//                    // 孵化过程回调
//                    FragmentActivity activity = getActivity();
//                    if (activity != null) {
//                        Toast.makeText(getActivity(), "初始化feed " + tag, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
////            // set container preload data
////            preloadDataService = new ExchangeDataService("43914");
////            preloadDataService.preloadData(getActivity(), new NTipsChangedListener() {
////                @Override
////                public void onChanged(int flag) {
////                    TextView view = (TextView) root
////                            .findViewById(R.id.umeng_example_xp_container_tips);
////                    if (flag == -1) {// 没有新广告
////                        view.setVisibility(View.INVISIBLE);
////                    } else if (flag > 1) {// 第一页新广告数量
////                        view.setVisibility(View.VISIBLE);
////                        view.setBackgroundResource(R.drawable.umeng_example_xp_new_tip_bg);
////                        view.setText("" + flag);
////                    } else if (flag == 0) {// 第一页全部都是新广告
////                        view.setVisibility(View.VISIBLE);
////                        view.setBackgroundResource(R.drawable.umeng_example_xp_new_tip);
////                    }
////                };
////            }, ExchangeConstants.type_container);
//
//            root.findViewById(R.id.umeng_example_xp_home_btn_container)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getActivity(),
//                                    ContainerExample.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_banner)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getActivity(),
//                                    BannerExample.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_handler)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getActivity(),
//                                    HandlerExample.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_credit)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getActivity(),
//                                    CreditWallExample.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_wap)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // TODO: implement this example.
//                            startActivity(new Intent(getActivity(),
//                                    WapExample.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_tab)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getActivity(),
//                                    TabExample.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_textlink)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getActivity(),
//                                    HyperlinkTextExample.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_container_with_header)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getActivity(),
//                                    ContainerHeaderExample.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_push_ad)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getActivity(),
//                                    PushActivity.class));
//                        }
//                    });
//
//            root.findViewById(R.id.umeng_example_xp_home_btn_custom)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // TODO: implement Text Link example.
//                            startActivity(new Intent(getActivity(),
//                                    CustomActivity.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_welcome)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // TODO: implement Text Link example.
//                            startActivity(new Intent(getActivity(),
//                                    WelcomeActivity.class));
//                        }
//                    });
//            root.findViewById(R.id.umeng_example_xp_home_btn_feeds)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // TODO: implement Text Link example.
//                            startActivity(new Intent(getActivity(),
//                                    FeedsExample.class));
//                        }
//                    });
//            return root;
//        }
//    }
//}
