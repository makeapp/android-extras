package com.makeapp.android.extras;

import android.app.Activity;
import android.hardware.Camera;

/**
 * Created by yuanyou on 2014/7/8.
 */

/**
 * <uses-permission android:name="android.permission.FLASHLIGHT" />
 * <p>
 * <uses-permission android:name="android.permission.CAMERA"/>
 * <p>
 * <uses-feature android:name="android.hardware.camera" />
 * <p>
 * <uses-feature android:name="android.hardware.autofocus"/>
 */
public class FunctionCameraFlash
        extends FunctionAndroid<String, Void> {

    Camera camera;

    @Override
    public Void apply(String s) {
        if (camera == null) {
            camera = Camera.open();
        }
        if ("on".equalsIgnoreCase(s)) {
            camera.startPreview();
            Camera.Parameters parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameter);
        } else if ("off".equalsIgnoreCase(s)) {
            Camera.Parameters parameter = camera.getParameters();
            parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameter);
            camera = null;
        }

        return null;
    }
}
