
package com.taobao.example.xp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.taobao.example.xp.fragments.IOrientationObserver;
import com.taobao.ui.BaseSinglePaneActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContainerActivity extends BaseSinglePaneActivity {
    public static final int FLAG_NO_ACTIONBAR = 1 << 1;
    public static final int FLAG_FULL_SCREEN = 1 << 2;
    public static final int FLAG_OBSERVER_CONFIGURATIONCHANGED = 1 << 3;
    String clzFragment;
    Fragment mFragment;
    
    boolean obConfigurationchanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        clzFragment = getIntent().getStringExtra("fragment");
        super.onCreate(savedInstanceState);
        int[] f = getIntent().getIntArrayExtra("flags");
        
        Set<Integer> flags = new HashSet<Integer>();
        for (int i : f) {
            flags.add(i);
        }

        if (flags.contains(FLAG_NO_ACTIONBAR)) {
            mActionBar.setVisibility(View.GONE);
        }else if(flags.contains(FLAG_FULL_SCREEN)){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else if(flags.contains(FLAG_OBSERVER_CONFIGURATIONCHANGED)){
            obConfigurationchanged = true;
        }

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected Fragment onCreatePane() {
        try {
            Class<?> clz = Class.forName(clzFragment);
            mFragment = (Fragment) clz.newInstance();
            return mFragment;
        } catch (Exception e) {
            throw new IllegalArgumentException("无法初始化fragment",e);
        }
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (obConfigurationchanged && mFragment != null && mFragment instanceof IOrientationObserver){
            ((IOrientationObserver)(mFragment)).onOrientationChanaged(newConfig.orientation);
        }
    }
    
    public static class IntentGenerator{
        Intent i;
        List<Integer> flags = new ArrayList<Integer>();
        
        public IntentGenerator(Context c,String fragment){
            i = new Intent(c, ContainerActivity.class);
            i.putExtra("fragment", fragment);
        }
        
        public IntentGenerator addFlag(int flag){
            flags.add(flag);
            return this;
        }
        
        public Intent build(){
            int[] fs = new int[flags.size()];
            int p=0;
            for(Integer flag:flags){
                fs[p++] = flag;
            }
            i.putExtra("flags", fs);
            
            return i;
        }
    }

}
