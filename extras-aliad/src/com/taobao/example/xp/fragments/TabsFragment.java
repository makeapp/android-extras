package com.taobao.example.xp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

import com.taobao.example.xp.config.Configuration;
import com.taobao.munion.demo.R;
import com.taobao.newxp.common.ExchangeConstants;
import com.taobao.newxp.controller.ExchangeDataService;
import com.taobao.newxp.view.ExchangeViewManager;

public class TabsFragment extends AnalyticFragment implements OnTabChangeListener{
    public static final String TAB_1 = "装机必备";
    public static final String TAB_2 = "下载排行";

    private View mRoot;
    private TabHost mTabHost;
    private int mCurrentTab;
    private TabWidget mTabWidget;
    private Context mContext;
    View tab1,tab2;
    int colorSelected,colorNormal;

    public TabsFragment() {
        
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        colorSelected = getResources().getColor(R.color.tab_text_selected);
        colorNormal = getResources().getColor(R.color.tab_text_nomal);
        mRoot = inflater.inflate(R.layout.taobao_example_xp_tabfragment, container, false);
        mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
        mTabWidget = (TabWidget) mRoot.findViewById(android.R.id.tabs);
        setupTabs();
        
        mRoot.findViewById(R.id.actionbar_home_logo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return mRoot;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        mTabHost.setOnTabChangedListener(this);
        mTabHost.setCurrentTab(mCurrentTab);

        ListView l1 = (ListView) mRoot.findViewById(R.id.list_1);
        ListView l2 = (ListView) mRoot.findViewById(R.id.list_2);

        ViewGroup vg1 = (ViewGroup) mRoot.findViewById(R.id.father1);
        ViewGroup vg2 = (ViewGroup) mRoot.findViewById(R.id.father2);
        
        ExchangeConstants.CONTAINER_AUTOEXPANDED = true;

        ExchangeDataService exchangeDataService1 = new ExchangeDataService(Configuration.SLOT_CONTAINER_TAB);

        new ExchangeViewManager(mContext, exchangeDataService1).addView(vg1, l1);
        ExchangeDataService exchangeDataService2 = new ExchangeDataService(Configuration.SLOT_CONTAINER);

        exchangeDataService2.show_progress_wheel = false;
        new ExchangeViewManager(mContext, exchangeDataService2).addView(vg2, l2);
    }

    private void setupTabs() {
        mTabHost.setup(); // must call this before adding tabs!

        tab1 = LayoutInflater.from(mContext).inflate(
                R.layout.taobao_example_tab_indicator, null);
        TextView tv1 = (TextView) tab1.findViewById(R.id.taobao_example_tab_text);
        tv1.setText(TAB_1);
        tv1.setTextColor(colorSelected);
        tab2 = LayoutInflater.from(mContext).inflate(
                R.layout.taobao_example_tab_indicator, null);
        TextView tv2 = (TextView) tab2.findViewById(R.id.taobao_example_tab_text);
        tv2.setText(TAB_2);
        tv2.setTextColor(colorNormal);
        
        tab1.setBackgroundResource(R.drawable.left_tab_btn);
        tab2.setBackgroundResource(R.drawable.right_tab_btn);
        
        mTabHost.addTab(mTabHost.newTabSpec(TAB_1).setIndicator(tab1)
                .setContent(R.id.list_1));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_2).setIndicator(tab2)
                .setContent(R.id.list_2));

    }

    @Override
    public void onTabChanged(String tabId) {
        TextView tv1 = (TextView) tab1.findViewById(R.id.taobao_example_tab_text);
        TextView tv2 = (TextView) tab2.findViewById(R.id.taobao_example_tab_text);
        if (TAB_1.equals(tabId)) {
            tv1.setTextColor(colorSelected);
            tv2.setTextColor(colorNormal);
            return;
        }
        if (TAB_2.equals(tabId)) {
            tv2.setTextColor(colorSelected);
            tv1.setTextColor(colorNormal);
            return;
        }
    }

    @Override
    public String getPageName() {
        return "内嵌多tab";
    }
}
