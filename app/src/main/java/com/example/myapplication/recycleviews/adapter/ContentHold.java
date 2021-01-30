package com.example.myapplication.recycleviews.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class ContentHold extends RecyclerView.ViewHolder {

    public TextView item;

    public ContentHold(@NonNull View itemView) {
        super(itemView);
        item=itemView.findViewById(R.id.tv_viewflipper);
    }

}
