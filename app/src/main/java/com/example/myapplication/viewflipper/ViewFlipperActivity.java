package com.example.myapplication.viewflipper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

/**
 *  ViewFlipper
 */
public class ViewFlipperActivity extends AppCompatActivity {

    public static void startViewFlipperActivity(Context context){
        Intent intent=new Intent(context,ViewFlipperActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewflipper_layout);
    }
}
