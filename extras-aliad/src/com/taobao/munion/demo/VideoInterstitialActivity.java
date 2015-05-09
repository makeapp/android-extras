package com.taobao.munion.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.taobao.munion.controller.interstitial.AdBaseInfo;
import com.taobao.munion.view.interstitial.MunionInterstitialView;
import com.taobao.munion.view.interstitial.MunionInterstitialView.InterstitialState;
import com.taobao.munion.view.interstitial.MunionInterstitialView.OnStateChangeCallBackListener;

public class VideoInterstitialActivity extends Activity {
	private MunionInterstitialView videoInterstitialView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_interstitial);

		videoInterstitialView = (MunionInterstitialView) findViewById(R.id.videoInterstitialView);
		Button videoInterstitialButton = (Button) findViewById(R.id.videoInterstitialLoad);
		videoInterstitialButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AdBaseInfo adInfo = AdBaseInfo.getInsVideoInstance("59740",
				// AdBaseInfo adInfo = AdBaseInfo.getInsVideoInstance("59338",
						"爸爸去哪儿", "100", "综艺节目");
				videoInterstitialView.load(adInfo);
				// videoInterstitialView.load("56577");
				// videoInterstitialView.setMunionId("56577");
			}
		});

//		Button videoInterstitialShowButton = (Button) findViewById(R.id.videoInterstitialShow);
//		videoInterstitialShowButton
//				.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
//						videoInterstitialView.show();
//					}
//				});

		Button videoInterstitialCloseButton = (Button) findViewById(R.id.videoInterstitialClose);
		videoInterstitialCloseButton
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						videoInterstitialView.close();
					}
				});

		videoInterstitialView
				.setOnStateChangeCallBackListener(new OnStateChangeCallBackListener() {

					@Override
					public void onStateChanged(InterstitialState state) {
						switch (state) {
						case CLOSE:
							Toast toast = Toast.makeText(
									VideoInterstitialActivity.this, "广告已经关闭",
									Toast.LENGTH_LONG);
							toast.show();
							break;
						case READY:
							Toast toastR = Toast.makeText(
									VideoInterstitialActivity.this,
									"广告已经加载成功", Toast.LENGTH_LONG);
							toastR.show();
							break;
						case ERROR:
							Toast toastE = Toast.makeText(
									VideoInterstitialActivity.this, "广告加载失败",
									Toast.LENGTH_LONG);
							toastE.show();
							break;
						default:
							break;
						}
					}
				});

	}

	protected void onResume() {
		super.onResume();
//		if (videoInterstitialView != null) {
//			videoInterstitialView.show();
//		}
	}

	protected void onPause() {
		super.onPause();
		if (videoInterstitialView != null) {
			videoInterstitialView.close();
		}
	}

}
