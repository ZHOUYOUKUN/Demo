package com.example.myapplication.recycleviews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

/**
 * @author zhouyoukun
 */
public class XidingFootAdapter extends RecyclerView.Adapter<ContentHold> {


    @NonNull
    @Override
    public ContentHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContentHold(LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_xiding_content_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContentHold holder, int position) {
        holder.item.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_e3e3e3));
        holder.item.setText("Foot:"+position);
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
