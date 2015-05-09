//package com.taobao.example.xp.fragments;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AbsListView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.taobao.example.xp.R;
//import com.taobao.example.xp.config.Configuration;
//import Promoter;
//import ExchangeConstants;
//import ExchangeDataService;
//import XpListenersCenter.AdapterListener;
//import XpListenersCenter.FitType;
//import XpListenersCenter.InitializeListener;
//import XpListenersCenter.onPageChangedListener;
//import ExchangeViewManager;
//import LargeGalleryConfig;
//
//public class ContainerAndLoopImageFragment extends AnalyticFragment{
//    Context mContext;
//
//    public ContainerAndLoopImageFragment() {
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
//        String containerSlot = "";
//        String gallerySlot = Configuration.SLOT_LOOP_IMAGE;
//        View root = inflater.inflate(
//                R.layout.umeng_example_xp_container_full, container,
//                false);
//
//        ExchangeConstants.CONTAINER_AUTOEXPANDED = true;
//
//        ViewGroup fatherLayout = (ViewGroup) root.findViewById(R.id.rootId);
//        final ListView listView = (ListView) root.findViewById(R.id.list);
//
//        ExchangeDataService containerService = new ExchangeDataService(containerSlot);
//        // add largeGallery header......
//        final RelativeLayout headerRoot = new RelativeLayout(mContext);
//        float scale = mContext.getResources().getDisplayMetrics().density;
//        int height = (int) (180 * scale + 0.5f);
//        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
//                LayoutParams.FILL_PARENT, height);
//        headerRoot.setLayoutParams(layoutParams);
//        final ExchangeDataService service = new ExchangeDataService(gallerySlot);
//
//        containerService.initializeListener = new InitializeListener() {
//            @Override
//            public void onStartRequestData(int type) {
//
//            }
//
//            @Override
//            public void onReceived(int count) {
//                ExchangeViewManager viewMgr = new ExchangeViewManager(mContext, service);
//
//                LargeGalleryConfig largeGalleryConfig = new LargeGalleryConfig();
//                largeGalleryConfig.setPageChangedListener(new onPageChangedListener() {
//
//                    @Override
//                    public void onPageChanged(int page, Promoter promoter, View parent) {// parent
//                                                                                         // 是
//                                                                                         // umeng_xp_large_gallery.xml
//                        TextView tv = (TextView) parent
//                                .findViewById(R.id.umeng_xp_gallery_title);
//                        tv.setText(promoter.title);
//                    }
//                });
//
//                viewMgr.setFeatureConfig(largeGalleryConfig);
//                viewMgr.addView(headerRoot, ExchangeConstants.type_large_image);
//
//                listView.addHeaderView(headerRoot);
//            }
//        };
//
//        ExchangeViewManager exchangeViewManager = new ExchangeViewManager(mContext,
//                containerService);
//
//        AdapterListener adapterListener = new AdapterListener() {
//            @Override
//            public void onFitType(View itemview, Promoter promoter, FitType fitType) {
//            }
//        };
//
//        exchangeViewManager.addView(fatherLayout, listView, adapterListener);
//
//        return root;
//    }
//
//    @Override
//    public String getPageName() {
//        return "内嵌轮播图组合";
//    }
//}
