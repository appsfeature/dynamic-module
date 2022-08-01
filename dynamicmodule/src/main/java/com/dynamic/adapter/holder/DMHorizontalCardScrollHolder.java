package com.dynamic.adapter.holder;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.dynamic.DynamicModule;
import com.dynamic.adapter.holder.base.DynamicCommonHolder;
import com.dynamic.listeners.DMFlingType;
import com.dynamic.model.DMCategory;

/**
 * @param <T1> : DMCategory
 * @param <T2> : DMContent
 */
public abstract class DMHorizontalCardScrollHolder<T1, T2> extends DynamicCommonHolder<T1, T2> {

    public boolean isScrollStateIdle = true;
    public boolean isStateChange = false;
    public int mSlotWidth = 0;

    public DMHorizontalCardScrollHolder(View view) {
        super(view);
    }

    public void setData(T1 item, int position, @DMFlingType int flingType) {
        super.setData(item, position, true);
        if(flingType != DMFlingType.None && recyclerView != null && recyclerView.getOnFlingListener() == null) {
            try {
                SnapHelper snapHelper = flingType == DMFlingType.PagerSnapHelper ? new PagerSnapHelper() : new LinearSnapHelper();
                recyclerView.setOnFlingListener(null);
                snapHelper.attachToRecyclerView(recyclerView);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        try {
            if (isEnableAutoScroll && mListSize > 0) {
                getRunnable();
                clearRunnable();
                DynamicModule.getInstance().getHandler().postDelayed(getRunnable(), mScrollSpeed);
                if (recyclerView != null) {
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            switch (newState) {
                                case RecyclerView.SCROLL_STATE_IDLE:
                                    isScrollStateIdle = true;
                                    if (isEnableAutoScroll) {
                                        clearRunnable();
                                        DynamicModule.getInstance().getHandler().postDelayed(getRunnable(), mScrollSpeed);
                                    }
                                    break;
                                case RecyclerView.SCROLL_STATE_DRAGGING:
                                    isStateChange = true;
                                    isScrollStateIdle = false;
                                    break;
                                case RecyclerView.SCROLL_STATE_SETTLING:
                                    break;
                            }
                        }
                    });
                }
            } else {
                clearRunnable();
            }
        } catch (Exception e) {
            e.printStackTrace();
            clearRunnable();
        }
    }
    public Runnable mRunnable;

    public Runnable getRunnable() {
        if(mRunnable == null) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isScrollStateIdle) {
                            int lastPos = -2;
                            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                                lastPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                            }
                            if (!isMoveToFirst && lastPos < mListSize) {
                                isMoveToFirst = (lastPos + 1) == mListSize;
                                smoothScrollRecyclerView(false);
                                clearRunnable();
                                DynamicModule.getInstance().getHandler().postDelayed(this, mScrollSpeed);
                            } else {
                                isMoveToFirst = false;
                                smoothScrollRecyclerView(true);
                                clearRunnable();
                                DynamicModule.getInstance().getHandler().postDelayed(this, mScrollSpeed);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        return mRunnable;
    }

    public void clearRunnable() {
        if (DynamicModule.getInstance().getHandler() != null) {
            DynamicModule.getInstance().getHandler().removeCallbacks(getRunnable());
        }
    }

    public boolean isMoveToFirst = false;


    public void smoothScrollRecyclerView(boolean isMoveToStart) {
        try {
            if (recyclerView != null) {
                if(isMoveToStart){
                    recyclerView.smoothScrollToPosition(0);
                }else {
                    if (isStateChange) {
                        isStateChange = false;
                        int currentPos = -1;
                        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                            currentPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        }
                        if (currentPos >= 0) {
                            RecyclerView.ViewHolder currViewHolder = recyclerView.findViewHolderForLayoutPosition(currentPos);
                            if (currViewHolder != null) {
                                Log.d("@Test", "smoothScrollBy.getX:" + (int) currViewHolder.itemView.getX());
                                recyclerView.smoothScrollBy((int) currViewHolder.itemView.getX(), 0);
                            }
                        }
                    } else {
                        if (mSlotWidth == 0) {
                            mSlotWidth = getSlotWidth();
                        }
                        Log.d("@Test", "smoothScrollBy:" + mSlotWidth);
                        recyclerView.smoothScrollBy(mSlotWidth, 0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSlotWidth() {
        try {
            int currentPos = -1;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                currentPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
            if (currentPos >= 0) {
                RecyclerView.ViewHolder currViewHolder = recyclerView.findViewHolderForLayoutPosition(currentPos);
                if (currViewHolder != null) {
                    return currViewHolder.itemView.getMeasuredWidth();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
