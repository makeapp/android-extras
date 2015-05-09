package com.makeapp.android.extras.baidu;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.makeapp.android.extras.FunctionAndroid;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class BaiduFunctionShare
        extends FunctionAndroid<String, Boolean> {

    Bundle metaData;

    private FrontiaSocialShare mSocialShare;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);

        mSocialShare = FrontiaSocialShare.newInstance(FunctionAndroid.context);
        mSocialShare.setContext(FunctionAndroid.context);
        mSocialShare.setClientId(FrontiaAuthorization.MediaType.SINAWEIBO.toString(), "SINA_APP_KEY");
        mSocialShare.setClientId(FrontiaAuthorization.MediaType.QZONE.toString(), "100358052");
        mSocialShare.setClientId(FrontiaAuthorization.MediaType.QQFRIEND.toString(), "100358052");
        mSocialShare.setClientName(FrontiaAuthorization.MediaType.QQFRIEND.toString(), "百度");
        mSocialShare.setClientId(FrontiaAuthorization.MediaType.WEIXIN.toString(), "wx329c742cb69b41b8");
    }

    @Override
    public Boolean apply(String s) {

        String[] params = s.split(" ");
        FrontiaSocialShareContent shareContent = new FrontiaSocialShareContent();

        shareContent.setTitle(params[1]);
        shareContent.setContent(params[2]);
        if (params.length > 3) {
            shareContent.setLinkUrl(params[3]);
        }
        if (params.length > 4) {
            shareContent.setImageUri(Uri.parse(params[4]));
        }

        if ("share".equalsIgnoreCase(params[0])) {
            mSocialShare.share(shareContent, FrontiaAuthorization.MediaType.BATCHSHARE.toString(), new ShareListener(), true);
        } else if ("show".equalsIgnoreCase(params[0])) {
            mSocialShare.show(activity.getWindow().getDecorView(), shareContent, FrontiaSocialShare.FrontiaTheme.DARK, new ShareListener());
        }

        return true;
    }

    private class ShareListener
            implements FrontiaSocialShareListener {

        @Override
        public void onSuccess() {
            Log.d("Test", "share success");
        }

        @Override
        public void onFailure(int errCode, String errMsg) {
            Log.d("Test", "share errCode " + errCode);
        }

        @Override
        public void onCancel() {
            Log.d("Test", "cancel ");
        }
    }
}
