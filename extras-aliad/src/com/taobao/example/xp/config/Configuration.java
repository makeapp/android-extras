
package com.taobao.example.xp.config;

import static com.taobao.example.xp.model.Feature.*;
import android.app.Activity;

import com.taobao.example.xp.model.Feature;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    public static List<Feature> getFeatures(Activity activity) {
        List<Feature> l = new ArrayList<Feature>();
        l.add(getHandlerFeature(activity));
//        l.add(getFeedsFeature(activity));
//        l.add(getContainerFeature(activity));
        l.add(getBannerFeature(activity));
//        l.add(getLoopImageFeature(activity));
        l.add(getPushFeature(activity));
//        l.add(getWelcomeFeature(activity));
//        l.add(getTextFeature(activity));

        return l;
    }

    public static String SLOT_TAOBAO_WALL = "60338";
    public static String SLOT_APP_WALL = "54351";
    public static String SLOT_TUAN_WALL = "54351";
//    public static String SLOT_FEED_IMG_ITEM = "54353";
    public static String SLOT_FEED_IMG_ITEM = "60466";
    public static String SLOT_FEED_SMALLIMG_APP = "60487";
    public static String SLOT_FEED_SMALLIMG_ITEM = "60488";
    public static String SLOT_FEED_ICON_ITEM = "54354";
    public static String SLOT_FEED_ICON_APP = "55280";
    public static String SLOT_CONTAINER = "54357";
    public static String SLOT_CONTAINER_TAB = "54356";
    public static String SLOT_BANNER_TAOBAO = "54358";
    public static String SLOT_BANNER_APP = "54358";
    public static String SLOT_BANNER_TAPANDAPP = "54360";
    public static String SLOT_LOOP_IMAGE = "54361";
    public static String SLOT_PUSH_IMAGE = "54364";
    public static String SLOT_WELCOME = "54366";
    public static String SLOT_TEXT_LINK = "54367";
    public static String SLOT_CREDIT_WALL = "54368";
    
    //线上环境appkey
    public static String TAOBAO_WALL_APP_KEY = "21737689";
    public static String TAOBAO_WALL_APP_SECRET = "1c50c394d33b4cbbc7f5f383031867f8";
    public static String TAOBAO_WALL_APP_SIGN = "758665872";

    //测试环境appkey
//    public static String TAOBAO_WALL_APP_KEY = "684645";
//    public static String TAOBAO_WALL_APP_SECRET = "df0ce832ca2b5c37f6174d71aeaae926";
//    public static String TAOBAO_WALL_APP_SIGN = "758665872";
    
}
