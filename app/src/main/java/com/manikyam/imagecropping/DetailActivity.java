package com.manikyam.imagecropping;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(getIntent().hasExtra("url")) {
            String path = getIntent().getStringExtra("url");
            if(path!=null) {
                File imgFile = new File(path);
                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    imageView.setImageBitmap(myBitmap);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
       finish();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
