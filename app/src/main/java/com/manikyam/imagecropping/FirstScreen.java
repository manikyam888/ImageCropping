package com.manikyam.imagecropping;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FirstScreen extends AppCompatActivity implements IOnPermissionsDenyListener, View.OnClickListener {
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 123;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        button = (Button) findViewById(R.id.pictureBT);
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED || permissionCheck1 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                button.setOnClickListener(this);
            }
        } else {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllPermissionsGranted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAllPermissionsGranted = false;
                break;
            }
        }
        if (!isAllPermissionsGranted) {
            CommonDialogs.permissionsAppDialog(this);
        } else {
            button.setOnClickListener(this);
        }
        return;
    }

    @Override
    public void onPermissionsDeny() {
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
