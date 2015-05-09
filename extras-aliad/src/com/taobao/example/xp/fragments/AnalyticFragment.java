package com.taobao.example.xp.fragments;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

public abstract class AnalyticFragment extends Fragment {
    public abstract String getPageName();
    
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageName());
    }
    
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageName());
    }

}
