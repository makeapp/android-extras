package com.makeapp.android.extras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.fun.Function;
import org.fun.FunctionFactory;
import org.fun.FunctionIterator;

/**
 * Created by yuanyou on 2014/7/8.
 */
public abstract class FunctionAndroid<T, V>
        implements Function<T, V> {

    static protected Context context;
    protected Activity activity;
    Bundle metaData;

    protected String name;
    String alias;

    public String getResString(String key) {
        if (key == null) {
            return null;
        }
        key = key.toLowerCase();
        Resources resources = context.getResources();
        int id = resources.getIdentifier(key, "string", context.getPackageName());
        if (id > 0) {
            return resources.getString(id);
        }
        return null;
    }

    public String[] getResStringArray(String key) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier(key, "string", context.getPackageName());
        if (id > 0) {
            return resources.getStringArray(id);
        }
        return null;
    }

    public String getConfig(String key) {
        return getMetaString("extras." + name + "." + key);
    }

    public String getConfig(String key, String def) {
        String value = getMetaString("extras." + name + "." + key);
        if (value == null) {
            return def;
        }
        return value;
    }

    public String getMetaString(String key) {
        if (metaData == null) {
            metaData = getMetaData();
        }
        String value = String.valueOf(metaData.get(key));
        if (value == null) {
            Log.i("Fn", "Can't find meta " + key);
            return null;
        }
        if (value.startsWith("$")) {
            return value.substring(1);
        }
        return value;
    }


    public Bundle getMetaData() {
        return getMetaData(context);
    }

    public static Bundle getMetaData(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null) {
                return applicationInfo.metaData;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new Bundle();
    }

    protected void onAppCreate(Context activity) {
        this.context = activity;
    }

    protected void onAppDestory() {

    }

    protected void onCreate(Activity activity) {
        this.activity = activity;
    }

    protected void onResume() {

    }

    protected void onPause() {

    }

    public void onStart() {

    }

    public void onStop() {

    }


    public void onShow(View rootView) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onDestroy() {

    }

    public void onResult(String action, String status) {
        onResult(action + " " + status);
    }

    public void onResult(String message) {
        if (name != null) {
            FunctionFactory.callLocal(name + "_callback", message);
        }
        if (alias != null) {
            FunctionFactory.callLocal(alias + "_callback", message);
        }
    }

    public static void doStop() {
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onStop();
                }
            }
        });
    }

    public static void doAppCreate(final Context activity) {
        Bundle resources = getMetaData(activity);
        String extras = getMetaString(resources, "extras");
        ;
        if (extras != null && extras.length() > 0) {
            String[] names = extras.split(";");
            for (String name : names) {
                String className = getMetaString(resources, "extras." + name);
                String aliasName = getMetaString(resources, "extras." + name + ".alias");
                addFunction(name, className, aliasName);
            }
        } else {
            for (String key : resources.keySet()) {
                if (key.startsWith("extras.")) {
                    String name = key.substring("extras.".length());
                    if (name.indexOf(".") == -1) {
                        String className = getMetaString(resources, key);
                        String aliasName = getMetaString(resources, "extras." + name + ".alias");
                        addFunction(name, className, aliasName);
                    }
                }
            }
        }
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onAppCreate(activity);
                }
            }
        });
    }

    public static void doAppDestory() {
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onAppDestory();
                }
            }
        });
    }

    public static void doCreate(final Activity activity) {
        if (context == null) {
            doAppCreate(activity);
        }
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onCreate(activity);
                }
            }
        });
    }

    private static String getMetaString(Bundle resources, String name) {
        try {
            return resources.getString(name);
        } catch (Exception e) {
        }
        return null;
    }

    private static void addFunction(String name, String className, String aliasName) {
        try {
            Class clazz = Class.forName(className);
            FunctionAndroid function = (FunctionAndroid) clazz.newInstance();
            function.name = name;
            function.alias = aliasName;
            FunctionFactory.registerFunction(name, function);
            if (aliasName != null) {
                FunctionFactory.registerFunction(aliasName, function);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doResume() {
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onResume();
                }
            }
        });
    }

    public static void doPause() {
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onPause();
                }
            }
        });
    }

    public static void doStart() {
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onStart();
                }
            }
        });
    }


    public static void doShow(final View view) {
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onShow(view);
                }
            }
        });
    }

    public static void doDestroy() {
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onDestroy();
                }
            }
        });
    }

    public static void doActivityResult(final int requestCode, final int resultCode, final Intent data) {
        FunctionFactory.forEach(new FunctionIterator() {
            @Override
            public void next(Function function) {
                if (function instanceof FunctionAndroid) {
                    FunctionAndroid androidFunction = (FunctionAndroid) function;
                    androidFunction.onActivityResult(requestCode, resultCode, data);
                }
            }
        });
    }

}
