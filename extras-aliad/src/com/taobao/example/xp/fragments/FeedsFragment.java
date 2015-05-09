//package com.taobao.example.xp.fragments;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.taobao.example.xp.HomeActivity;
//import com.taobao.example.xp.config.Configuration;
//import com.taobao.munion.demo.R;
//import com.taobao.newxp.view.feed.Feed;
//import com.taobao.newxp.view.feed.FeedView;
//import com.taobao.newxp.view.feed.FeedViewFactory;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Queue;
//import java.util.Stack;
//
//public class FeedsFragment extends AnalyticFragment {
//    Context mContext;
//    static List<Feed> mGlobalFeeds = new ArrayList<Feed>();
//
//    public FeedsFragment() {
//    }
//
//    ;
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mContext = activity;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.taobao_example_xp_feeds, container, false);
//
//        ListView list = (ListView) root.findViewById(R.id.list);
//
//        int[] mOccupied = new int[] {
//                1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1
//        };//1代表后面可插入广告
//
//        //        MainActivity.feedsManager.getProducts(mGlobalFeeds);
//
//        Queue<Feed> feeds = new LinkedList<Feed>();
//        //        feeds.addAll(mGlobalFeeds);
//
//        if (HomeActivity.feedsManager != null) {
//            Feed iconApp = HomeActivity.feedsManager.getProduct(Configuration.SLOT_FEED_ICON_APP);
//            if (iconApp != null) {
//                feeds.add(iconApp);
//            }
//
//            Feed iconItem = HomeActivity.feedsManager.getProduct(Configuration.SLOT_FEED_ICON_ITEM);
//            if (iconItem != null) {
//                feeds.add(iconItem);
//            }
//
//            Feed imgItem = HomeActivity.feedsManager.getProduct(Configuration.SLOT_FEED_IMG_ITEM);
//            if (imgItem != null) {
//                feeds.add(imgItem);
//            }
//
//            Feed smallImgApp =
//                    HomeActivity.feedsManager.getProduct(Configuration.SLOT_FEED_SMALLIMG_APP);
//            if (smallImgApp != null) {
//                feeds.add(smallImgApp);
//            }
//            Feed smallImgItem =
//                    HomeActivity.feedsManager.getProduct(Configuration.SLOT_FEED_SMALLIMG_ITEM);
//            if (smallImgApp != null) {
//                feeds.add(smallImgItem);
//            }
//        }
//
//        //        Feed imgTuan = HomeActivity.feedsManager.getProduct(Configuration.SLOT_FEED_IMG_TUAN);
//        //        if(imgTuan != null){
//        //            feeds.add(imgTuan);
//        //        }
//
//        //再次使用feed必须调用cleanReportFlag，否则将不发送展示报告
//        if(feeds !=null && feeds.size() > 0){
//            for (Feed feed : feeds) {
//                if(feed != null){
//                    feed.cleanReportFlag();
//                }
//            }
//        }
//
//        Toast.makeText(mContext, "get feeds " + feeds.size(), 1).show();
//
//        List<Object> result = new ArrayList<Object>();
//        for (int i : mOccupied) {
//            result.add(i);
//            if (i == 1) {
//                Feed feed = feeds.poll();
//                if (feed != null) result.add(feed);
//            }
//        }
//
//        final ImageCache iCache = new ImageCache();
//        list.setAdapter(new FeedsAdapter(getActivity(), result, iCache));
//
//        //        list.setOnScrollListener(new OnScrollListener() {
//        //            int firstItem,lastItem;
//        //            @Override
//        //            public void onScrollStateChanged(AbsListView view, int scrollState) {
//        //                if(OnScrollListener.SCROLL_STATE_IDLE == scrollState){
//        //                    for(int i=0;i<=lastItem;i++){
//        //                        try {
//        //                        View v = view.getChildAt(i);
//        //                        ImageView iv = (ImageView) v.findViewById(R.id.image);
//        //                        iv.setImageDrawable(null);
//        //                            iv.setImageResource(iCache.iterlator());
//        //                        } catch (Exception e) {
//        //                        }
//        //                    }
//        //                }
//        //            }
//        //
//        //            @Override
//        //            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
//        //                    int totalItemCount) {
//        //                firstItem = firstVisibleItem;
//        //                lastItem = visibleItemCount + firstVisibleItem;
//        //            }
//        //        });
//
//        return root;
//    }
//
//    static class FeedsAdapter extends BaseAdapter {
//        Activity activity;
//        ImageCache iCache;
//        List<Object> mOccupied;
//
//        public FeedsAdapter(Activity activity, List<Object> l, ImageCache iCache) {
//            super();
//            this.activity = activity;
//            this.iCache = iCache;
//            mOccupied = l;
//        }
//
//        @Override
//        public int getCount() {
//            return mOccupied.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            Object object = mOccupied.get(position);
//            if (object instanceof Feed) {
//                Feed feed = (Feed) object;
//                FeedViewFactory.context = activity.getApplicationContext();
//                View feedView = FeedViewFactory.getFeedView(activity, feed);
//                feedView.setBackgroundResource(R.drawable.share_gradient_write);
//                return feedView;
//            } else {
//                View view;
//                if (convertView == null || convertView instanceof FeedView) {
//                    view = View.inflate(activity, R.layout.taobao_example_xp_feeds_item, null);
//                    //((ImageView) view.findViewById(R.id.image)).setImageResource(iCache.iterlator());
//                } else {
//                    view = convertView;
//                    //((ImageView) view.findViewById(R.id.image)).setImageDrawable(null);
//                }
//
//                return view;
//            }
//        }
//    }
//
//    static class ImageCache {
//        Stack<Integer> cacheA = new Stack<Integer>();
//        Stack<Integer> cacheB = new Stack<Integer>();
//        int cusor = 0;// %2==0 ? cacheA : cacheB
//
//        public ImageCache() {
//            cacheA.push(R.drawable.taobao_xp_example_feeds_imga);
//            cacheA.push(R.drawable.taobao_xp_example_feeds_imgb);
//            cacheA.push(R.drawable.taobao_xp_example_feeds_imgc);
//            cacheA.push(R.drawable.taobao_xp_example_feeds_imgd);
//            cacheA.push(R.drawable.taobao_xp_example_feeds_imge);
//            cacheA.push(R.drawable.taobao_xp_example_feeds_imgf);
//            cacheA.push(R.drawable.taobao_xp_example_feeds_imgg);
//            cacheA.push(R.drawable.taobao_xp_example_feeds_imgh);
//            cacheA.push(R.drawable.taobao_xp_example_feeds_imgi);
//            cusor = 0;
//        }
//
//        public synchronized int iterlator() {
//            Stack<Integer> dumpCache = cusor % 2 == 0 ? cacheA : cacheB;
//            Stack<Integer> pushCache = cusor % 2 == 0 ? cacheB : cacheA;
//
//            Integer integer = dumpCache.pop();
//            pushCache.push(integer);
//
//            if (dumpCache.size() == 0) cusor++;
//
//            return integer;
//        }
//    }
//
//    @Override
//    public String getPageName() {
//        return "信息流";
//    }
//}
