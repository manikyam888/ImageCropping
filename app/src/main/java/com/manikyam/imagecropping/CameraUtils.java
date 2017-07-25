package com.manikyam.imagecropping;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import java.util.List;


public class CameraUtils {

    public static boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
            Camera.Parameters param= c.getParameters();// attempt to get a Camera instance
            param.setJpegQuality(100);
            param.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            /* Set Auto focus */
            List<String> focusModes = param.getSupportedFocusModes();
            if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)){
                param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else
            if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
                param.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }

            //param.setPreviewFrameRate(100);
            //param.setPreviewSize(1080,1548);
            List<Camera.Size> sizes = param.getSupportedPreviewSizes();
                Camera.Size size = sizes.get(0);
                for(int i=0;i<sizes.size();i++)
                {
                    if(sizes.get(i).width > size.width)
                        size = sizes.get(i);
            }
            param.setPictureSize(size.width, size.height);
            c.setParameters(param);
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public int getNumberOfCameras(Context context) {
        return Camera.getNumberOfCameras();
    }
}
