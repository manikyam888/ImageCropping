package com.manikyam.imagecropping;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;


public class CameraUtils {

    public static boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public int getNumberOfCameras(Context context) {
        return Camera.getNumberOfCameras();
    }
}
