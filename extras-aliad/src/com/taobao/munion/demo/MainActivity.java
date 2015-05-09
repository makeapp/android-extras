package com.taobao.munion.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    static String BANNER_ID = "59335";
//    static String INSET_ID = "59675";
    static String INSET_ID = "59338";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,
                        BannerActivity.class);
                startActivity(intent);

            }
        });


        findViewById(R.id.interstitial).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,
                                InterstitialActivity.class);
                        startActivity(intent);

                    }
                }
        );

        findViewById(R.id.videoInterstitial).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,
                                VideoInterstitialActivity.class);
                        startActivity(intent);

                    }
                }
        );


//        findViewById(R.id.effective).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText hostEt = (EditText) findViewById(R.id.host);
//                EditText bannerEt = (EditText) findViewById(R.id.bannerid);
//                EditText insetEt = (EditText) findViewById(R.id.interstitialId);
//
//                String eHost = hostEt.getText().toString().trim();
//                String eBanner = bannerEt.getText().toString().trim();
//                String eInset = insetEt.getText().toString().trim();
//
//                String host = TextUtils.isEmpty(eHost) ? Munion.HOST : eHost;
//                String bannerId = TextUtils.isEmpty(eBanner) ? BANNER_ID : eBanner;
//                String inset = TextUtils.isEmpty(eInset) ? INSET_ID : eInset;
//
//                Munion.HOST = host;
//                BANNER_ID = bannerId;
//                INSET_ID = inset;
//
//                ((TextView) findViewById(R.id.config_info)).setText("当前配置：\nhost:" + Munion.HOST + "\nbanner id:" + BANNER_ID + "\ninnset id:" + INSET_ID);
//            }
//        });
//
//        ((TextView) findViewById(R.id.config_info)).setText("当前配置：\nhost:" + Munion.HOST + "\nbanner id:" + BANNER_ID + "\ninnset id:" + INSET_ID);
    }

}
