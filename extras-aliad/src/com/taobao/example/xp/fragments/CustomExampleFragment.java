//package com.taobao.example.xp.fragments;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.taobao.example.xp.utils.ImageLoadTask;
//import Promoter;
//import ExchangeConstants;
//import ExchangeDataService;
//import XpListenersCenter.ExchangeDataRequestListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CustomExampleFragment extends AnalyticFragment{
//    Context mContext;
//    ExchangeDataService service;
//    ExchangeDataRequestListener listener;
//
//    public CustomExampleFragment() {
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
//        final View root = inflater.inflate(
//                R.layout.umeng_example_xp_custom_promoter_wall, container,
//                false);
//        service = new ExchangeDataService("40459") {
//            @Override
//            public List<String> onUpload() {
//                PackageManager pm = mContext.getPackageManager();
//                List<PackageInfo> packages = pm
//                        .getInstalledPackages(PackageManager.GET_ACTIVITIES);
//                List<String> pkgs = new ArrayList<String>();
//                for (PackageInfo pkg : packages) {
//                    if (!isSystemPackage(pkg))
//                        pkgs.add(pkg.packageName);
//                }
//
//                Log.d("UMUpload", "onUpload....");
//                return pkgs;
//            }
//        };
//        // 添加feature 类型 （必须）
//        // 请根据相应广告位的选择合适的类型
//        // 横幅：ExchangeConstants.type_standalone_handler
//        // 自定义入口：ExchangeConstants.type_list_curtain
//        // 轮播大图:ExchangeConstants.type_large_image
//        // 内嵌入口:ExchangeConstants.type_container
//        service.layoutType = ExchangeConstants.type_large_image;
//
//        // 异步请求数据
//        listener = new ExchangeDataRequestListener() {
//
//            @Override
//            public void dataReceived(int status, List<Promoter> data) {
//                if (status == 1 && data != null) {// 成功获取数据
//                    // 将数据封装到自定义的View上。
//                    packagePromoter(data, root, service);
//                }
//            }
//        };
//        service.requestDataAsyn(mContext, listener, true);
//
//        return root;
//    }
//
//    public void paging() {
//        service.requestDataAsyn(mContext, listener, false);
//    }
//
//    protected void packagePromoter(List<Promoter> data, View root,
//            final ExchangeDataService service) {
//        for (int i = 0; i < data.size() && i < 3; i++) {
//            final Promoter promoter = data.get(i);
//
//            // ××××××× 选择合适的View START×××××××
//            ViewGroup parent = null;
//            switch (i) {
//                case 0:
//                    parent = (ViewGroup) root.findViewById(R.id.promoter_main);
//                    break;
//                case 1:
//                    parent = (ViewGroup) root.findViewById(R.id.promoter_left);
//                    break;
//                case 2:
//                    parent = (ViewGroup) root.findViewById(R.id.promoter_right);
//                    break;
//            }
//            // ××××××× 选择合适的View END×××××××
//
//            final ImageView imv = (ImageView) parent.findViewById(R.id.imagev);
//            TextView adTv = (TextView) parent.findViewById(R.id.adword);
//            TextView titleTv = (TextView) parent.findViewById(R.id.title);
//
//            // 设置标题
//            if (titleTv != null)
//                titleTv.setText(promoter.title);
//
//            // 加载图片
//            // 一般广告使用icon字段作为图标url 大图广告是用img字段作为图片url 过滤大图广告可使用img字段过滤
//            String ic = TextUtils.isEmpty(promoter.img) ? promoter.icon : promoter.img;
//            if (!TextUtils.isEmpty(ic))
//                new ImageLoadTask(ic) {
//                    @Override
//                    public void onRecived(Drawable result) {
//                        if (result != null)
//                            imv.setImageDrawable(result);
//                    }
//                }.execute();
//
//            // 设置广告语
//            adTv.setText(promoter.ad_words);
//
//            // 发送展示report（必须）
//            service.reportImpression(promoter);
//
//            // 添加广告点击事件（必须）
//            root.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    service.clickOnPromoter((Activity)mContext,promoter);
//                }
//            });
//        }
//    }
//    
//
//
//    /**
//     * Return whether the given PackgeInfo represents a system package or not.
//     * User-installed packages (Market or otherwise) should not be denoted as
//     * system packages.
//     * 
//     * @param pkgInfo
//     * @return
//     */
//    private static boolean isSystemPackage(PackageInfo pkgInfo) {
//        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
//                : false;
//    }
//
//    @Override
//    public String getPageName() {
//        return "自定义样式";
//    }
//}
