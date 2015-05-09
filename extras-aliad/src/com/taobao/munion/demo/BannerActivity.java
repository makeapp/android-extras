package com.taobao.munion.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.taobao.munion.base.Log;
import com.taobao.munion.view.banner.MunionBannerView;

/**
 * Created by jhenxu on 14-5-14.
 */
public class BannerActivity extends Activity {


    MunionBannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_example);

        bannerView = (MunionBannerView) findViewById(R.id.bannerView);
//        bannerView.setMunionId("58320");
        bannerView.setMunionId(MainActivity.BANNER_ID);//设置广告位ID

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerView.load();//重新加载Banner广告
            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerView.close();
            }
        });

        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bannerView.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if(bannerView != null){
//            bannerView.load();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(bannerView != null)
//            bannerView.close();
    }

    @Override
    public void onBackPressed() {
        boolean interrupt = false;
        if (bannerView != null) {//通知Banner推广返回键按下，如果Banner进行了一些UI切换将返回true
                                // 否则返回false(如从 expand状态切换会normal状态将返回true)
            interrupt = bannerView.onBackPressed();
        }

        if (!interrupt)
            super.onBackPressed();
    }
}
