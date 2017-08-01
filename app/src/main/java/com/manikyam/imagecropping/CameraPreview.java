package com.manikyam.imagecropping;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    public static final String TAG = "cam.sample";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Activity activity;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        this.activity= (Activity) context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera outerLayout: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera outerLayout in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your outerLayout can change or rotate, take care of those events here.
        // Make sure to stop the outerLayout before resizing or reformatting it.
        if (mHolder.getSurface() == null) {
            // outerLayout surface does not exist
            return;
        }
        // stop outerLayout before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent outerLayout
        }
        // set outerLayout size and make any resize, rotate or
        // reformatting changes here
        // start outerLayout with new settings
        try {
            mHolder.setFormat(PixelFormat.RGBA_8888);
            mCamera.setPreviewDisplay(mHolder);
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(0, info);
            int degrees = 0;
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }
            int result = (info.orientation - degrees + 360) % 360;
            mCamera.setDisplayOrientation(result);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera outerLayout: " + e.getMessage());
        }
    }
}
