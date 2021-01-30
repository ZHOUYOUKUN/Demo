package com.example.myapplication.recycleviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.recycleviews.adapter.XidingAdapter;

public class XiDingRecyclerViewActivity extends Activity {

    public static void startXiDingRecyclerViewActivity(Context context) {
        context.startActivity(new Intent(context, XiDingRecyclerViewActivity.class));
    }

    private XiDingRecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xd_recyclerview_layout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.initLayoutManager(this);
        recyclerView.setAdsorbentY(dip2Pixel(this, 80));
        recyclerView.setAdapter(new XidingAdapter());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        findViewById(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });
    }


    public int dip2Pixel(Context context, float defValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defValue, context.getResources().getDisplayMetrics());
    }

}
