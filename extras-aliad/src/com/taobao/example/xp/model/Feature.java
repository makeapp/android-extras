
package com.taobao.example.xp.model;

import android.app.Activity;
import android.content.Intent;

import static com.taobao.example.xp.config.Configuration.*;
import com.taobao.example.xp.ContainerActivity;
import com.taobao.example.xp.ContainerActivity.IntentGenerator;
import com.taobao.example.xp.WelcomeActivity;
//import com.taobao.example.xp.fragments.ContainerExampleFragment;
//import com.taobao.example.xp.fragments.FeedsFragment;
import com.taobao.example.xp.fragments.HandlerExampleFragment;
//import com.taobao.example.xp.fragments.LoopImageFragment;
import com.taobao.example.xp.fragments.TabsFragment;
import com.taobao.example.xp.fragments.TextLinkExampleFragment;
import com.taobao.munion.demo.BannerActivity;
import com.taobao.munion.demo.InterstitialActivity;
import com.taobao.munion.demo.R;

public class Feature {
    public String title;
    public int icon;
    public Action[] action;

    public Feature(int icon, String title, Action[] action) {
        super();
        this.title = title;
        this.action = action;
        this.icon = icon;
    }

    public static Feature getHandlerFeature(final Activity activity) {
        return new Feature(R.drawable.ic_handler, "自定义入口", new Action[] {
                new Action("电商墙") {
                    @Override
                    public void performAction() {
                        IntentGenerator generator = new IntentGenerator(activity,
                                HandlerExampleFragment.class.getName());
                        generator.addFlag(ContainerActivity.FLAG_NO_ACTIONBAR);

                        Intent intent = generator.build();
                        intent.putExtra("slot_id", SLOT_TAOBAO_WALL);
                        intent.putExtra("feature", 0);

                        activity.startActivity(intent);
                    }
                }, new Action("应用墙") {
            @Override
            public void performAction() {
                IntentGenerator generator =
                        new IntentGenerator(activity, HandlerExampleFragment.class.getName());
                generator.addFlag(ContainerActivity.FLAG_NO_ACTIONBAR);

                Intent intent = generator.build();
                intent.putExtra("slot_id", SLOT_APP_WALL);
                intent.putExtra("feature", 1);

                activity.startActivity(intent);
            }
        }
                // ,new Action("团购墙"){
                // @Override
                // public void performAction() {
                // IntentGenerator generator = new IntentGenerator(activity,
                //HandlerExampleFragment.class.getName());
        // generator.addFlag(ContainerActivity.FLAG_NO_ACTIONBAR);
        //
        // Intent intent = generator.build();
        // intent.putExtra("slot_id", SLOT_TAOBAO_WALL);
        // intent.putExtra("feature", 2);
        //
        // activity.startActivity(intent);
        // }
        // }
        });
    }

    public static Feature getFeedsFeature(final Activity activity) {
        Action a = new Action("信息流") {
            @Override
            public void performAction() {
//                IntentGenerator generator = new IntentGenerator(activity,
//                        FeedsFragment.class.getName());
//                activity.startActivity(generator.build());
            }
        };
        return new Feature(R.drawable.ic_feeds, "信息流", new Action[] {
                a
        });
    }

    public static Feature getContainerFeature(final Activity activity) {
        return new Feature(R.drawable.ic_container, "内嵌", new Action[] {
                new Action("局部内嵌") {
                    @Override
                    public void performAction() {
//                        IntentGenerator generator = new IntentGenerator(activity,
//                                ContainerExampleFragment.class.getName());
//                        generator.addFlag(ContainerActivity.FLAG_NO_ACTIONBAR);
//                        activity.startActivity(generator.build());
                    }
                }
                , new Action("内嵌多tab") {
                    @Override
                    public void performAction() {
                        IntentGenerator generator = new IntentGenerator(activity,
                                TabsFragment.class.getName());
                        generator.addFlag(ContainerActivity.FLAG_NO_ACTIONBAR);
                        activity.startActivity(generator.build());
                    }
                }
        });
    }

    public static Feature getBannerFeature(final Activity activity) {
        Action a = new Action("横幅") {
            @Override
            public void performAction() {
                // IntentGenerator generator = new IntentGenerator(activity,
                // BannerExampleFragment.class.getName());
                // generator.addFlag(ContainerActivity.FLAG_NO_ACTIONBAR);
                // activity.startActivity(generator.build());
                Intent intent = new Intent();
                intent.setClass(activity, BannerActivity.class);
                activity.startActivity(intent);
            }
        };
        return new Feature(R.drawable.ic_banner, "横幅", new Action[] {
                a
        });
    }

    public static Feature getLoopImageFeature(final Activity activity) {
        String text = "轮播大图";
        Action a = new Action(text) {
            @Override
            public void performAction() {
//                IntentGenerator generator = new IntentGenerator(activity,
//                        LoopImageFragment.class.getName());
//                activity.startActivity(generator.build());
            }
        };
        return new Feature(R.drawable.ic_loop, text, new Action[] {
                a
        });
    }

    public static Feature getPushFeature(final Activity activity) {
        String text = "插屏";
        Action a = new Action(text) {
            @Override
            public void performAction() {
                // IntentGenerator generator = new IntentGenerator(activity,
                // PushExampleFragment.class.getName())
                // .addFlag(ContainerActivity.FLAG_OBSERVER_CONFIGURATIONCHANGED);
                // activity.startActivity(generator.build());
                Intent intent = new Intent(activity, InterstitialActivity.class);
                activity.startActivity(intent);
            }
        };
        return new Feature(R.drawable.ic_fullscreen, text, new Action[] {
                a
        });
    }

    public static Feature getWelcomeFeature(final Activity activity) {
        String text = "开屏";
        Action a = new Action(text) {
            @Override
            public void performAction() {
                activity.startActivity(new Intent(activity, WelcomeActivity.class));
            }
        };
        return new Feature(R.drawable.ic_welcome, text, new Action[] {
                a
        });
    }

    public static Feature getTextFeature(final Activity activity) {
        String text = "文字链";
        Action a = new Action(text) {
            @Override
            public void performAction() {
                IntentGenerator generator = new IntentGenerator(activity,
                        TextLinkExampleFragment.class.getName());
                activity.startActivity(generator.build());
            }
        };
        return new Feature(R.drawable.ic_text, text, new Action[] {
                a
        });
    }

}
