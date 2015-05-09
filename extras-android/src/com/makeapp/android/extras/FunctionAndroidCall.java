package com.makeapp.android.extras;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yuanyou on 2015/5/8.
 */
public class FunctionAndroidCall
        extends FunctionAndroid<String, String> {

    public static Object invokeMethod(Object obj, Method m, Object... args) {
        if (m != null) {
            try {
                return m.invoke(obj, args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Method getMethod(Object obj, String name, Class<?>... parameterTypes) {
        try {
            return obj.getClass().getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String apply(String s) {
        String[] params = s.split(" ");
        if (params.length > 1) {
            Method method = getMethod(this, params[0], String[].class);
            if (method != null) {
                return (String) invokeMethod(this, method, params);
            }
        }
        return null;
    }

}
