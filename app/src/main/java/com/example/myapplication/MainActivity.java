package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.recycleviews.XiDingRecyclerViewActivity;
import com.example.myapplication.viewflipper.ViewFlipperActivity;
import com.example.myapplication.wallpaper.Constant;
import com.example.myapplication.wallpaper.WallpaperDynamicService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        findViewById(R.id.tv_viewflipper).setOnClickListener(this);
        findViewById(R.id.tv_xidinglist).setOnClickListener(this);
        findViewById(R.id.tv_wallpaper).setOnClickListener(this);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_viewflipper:
                ViewFlipperActivity.startViewFlipperActivity(this);
                break;
            case R.id.tv_xidinglist:
                XiDingRecyclerViewActivity.startXiDingRecyclerViewActivity(this);
                break;
            case R.id.tv_wallpaper:
                String path = "/storage/emulated/0/DCIM/Camera/VID_20210130_173911.mp4";
                WallpaperDynamicService.setToWallPaper(this, path);
                break;
            default:
                break;
        }
    }
}

