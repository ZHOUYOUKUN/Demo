package com.example.myapplication.recycleviews.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

/**
 * @author zhouyoukun
 */
public class XidingAdapter extends RecyclerView.Adapter {


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return new ContentHold(LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_xiding_content_layout, parent, false));
            case 11:
                return new FootHold(LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_xiding_foot_layout, parent, false));
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return 12;
    }
}
