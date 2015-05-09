//package com.taobao.example.xp.fragments;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.taobao.example.xp.R;
//import ExchangeConstants;
//import ExchangeDataService;
//import ExchangeViewManager;
//
//public class WapExampleFragment extends AnalyticFragment {
//    Context mContext;
//
//    public WapExampleFragment() {
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
//        View root = inflater.inflate(
//                R.layout.umeng_example_xp_wap, container,
//                false);
//
//        // 本地图片入口方式集成
//        ImageView imageview1 = (ImageView) root.findViewById(R.id.imageview1);
//        new ExchangeViewManager(mContext, new ExchangeDataService())
//                .addView(ExchangeConstants.type_wap_style, imageview1, mContext.getResources()
//                        .getDrawable(R.drawable.umeng_example_handler));
//
//        // 云端图片入口方式集成,需在网页端配置入口图片
//        ImageView imageview2 = (ImageView) root.findViewById(R.id.imageview2);
//        new ExchangeViewManager(mContext, new ExchangeDataService("40174"))
//                .addView(ExchangeConstants.type_wap_style, imageview2);
//
//        // 直接打开
//        ImageView imageview3 = (ImageView) root.findViewById(R.id.imageview3);
//        imageview3.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                new ExchangeViewManager(mContext, new ExchangeDataService())
//                        .addView(ExchangeConstants.type_wap_style, null);
//
//            }
//        });
//
//        return root;
//    }
//
//    @Override
//    public String getPageName() {
//        return "Wap样式";
//    }
//}
