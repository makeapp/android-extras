
package com.taobao.munion.demo;

import com.taobao.munion.view.webview.windvane.mraid.MraidWebView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class MraidWebViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        final String url = "http://adn3.umeng.us/sdk/template/banner?slot_id=57122&sz=320x50&chl=abc&asid=693996527&adnm=%E7%88%B1%E5%A5%87%E8%89%BA%E8%A7%86%E9%A2%91&apnm=com.qiyi.video&apvn=&apvc=2.1.1&br=Pioneer&mac=8c%3A77%3A16%3A83%3Ab0%3A86&dm=E91w&rs=800x600&imei=864687010564887&dpr=1.0&idfa=70DC8F87-AFB6-4CAF-B9CC-C0F24C715718&utdid=Uvj2Ix6eO1wDAJ7U2RvyOV%2FU&nop=%E4%B8%AD%E5%9B%BD%E8%81%94%E9%80%9A&net=cell&netp=&os=iOS&osv=5.1.1&cn=CN&lang=zh-Hant&lc=%E4%B8%AD%E6%96%87+%20%28%E4%B8%AD%E5%9B%BD%29&tz=8&ts=&lat=&ln"
//                +
//                "g=&alt=&pt=&pts=&pac=&sdkv=4.5.1.20131117&opac=0.2&zix=1&seq=2&sid=533ecf28498eb4486b262cec&_rnd=";
        // final String url =
        // "http://10.62.51.4:8080/sdk/template/banner?sdkv=&slot_id=56546&os=android&sz=202x55&net=cell";
//         final String url = "http://10.125.204.27:3000/test/testApi.html";
         final String url =
         "http://10.62.9.172:8080/sdk/template/banner?sdkv=&slot_id=56546&os=android&sz=202x55&net=cell";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mraidwebview);
        final MraidWebView webview = (MraidWebView) findViewById(R.id.webview);
        // findViewById(R.id.btrefresh).setOnClickListener(new OnClickListener()
        // {
        //
        // @Override
        // public void onClick(View v) {
        // webview.loadUrl(url);
        //
        // }
        // });
        // ViewGroup root = (ViewGroup) findViewById(R.id.llroot);
        // MraidWebView webview = new MraidWebView(this);
        // ViewGroup.LayoutParams pa = new ViewGroup.LayoutParams(960,165);
        // webview.loadUrl("http://10.62.50.245:3000/test/adCon.html");
        // root.addView(webview,pa);
        // webview.loadUrl("http://10.125.204.27:3000/test/testApi.html");
        // webview.loadUrl("http://10.62.5.239:8080/sdk/template/banner?sdkv=&slot_id=56546&os=android&sz=202x55&net=cell");
        webview.loadUrl(url);
    }
}
