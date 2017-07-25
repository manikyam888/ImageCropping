package com.manikyam.imagecropping;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {

    RelativeLayout outerLayout;
    RelativeLayout mainLayout;
    private String TAG = "======Camera.sample";
    private String path;
    private Camera mCamera;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap _bitmapPicture = BitmapFactory.decodeByteArray(data, 0, data.length);
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(0, info);
            int degrees = 0;
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }
            int result = (info.orientation - degrees + 360) % 360;

            Matrix matrix = new Matrix();
            matrix.postRotate(result);
            Bitmap bitmapPicture = null;
            try {
                 bitmapPicture = Bitmap.createBitmap(_bitmapPicture, 0, 0, _bitmapPicture.getWidth(), _bitmapPicture.getHeight(), matrix, true);
                //bitmapPicture = Bitmap.createScaledBitmap(bp, (int) (bp.getWidth() * 0.7), (int) (bp.getHeight() * 0.7), true);
            } catch (OutOfMemoryError e){
                return;
            }
            int bitmapWidth = bitmapPicture.getWidth();
            int bitmapHeight = bitmapPicture.getHeight();
            Log.d(TAG, "bitmapWidth = " + bitmapWidth + " & bitmapHeight = " + bitmapHeight);
            int outerLayoutWidth = outerLayout.getWidth();
            int outerLayoutHeight = outerLayout.getHeight();
            Log.d(TAG, "outerLayoutWidth = " + outerLayoutWidth + " & outerLayoutHeight = " + outerLayoutHeight);
            int mainLayoutWidth = mainLayout.getWidth();
            int mainLayoutHeight = mainLayout.getHeight();
            Log.d(TAG, "mainLayoutWidth = " + mainLayoutWidth + " & mainLayoutHeight = " + mainLayoutHeight);
            float widthPercentage = (float) mainLayoutWidth / (float) outerLayoutWidth;
            float heightPercentage = (float) mainLayoutHeight / (float) outerLayoutHeight;
            Log.d(TAG, "widthPercentage = " + widthPercentage + " & heightPercentage = " + heightPercentage);
            int newBitmapWidth = (int) (bitmapWidth * widthPercentage);
            int newBitmapHeight = (int) (bitmapHeight * heightPercentage);
            Log.d(TAG, "newBitmapWidth = " + newBitmapWidth + " & newBitmapHeight = " + newBitmapHeight);
            int newBitmapLeft = (int) (bitmapWidth * ((1 - widthPercentage) / 2));
            int newBitmapTop = (int) (bitmapHeight * ((1 - heightPercentage) / 2));
            Log.d(TAG, "newBitmapLeft = " + newBitmapLeft + " & newBitmapTop = " + newBitmapTop);
            Bitmap newBitmapPicture = Bitmap.createBitmap(bitmapPicture, newBitmapLeft, newBitmapTop, newBitmapWidth, newBitmapHeight, null, true);
            String pictureFilePath = getOutputMediaFilePath();
            File pictureFile = new File(pictureFilePath + ".jpg");
             path=pictureFilePath + "_cropped.jpg";
            File croppedPictureFile = new File(path);
            FileOutputStream out = null;
            FileOutputStream out1 = null;
            try {
               // out = new FileOutputStream(pictureFile);
                //bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out1 = new FileOutputStream(croppedPictureFile);
                newBitmapPicture.compress(Bitmap.CompressFormat.JPEG, 100, out1);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                  /*  if (out != null) {
                        out.flush();
                        out.close();
                    }*/
                    if (out1 != null) {
                        out1.flush();
                        out1.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Uri uri = Uri.fromFile(pictureFile);
            Uri uri1 = Uri.fromFile(croppedPictureFile);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uri);
            sendBroadcast(mediaScanIntent);
            Intent mediaScanIntent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent1.setData(uri1);
            sendBroadcast(mediaScanIntent1);
        }
    };

    private void callIntent() {
        Intent intent=new Intent(this,DetailActivity.class);
       intent.putExtra("url",path);
        startActivity(intent);
        finish();

    }





    private static String getOutputMediaFilePath() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        outerLayout = (RelativeLayout) findViewById(R.id.frameLayout);
        mainLayout = (RelativeLayout) findViewById(R.id.frameLayout1);

        // Create an instance of Camera

    }



    @Override
    protected void onResume() {
        super.onResume();

        mCamera = CameraUtils.getCameraInstance();
        CameraPreview mPreview = new CameraPreview(this, mCamera);
        outerLayout.removeAllViews();
        outerLayout.addView(mPreview);
    }

    public void captureImage(View view) {
        mCamera.takePicture(null, null, mPicture);
        Toast.makeText(this, "Captured Image", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callIntent();
            }
        },1000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}