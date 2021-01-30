package com.example.myapplication.recycleviews.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class FootHold extends RecyclerView.ViewHolder {

    public RecyclerView recyclerView;

    public FootHold(@NonNull View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.rc_footlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        recyclerView.setAdapter(new XidingFootAdapter());
    }

}
