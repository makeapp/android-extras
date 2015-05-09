package com.taobao.example.xp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taobao.example.xp.config.Configuration;
import com.taobao.munion.demo.R;
import com.taobao.newxp.common.ExchangeConstants;
import com.taobao.newxp.controller.ExchangeDataService;
import com.taobao.newxp.view.ExchangeViewManager;

public class TextLinkExampleFragment extends AnalyticFragment {
    Context mContext;

    public TextLinkExampleFragment() {
        
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(
                R.layout.taobao_example_xp_hyperlinktext_activity, container,
                false);

        ExchangeViewManager em = new ExchangeViewManager(mContext, new ExchangeDataService(
                Configuration.SLOT_TEXT_LINK));
        em.addView((ViewGroup) root.findViewById(R.id.link_root),
                ExchangeConstants.type_hypertextlink_banner);

        return root;
    }
    @Override
    public String getPageName() {
        return "文字链";
    }

}
