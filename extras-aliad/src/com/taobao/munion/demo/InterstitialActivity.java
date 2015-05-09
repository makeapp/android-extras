package com.taobao.munion.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.taobao.munion.view.interstitial.MunionInterstitialView;
import com.taobao.munion.view.interstitial.MunionInterstitialView.InterstitialState;
import com.taobao.munion.view.interstitial.MunionInterstitialView.OnStateChangeCallBackListener;

public class InterstitialActivity extends Activity {
	
	private MunionInterstitialView interstitialView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interstitial);

		interstitialView = (MunionInterstitialView) findViewById(R.id.interstitialView);
		Button interstitialButton = (Button) findViewById(R.id.interstitialLoad);
		interstitialButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				interstitialView.load(adInfo);
				interstitialView.load(MainActivity.INSET_ID);
//				interstitialView.load("59213");
			}
		});

//		Button interstitialShowButton = (Button) findViewById(R.id.interstitialShow);
//		interstitialShowButton
//				.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						interstitialView.show();
//					}
//				});

		Button interstitialCloseButton = (Button) findViewById(R.id.interstitialClose);
		interstitialCloseButton
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						interstitialView.close();
					}
				});

		interstitialView
				.setOnStateChangeCallBackListener(new OnStateChangeCallBackListener() {

					@Override
					public void onStateChanged(InterstitialState state) {
						switch (state) {
						case CLOSE:
							Toast toast = Toast.makeText(
									InterstitialActivity.this, "广告已经关闭",
									Toast.LENGTH_LONG);
							toast.show();
							Log.i("munion","ad colse");
							break;
						case READY:
							Toast toastR = Toast.makeText(
									InterstitialActivity.this, "广告已经加载成功",
									Toast.LENGTH_LONG);
							toastR.show();
							Log.i("munion","ad ready");
							break;
						default:
							break;
						}
					}
				});

	}
	
	protected void onResume(){
		super.onResume();
//		if(interstitialView != null){
//			interstitialView.show();
//		}
	}
	
	protected void onPause(){
		super.onPause();
		if(interstitialView != null){
			interstitialView.close();
		}
	}

}
