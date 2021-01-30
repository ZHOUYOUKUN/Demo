package com.example.myapplication.recycleviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.recycleviews.adapter.FootHold;

public class XiDingRecyclerView extends RecyclerView {

    private int adsorbentY = 0;

    public XiDingRecyclerView(@NonNull Context context) {
        super(context);
    }

    public XiDingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public XiDingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void initLayoutManager(Context context) {
        this.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                if (isLastItem()) {
                    stopScroll();
                    return false;
                }
                return super.canScrollVertically();
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (isLastItem()) {
            stopScroll();
            FootHold footHold = (FootHold) findViewHolderForAdapterPosition(getAdapter().getItemCount() - 1);
            if (footHold != null && footHold.recyclerView != null) {
                footHold.recyclerView.dispatchTouchEvent(e);
            }
            return false;
        }
        return super.onTouchEvent(e);
    }


    public boolean isLastItem() {

        Log.d("----------zyk", "isLastItem");
        LinearLayoutManager linearManager = (LinearLayoutManager) getLayoutManager();
        if (linearManager == null) {
            return false;
        }
        int position = linearManager.findFirstVisibleItemPosition();
        if (getAdapter() == null || getAdapter().getItemCount() <= 0) {
            return false;
        }

        Log.d("----------zyk", "isLastItem position" + position + "  getAdapter().getItemCount() - 2:" + (getAdapter().getItemCount() - 2) + " adsorbentY" + adsorbentY);

        //倒数第二个item或者已经是最后一个item
        if (position == getAdapter().getItemCount() - 2) {
            //根据position找到这个Item
            View firstVisiableChildView = linearManager.findViewByPosition(position);
            if (firstVisiableChildView == null) {
                return false;
            }
            int holdHeight = firstVisiableChildView.getHeight();
            //算出该Item还未移出屏幕的高度
            int itemTop = firstVisiableChildView.getTop();
            //滚动到了指定距离
            if ((holdHeight - Math.abs(itemTop)) <= adsorbentY) {

                return true;
            }
        } else if (position > getAdapter().getItemCount() - 2) {
            return true;
        }
        return false;
    }

    public void setAdsorbentY(int adsorbentY) {
        this.adsorbentY = adsorbentY;
    }


}
