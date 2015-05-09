//
//package com.taobao.example.xp.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
//import com.taobao.example.xp.config.Configuration;
//import com.taobao.munion.demo.R;
//import com.taobao.newxp.Promoter;
//import com.taobao.newxp.common.ExchangeConstants;
//import com.taobao.newxp.controller.ExchangeDataService;
//import com.taobao.newxp.controller.XpListenersCenter.onPageChangedListener;
//import com.taobao.newxp.view.ExchangeViewManager;
//import com.taobao.newxp.view.largeimage.LargeGalleryConfig;
//
//public class LoopImageFragment extends AnalyticFragment {
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        String gallerySlot = Configuration.SLOT_LOOP_IMAGE;
//        View root = inflater.inflate(
//                R.layout.taobao_example_loop_image, container,
//                false);
//        FrameLayout parentVp = (FrameLayout) root.findViewById(R.id.loop);
//
//        ExchangeDataService service = new ExchangeDataService(gallerySlot);
//        ExchangeViewManager viewMgr = new ExchangeViewManager(getActivity(), service);
//
//        LargeGalleryConfig largeGalleryConfig = new LargeGalleryConfig();
//        largeGalleryConfig.setPageChangedListener(new onPageChangedListener() {
//
//            @Override
//            public void onPageChanged(int page, Promoter promoter, View parent) {// parent
//                                                                                 // 是
//                                                                                 // umeng_xp_large_gallery.xml
//                TextView tv = (TextView) parent
//                        .findViewById(R.id.taobao_xp_gallery_title);
//                tv.setText(promoter.title);
//            }
//        });
//
//        viewMgr.setFeatureConfig(largeGalleryConfig);
//        viewMgr.addView(parentVp, ExchangeConstants.type_large_image);
//
//        return root;
//    }
//
//    @Override
//    public String getPageName() {
//        return "轮播图";
//    }
//}
