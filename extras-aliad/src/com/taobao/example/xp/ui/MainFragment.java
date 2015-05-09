package com.taobao.example.xp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.taobao.example.xp.config.Configuration;
import com.taobao.example.xp.model.Action;
import com.taobao.example.xp.model.Feature;
import com.taobao.munion.demo.R;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

import java.util.List;

public class MainFragment extends Fragment {
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.taobao_example_main,
                container, false);

        ActionSlideExpandableListView list = (ActionSlideExpandableListView) root
                .findViewById(R.id.list);

        ListAdapter adapter = buildFeatures();

        list.setAdapter(adapter);
//        ((Feature)list.getAdapter().getItem(0)).action

        return root;
    }

    private ListAdapter buildFeatures() {
        final List<Feature> features = Configuration.getFeatures(getActivity());
//        features.add(getTestFeature());

        ListAdapter adapter = new BaseAdapter() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(getActivity(), R.layout.expandable_list_item, null);
                final Feature f = features.get(position);
                ImageView icon = (ImageView) view.findViewById(R.id.icon);
                TextView tv = (TextView) view.findViewById(R.id.text);
                final View toggle = view.findViewById(R.id.expandable_toggle_button);

                icon.setImageResource(f.icon);
                final LinearLayout ll = (LinearLayout) view.findViewById(R.id.expandable);
                ll.removeAllViews();
                //                    ll.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                //                        @Override
                //                        public void onGlobalLayout() {
                //                          if(View.VISIBLE == ll.getVisibility()){
                //                              toggle.setBackgroundResource(R.drawable.arrow_rigint);
                //                          }else{
                //                              toggle.setBackgroundResource(R.drawable.arrow_rigint);
                //                          }
                //                        }
                //                    });

                tv.setText(f.title);
                if (f.action.length > 1) {
                    toggle.setVisibility(View.VISIBLE);
                    for (Action a : f.action) {
                        final Action mAction = a;
                        Button v = (Button) View.inflate(getActivity(),
                                R.layout.expandable_action_item, null);
                        v.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAction.performAction();
                            }
                        });
                        v.setText(a.title);

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
                        lp.weight = 0.5f;
                        ll.addView(v, lp);
                    }
                } else {
                    toggle.setVisibility(View.GONE);
                }

                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (View.VISIBLE == toggle.getVisibility() && toggle != null) {
                            toggle.performClick();
                        } else {
                            f.action[0].performAction();
                        }
                    }
                });

                return view;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public Object getItem(int position) {
                return features.get(position);
            }

            @Override
            public int getCount() {
                return features.size();
            }
        };

        return adapter;
    }
    
}
