//package com.taobao.example.xp.fragments;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.taobao.example.xp.HomeActivity;
//import com.taobao.example.xp.config.Configuration;
//import com.taobao.munion.demo.R;
//import com.taobao.newxp.common.Log;
//import com.taobao.newxp.Promoter;
//import com.taobao.newxp.common.ExchangeConstants;
//import com.taobao.newxp.controller.ExchangeDataService;
//import com.taobao.newxp.controller.XpListenersCenter.AdapterListener;
//import com.taobao.newxp.controller.XpListenersCenter.FitType;
//import com.taobao.newxp.view.ExchangeViewManager;
//
//public class ContainerExampleFragment extends AnalyticFragment {
//    Context mContext;
//
//    public ContainerExampleFragment() {
//
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mContext = activity;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        Log.LOG = true;
//        View root = inflater.inflate(
//                R.layout.taobao_example_xp_container_activity, container,
//                false);
//
//        ExchangeConstants.CONTAINER_AUTOEXPANDED = false;
//
//        final ViewGroup fatherLayout = (ViewGroup) root.findViewById(R.id.ad);
//        final ListView listView = (ListView) root.findViewById(R.id.list);
//
//        ExchangeDataService exchangeDataService = HomeActivity.preloadDataService != null ? HomeActivity.preloadDataService
//                : new ExchangeDataService(Configuration.SLOT_CONTAINER);
//        final ExchangeViewManager exchangeViewManager = new ExchangeViewManager(mContext,
//                exchangeDataService);
//
//        AdapterListener listener = new AdapterListener() {
//            @Override
//            public void onFitType(View itemview, Promoter promoter, FitType fitType) {
//                Button button = (Button) itemview.findViewById(R.id.taobao_xp_ad_action_btn);
//                TextView tv = (TextView) itemview.findViewById(R.id.taobao_xp_name);
//                tv.setTextColor(Color.WHITE);
//                TextView des = (TextView) itemview.findViewById(R.id.taobao_xp_des);
//                des.setTextColor(Color.WHITE);
//                button.setBackgroundResource(R.drawable.arrow_right2);
//                switch (fitType) {
//                    case BROWSE:
//                        button.setText("");
//                        break;
//                    case OPEN:
//                        button.setText("");
//                        break;
//                    case PHONE:
//                        button.setText("");
//                        break;
//                    case DOWNLOAD:
//                        button.setText("");
//                        break;
//                    case NEW:
//                        button.setText("");
//                        break;
//                }
//            }
//        };
//
//        exchangeViewManager.addView(fatherLayout, listView, listener);
//
//        return root;
//    }
//
//    @Override
//    public String getPageName() {
//        return "内嵌样式";
//    }
//}
