//package com.example.myapplication.recycleviews;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewConfiguration;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.comm.libary.utils.LogUtils;
//import com.geek.luck.calendar.app.widget.ChildRecyclerView;
//import com.xiaoniu.cleanking.ui.home.adapter.CleanAdapter;
//
///**
// * Desc:
// * <p>
// * Author: youkun_zhou
// * Date: 2020/12/31
// * Copyright: Copyright (c) 2016-2020
// * Company: @小牛科技
// * Update Comments:
// * 构建配置参见:
// *
// * @author youkun_zhou
// */
//public class CleanHomeRecyclerView extends RecyclerView {
//
//    private TouchState mTouchState = TouchState.DEF;
//
//    private ChangeListener.State mCurrentState = ChangeListener.State.IDLE;
//    private int mTouchSlop;
//    private int mScrollPointerId;
//    private int mInitialTouchX, mInitialTouchY;
//
//    /**
//     * 距离顶部吸附的距离
//     */
//    private int adsorbentY = 0;
//
//    public CleanHomeRecyclerView(@NonNull Context context) {
//        super(context);
//    }
//
//    public CleanHomeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    public CleanHomeRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(context);
//    }
//
//    private void init(Context context) {
//        ViewConfiguration vc = ViewConfiguration.get(getContext());
//        this.mTouchSlop = vc.getScaledTouchSlop();
//    }
//
//
//    public void initLayoutManager(Context context) {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context) {
//            @Override
//            public int scrollVerticallyBy(int dy, Recycler recycler, State state) {
//                try {
//                    return super.scrollVerticallyBy(dy, recycler, state);
//                } catch (Exception e) {
//                    return 0;
//                }
//            }
//
//            @Override
//            public void onLayoutChildren(Recycler recycler, State state) {
//                try {
//                    super.onLayoutChildren(recycler, state);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public boolean canScrollVertically() {
//                if (isReset) {
//                    return super.canScrollVertically();
//                }
//                boolean isNewsItem = isNewsItem();
//                if (!isNewsItem) {
//                    return super.canScrollVertically();
//                }
//                return false;
//            }
//
//
//            @Override
//            public void addDisappearingView(View child) {
//                try {
//                    super.addDisappearingView(child);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public boolean supportsPredictiveItemAnimations() {
//                return false;
//            }
//
//        };
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        setLayoutManager(linearLayoutManager);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (mEnableListener != null && mEnableListener.enableScroll()) {
//            return true;
//        }
//        int action = ev.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
//            //Log.e("MyRecyclerView", "-->> dispatchTouchEvent ACTION_DOWN");
//            mTouchState = TouchState.DEF;
//            stopScroll();
//
//            ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
//            if (childRecyclerView != null) {
//                childRecyclerView.stopScroll();
//            }
//        }
//
//        // <方案一>
//        // 仅仅处理吸顶后，交付给子view处理
//        // 如果父RecyclerView已经滑动到底部，需要让子RecyclerView处理滑动事件
////        if (isScrollEnd()) {
////            final View childViewPager = findNestedScrollingChildViewPager();
////            if (childViewPager != null) {
////                changeState(ChangeListener.State.COLLAPSED);
////                boolean childFlag = childViewPager.dispatchTouchEvent(ev);
////                return childFlag;
////            }
////        }
////
////        changeState(ChangeListener.State.EXPANDED);
//
//        try {
//            return super.dispatchTouchEvent(ev);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public void setScrollingTouchSlop(int slopConstant) {
//        ViewConfiguration vc = ViewConfiguration.get(this.getContext());
//        switch (slopConstant) {
//            case 0:
//                //LogUtils.w("MyRecyclerView", "setScrollingTouchSlop(): vc.getScaledTouchSlop()");
//                this.mTouchSlop = vc.getScaledTouchSlop();
//                break;
//            case 1:
//                this.mTouchSlop = vc.getScaledPagingTouchSlop();
//                //LogUtils.w("MyRecyclerView", "setScrollingTouchSlop(): vc.getScaledPagingTouchSlop()");
//                break;
//            default:
//                break;
//                //LogUtils.w("MyRecyclerView", "setScrollingTouchSlop(): bad argument constant " + slopConstant + "; using default value");
//
//        }
//        super.setScrollingTouchSlop(slopConstant);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        boolean canScrollHorizontally = getLayoutManager().canScrollHorizontally();
//        boolean canScrollVertically = getLayoutManager().canScrollVertically();
//        int action = e.getActionMasked();
//        int actionIndex = e.getActionIndex();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//
//                mScrollPointerId = e.getPointerId(0);
//                this.mInitialTouchX = (int) (e.getX() + 0.5F);
//                this.mInitialTouchY = (int) (e.getY() + 0.5F);
//                return super.onInterceptTouchEvent(e);
//
//            case MotionEvent.ACTION_MOVE:
//
//                int index = e.findPointerIndex(this.mScrollPointerId);
//                if (index < 0) {
//                    return false;
//                }
//
//                int x = (int) (e.getX(index) + 0.5F);
//                int y = (int) (e.getY(index) + 0.5F);
//                if (getScrollState() != 1) {
//                    int dx = x - this.mInitialTouchX;
//                    int dy = y - this.mInitialTouchY;
//                    boolean startScroll = false;
//                    if (canScrollHorizontally && Math.abs(dx) > this.mTouchSlop && Math.abs(dx) > Math.abs(dy)) {
//                        startScroll = true;
//                    }
//
//                    if (canScrollVertically && Math.abs(dy) > this.mTouchSlop && Math.abs(dy) > Math.abs(dx)) {
//                        startScroll = true;
//                    }
//
//                    //LogUtils.d("MyRecyclerView", "canX:" + canScrollHorizontally + "--canY" + canScrollVertically + "--dx:" + dx + "--dy:" + dy + "--startScorll:" + startScroll + "--mTouchSlop" + mTouchSlop);
//
//                    return startScroll && super.onInterceptTouchEvent(e);
//                }
//                return super.onInterceptTouchEvent(e);
//            case MotionEvent.ACTION_POINTER_DOWN:
//                //LogUtils.e("MyRecyclerView", "onInterceptTouchEvent  ACTION_POINTER_DOWN");
//
//                this.mScrollPointerId = e.getPointerId(actionIndex);
//                this.mInitialTouchX = (int) (e.getX(actionIndex) + 0.5F);
//                this.mInitialTouchY = (int) (e.getY(actionIndex) + 0.5F);
//                return super.onInterceptTouchEvent(e);
//            default:
//        }
//
//        return super.onInterceptTouchEvent(e);
//
//    }
//
//    /**
//     * 检查是不是资讯流Item
//     *
//     * @return
//     */
//    public boolean isNewsItem() {
//        ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
//        //信息流不存在
//        if (childRecyclerView == null) {
//            return false;
//        }
//        LinearLayoutManager linearManager = (LinearLayoutManager) getLayoutManager();
//        if (linearManager == null) {
//            return false;
//        }
//        int position = linearManager.findFirstVisibleItemPosition();
//        if (getAdapter() == null || getAdapter().getItemCount() <= 0) {
//            return false;
//        }
//        //信息流不存在
//        if (findNestedScrollingChildRecyclerView() == null) {
//            return false;
//        }
//        //倒数第二个item或者已经是最后一个item
//        if (position == getAdapter().getItemCount() - 2) {
//            //根据position找到这个Item
//            View firstVisiableChildView = linearManager.findViewByPosition(position);
//            if (firstVisiableChildView == null) {
//                return false;
//            }
//            int holdHeight = firstVisiableChildView.getHeight();
//            //算出该Item还未移出屏幕的高度
//            int itemTop = firstVisiableChildView.getTop();
//            //滚动到了指定距离
//            if ((holdHeight - Math.abs(itemTop)) <= adsorbentY) {
//                linearManager.scrollToPositionWithOffset(getAdapter().getItemCount() - 1, adsorbentY);
//                stopScroll();
//                stopNestedScroll();
//                return true;
//            }
//        } else if (position > getAdapter().getItemCount() - 2) {
//            linearManager.scrollToPositionWithOffset(getAdapter().getItemCount() - 1, adsorbentY);
//            stopScroll();
//            stopNestedScroll();
//            return true;
//        }
//
//
//        return false;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        boolean isNewsItem = isNewsItem();
//        if (isNewsItem) {
//            isReset = false;
//            if (isScrollEnd()) {
//                ChildRecyclerView childRecyclerView = findNestedScrollingChildRecyclerView();
//                if (childRecyclerView != null) {
//                    changeState(com.xiaoniu.cleanking.ui.home.recycler.ChangeListener.State.COLLAPSED);
//                    mTouchState = TouchState.CHILD;
//                    childRecyclerView.option(e);
//                }
//            }
//            // 查找第一个可见的item的position
//        } else {
//            if (mTouchState == TouchState.CHILD) {
//                mTouchState = TouchState.PARENT;
//                changeState(com.xiaoniu.cleanking.ui.home.recycler.ChangeListener.State.EXPANDED);
//                dispatchTouchEvent(e);
//            }
//        }
//        try {
//            return super.onTouchEvent(e);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//        return false;
//    }
//
//    @Override
//    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
//        return false;
//    }
//
//    /**
//     * 切换
//     *
//     * @param expanded
//     */
//    private void changeState(com.xiaoniu.cleanking.ui.home.recycler.ChangeListener.State expanded) {
//        if (mCurrentState != expanded) {
//            if (mChangeListener != null) {
//                mChangeListener.onStateChanged(expanded);
//            }
//            mCurrentState = expanded;
//        }
//    }
//
//    private boolean isScrollEnd() {
//        //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
//        return !canScrollVertically(1);
//    }
//
//    private ChildRecyclerView findNestedScrollingChildRecyclerView() {
//        if (getAdapter() instanceof CleanAdapter) {
//            CleanAdapter adapter = (CleanAdapter) getAdapter();
//            return adapter.getCurrentChildRecyclerView();
//        }
//        return null;
//    }
//
//
//    private com.xiaoniu.cleanking.ui.home.recycler.ChangeListener mChangeListener = null;
//
//
//    /**
//     * 重置状态回到顶端
//     */
//    private boolean isReset = false;
//
//    public void reset() {
//        isReset = true;
//        mTouchState = TouchState.DEF;
//        smoothScrollToPosition(0);
//        changeState(com.xiaoniu.cleanking.ui.home.recycler.ChangeListener.State.EXPANDED);
//    }
//
//    private com.xiaoniu.cleanking.ui.home.recycler.CleanHomeRecyclerView.EnableListener mEnableListener = null;
//
//    public interface EnableListener {
//        boolean enableScroll();
//    }
//
//    public void setAdsorbentY(int adsorbentY) {
//        this.adsorbentY = adsorbentY;
//    }
//
//    public com.xiaoniu.cleanking.ui.home.recycler.ChangeListener.State getTouchState() {
//        return mCurrentState;
//    }
//
//
//}
//
